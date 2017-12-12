package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import network.IO;

import org.dom4j.Node;

import util.EmailErrorReporter;
import bus.CommsBus;
import bus.CommsBusListener;
import database.DatabaseManager;
import ebay.Category;
import ebay.ItemView;
import ebay.ResultsPageBuilder;

public class Application {

	public static void openHelp() {
		File file = new File(FileAndPathNames.HELP_PAGE_PATH.toString());
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				CommsBus.messageSent("Cannot open help: " + e);
				EmailErrorReporter.addError(e, "Something went wrong while opening help file ", Application.class.getName(), "openHelp");
			}
		} else {
			CommsBus.messageSent("No browser support for help");
		}
	}

	public static void addMessageListener(CommsBusListener listener) {
		CommsBus.addMessageListener(listener);
	}

	public static void addErrorMessageListener(CommsBusListener listener) {
		CommsBus.addErrorMessageListener(listener);
	}

	public static void saveAllSelectionsToConfig(String wordRestriction, Object domain, List<Category> categories) throws ConfigurationException {
		Config.saveSelectedWordRestriction(wordRestriction);
		Config.saveSelectedDomain((String) domain);

		//not actually using pre selection in gui yet
		//saveSelectedCategoriesToConfig(categories);
	}
	
	public static void saveConfig()throws ConfigurationException {
		Config.saveConfig();
	}

	private static void saveSelectedCategoriesToConfig(List<Category> categories) throws ConfigurationException {
		Config.setAllCategoriesSelected("false");
		Config.setCategoriesSelected(categories);
	}

	public static boolean showSearchresults() {
		paginateSearchResults();
		if (Desktop.isDesktopSupported()) {
			try {
				File file = IO.getFile(FileAndPathNames.RESULTS_PAGE_PATH.toString());
				Desktop.getDesktop().open(file);
				return true;
			} catch (IOException e) {
				CommsBus.errorMessageSent("Cannot open: " + e);
				EmailErrorReporter.addError(e, "Something went wrong while showing search results ", Application.class.getName(), "showSearchResults");

				return false;
			}
		} else {
			CommsBus.messageSent("No browser support for opening results page.");
			return false;
		}
	}

	private static void paginateSearchResults() {
		try {
			DatabaseManager manager = new DatabaseManager();
			List<ItemView> items = manager.fetchAll(new Integer(Config.getSelectedWordRestriction()));
			ResultsPageBuilder pageBuilder = new ResultsPageBuilder();
			String resultsPage = pageBuilder.buildPage(items);
			IO.writeFile(FileAndPathNames.RESULTS_PAGE_PATH.toString(), resultsPage);
			manager.disconnect();

			// Zip.zip(Config.getHTMLFilename(),
			// Config.getCompressedFilename());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommsBus.errorMessageSent(e.getLocalizedMessage());
			EmailErrorReporter.addError(e, "Something went wrong while paginating results ", Application.class.getName(), "paginateSearchResults");
			
		}
	}

	public static void shutdown() {
		new DatabaseManager().shutdown();// clears tables and disconnects
	}

	// any application specific init goes here
	public static void initialize() throws SecurityException, IOException, SQLException, ConfigurationException {

		IO.createDir(FileAndPathNames.USER_SUBDIR.toString());
		IO.copyTextFile(FileAndPathNames.CONFIG_FILE_PATH.toString(), FileAndPathNames.CONFIG_FILE_NAME.toString(), "UTF8");
		IO.copyTextFile(FileAndPathNames.CATEGORY_CONFIG_FILE_PATH.toString(), FileAndPathNames.CATEGORY_CONFIG_FILE_NAME.toString(), "UTF8");
		IO.copyTextFile(FileAndPathNames.USA_CATEGORY_CONFIG_FILE_PATH.toString(), FileAndPathNames.USA_CATEGORY_CONFIG_FILE_NAME.toString(), "UTF8");

		Config.init();

		IO.copyTextFile(FileAndPathNames.HELP_PAGE_PATH.toString(), FileAndPathNames.HELP_FILE_NAME.toString(), "UTF8");
		IO.copyImageFile(FileAndPathNames.NO_IMAGE_PATH.toString(), FileAndPathNames.NO_IMAGE_FILE_NAME.toString(), "gif");
		IO.copyImageFile(FileAndPathNames.LOGO_PATH.toString(), FileAndPathNames.LOGO_FILE_NAME.toString(), "gif");
		
		DatabaseManager.createSchema();
		//IO.copyFile(FileAndPathNames.LOGO_PATH.toString(), FileAndPathNames.LOGO_FILE_NAME.toString(), null);
		//IO.copyFile(FileAndPathNames.TREE_ICON_PATH.toString(), FileAndPathNames.TREE_FILE_NAME.toString(), null);
		//IO.copyFile(FileAndPathNames.TRAY_ICON_PATH.toString(), FileAndPathNames.TRAY_FILE_NAME.toString(), null);

	}
	public static String getEmailSubject() throws ConfigurationException {
		return Config.getEmailSubject();
	}

	public static String getEmailBody() throws ConfigurationException {
		return Config.getEmailBody();
	}

	public static String getEmailUsername() throws ConfigurationException {
		return Config.getEmailUsername();
	}

	public static String getEmailPassword() throws ConfigurationException {
		return Config.getEmailPassword();
	}

	public static String getEmailFrom() throws ConfigurationException {
		return Config.getEmailFrom();
	}

	public static String[] getEmailRecipients() throws ConfigurationException {
		return Config.getEmailRecipients();
	}
	
	public static int getSelectedWordRestriction() {
		return Config.getSelectedWordRestriction();
	}
	
	public static String getDatabaseUser() throws ConfigurationException {
		return Config.getDatabaseUser();
	}

	public static String getDatabasePassword() throws ConfigurationException {
		return Config.getDatabasePassword();
	}

	public static String getDatabaseConnectionVariables() throws ConfigurationException {
		return Config.getDatabaseConnectionVariables();
	}

	public static String getDatabaseName() throws ConfigurationException {
		return Config.getDatabaseName();
	}

	public static String getDatabaseDriver() throws ConfigurationException {
		return Config.getDatabaseDriver();
	}

	public static String getDatabaseUrl() throws ConfigurationException{
		return Config.getDatabaseUrl();		
	}
	
	public static void saveSelectedWordRestriction(String wordRestriction) throws ConfigurationException {
	 Config.saveSelectedWordRestriction(wordRestriction);
	}
	public static void saveSelectedDomain(String domain) throws ConfigurationException {
		Config.saveSelectedDomain(domain);
	}
	
	public static String getSelectedDomain() {
		return Config.getSelectedDomain();
	}
	
	public static boolean switchedToCoUk(){
		return getSelectedDomain().equalsIgnoreCase(FileAndPathNames.UK_DOMAIN.toString());
	}
	
	public static LinkedHashMap<String, Category> getCategories(String selectedDomain) throws ConfigurationException {
		return Config.getCategories( selectedDomain);
	}
	
	public static void setAllCategoriesActiveAttribute(String boolValue) throws ConfigurationException {
		Config.setAllCategoriesSelected(boolValue);
	}
	
	public static void setCategoriesSelected(List<Category> categories) throws ConfigurationException {
		Config.setCategoriesSelected(categories);
	}
	
	public static List<Node> getActiveCategories() throws ConfigurationException {
		return Config.getActiveCategories();
	}
	
	public static Node getCategoryNode(String id) throws ConfigurationException {
		return Config.getCategoryNode(id);
	}
	
	public static String[] getDomains() throws ConfigurationException {
		return Config.getDomains();
	}
  
}
