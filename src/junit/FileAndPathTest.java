package junit;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Test;

import application.FileAndPathNames;


public class FileAndPathTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		
		assertEquals( "/home/henrose/ebad", FileAndPathNames.USER_SUBDIR.toString());
		assertEquals( "/home/henrose/ebad/ebay-auction.html", FileAndPathNames.RESULTS_PAGE_PATH.toString());
		assertEquals( "/home/henrose/ebad/ebay-auction.gzip", FileAndPathNames.COMPRESSED_RESULTS_PAGE_PATH.toString());
		assertEquals( "/home/henrose/ebad/ebad-config.xml", FileAndPathNames.CONFIG_FILE_PATH.toString());
		assertEquals( "/home/henrose/ebad/imgNoImg.gif", FileAndPathNames.NO_IMAGE_PATH.toString());
	}

}
