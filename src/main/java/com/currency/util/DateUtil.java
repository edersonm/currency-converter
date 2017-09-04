package com.currency.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * Convert date to yyyy-MM-dd formated String.
	 * 
	 * @param date date to be converted.
	 * @return yyyy-MM-dd string.
	 * @throws DateTimeException if date is null.
	 */
	public static final String parseFromDate(Date date) throws DateTimeException {

		if (date == null)
			throw new DateTimeException("Invalid query date");

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		return year + "-" + lpadInt(String.valueOf((month + 1))) + "-" + lpadInt(String.valueOf((day)));
	}

	/**
	 * Convert a set of ints in a java.util.Date object.
	 * 
	 * @param year to be set on the new date.
	 * @param mounth to be set on the new date.
	 * @param day to be set on the new date.
	 * @return new Date object with the specified parameters set.
	 * @throws DateTimeException if <b>year</b> is bellow 1900<br/> or <b>mounth</b> is not between 1 and 12<br/> or <b>day</b> is not between 1 and 31. 
	 */
	public static final Date parseToDate(int year, int mounth, int day) throws DateTimeException {
		Calendar cal = Calendar.getInstance();

		if (year < 1900)
			throw new DateTimeException("Invalid year");

		if (mounth < 1 || mounth > 12)
			throw new DateTimeException("Invalid mounth");
		
		if (day < 1 || day > 31)
			throw new DateTimeException("Invalid day");

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, mounth - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);

		return cal.getTime();
	}

	/**
	 * Parse date string to yyyy-MM-dd'T'HH:mm:ss.SSS format.
	 * 
	 * @param toConvert String to be parsed.
	 * @return Date object created from toConvert.
	 * @throws ParseException if toConvert is invalid or null.
	 */
	public static Date dateConverter(String toConvert) throws ParseException{
	    if(toConvert == null)
	        throw new ParseException("null date", 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date dateObj = sdf.parse(toConvert);
        return dateObj;
	}
	
	/**
	 * Add "0" at the left of Strings of size < 2.
	 * 
	 * @param value String to be changed.
	 * @return "0" + value if value.length < 2 .
	 */
	private static String lpadInt(String value){
		if(value.length()<2)
		   return "0" + value;
		return value;
	}
}
