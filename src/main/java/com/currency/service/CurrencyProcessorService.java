package com.currency.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import com.currency.core.CurrencyData;
import com.currency.core.CurrencyException;
import com.currency.db.CurrencyDAO;
import com.currency.model.service.RequestData;
import com.currency.util.DateUtil;
import com.currency.util.UpdaterUtil;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * @author ederson
 *
 *         Service provider to CurrencyResource class.
 *
 */
public class CurrencyProcessorService {

    private static final String URL = "http://api.fixer.io/";
    private Client client = ClientBuilder.newClient();
    private CurrencyDAO currencyDao;

    /**
     * Default constructor to receive a DAO instance.
     * 
     * @param dao
     *            instance to be used.
     */
    public CurrencyProcessorService(CurrencyDAO dao) {
        this.currencyDao = dao;
    }

    /**
     * Default constructor to be used in future mock tests.
     */
    public CurrencyProcessorService() {
    }

    /**
     * Get current currency from provider or in-memory cache depending on
     * <b>Util.getGoToDbTimer</b> value.
     * 
     * @return CurrencyData object
     * @throws CurrencyException
     *             in case of invalid parameter.
     * @throws InterruptedException
     *             in case of problems accessing concurrent variables.
     */
    @UnitOfWork
    public CurrencyData getCurrentCurrency() throws InterruptedException, CurrencyException {
        return getCurrency(new Date());
    }

    /**
     * Get currency from web provider or local in-memory for a specific date
     * received in currencyDate and based on <b>Util.getGoToDbTimer</b> value.
     * 
     * @param currencyDate
     *            date the query was initiated
     * @return CurrentData object loaded from memory or from provider
     * @throws InterruptedException
     *             in case of problems accessing concurrent variables.
     * @throws CurrencyException
     */
    public CurrencyData getCurrency(Date currencyDate) throws InterruptedException, CurrencyException {
        if (currencyDate == null)
            throw new CurrencyException("Invalid date!");

        CurrencyData currency = null;

        if (UpdaterUtil.getGoToDbTimer() > 0) {
            currency = checkDb(currencyDate);
        }

        if (currency == null) {
            currency = makeRequestAndSave(currencyDate);
        }
        return currency;
    }

    /**
     * Gets, from database, a list of CurrencyData for request made between
     * currencyStartDate and currencyFinalDate(both included)
     * 
     * @param currencyStartDate
     *            start date for search
     * @param currencyFinalDate
     *            final date for search
     * @param queryByRequest
     *            Defines if the search will be made considering the actual
     *            request date or the currency date(there is no currency update
     *            during weekend, so if a request is made during a Sunday, the
     *            "currency date" will be Friday).
     * @return a List of CurrencyData found in the dates described between
     *         currencyStartDate and currencyFinalDate.
     * @throws CurrencyException
     *             currencyStartDate and currencyFinalDate is null.
     */
    public List<CurrencyData> getCurrency(Date currencyStartDate, Date currencyFinalDate, boolean queryByRequest)
            throws CurrencyException {
        if (!(currencyStartDate != null && currencyFinalDate != null)) {
            throw new CurrencyException("Invalid date!");
        }
        if (queryByRequest) {
            return currencyDao.findByDate(currencyStartDate, currencyFinalDate);
        } else {
            return currencyDao.findByDateBetween(currencyStartDate, currencyFinalDate);
        }
    }

    /**
     * Assumes the requested value is not in memory or the caller requested a
     * call to the provider, makes the request and records the result do
     * database.
     * 
     * @param currencyDate
     *            the date when the request was initiated.
     * @return CurrentData object returned from the provider.
     * @throws CurrencyException 
     */
    protected CurrencyData makeRequestAndSave(Date currencyDate) throws CurrencyException {
        RequestData request = doRequest(currencyDate);
        CurrencyData currency = parseData(request);
        currencyDao.create(currency);
        return currency;
    }

    /**
     * Calls the web provider for the current(if currencyDate equals today, the
     * provider will return the latest currency) or past currencies depending on
     * currencyDate value;
     * 
     * @param currencyDate
     *            Date which we suppose to get from the provider
     * @return RequestData object returned from the provider or null in case of
     *         problems.
     * @throws CurrencyException
     */
    protected RequestData doRequest(Date currencyDate) throws CurrencyException {
        try {
            RequestData data = client.target(URL).path(DateUtil.parseFromDate(currencyDate))
                    .queryParam("symbols", "USD").request(MediaType.APPLICATION_JSON).get(RequestData.class);
            return data;
        } catch (Exception e) {
            throw new CurrencyException("Error connecting to currency server");
        }
    }

    /**
     * Checks if there is any stored result in the (now -
     * UpdaterUtil.getGoToDbTimer()) seconds interval and returns it if found.
     * This method has the propose of reduce repeated requests to the provider
     * 
     * @param currencyDate
     *            Date which we suppose to get from the provider.
     * @return CurrencyData if a request was made in the last (now -
     *         UpdaterUtil.getGoToDbTimer()) seconds.
     * @throws InterruptedException
     *             in case of problems accessing concurrent variables.
     */
    protected CurrencyData checkDb(Date currencyDate) throws InterruptedException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currencyDate);
        cal.add(Calendar.SECOND, -1 * UpdaterUtil.getGoToDbTimer());

        List<CurrencyData> currencyList = currencyDao.findByDate(cal.getTime(), currencyDate);
        if (currencyList != null) {
            return currencyDao.findByDate(cal.getTime(), currencyDate).get(0);
        } else {
            return null;
        }
    }

    /**
     * Convert RequestData object to CurrencyData.
     * 
     * @param request
     *            object to be converted.
     * @return CurrencyData based on RequestData received.
     */
    protected CurrencyData parseData(RequestData request) {
        CurrencyData data = new CurrencyData();
        data.setCurrencyFromName("EUR");
        data.setCurrencyFromValue(1.0);
        data.setCurrencyName("USD");
        data.setCurrencyValue(request.getRates().get("USD"));
        data.setDate(request.getDate());
        data.setRequestDate(new Date());
        return data;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
