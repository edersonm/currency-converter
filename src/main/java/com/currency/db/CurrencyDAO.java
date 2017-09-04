package com.currency.db;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import com.currency.core.CurrencyData;

import io.dropwizard.hibernate.AbstractDAO;

public class CurrencyDAO extends AbstractDAO<CurrencyData> {
	public CurrencyDAO(SessionFactory factory) {
		super(factory);
	}
	
	public Optional<CurrencyData> findById(Long id) {
        return Optional.ofNullable(get(id));
    }
	
	public List<CurrencyData> findByDate(Date start, Date end){
		List<CurrencyData> list = list(namedQuery("com.currency.core.findCurrencyBetweenRequestDate").setTimestamp("start", start).setTimestamp("end", end));
		if(list != null && list.size() > 0)
			return list;
		return null;
    }
	
	public List<CurrencyData> findByDateBetween(Date start, Date end) {
		return list(namedQuery("com.currency.core.findCurrencyBetween").setTimestamp("start", start).setTimestamp("end", end));
    }

    public CurrencyData create(CurrencyData currency) {
        return persist(currency);
    }

}
