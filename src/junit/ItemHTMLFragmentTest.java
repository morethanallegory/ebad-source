package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import network.IO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.BeforeClass;
import org.junit.Test;

import util.StringUtils;
import ebay.ValidDateFormats;

public class ItemHTMLFragmentTest implements ValidDateFormats {
	static Document fragmentOne;
	static Document fragmentTwo;
	static Document fragmentThree;
	static Document usaFragmentOne;
	static Document usaFragmentTwo;
	static Document usaFragmentThree;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fragmentOne = Jsoup.parse(IO.readFile("item-fragment.html"));
		fragmentTwo = Jsoup.parse(IO.readFile("item-fragment2.html"));
		fragmentThree = Jsoup.parse(IO.readFile("item-fragment3.html"));
		usaFragmentOne = Jsoup.parse(IO.readFile("usa-item-fragment1.html"));
		usaFragmentTwo = Jsoup.parse(IO.readFile("usa-item-fragment2.html"));
		usaFragmentThree = Jsoup.parse(IO.readFile("usa-item-fragment3.html"));
	}

	@Test
	public void testGetUSAItemCount() {
		Element itemCount = usaFragmentOne.select(".rcnt").first();
		assertNotNull(itemCount);
		assertEquals(itemCount.text(), "174");
	}

	@Test
	public void testGetUSATitle() {
		Element title = usaFragmentOne.select(".lvtitle").first();
		assertNotNull(title);
		assertFalse(title.text().trim().isEmpty());
	}

	@Test
	public void testGetUSAPrice() {
		Element el = usaFragmentOne.select(".g-b").first();
		assertNotNull(el);
		assertFalse(el.text().trim().isEmpty());
	}//<ul class="lvdetails

	@Test
	public void testGetUSAShipping() {//different from uk version
		Element el = usaFragmentOne.select(".lvdetails").first().select("li:nth-child(2)").first();
		assertNotNull(el);
		assertFalse(el.text().trim().isEmpty());
		System.out.println(el.text());

		el = usaFragmentTwo.select(".lvdetails").first().select("li:nth-child(2)").first();
		assertNotNull(el);
		assertFalse(el.text().trim().isEmpty());
		System.out.println(el.text());


		el = usaFragmentThree.select(".lvdetails").first();
		assertNotNull(el);
		el = el.select("li:nth-child(2)").first();
		assertNotNull(el);
		System.out.println(el.text());

	}

	@Test
	public void testGetUSAURL() {
		Element el = usaFragmentOne.select("[href]").first();
		assertNotNull(el);
		assertFalse(el.attr("href").trim().isEmpty());
	}

	@Test
	public void testGetUSAImageURL() {
		//two image classes used. img and stockImg
		Element el = usaFragmentOne.select("img[src]").first();
		assertNotNull(el);
		assertFalse(el.attr("src").trim().isEmpty());
	}

	@Test
	public void testGetUSABids() {
		Element el = usaFragmentOne.select(".bids").first();
		assertNotNull(el);
		assertFalse(el.text().trim().isEmpty());
	}

	@Test
	public void testGetUSATime() {

		// 01:53
		String string = null;

		Element time = usaFragmentOne.select("span.tme").first();
		if (time != null && !time.text().trim().isEmpty()) {
			string = time.text();
			assertTrue(StringUtils.conformsToDateFormatInText(USA_FORMAT_TEXT, string));
		}

		// if string is still unassigned it's because they have changed the time
		// format
		// perhaps to milliseconds...again

		if (string == null) {// a[href], [title]
			time = fragmentOne.select("[timems]").first();
			if (time != null) {
				assertNotNull(time);
				string = time.attr("timems");
				assertNotNull(StringUtils.conformsToDateFormatInMillis(FORMAT_MILLIS, string));
			}
		}
		assertNotNull(string);
		// System.out.println(StringUtils.conformsToDateFormatInMillis(formatMillis,
		// string));

	}

	/*
	 * 
	 * <span class="tme" > <span aria-label="Ending time: " class="red">29-Oct
	 * 01:53</span> </span>
	 * 
	 * OR
	 * 
	 * <span class="tme"> <span aria-label="Ending time: "
	 * class="SECONDS timeMs" timems="1414655378000"> </span>
	 */
	@Test
	public void testNextLink() {
		Element nextLink = fragmentOne.select(".next").first();
		assertNotNull(nextLink);
		assertFalse(nextLink.attr("href").trim().isEmpty());
	}

	@Test
	public void testGetItemCount() {
		Element itemCount = fragmentOne.select(".rcnt").first();
		assertNotNull(itemCount);
		assertEquals(itemCount.text(), "54");
	}

	@Test
	public void testGetTitle() {
		Element title = fragmentOne.select(".lvtitle").first();
		assertNotNull(title);
		assertFalse(title.text().trim().isEmpty());
	}

	@Test
	public void testGetPrice() {
		Element el = fragmentOne.select(".g-b").first();
		assertNotNull(el);
		assertFalse(el.text().trim().isEmpty());
	}

	@Test
	public void testGetShipping() {
		Element el = fragmentOne.select(".ship").first();
		assertNotNull(el);
		assertFalse(el.text().trim().isEmpty());
	}

	@Test
	public void testGetURL() {
		Element el = fragmentOne.select("[href]").first();
		assertNotNull(el);
		assertFalse(el.attr("href").trim().isEmpty());
	}

	@Test
	public void testGetImageURL() {
		//two image classes used. img and stockImg
		Element el = fragmentOne.select("img[src]").first();
		assertNotNull(el);
		assertFalse(el.attr("src").trim().isEmpty());
	}

	@Test
	public void testGetImageURL2() {
		//two image classes used. img and stockImg
		Element el = fragmentTwo.select("img[imgurl]").first();
		assertNotNull(el);
		assertFalse(el.attr("imgurl").trim().isEmpty());
	}

	@Test
	public void testGetImageURL3() {
		//two image classes used. img and stockImg
		Element el = fragmentThree.select("img[src]").first();
		assertNotNull(el);
		assertFalse(el.attr("src").trim().isEmpty());
	}

	@Test
	public void testGetBids() {
		Element el = fragmentOne.select(".bids").first();
		assertNotNull(el);
		assertFalse(el.text().trim().isEmpty());
	}

	@Test
	public void testGetTime() {
		// 29-Oct 01:53
		// 29-Oct
		// 01:53
		String string = null;

		Element time = fragmentOne.select("span.tme").first();
		if (time != null && !time.text().trim().isEmpty()) {
			string = time.text();
			assertTrue(StringUtils.conformsToDateFormatInText(FORMAT_TEXT, string));
		}

		// if string is still unassigned it's because they have changed the time
		// format
		// perhaps to milliseconds...again

		if (string == null) {// a[href], [title]
			time = fragmentOne.select("[timems]").first();
			if (time != null) {
				assertNotNull(time);
				string = time.attr("timems");
				assertNotNull(StringUtils.conformsToDateFormatInMillis(FORMAT_MILLIS, string));
			}
		}
		assertNotNull(string);
		// System.out.println(StringUtils.conformsToDateFormatInMillis(formatMillis,
		// string));

	}

}
