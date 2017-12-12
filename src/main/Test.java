package main;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.dom4j.DocumentException;

public class Test {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws ParseException
	 * @throws DocumentException
	 */
	public static void main(String[] args) throws InterruptedException, IOException, ParseException {
		/*
		String nextLink = "http://www.ebay.co.uk/sch/ACEO-Art-Cards-/121468/i.html";
		
		String sacat = StringUtils.getNumberFromURL(nextLink);
		String params = "?_sacat=".concat(sacat).concat("&LH_Auction=1&_ftrv=48&_ipg=200");
		nextLink = nextLink.concat(params);
		System.out.println(nextLink);
		
		
		InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("ebay.png");
		Image image = ImageIO.read(input);
		
		
		 InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("ebad-config.xml");  
		    BufferedReader rd = new BufferedReader(new InputStreamReader(in));  
		    String s;  
		    try {  
		            while (null != (s = rd.readLine())) {  
		                System.out.println(s);  
		            }  
		    } finally {  
		            rd.close();  
		    }  
		   */
		String s1 = "02/11/2014 22:56:52";
		
		String s2 = "28/12/2014 22:56:52";

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date auctiondate = formatter.parse(s1);
		System.out.println("Past date "+ auctiondate);
		
		Date auctiondate2 = formatter.parse(s2);
		
		System.out.println( "Future date "+ auctiondate2);
		Date now =  Calendar.getInstance().getTime();
		System.out.println( "Now "+ now);
		
		
		System.out.println( now.after(auctiondate) );
		System.out.println( now.after(auctiondate2) );
		
		
		Category cat = new Category() {
			@Override
			public String getName() {
				return "";
			}

			@Override
			public void fetch() {
				// TODO Auto-generated method stub
				
			}
		};
		
	}

}
