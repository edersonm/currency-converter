package com.currency.model.service;

import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RequestData {
	private String base;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="GMT-3")
	private Date date;
	
	private HashMap<String,Double> rates;
	
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public HashMap<String, Double> getRates() {
		return rates;
	}
	public void setRates(HashMap<String, Double> rates) {
		this.rates = rates;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((rates == null) ? 0 : rates.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestData other = (RequestData) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (rates == null) {
			if (other.rates != null)
				return false;
		} else if (!rates.equals(other.rates))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RequestData [base=" + base + ", date=" + date + ", rates=" + rates + "]";
	}
	
	
	
}
