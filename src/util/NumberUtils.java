package util;

public class NumberUtils {
	private static final String regex = "[0-9]+"; 
	
	public static boolean isNumeric(String str) {
		if( str == null)return false;
		return str.replace(",", "").replace(".", "").trim().matches(regex);
	}
}
