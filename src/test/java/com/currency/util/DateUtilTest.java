package com.currency.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;

import org.junit.Test;

public class DateUtilTest {
    @Test
    public void testDateParse() throws ParseException{
        String date = DateUtil.parseFromDate(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        Date dateObj = sdf.parse(date);
        assert(dateObj != null);
    }
    
    @Test(expected = DateTimeException.class)
    public void testDateParseException(){
        DateUtil.parseFromDate(null);
    }
    
    @Test
    public void parseToDateTest(){
        Date d = DateUtil.parseToDate(2019,9,10);
        assert(d != null); 
    }
    
    @Test(expected=DateTimeException.class)
    public void parseToDateYearTest(){
        DateUtil.parseToDate(1899,9,10); 
    }
    
    @Test(expected=DateTimeException.class)
    public void parseToDateMounthTest(){
        DateUtil.parseToDate(1901,13,10); 
    }
    
    @Test(expected=DateTimeException.class)
    public void parseToDateDayTest(){
        DateUtil.parseToDate(1901,9,32); 
    }
    
    @Test
    public void dateConverter() throws ParseException{
        Date d = DateUtil.dateConverter("2017-09-02T00:00:00.000");
        assert(d != null);
    }
    
    @Test(expected=ParseException.class)
    public void dateConverterException() throws ParseException{
        DateUtil.dateConverter("xx-09-02T00:00:00.000");
    }
    
    @Test(expected=ParseException.class)
    public void dateConverterExceptionNull() throws ParseException{
        DateUtil.dateConverter(null);
    }
}
