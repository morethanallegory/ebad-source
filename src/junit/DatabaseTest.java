package junit;

import static application.Application.initialize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import application.ConfigurationException;
import database.DatabaseManager;
import ebay.ItemView;

public class DatabaseTest {
	static DatabaseManager databaseManager;
	private static ItemView itemOne;
	private static ItemView itemTwo;

	@BeforeClass
	public static void method() throws SecurityException, IOException, SQLException, ConfigurationException {
		initialize();
		itemOne = new ItemView();
		itemOne.addItem("title", "VILLAGE SCHOOL---UNBOXED-----LOVELY ITEM!!");
		itemOne.addItem("shipping", "+ £4.80 postage");
		itemOne.addItem("image_url",
				"http://thumbs3.ebaystatic.com/d/l225/m/muGid2-TEhxbbdbmgadowWA.jpg");
		itemOne.addItem("price", "£4.50");
		itemOne.addItem("bids", "1 bid");
		itemOne.addItem(
				"url",
				"http://www.ebay.co.uk/itm/VILLAGE-SCHOOL-UNBOXED-LOVELY-ITEM-/311119893042?pt=UK_Trains_Railway_Models&hash=item487030d232");
		itemOne.addItem("id", "item4856d45866");
		itemOne.addItem("time", "2014-10-15 16:11:04");

		itemTwo = new ItemView();
		itemTwo.addItem("title", "VILLAGE IDIOT");
		itemTwo.addItem("shipping", "+ £4.80 postage");
		itemTwo.addItem("image_url",
				"http://thumbs3.ebaystatic.com/d/l225/m/muGid2-TEhxbbdbmgadowWA.jpg");
		itemTwo.addItem("price", "£4.50");
		itemTwo.addItem("bids", "1 bid");
		itemTwo.addItem(
				"url",
				"http://www.ebay.co.uk/itm/VILLAGE-SCHOOL-UNBOXED-LOVELY-ITEM-/311119893042?pt=UK_Trains_Railway_Models&hash=item487030d232");
		itemTwo.addItem("id", "item487030d232");
		itemTwo.addItem("time", "2014-10-15 16:11:04");
		databaseManager = new DatabaseManager();

	}
	
	@AfterClass
	public static void close(){
		databaseManager.disconnect();
	}

	@Test
	public void test() throws SQLException {
			databaseManager.persist(itemOne);
			databaseManager.persist(itemTwo);

		assertTrue(databaseManager.existsInItemsViewed(itemOne));
		assertTrue(databaseManager.existsInItemsViewed(itemTwo));

		assertTrue(databaseManager.existsInItems(itemOne));
		assertTrue(databaseManager.existsInItems(itemTwo));

		List<ItemView> itemsStored = databaseManager.fetchAll(2);

		assertTrue(itemsStored.size() == 1);

		itemsStored = databaseManager.fetchAll(1);

		assertTrue(itemsStored.size() == 0);
	}
	
	@Test
	public void counterTest() throws SQLException{	
		assertEquals( databaseManager.getPageCount(), 0);
		databaseManager.incrementPageCount();
		assertEquals( 1, databaseManager.getPageCount());
		databaseManager.incrementPageCount();
		assertEquals(2,  databaseManager.getPageCount());
		
		assertEquals( databaseManager.getCategoryCount(), 0);
		databaseManager.incrementCategoryCount();
		assertEquals( 1, databaseManager.getCategoryCount());
		databaseManager.incrementCategoryCount();
		assertEquals(2,  databaseManager.getCategoryCount());
		
		
		assertEquals( databaseManager.getItemCount(), 0);
		databaseManager.increaseItemCount(20);
		assertEquals( 20, databaseManager.getItemCount());
		databaseManager.increaseItemCount(39);
		assertEquals(59,  databaseManager.getItemCount());
		
	}

}
