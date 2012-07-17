package org.cloudsdale.android.models;

import org.cloudsdale.android.ISO8601;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Basic model class for shared properties and model logic
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class Model {

	/**
	 * Helper method to convert ISO8601 strings to Calendar objects
	 * 
	 * @param isoString
	 *            The ISO8601 date string to convert
	 * @return Calendar object with the date of the ISO8601 string
	 */
	protected Calendar convertIsoString(String isoString) {
		try {
			return ISO8601.toCalendar(isoString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
