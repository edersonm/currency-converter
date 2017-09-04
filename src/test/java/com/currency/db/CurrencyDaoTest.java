package com.currency.db;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.currency.core.CurrencyData;

import io.dropwizard.testing.junit.DAOTestRule;

public class CurrencyDaoTest {
	
	@Rule
	public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
    .addEntityClass(CurrencyData.class)
    .build();
	
	private CurrencyDAO currencyDao;
	
	@Before
	public void setUp() throws Exception{
		currencyDao = new CurrencyDAO(daoTestRule.getSessionFactory());
	}
	
	@Test
    public void createCurrency() {
		CurrencyData toInsert = new CurrencyData();
		toInsert.setCurrencyFromName("EUR");
		toInsert.setCurrencyFromValue(1.0);
		toInsert.setCurrencyName("USD");
		toInsert.setCurrencyValue(2.5343);
		toInsert.setDate(getDate());
		toInsert.setRequestDate(getDate());
        final CurrencyData currency = daoTestRule.inTransaction(() -> currencyDao.create(toInsert));
        assert(currency.getId() > 0);
        assert(currency.getCurrencyFromName().equals("EUR"));
        assert(currency.getCurrencyFromValue() == 1.0);
        assert(currency.getCurrencyName().equals("USD"));
        assert(currency.getCurrencyValue() == 2.5343);
        assert(currency.getDate().equals(getDate()));
        assert(currency.getRequestDate().equals(getDate()));
    }
	
	private CurrencyData createNewRandomCurrency(){
		CurrencyData toInsert = new CurrencyData();
		toInsert.setCurrencyFromName("EUR");
		toInsert.setCurrencyFromValue(Math.random());
		toInsert.setCurrencyName("USD");
		toInsert.setCurrencyValue(Math.random());
		toInsert.setDate(getDate());
		toInsert.setRequestDate(getDate());
		return toInsert;
	}

	private CurrencyData createNewFixedCurrency(){
		CurrencyData toInsert = new CurrencyData();
		toInsert.setCurrencyFromName("EUR");
		toInsert.setCurrencyFromValue(1.0);
		toInsert.setCurrencyName("USD");
		toInsert.setCurrencyValue(2.5343);
		toInsert.setDate(getDate());
		toInsert.setRequestDate(getDate());
		return toInsert;
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

    @Test
    public void findByDate() {
        daoTestRule.inTransaction(() -> {
        	currencyDao.create(createNewFixedCurrency());
        	currencyDao.create(createNewRandomCurrency());
        	currencyDao.create(createNewRandomCurrency());
        });

        final List<CurrencyData> currencies = currencyDao.findByDate(getDate(), getDate());
        assert(!currencies.isEmpty());
        assert(currencies.size() == 3);
       
        assert(currencies.contains(setIdOnCurrencyData()));
        
    }

    @Test
    public void findByDateBetween() {
        daoTestRule.inTransaction(() -> {
        	currencyDao.create(createNewFixedCurrency());
        	currencyDao.create(createNewRandomCurrency());
        	currencyDao.create(createNewRandomCurrency());
        });

        final List<CurrencyData> currencies = currencyDao.findByDateBetween(getDate(), getDate());
        assert(!currencies.isEmpty());
        assert(currencies.size() == 3);
        assert(currencies.contains(setIdOnCurrencyData()));
    }
    
    @Test(expected = RuntimeException.class)
    public void testRepeatedKey() {
        daoTestRule.inTransaction(() -> {
        	currencyDao.create(setIdOnCurrencyData());
        	currencyDao.create(setIdOnCurrencyData());
        });
    }
    
	private CurrencyData setIdOnCurrencyData() {
		CurrencyData data = createNewFixedCurrency();
        data.setId(1L);
		return data;
	}
}
