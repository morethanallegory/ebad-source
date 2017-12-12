package ebay;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.TimeTaken;

public class UKCategoryBuilder {

	public UKCategoryBuilder() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new UKCategoryBuilder().fucktitty("http://www.ebay.co.uk/sch/allcategories/all-categories/?_rdc=1");
	}

	private void fucktitty(String url) throws IOException{

		TimeTaken t = new TimeTaken("Fucktitty");
		t.start();
		org.dom4j.Document xmlDocument = DocumentHelper.createDocument();
		org.dom4j.Element root = xmlDocument.addElement("categories");
		// Business, Office & Industrial > Agriculture/ Farming > Farm
		// Implements & Equipment
		// Antiques > Antique Clocks > Bracket Clocks > Pre-1900

		
			Connection conn = Jsoup.connect(url.toString());
			conn.timeout(0);

			Document document = conn.get();
			Map<String, String> cookies = conn.response().cookies();
			System.out.println("Cookies "+cookies);
			//<a class="ch" href="http://www.ebay.com/chp/Antiquities-/37903">Antiquities</a>
			Elements categories = document.select("a[href*=i.html][class=ch]");
			int categoryId = 1;
			for (Element category : categories) {
				org.dom4j.Element categoryElement = root.addElement("category");
				categoryElement.setText(category.text());
				document = conn.url(category.attr("abs:href")).timeout(0).cookies(cookies).get();

				cookies = conn.response().cookies();
				Elements subCategories = followCatLink(conn, document);
				if (!subCategories.isEmpty()) {// more fucking links
					for (Element subCategory : subCategories) {
						org.dom4j.Element subCategoryElement = categoryElement.addElement("first-level-subcategory");
						subCategoryElement.setText(subCategory.text());

						document = conn.url(subCategory.attr("abs:href")).timeout(0).cookies(cookies).get();
						cookies = conn.response().cookies();
						Elements subSubCategories = followCatLink(conn, document);
						if (!subSubCategories.isEmpty()) {// more fucking links
							for (Element subSubCategory : subSubCategories) {
								org.dom4j.Element subSubCategoryElement = subCategoryElement.addElement("second-level-subcategory");
								subSubCategoryElement.setText(subSubCategory.text());

								document = conn.url(subSubCategory.attr("abs:href")).timeout(0).cookies(cookies).get();
								cookies = conn.response().cookies();
								Elements subSubSubCategories = followCatLink(conn, document);
								// if(true)break outer;
								if (!subSubSubCategories.isEmpty()) {
									for (Element subSubSubCategory : subSubSubCategories) {
										org.dom4j.Element subSubSubCategoryElement = subSubCategoryElement.addElement("third-level-subcategory");
										subSubSubCategoryElement.setText(subSubSubCategory.text());

										document = conn.url(subSubSubCategory.attr("abs:href")).timeout(0).cookies(cookies).get();
										cookies = conn.response().cookies();
										Elements subSubSubSubCategories = followCatLink(conn, document);
										if (!subSubSubSubCategories.isEmpty()) {
											for (Element subSubSubSubCategory : subSubSubSubCategories) {
												org.dom4j.Element subSubSubSubCategoryElement = subSubSubCategoryElement.addElement("forth-level-subcategory");
												subSubSubSubCategoryElement.setText(subSubSubCategory.text());

												document = conn.url(subSubSubSubCategory.attr("abs:href")).timeout(0).cookies(cookies).get();
												cookies = conn.response().cookies();
												Elements subSubSubSubSubCategories = followCatLink(conn, document);
												if (!subSubSubSubSubCategories.isEmpty()) {
													for (Element subSubSubSubSubCategory : subSubSubSubSubCategories) {
														org.dom4j.Element subSubSubSubSubCategoryElement = subSubSubSubCategoryElement.addElement("fifth-level-subcategory");
														subSubSubSubSubCategoryElement.setText(subSubSubSubCategory.text());
														subSubSubSubSubCategoryElement.addElement("url").addAttribute("selected", "false").addAttribute("id", String.valueOf(categoryId++)).addCDATA(subSubSubSubSubCategory.attr("abs:href"));
														// now give up burrowing
														System.out.println(subSubSubSubSubCategory.attr("abs:href"));
													}
												} else {
													subSubSubSubCategoryElement.addElement("url").addAttribute("selected", "false").addAttribute("id", String.valueOf(categoryId++)).addCDATA(subSubSubSubCategory.attr("abs:href"));
													System.out.println(subSubSubSubCategory.attr("abs:href"));
												}
											}
										} else {
											subSubSubCategoryElement.addElement("url").addAttribute("selected", "false").addAttribute("id", String.valueOf(categoryId++)).addCDATA(subSubSubCategory.attr("abs:href"));
											System.out.println(subSubSubCategory.attr("abs:href"));
										}
									}
								} else {
									subSubCategoryElement.addElement("url").addAttribute("selected", "false").addAttribute("id", String.valueOf(categoryId++)).addCDATA(subSubCategory.attr("abs:href"));
									System.out.println(subSubCategory.attr("abs:href"));
								}
							}
						} else {
							subCategoryElement.addElement("url").addAttribute("selected", "false").addAttribute("id", String.valueOf(categoryId++)).addCDATA(subCategory.attr("abs:href"));
							System.out.println(subCategory.attr("abs:href"));
						}
					}

				} else {
					// should never be true as this would mean no top level cats
					categoryElement.addElement("url").addAttribute("selected", "false").addAttribute("id", String.valueOf(categoryId++)).addCDATA(category.attr("abs:href"));
					System.out.println(category.attr("abs:href"));
				}
			}
		
		t.finish();
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writerr = new XMLWriter(System.out, format);
		writerr.write(xmlDocument);
		XMLWriter writer = new XMLWriter(new FileWriter("ebad-config.xml"));
		writer.write(xmlDocument);
		writer.close();
		System.out.println(t);
	}

	private Elements followCatLink(Connection conn, Document document) throws IOException {
		Elements links = document.select("div[class=cat-link]").select("a[href*=i.html]");
		return links;
	}
}
