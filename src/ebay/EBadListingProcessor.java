package ebay;

import gui.NewApplicationWindow;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.EmailErrorReporter;
import bus.CommsBus;
import database.DatabaseManager;

public class EBadListingProcessor{
	private DatabaseManager databaseManager = new DatabaseManager();
	

	public void processCategory(Category category) throws IOException {
		int itemsScanned = 0, pagesVisited = 0;
		String numberOfItemsInCategory = null;
		ListingProsessorMessageBuilder message = new ListingProsessorMessageBuilder();
		LinkBuilder linkBuilder  = new LinkBuilder();
		String nextLink = linkBuilder.intitialLink(category.getUrl());
		Map<String, String> cookies =null;
		while (true) {
			if (pagesVisited == 49)
				break;
			if (!NewApplicationWindow.RUN)
				break;
			try {
			//	System.out.println("Jsoup connecting");
				
				Connection connection =  Jsoup.connect(nextLink).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21");
				if( cookies != null){
					connection.cookies(cookies);
				}
				
				Document document = connection.timeout(10000).execute().parse();
				cookies = connection.response().cookies();
				
				Element itemCount = document.select(".rcnt").first();
				if (itemCount != null && itemCount.hasText()) {
					numberOfItemsInCategory = itemCount.text();
				} else {
					numberOfItemsInCategory = null;
				}
			//	System.out.println("Counted items");
				// sresult lvresult clearfix li item class
				Elements items = document.select(".li");
				if (!items.isEmpty()) {
					storeItems(category, items);
				} else {
					System.out.println("No items returned " +nextLink);
					System.out.println("Original link for category was "+category.getUrl());
					//because there are literally no items in this category
					break;
				}
				pagesVisited++;
				itemsScanned += items.size();
				/*
				try {
					databaseManager.incrementPageCount();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					EmailErrorReporter.addError(e1, "Something went wrong while persisting page count "+ category.getName(), this.getClass().getName(), "processCategory");
				}
				*/
				// *******MESSAGE
				CommsBus.messageSent(message.buildMessage(category, itemsScanned, numberOfItemsInCategory));
				// *************
				//System.out.println("Done message");
				Element next = document.select(".next").first();
				if (next == null) {
					break;
				}
				nextLink = linkBuilder.nextLink(next.attr("href"));
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {

				}
				//System.out.println("fuck sleep");
			} catch (HttpStatusException e) {
				e.printStackTrace();
				EmailErrorReporter.addError(e, "Something went wrong while searching through category "+ category.getName(), this.getClass().getName(), "processCategory");

				int responseCode = e.getStatusCode();
				if (responseCode == 404) {
					System.out.println("We were unable to find (" + category.getUrl() + ") " + nextLink);
					EmailErrorReporter.addError(e, "We were unable to find (" + category.getUrl() + ") " + nextLink +" "+ category.getName(), this.getClass().getName(), "processCategory");
					
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				CommsBus.errorMessageSent("Connection timed out for category "+ category.pathSteps()+". Retrying...");
				EmailErrorReporter.addError(e, "Something went wrong while searching through category "+ category.getName(), this.getClass().getName(), "processCategory");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException ezzz) {

				}
			}
		}
	}

	private void storeItems(Category category, Elements items) {
		
		for (Element item : items) {
			ItemView itemView = ItemTransformFactory.getTransform().transformToItemView(item);
			

			if(itemView == null ){
				//couldn't make an interesting item out of it
				continue;
			}
			itemView.addItem("category", category.pathSteps());			

			try {
				databaseManager.persist(itemView);
			} catch (SQLException ex) {
				ex.printStackTrace();
				System.out.println(category.getName());
				System.out.println(itemView);
				System.out.println( item.html());
				
				EmailErrorReporter.addError(ex, "Something went wrong while trying to persist this item "+ itemView + System.lineSeparator()+ item.html()+System.lineSeparator(), this.getClass().getName(), "storeItems");
			}
		}
	}


	public void closeConnections() {
		databaseManager.disconnect();
		// httpManager.close();
	}
}
