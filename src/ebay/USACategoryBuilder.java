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

public class USACategoryBuilder {

	public USACategoryBuilder() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		new USACategoryBuilder().fucktitty("http://www.ebay.com/sch/allcategories/all-categories/?_rdc=1");
	}

	String fuckWithURI(String uri) {
		if (uri.endsWith("i.html")) {
			return uri;
		} else {
			return uri.concat("/i.html");
		}
	}

	private void fucktitty(String url) throws IOException {

		TimeTaken t = new TimeTaken("Fucktitty");
		t.start();
		org.dom4j.Document xmlDocument = DocumentHelper.createDocument();
		org.dom4j.Element root = xmlDocument.addElement("categories");

		// Business, Office & Industrial > Agriculture/ Farming > Farm
		// Implements & Equipment
		Connection.Response response = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(8000).execute();
		//System.out.println("Jsoup parsing");
		Document document = response.parse();

		Map<String, String> cookies = response.cookies();
		//<a class="ch" href="http://www.ebay.com/chp/Antiquities-/37903">Antiquities</a>
		Elements categories = document.select("a[class=ch]");
		System.out.println("All categories " + categories.size());
		int categoryId = 1;

		for (Element category : categories) {
			try {
				org.dom4j.Element categoryElement = root.addElement("category");
				categoryElement.setText(category.text());
				System.out.println("Category " + category.text() + " " + category.attr("abs:href"));

				response = Jsoup.connect(category.attr("abs:href")).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").cookies(cookies).timeout(8000).execute();
				document = response.parse();
				//document = conn.url(category.attr("abs:href")).timeout(0).cookies(cookies).get();

				cookies = response.cookies();
				Elements subCategories = followCatLink(document);
				//System.out.println("Sub categories " + subCategories.size());
				if (!subCategories.isEmpty()) {// more fucking links
					for (Element subCategory : subCategories) {
						org.dom4j.Element subCategoryElement = categoryElement.addElement("first-level-subcategory");
						subCategoryElement.setText(subCategory.text());
						//System.out.println("Sub Category "+ subCategory.text() +" " +subCategory.attr("abs:href"));

						response = Jsoup.connect(subCategory.attr("abs:href")).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").cookies(cookies).timeout(8000).execute();
						document = response.parse();
						cookies = response.cookies();
						//document = conn.url(subCategory.attr("abs:href")).timeout(0).cookies(cookies).get();
						//cookies = conn.response().cookies();
						Elements subSubCategories = followCatLink(document);
						//System.out.println("Subsub categories " + subSubCategories.size());
						if (!subSubCategories.isEmpty()) {// more fucking links
							for (Element subSubCategory : subSubCategories) {
								org.dom4j.Element subSubCategoryElement = subCategoryElement.addElement("second-level-subcategory");
								subSubCategoryElement.setText(subSubCategory.text());
								//System.out.println("Sub sub Category "+ subCategory.text() +" " + subSubCategory.attr("abs:href"));
								response = Jsoup.connect(subSubCategory.attr("abs:href")).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").cookies(cookies).timeout(8000).execute();
								document = response.parse();
								cookies = response.cookies();
								//document = conn.url(subSubCategory.attr("abs:href")).timeout(0).cookies(cookies).get();
								//cookies = conn.response().cookies();
								Elements subSubSubCategories = followCatLink(document);
								//System.out.println("Second level sub categories " + subSubSubCategories.size());
								// if(true)break outer;
								if (!subSubSubCategories.isEmpty()) {
									for (Element subSubSubCategory : subSubSubCategories) {
										org.dom4j.Element subSubSubCategoryElement = subSubCategoryElement.addElement("third-level-subcategory");
										subSubSubCategoryElement.setText(subSubSubCategory.text());

										response = Jsoup.connect(subSubSubCategory.attr("abs:href")).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").cookies(cookies).timeout(8000).execute();
										document = response.parse();
										cookies = response.cookies();
										//document = conn.url(subSubSubCategory.attr("abs:href")).timeout(0).cookies(cookies).get();
										//	cookies = conn.response().cookies();
										Elements subSubSubSubCategories = followCatLink(document);
										if (!subSubSubSubCategories.isEmpty()) {
											for (Element subSubSubSubCategory : subSubSubSubCategories) {
												org.dom4j.Element subSubSubSubCategoryElement = subSubSubCategoryElement.addElement("forth-level-subcategory");
												subSubSubSubCategoryElement.setText(subSubSubCategory.text());

												response = Jsoup.connect(subSubSubSubCategory.attr("abs:href")).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").cookies(cookies).timeout(8000).execute();
												document = response.parse();
												cookies = response.cookies();
												//document = conn.url(subSubSubSubCategory.attr("abs:href")).timeout(0).cookies(cookies).get();
												//cookies = conn.response().cookies();
												Elements subSubSubSubSubCategories = followCatLink(document);
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
			} catch (IOException e) {
				e.printStackTrace();
				try {
					OutputFormat format = OutputFormat.createPrettyPrint();
					XMLWriter writerr = new XMLWriter(System.out, format);
					writerr.write(xmlDocument);
					XMLWriter writer = new XMLWriter(new FileWriter("ebad-yank-config.xml"));
					writer.write(xmlDocument);
					writer.close();
				} catch (Exception e1) {
				}
				try {
					Thread.sleep(10000);

				} catch (Exception n) {
				}
			}
		}

		t.finish();

		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writerr = new XMLWriter(System.out, format);
		writerr.write(xmlDocument);
		XMLWriter writer = new XMLWriter(new FileWriter("ebad-yank-config.xml"));
		writer.write(xmlDocument);
		writer.close();
		System.out.println(t);
	}

	private Elements followCatLink(Document document) throws IOException {
		//System.out.println(document.html());
		//<li style="" class="gsfs"><a href="http://www.ebay.com/chp/The-Americas-/37908" _sp="p2051337.m1685.c1">The Americas</a> </li>

		Elements links = document.select("ul[class=gscn gsfo").select("li").select("a[href]");
		return links;
	}
}
