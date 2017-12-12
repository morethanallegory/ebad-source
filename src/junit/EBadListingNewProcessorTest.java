package junit;


import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Test;

import ebay.Category;
import ebay.EBadListingProcessor;

public class EBadListingNewProcessorTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testProcessCategory() throws IOException {
		Category c = new Category();
		c.setUrl( "http://www.ebay.co.uk/sch/Clock-Parts-/112089/i.html?_sacat=112089&LH_Auction=1&_ftrv=48&_ipg=200");
		EBadListingProcessor p = new EBadListingProcessor();
		p.processCategory(c);
	}

}
