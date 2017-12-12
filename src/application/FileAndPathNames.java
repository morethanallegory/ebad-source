package application;

public enum FileAndPathNames {
		
	RESULTS_PAGE_PATH(System.getProperty("user.home")
			+ System.getProperty("file.separator") + "ebad"
			+ System.getProperty("file.separator") + "ebay-auction.html"), 
	COMPRESSED_RESULTS_PAGE_PATH(System.getProperty("user.home")
					+ System.getProperty("file.separator") + "ebad"
					+ System.getProperty("file.separator") + "ebay-auction.gzip"), 
	HELP_PAGE_PATH(
			System.getProperty("user.home")
					+ System.getProperty("file.separator")
					+ "ebad"
					+ System.getProperty("file.separator")
					+ "help.html"),
	CONFIG_FILE_PATH(System.getProperty("user.home")
							+ System.getProperty("file.separator") + "ebad"
							+ System.getProperty("file.separator") +  "ebad-config.xml"), 
	CATEGORY_CONFIG_FILE_PATH(System.getProperty("user.home")
									+ System.getProperty("file.separator") + "ebad"
									+ System.getProperty("file.separator") +  "category-config.xml"), 
    USA_CATEGORY_CONFIG_FILE_PATH(System.getProperty("user.home")
											+ System.getProperty("file.separator") + "ebad"
											+ System.getProperty("file.separator") +  "usa-category-config.xml"),
	TEMP_CONFIG_FILE_PATH(System.getProperty("user.home")
									+ System.getProperty("file.separator") + "ebad"
									+ System.getProperty("file.separator") +  "tmp-conf.xml"), 
	LOGO_PATH(
									System.getProperty("user.home")
											+ System.getProperty("file.separator")
											+ "ebad"
											+ System.getProperty("file.separator")
											+ "ebad1.gif"),
	USER_SUBDIR(System.getProperty("user.home")
			+ System.getProperty("file.separator") + "ebad"),
	
    NO_IMAGE_PATH(
							System.getProperty("user.home")
									+ System.getProperty("file.separator")
									+ "ebad"
									+ System.getProperty("file.separator")
									+ "imgNoImg.gif"),
	
   CONFIG_FILE_NAME("ebad-config.xml"),
   CATEGORY_CONFIG_FILE_NAME("category-config.xml"),
   USA_CATEGORY_CONFIG_FILE_NAME("usa-category-config.xml"),
   HIDDEN_CONFIG_FILE_NAME("np-ebad-config.xml"),
   HELP_FILE_NAME("help.html"),
   LOGO_FILE_NAME("ebad1.gif"),
   NO_IMAGE_FILE_NAME("imgNoImg.gif"),
   UK_DOMAIN("www.ebay.co.uk"),
   USA_DOMAIN("www.ebay.com");
	
	


	private String value;

	private FileAndPathNames(String path) {
		this.value = path;
	}
	 @Override
	  public String toString(){
		 return this.value;
	 }

}
