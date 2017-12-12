package ebay;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import network.IO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import util.ExpireProcess;
import application.FileAndPathNames;

public class ResultsPageBuilder {
	private Document resultsPage;

	public ResultsPageBuilder() {
		String temp = IO.readText("results-page-template.html", "UTF-8");
		if (temp != null)
			resultsPage = Jsoup.parse(temp);

	}

	public String buildPage(List<ItemView> items) {
		if (resultsPage == null) {
			return "";
		}
		

		Element allitemsul = resultsPage.getElementById("ListViewInner");

		for (ItemView item : items) {
			//Element body = resultsPage.body();
			Element li = allitemsul.appendElement("li").attr("class", "sresult lvresult clearfix li");

			Element divli1 = li.appendElement("div").attr("class", "lvpic pic p225 img left");

			Element divli2 = divli1.appendElement("div").attr("class", "lvpicinner full-width picW s225");

			Element pic = divli2.appendElement("a").attr("target", "_blank").attr("href", item.getValue("url"));

			pic.appendElement("img").attr("src", item.getValue("image_url")).attr("class", "img");

			Element h3 = li.appendElement("h3").attr("class", "lvtitle");

			Element a = h3.appendElement("a").attr("target", "_blank").attr("href", item.getValue("url")).appendText(item.getValue("title"));

			String time = item.getValue("time");
			if (ExpireProcess.expired(time))
				a.attr("class", "vips");
			else
				a.attr("class", "vip");

			Element divcat = li.appendElement("div").attr("class", "clt");

			divcat.appendElement("h1").attr("class", "rsHdr").appendElement("span").appendText("In ").appendElement("b").appendText(item.getValue("category"));

			Element ul = li.appendElement("ul").attr("class", "lvprices left space-zero");

			ul.appendElement("li").attr("class", "lvprice prc").appendElement("span").attr("class", "g-b").appendText(item.getValue("price"));

			ul.appendElement("li").attr("class", "lvformat bids").appendElement("span").appendText(item.getValue("bids"));

			ul.appendElement("li").attr("class", "lvshipping").appendElement("span").attr("class", "ship").appendElement("span").attr("class", "fee").appendText(item.getValue("shipping"));

			ul.appendElement("li").attr("class", "lvextras");

			li.appendElement("ul").attr("class", "lvdetails left space-zero full-width").appendElement("li").attr("class", "timeleft").appendElement("span").attr("class", "tme").appendElement("span").attr("class", "TOMORROW timeMs").appendText("Ending time: " + time);

		}

		return resultsPage.html();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*
		 item.addItem("wordcount", rs.getString("wordcount"));
				item.addItem("title", rs.getString("title"));
				item.addItem("price", rs.getString("price"));
				item.addItem("bids", rs.getString("bids"));
				item.addItem("shipping", rs.getString("shipping"));
				item.addItem("time", rs.getString("time_left"));
				item.addItem("url", rs.getString("url"));
				item.addItem("image_url", rs.getString("image_url"));
				item.addItem("id", rs.getString("id"));
				item.addItem("category", rs.getString("category"));
		 */
		ItemView itemOne, itemTwo;

		itemOne = new ItemView();
		itemOne.addItem("title", "VILLAGE SCHOOL---UNBOXED-----LOVELY ITEM!!");
		itemOne.addItem("shipping", "+ £4.80 postage");
		itemOne.addItem("image_url", "http://thumbs3.ebaystatic.com/d/l225/m/muGid2-TEhxbbdbmgadowWA.jpg");
		itemOne.addItem("price", "£4.50");
		itemOne.addItem("bids", "1 bid");
		itemOne.addItem("url", "http://www.ebay.co.uk/itm/VILLAGE-SCHOOL-UNBOXED-LOVELY-ITEM-/311119893042?pt=UK_Trains_Railway_Models&hash=item487030d232");
		itemOne.addItem("id", "item4856d45866");
		itemOne.addItem("time", "2/11/2014 19:20:12");
		itemOne.addItem("wordcount", "2");
		itemOne.addItem("category", "Antigues/Stupid train shit/For odd people/Models and shit");

		itemTwo = new ItemView();
		itemTwo.addItem("title", "VILLAGE IDIOT");
		itemTwo.addItem("shipping", "+ £4.90 postage");
		itemTwo.addItem("image_url", "http://thumbs3.ebaystatic.com/d/l225/m/muGid2-TEhxbbdbmgadowWA.jpg");
		itemTwo.addItem("price", "£4.80");
		itemTwo.addItem("bids", "5 bids");
		itemTwo.addItem("url", "http://www.ebay.co.uk/itm/VILLAGE-SCHOOL-UNBOXED-LOVELY-ITEM-/311119893042?pt=UK_Trains_Railway_Models&hash=item487030d232");
		itemTwo.addItem("id", "item487030d232");
		itemTwo.addItem("time", "30/11/2014 19:20:12");
		itemTwo.addItem("wordcount", "1");
		itemTwo.addItem("category", "Models");
		List<ItemView> items = new ArrayList<ItemView>();

		items.add(itemOne);
		items.add(itemTwo);

		String resultsPage = new ResultsPageBuilder().buildPage(items);
		IO.writeFile(FileAndPathNames.RESULTS_PAGE_PATH.toString(), resultsPage);
		File file = IO.getFile(FileAndPathNames.RESULTS_PAGE_PATH.toString());
		Desktop.getDesktop().open(file);
	}
}
