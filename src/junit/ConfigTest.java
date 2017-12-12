package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import application.Application;
import application.ConfigurationException;
import application.FileAndPathNames;
import ebay.Category;

public class ConfigTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Application.initialize();
	}

	@Test
	public void countriesTest() throws ConfigurationException {
		String[] countries = Application.getDomains();
		assertTrue(Arrays.asList(countries).contains("www.ebay.co.uk"));
	}

	@Test
	public void readWriteDomainTest()throws ConfigurationException  {
		String []domains = Application.getDomains();
		assertNotNull( domains);
		assertTrue( domains.length == 2);
		String preserveDomain = Application.getSelectedDomain();
		String domain = "www.ebay.co.uk";
		Application.saveSelectedDomain(domain);
		domain = Application.getSelectedDomain();
		assertEquals(domain, "www.ebay.co.uk");
		Application.saveSelectedDomain("www.ebay.com");
		domain = Application.getSelectedDomain();
		assertEquals(domain, "www.ebay.com");

		// finally reset to original
		Application.saveSelectedDomain(preserveDomain);
	}

	@Test
	public void readWriteRestrictedWordCountTest() throws ConfigurationException {
		String preserveWordRestriction = String.valueOf(Application.getSelectedWordRestriction());
		String wordRestriction = "3";
		Application.saveSelectedWordRestriction(wordRestriction);
		wordRestriction = String.valueOf(Application.getSelectedWordRestriction());
		assertEquals(wordRestriction, "3");
		Application.saveSelectedWordRestriction("4");
		wordRestriction = String.valueOf(Application.getSelectedWordRestriction());
		assertEquals(wordRestriction, "4");

		// finally reset to original
		Application.saveSelectedWordRestriction(preserveWordRestriction);
	}

	@Test
	public void setAllCategoriesActiveAttribute() throws ConfigurationException {
		Application.setAllCategoriesActiveAttribute("false");
		Application.getActiveCategories().isEmpty();
		Application.setAllCategoriesActiveAttribute("true");
		assertFalse(Application.getActiveCategories().isEmpty());
	}
	
	@Test
	public void switchCategoriesPointer() throws ConfigurationException {
		String domain = "www.ebay.co.uk";
		Application.saveSelectedDomain(domain);
		assertEquals(FileAndPathNames.UK_DOMAIN.toString(), Application.getSelectedDomain());
		Map<String, Category> categories = Application.getCategories(Application.getSelectedDomain());
		assertFalse(categories.isEmpty());
		Category cat = categories.entrySet().iterator().next().getValue();
		while(cat.hasChildren()){
			cat = cat.getCategories().get(0);
		}
		assertTrue( cat.getUrl().contains(FileAndPathNames.UK_DOMAIN.toString()));
		
		
		domain = "www.ebay.com";
		Application.saveSelectedDomain(domain);
		assertEquals(FileAndPathNames.USA_DOMAIN.toString(), Application.getSelectedDomain());
		categories = Application.getCategories(Application.getSelectedDomain());
		assertFalse(categories.isEmpty());
		cat = categories.entrySet().iterator().next().getValue();
		while(cat.hasChildren()){
			cat = cat.getCategories().get(0);
		}
		assertTrue( cat.getUrl().contains(FileAndPathNames.USA_DOMAIN.toString()));
	}

	@Test
	public void readWriteCategoriesTest() throws ConfigurationException {
		List<Category> categories = new LinkedList<Category>();
		Category c1 = new Category();
		c1.setId("2");
		categories.add(c1);
		Category c2 = new Category();
		c2.setId("4");
		categories.add(c2);

		// first set all categories to be active 'false'
		Application.setAllCategoriesActiveAttribute("false");
		assertTrue(Application.getActiveCategories().isEmpty());

		Application.setCategoriesSelected(categories);

		Node node = Application.getCategoryNode(c1.getId());
		//System.out.println("s "+node.asXML());
		assertEquals(node.selectSingleNode("@selected").getText(), "true");
		node = Application.getCategoryNode(c2.getId());
		//System.out.println("s "+node.asXML());
		assertEquals(node.selectSingleNode("@selected").getText(), "true");
	}

}
