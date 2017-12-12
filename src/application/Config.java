package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import network.IO;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import bus.CommsBus;
import ebay.Category;

class Config {
	private static Document configDocument = null;
	private static Document categoryConfigDocument = null;
	private static Document usaCategoryConfigDocument = null;
	private static Document hiddenConfigDocument = null;
	private static Document currentCategoryDocumentPointer;

	//public static final String configFileName =;
	//public static final String userDataDirName = "ebad";

	private static Document readConfigFile(String fileName) throws ConfigurationException {
		File xml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("UTF-8");
		try {
			return saxReader.read(xml);
		} catch (DocumentException e) {
			throw new ConfigurationException(e);
		}
	}

	private static void readConfig() throws ConfigurationException {
		configDocument = readConfigFile(FileAndPathNames.CONFIG_FILE_PATH.toString());
	}

	private static void readCategoryConfig() throws ConfigurationException {
		categoryConfigDocument = readConfigFile(FileAndPathNames.CATEGORY_CONFIG_FILE_PATH.toString());
	}

	private static void readUSACategoryConfig() throws ConfigurationException {
		usaCategoryConfigDocument = readConfigFile(FileAndPathNames.USA_CATEGORY_CONFIG_FILE_PATH.toString());
	}

	private static void readHiddenConfig() throws ConfigurationException {
		Reader reader = null;
		try {
			reader = IO.filefromJar(FileAndPathNames.HIDDEN_CONFIG_FILE_NAME.toString(), "UTF-8");
			SAXReader saxParser = new SAXReader();
			hiddenConfigDocument = saxParser.read(reader);

		} catch (IOException | DocumentException e) {
			throw new ConfigurationException(e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
			}
		}
	}

	private static void refresh() throws ConfigurationException {
		//System.out.println("Refreshing config");
		readConfig();
	}

	static void saveConfig() throws ConfigurationException {
		//System.out.println("Saving config");

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(FileAndPathNames.CONFIG_FILE_PATH.toString()), "UTF8"), format);
			xmlWriter.write(configDocument);
			xmlWriter.flush();
			xmlWriter.close();
			refresh();
		} catch (IOException e) {
			e.printStackTrace();
			CommsBus.messageSent("Unable to write config: " + e.getMessage());
			throw new ConfigurationException(e);
		}
	}

	static void setAllCategoriesSelected(String boolValue) throws ConfigurationException {
		@SuppressWarnings("unchecked")
		List<Node> cats = currentCategoryDocumentPointer.selectNodes("//@selected");
		for (Iterator<Node> iter = cats.iterator(); iter.hasNext();) {
			Node attribute = (Attribute) iter.next();
			attribute.setText(boolValue);
		}
	}

	static void setCategoriesSelected(List<Category> categories) throws ConfigurationException {
		for (Category category : categories) {
			Node node = currentCategoryDocumentPointer.selectSingleNode("//url[@id='" + category.getId() + "']");//"//contains(.,'"+name+"')");
			if (node != null) {
				node.selectSingleNode("@selected").setText("true");
			}
		}
	}

	static Node getCategoryNode(String id) throws ConfigurationException {
		return currentCategoryDocumentPointer.selectSingleNode("//url[@id='" + id + "']");
	}

	static void saveSelectedWordRestriction(String wordRestriction) throws ConfigurationException {
		configDocument.selectSingleNode("//max-word-count").setText(wordRestriction);
	}

	static void saveSelectedDomain(String domain) throws ConfigurationException {
		configDocument.selectSingleNode("//selected-domain").setText(domain);
		Config.setCurrentCategoryPointer(domain);
	}

	@SuppressWarnings("unchecked")
	static List<Node> getActiveCategories() throws ConfigurationException {
		return currentCategoryDocumentPointer.selectNodes("//url[@selected='true']");
	}

	static String getDatabaseUser() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//user").getStringValue();
	}

	static String getDatabasePassword() throws ConfigurationException {

		return hiddenConfigDocument.selectSingleNode("//dbpassword").getStringValue();
	}

	static String getDatabaseConnectionVariables() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//variables").getStringValue();
	}

	static String getDatabaseUrl() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//db-url").getStringValue();
	}

	static String getDatabaseName() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//name").getStringValue();
	}

	static String getDatabaseDriver() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//driver").getStringValue();
	}

	static String getEmailSubject() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//subject").getStringValue();
	}

	static String getEmailBody() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//body").getStringValue();
	}

	static String getEmailUsername() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//username").getStringValue();
	}

	static String getEmailPassword() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//password").getStringValue();
	}

	static String getEmailFrom() throws ConfigurationException {
		return hiddenConfigDocument.selectSingleNode("//from").getStringValue();
	}

	/*
		 static String getCompressedFilename() {
			return configDocument.selectSingleNode("//compressed-file")
					.getStringValue();
		}

		 static String getNoImageImageFileName() {
			return configDocument.selectSingleNode("//no-image-file")
					.getStringValue();
		}

		 static String getLogoFileName() {
			return configDocument.selectSingleNode("//logo-file").getStringValue();
		}

		 static String getHelpFileName() {
			return configDocument.selectSingleNode("//help-file").getStringValue();
		}

		 static String getHTMLFilename() {
			return configDocument.selectSingleNode("//html-file").getStringValue();
		}
	*/
	static int getSelectedWordRestriction() {
		return Integer.valueOf(configDocument.selectSingleNode("//max-word-count").getStringValue()).intValue();
	}

	@SuppressWarnings("unchecked")
	static String[] getEmailRecipients() throws ConfigurationException {
		return toArray(hiddenConfigDocument.selectNodes("//recipient"));
	}

	@SuppressWarnings("unchecked")
	static String[] getDomains() throws ConfigurationException {
		return toArray(configDocument.selectNodes("//domains/domain"));
	}

	static String getSelectedDomain() {
		return configDocument.selectSingleNode("//selected-domain").getStringValue();
	}

	private static String[] toArray(List<Node> nodes) {
		String[] arr = new String[nodes.size()];
		int i = 0;
		for (Node node : nodes) {
			arr[i++] = node.getText();
		}
		return arr;
	}


	@SuppressWarnings("unchecked")
	static LinkedHashMap<String, Category> getCategories(String selectedDomain) throws ConfigurationException {
			return buildCategoryMap(currentCategoryDocumentPointer.selectNodes("//category"));
	}

	private static LinkedHashMap<String, Category> buildCategoryMap(List<Node> nodes) throws ConfigurationException {
		LinkedHashMap<String, Category> categories = new LinkedHashMap<String, Category>();

		// int howmanycats = 1;
		for (Node node1 : nodes) {
			// if (howmanycats++ == 3) break;
			// for every top level node create a new category
			Category parent = new Category(node1.getText().trim());
			categories.put(parent.getName(), parent);// add it to the map
			// see if there are any children of this node
			List<Node> nodes2 = node1.selectNodes("child::*");
			for (Node node2 : nodes2) {
				// for each childCategory child;
				if (node2.getName().equalsIgnoreCase("url")) {
					parent.setUrl(node2.getText().trim());
					parent.setActive(Boolean.valueOf(((Element) node2).attributeValue("selected")));
					parent.setId(((Element) node2).attributeValue("id"));
					continue;
				}
				Category child = new Category(node2.getText().trim());
				parent.add(child);
				// see if there are any children of this node
				List<Node> nodes3 = node2.selectNodes("child::*");
				for (Node node3 : nodes3) {
					if (node3.getName().equalsIgnoreCase("url")) {
						child.setUrl(node3.getText().trim());
						child.setActive(Boolean.valueOf(((Element) node3).attributeValue("selected")));
						child.setId(((Element) node3).attributeValue("id"));
						continue;
					}
					Category grandChild = new Category(node3.getText().trim());
					child.add(grandChild);
					// see if there are any children of this node
					List<Node> nodes4 = node3.selectNodes("child::*");
					for (Node node4 : nodes4) {
						if (node4.getName().equalsIgnoreCase("url")) {
							grandChild.setUrl(node4.getText().trim());
							grandChild.setActive(Boolean.valueOf(((Element) node4).attributeValue("selected")));
							grandChild.setId(((Element) node4).attributeValue("id"));
							continue;
						}
						Category greatGrandChild = new Category(node4.getText().trim());

						grandChild.add(greatGrandChild);
						// see if there are any children of this node
						List<Node> nodes5 = node4.selectNodes("child::*");
						for (Node node5 : nodes5) {
							if (node5.getName().equalsIgnoreCase("url")) {
								greatGrandChild.setUrl(node5.getText().trim());
								greatGrandChild.setActive(Boolean.valueOf(((Element) node5).attributeValue("selected")));
								greatGrandChild.setId(((Element) node5).attributeValue("id"));
								continue;
							}
							Category greatGreatGrandChild = new Category(node5.getText().trim());

							greatGrandChild.add(greatGreatGrandChild);
						}
					}

				}
			}
		}
		return categories;

	}
	
	private static void setCurrentCategoryPointer(String selectedDomain){
		if(selectedDomain. equalsIgnoreCase(FileAndPathNames.UK_DOMAIN.toString())){
			currentCategoryDocumentPointer = categoryConfigDocument;
		}else{
			currentCategoryDocumentPointer = usaCategoryConfigDocument;
		}
	}

	static void init() throws ConfigurationException {

		if (hiddenConfigDocument == null) {
			readHiddenConfig();
		}

		if (configDocument == null) {
			readConfig();
		}

		if (categoryConfigDocument == null) {
			readCategoryConfig();
		}

		if (usaCategoryConfigDocument == null) {
			readUSACategoryConfig();
		}
		
		String selectedDomain = getSelectedDomain();
		if(selectedDomain. equalsIgnoreCase(FileAndPathNames.UK_DOMAIN.toString())){
			currentCategoryDocumentPointer = categoryConfigDocument;
		}else{
			currentCategoryDocumentPointer = usaCategoryConfigDocument;
		}
	}
}
