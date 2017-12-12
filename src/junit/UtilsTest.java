package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.NumberUtils;
import util.StringUtils;

public class UtilsTest {
	@Test
	public void isNumericTest() {
		assertTrue(NumberUtils.isNumeric("123"));
		assertTrue(NumberUtils.isNumeric("1.23"));
		assertTrue(NumberUtils.isNumeric("12.3"));
		assertFalse(NumberUtils.isNumeric("1s.23"));
		assertTrue(NumberUtils.isNumeric("13554,5364"));

	}

	@Test
	public void isValidDateFormatTest() {
		String format = "dd/mm/yyyy";
		String date = "29/10/2014";
		assertTrue(StringUtils.conformsToDateFormatInText(format, date));
		String aFormat = "d-MMM h:mm";
		String aDate = "29-Oct 01:53";
		assertTrue(StringUtils.conformsToDateFormatInText(aFormat, aDate));
		String bFormat = "d-MMM h:mm";
		String bDate = "29-October";
		assertFalse(StringUtils.conformsToDateFormatInText(bFormat, bDate));

	}
	
	@Test 
	public void urlManipulationTest(){
		String page49 = "http://www.ebay.co.uk/sch/Art-/550/i.html?_from=R40|R40|R40|R40|R40|R40|R40|R40|R40&_nkw=&_udlo=&_udhi=&LH_Auction=1&LH_Time=1&_ftrt=901&_ftrv=48&_sabdlo=&_sabdhi=&_samilow=&_samihi=&_sadis=15&_stpos=&_sop=12&_dmd=1&_pgn=49&_skc=9800&rt=nc";
	   
		String page50 = "http://www.ebay.co.uk/sch/Art-/550/i.html?_from=R40|R40|R40|R40|R40|R40|R40|R40|R40&_nkw=&_udlo=&_udhi=&LH_Auction=1&LH_Time=1&_ftrt=901&_ftrv=48&_sabdlo=&_sabdhi=&_samilow=&_samihi=&_sadis=15&_stpos=&_sop=12&_dmd=1&_pgn=50&_skc=9800&rt=nc";
		   
		String page9 = "http://www.ebay.co.uk/sch/Art-/550/i.html?_from=R40|R40|R40|R40|R40|R40|R40|R40|R40&_nkw=&_udlo=&_udhi=&LH_Auction=1&LH_Time=1&_ftrt=901&_ftrv=48&_sabdlo=&_sabdhi=&_samilow=&_samihi=&_sadis=15&_stpos=&_sop=12&_dmd=1&_pgn=9&_skc=9800&rt=nc";
		   
		String page20 = "http://www.ebay.co.uk/sch/Art-/550/i.html?_from=R40|R40|R40|R40|R40|R40|R40|R40|R40&_nkw=&_udlo=&_udhi=&LH_Auction=1&LH_Time=1&_ftrt=901&_ftrv=48&_sabdlo=&_sabdhi=&_samilow=&_samihi=&_sadis=15&_stpos=&_sop=12&_dmd=1&_pgn=20&_skc=9800&rt=nc";
		
		assertEquals( page49, StringUtils.checkPageNumberParam(page49));
		assertNotEquals( page50, StringUtils.checkPageNumberParam(page50));
		assertEquals( page9, StringUtils.checkPageNumberParam(page9));
		assertEquals( page20, StringUtils.checkPageNumberParam(page20));
	    
	}
}
