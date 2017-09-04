package com.currency.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.currency.core.CurrencyData;
import com.currency.core.CurrencyException;
import com.currency.db.CurrencyDAO;

import io.dropwizard.testing.junit.DAOTestRule;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyProcessorServiceTest {
	@Rule
	public DAOTestRule daoTestRule = DAOTestRule.newBuilder().addEntityClass(CurrencyData.class).build();

	private CurrencyDAO currencyDao;
	private CurrencyProcessorService service;

	@Before
	public void setUp() throws Exception {
		currencyDao = new CurrencyDAO(daoTestRule.getSessionFactory());
		service = new CurrencyProcessorService(currencyDao);
	}

	//Not using Mockito as it would be a bit of overkill.
	@Test
	public void testGetCurrentCurrencyTest() {
		try {
			CurrencyData data = service.getCurrentCurrency();
			assert(data != null);
			assert(data.getCurrencyFromName() == "EUR");
			assert(data.getCurrencyFromValue() == 1.0);
			assert(data.getCurrencyValue() > 0);
			assert(data.getRequestDate() != null);
			assert(data.getDate() != null);
		} catch (CurrencyException | InterruptedException e) {
			assert(false);
		}
	}

	@Test
	public void getCurrencyTest() {
		try {
			daoTestRule.inTransaction(() -> {
				currencyDao.create(createNewFixedCurrency());
			});
			CurrencyData data = service.getCurrency(getDate());
			assert(data != null);
			assert(data.getCurrencyFromName() == "EUR");
			assert(data.getCurrencyFromValue() == 1.0);
			assert(data.getCurrencyValue() > 0);
			assert(data.getRequestDate() != null);
			assert(data.getDate() != null);
		} catch (CurrencyException | InterruptedException e) {
			assert(false);
		} 
	}
	
	@Test(expected=CurrencyException.class)
	public void getCurrencyExceptionTest() throws InterruptedException, CurrencyException {
	    service.getCurrency(null);
	}

	@Test
	public void getCurrencyMultipleTest() {
		try {
			daoTestRule.inTransaction(() -> {
				currencyDao.create(createNewFixedCurrency());
				currencyDao.create(createNewFixedCurrency());
				currencyDao.create(createNewFixedCurrency());
				currencyDao.create(createNewFixedCurrency());
				currencyDao.create(createNewFixedCurrency());
			});
			List<CurrencyData> data = service.getCurrency(getDate(), getDate(), false);

			assert(data != null);
			assert(data.get(0).getCurrencyFromName() == "EUR");
			assert(data.get(1).getCurrencyFromValue() == 1.0);
			assert(data.get(2).getCurrencyValue() > 0);
			assert(data.get(3).getRequestDate() != null);
			assert(data.get(4).getDate() != null);
			assert(data.size() == 5);
		} catch (CurrencyException e) {
			assert(false);
		}

	}
	
	@Test
    public void getCurrencyMultipleRequestDateTest() {
        try {
            daoTestRule.inTransaction(() -> {
                currencyDao.create(createNewFixedCurrency());
                currencyDao.create(createNewFixedCurrency());
                currencyDao.create(createNewFixedCurrency());
                currencyDao.create(createNewFixedCurrency());
                currencyDao.create(createNewFixedCurrency());
            });
            List<CurrencyData> data = service.getCurrency(getDate(), getDate(), true);

            assert(data != null);
            assert(data.get(0).getCurrencyFromName() == "EUR");
            assert(data.get(1).getCurrencyFromValue() == 1.0);
            assert(data.get(2).getCurrencyValue() > 0);
            assert(data.get(3).getRequestDate() != null);
            assert(data.get(4).getDate() != null);
            assert(data.size() == 5);
        } catch (CurrencyException e) {
            assert(false);
        }

    }
	
	@Test(expected=CurrencyException.class)
    public void getCurrencyMultipleExceptionTest() throws InterruptedException, CurrencyException {
        service.getCurrency(null,null,false);
    }
	
	@Test(expected=CurrencyException.class)
    public void getCurrencyMultipleExceptionOneDateTest() throws InterruptedException, CurrencyException {
        service.getCurrency(new Date(),null,false);
    }
	
	@Test(expected=CurrencyException.class)
    public void getCurrencyMultipleExceptionEndDateTest() throws InterruptedException, CurrencyException {
        service.getCurrency(null,new Date(),false);
    }
	
	@Test
	public void getCurrencyMultipleTestRequest() {
		try {
			daoTestRule.inTransaction(() -> {
				currencyDao.create(createNewFixedCurrency());
				currencyDao.create(createNewFixedCurrency());
				currencyDao.create(createNewFixedCurrency());
				currencyDao.create(createNewFixedCurrency());
				currencyDao.create(createNewFixedCurrency());
			});
			List<CurrencyData> data = service.getCurrency(getDate(), getDate(), true);

			assert(data != null);
			assert(data.get(0).getCurrencyFromName() == "EUR");
			assert(data.get(1).getCurrencyFromValue() == 1.0);
			assert(data.get(2).getCurrencyValue() > 0);
			assert(data.get(3).getRequestDate() != null);
			assert(data.get(4).getDate() != null);
			assert(data.size() == 5);
		} catch (CurrencyException e) {
			assert(false);
		}

	}

	private Date getDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.YEAR, 2017);
		c.set(Calendar.MONTH, 8);
		c.set(Calendar.DAY_OF_MONTH, 3);
		c.set(Calendar.HOUR_OF_DAY, 22);
		c.set(Calendar.MINUTE, 45);
		c.set(Calendar.SECOND, 01);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	private CurrencyData createNewFixedCurrency() {
		CurrencyData toInsert = new CurrencyData();
		toInsert.setCurrencyFromName("EUR");
		toInsert.setCurrencyFromValue(1.0);
		toInsert.setCurrencyName("USD");
		toInsert.setCurrencyValue(2.5343);
		toInsert.setDate(getDate());
		toInsert.setRequestDate(getDate());
		return toInsert;
	}

}
