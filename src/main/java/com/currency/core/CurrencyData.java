package com.currency.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "currency_history")
@NamedQueries(
	    {
	        @NamedQuery(
		            name = "com.currency.core.findCurrencyBetween",
		            query = "SELECT c FROM CurrencyData c WHERE currency_date >= :start and currency_date <= :end order by currency_date desc"
		   ),
	        @NamedQuery(
		            name = "com.currency.core.findCurrencyBetweenRequestDate",
		            query = "SELECT c FROM CurrencyData c WHERE request_date >= :start and request_date <= :end order by request_date desc"
		   )
	    }
	)
public class CurrencyData {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="currency_from_name", nullable = false)
	private String currencyFromName;
	
	@Column(name="currency_from_value", nullable = false)
	private Double currencyFromValue;
	
	@Column(name="currency_name", nullable = false)
	private String currencyName;
	
	@Column(name="currency_value", nullable = false)
	private Double currencyValue;
	
	@Column(name="currency_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone="GMT-3")
	private Date date;
	
	@Column(name="request_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone="GMT-3")
	private Date requestDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrencyFromName() {
		return currencyFromName;
	}

	public void setCurrencyFromName(String currencyFromName) {
		this.currencyFromName = currencyFromName;
	}

	public Double getCurrencyFromValue() {
		return currencyFromValue;
	}

	public void setCurrencyFromValue(Double currencyFromValue) {
		this.currencyFromValue = currencyFromValue;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Double getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(Double currencyValue) {
		this.currencyValue = currencyValue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currencyFromName == null) ? 0 : currencyFromName.hashCode());
		result = prime * result + ((currencyFromValue == null) ? 0 : currencyFromValue.hashCode());
		result = prime * result + ((currencyName == null) ? 0 : currencyName.hashCode());
		result = prime * result + ((currencyValue == null) ? 0 : currencyValue.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((requestDate == null) ? 0 : requestDate.hashCode());
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
		CurrencyData other = (CurrencyData) obj;
		if (currencyFromName == null) {
			if (other.currencyFromName != null)
				return false;
		} else if (!currencyFromName.equals(other.currencyFromName))
			return false;
		if (currencyFromValue == null) {
			if (other.currencyFromValue != null)
				return false;
		} else if (!currencyFromValue.equals(other.currencyFromValue))
			return false;
		if (currencyName == null) {
			if (other.currencyName != null)
				return false;
		} else if (!currencyName.equals(other.currencyName))
			return false;
		if (currencyValue == null) {
			if (other.currencyValue != null)
				return false;
		} else if (!currencyValue.equals(other.currencyValue))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (requestDate == null) {
			if (other.requestDate != null)
				return false;
		} else if (!requestDate.equals(other.requestDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CurrencyData [id=" + id + ", currencyFromName=" + currencyFromName + ", currencyFromValue="
				+ currencyFromValue + ", currencyName=" + currencyName + ", currencyValue=" + currencyValue + ", date="
				+ date + ", requestDate=" + requestDate + "]";
	}
	
	
}
