package org.cloudsdale.android.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Basic model class for shared properties and model logic
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Model {

	protected Calendar convertDateString(String dateString) {
		// 2012/02/02 18:34:31 +0000
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss Z");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(dateString));
			return cal;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
