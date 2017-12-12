package ebay;

 class ListingProsessorMessageBuilder {
	private StringBuilder message = new StringBuilder();
	
	public ListingProsessorMessageBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	String buildMessage(Category category, int itemsScanned, String numberOfItemsInCategory){
		message.setLength(0);
		message.append("Scanned ");
		message.append(itemsScanned);
		if (numberOfItemsInCategory != null) {
			message.append(" of ");
			message.append(numberOfItemsInCategory);
		}
		message.append(" items in ");
		message.append(category.pathSteps());
		return message.toString();
	}

}
