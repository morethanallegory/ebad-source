package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExpireProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(ExpireProcess.expired("27/11/2014 00:00:00"));
	}

	public static boolean expired(String dateString) {// "29/09/2013"
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now =  Calendar.getInstance().getTime();
		try {
			Date d1 = formatter.parse(dateString);
			return  now.after(d1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
