package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class StringUtils {
	public static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static int wordCount(String sentence) {
		if (sentence == null || sentence.equals(""))
			return 0;
		return new StringTokenizer(sentence, " ").countTokens();
	}

	/*
	 * Returns formatted date or null if parse error
	 */
	public static String conformsToDateFormatInMillis(String format,
			String milliSeconds) {
		/* Check if date is 'null' */
		if (!NumberUtils.isNumeric(milliSeconds)) {
			return null;
		}
		DateFormat sdfrmt = new SimpleDateFormat(format);
		sdfrmt.setLenient(false);
		Calendar calendar = Calendar.getInstance();

		long millis = Long.parseLong(milliSeconds);
		// long milliSeconds =
		// Long.parseLong(String.valueOf(System.currentTimeMillis()));
		calendar.setTimeInMillis(millis);
		return sdfrmt.format(calendar.getTime());

	}

	public static boolean conformsToDateFormatInText(String format,
			String strDate) {
		/* Check if date is 'null' */
		if (strDate.trim().equals("")) {
			return false;
		}
		/* Date is not 'null' */

		/*
		 * Set preferred date format, For example MM-dd-yyyy,
		 * MM.dd.yyyy,dd.MM.yyyy etc.
		 */
		SimpleDateFormat sdfrmt = new SimpleDateFormat(format);
		sdfrmt.setLenient(false);
		/* parse the string into date form */
		try {
			sdfrmt.parse(strDate);
		}
		/* Date format is invalid */
		catch (ParseException e) {
			// System.out.println("The date you provided is in an "
			// + "invalid date format. " + format + " for " + strDate);
			return false;
		}
		/* Return 'true' - since date is in valid format */
		return true;

	}
	
	public static String getNumberFromURL(String url){
	//	"http://www.ebay.co.uk/sch/ACEO-Art-Cards-/121468/i.html"
		if(url == null || url.isEmpty())return "";
		String tok = "-/";
		int beginIndex = url.indexOf(tok);
		if(beginIndex <0 )return "";
		beginIndex += tok.length();
		int endIndex=beginIndex;
		for(int i = beginIndex; i < url.length(); i++){
			if( Character.isDigit(url.charAt(i)))endIndex++;
			else break;			
		}
		if(beginIndex == endIndex )return "";
		return url.substring(beginIndex, endIndex);
	}
	
	public static String checkPageNumberParam(String url){
		
		String pageParam = "_pgn=";
		if(! url.contains(pageParam)){
			return url;
		}
		String newUrl = url;
			int beginIndex = url.indexOf(pageParam)+ pageParam.length();
			//System.out.println("Begin index " +beginIndex);
			String thePageNumberParam = url.substring(beginIndex , (beginIndex+2));
			//System.out.println("thePageNumberParam " +thePageNumberParam);
			if( NumberUtils.isNumeric(thePageNumberParam)){				
				if( Integer.valueOf(thePageNumberParam) > 49){
					//System.out.println("The page number " +thePageNumberParam+" is above 49 "  );
					newUrl = new StringBuilder(url).replace(beginIndex, (beginIndex+2), "49").toString();
				}
			}
			
			//System.out.println("Returning "+ newUrl);
			return newUrl;			
	}

}
