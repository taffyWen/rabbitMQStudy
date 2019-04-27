package com.mable.springbootproducer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	
	public static Date getNowDate() throws ParseException {  
	    Date currentTime = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    String dateString = formatter.format(currentTime);  
	    //ParsePosition pos = new ParsePosition(8);  
	    Date currentTime_2 = formatter.parse(dateString);  
	    return currentTime_2;  
	}  
}
