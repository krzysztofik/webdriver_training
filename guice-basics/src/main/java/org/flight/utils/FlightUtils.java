package org.flight.utils;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightUtils {
	public static Date parseDate(String date) throws ParseException{
		DateFormat formatter = new SimpleDateFormat(dateFormat);
	    return formatter.parse(date);
	}

    @Inject
    public static void setDateFormat(
            @Named("dateFormat")String dateFormat){
        FlightUtils.dateFormat = dateFormat;
    }

    private static String dateFormat;
}
