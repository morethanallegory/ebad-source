package ebay;

import org.jsoup.nodes.Element;

import util.StringUtils;
import application.Application;
import application.FileAndPathNames;

class ItemTransform implements ValidDateFormats {
	private final static int maxWordCount = Application.getSelectedWordRestriction();

	public ItemTransform() {
		// TODO Auto-generated constructor stub
	}

	private String getTitle(Element item) {
		Element el = item.select(".lvtitle").first();
		if (el != null) {
			return el.text();
		}
		return null;
	}

	private String getTime(Element item) {
		Element time = item.select(".tme").first();
		if (time != null && !time.hasText() && StringUtils.conformsToDateFormatInText(FORMAT_TEXT, time.text())) {
			//itemView.addItem("time", time.text());
			return time.text();
		} else {
			time = item.select("[timems]").first();
			if (time != null) {
				String tms = StringUtils.conformsToDateFormatInMillis(FORMAT_MILLIS, time.attr("timems"));
				return tms;
			}
		}
		return null;
	}

	private String getPrice(Element item) {
		Element el = item.select(".g-b").first();
		if (el != null) {
			return el.text();
		}
		return null;
	}

	 String getShipping(Element item) {
		Element el = item.select(".ship").first();
		if (el != null) {
			return el.text();
		}
		return null;
	}

	private String getURI(Element item) {
		Element url = item.select("[href]").first();
		if (url != null) {
			return url.attr("href");
		}
		return null;
	}

	private String getID(String s) {
		int i = s.lastIndexOf('=');
		if (i > -1 && i <= s.length() + 1)
			return s.substring(s.lastIndexOf('=') + 1);
		return null;
	}

	private String getImageURI(Element item) {
		// image src is tricky because there are multiple tags
		Element image = item.select("img[imgurl]").first();
		if (image != null) {
			return image.attr("imgurl");
		} else {
			image = item.select("img[src]").first();
			if (image != null) {
				return image.attr("src");
			}
		}
		return null;
	}

	private String getBids(Element item) {
		Element bids = item.select(".bids").first();
		if (bids != null) {
			return bids.text();
		}
		return null;
	}

	ItemView transformToItemView(Element item) {
		String title = getTitle(item);
		if (title == null) {
			return null;
		}

		String titleText = title.replace('/', ' ');
		if (StringUtils.wordCount(titleText) > maxWordCount) {
			return null;
		}
		ItemView itemView = new ItemView();
		itemView.addItem("title", titleText);
		itemView.addItem("categoryName", titleText);

		String time = getTime(item);
		if (time != null) {
			itemView.addItem("time", time);
		}

		// price g-b
		String price = getPrice(item);
		if (price != null) {
			itemView.addItem("price", price);
		}

		// shipping
		String shipping = getShipping(item);
		if (shipping != null) {
			itemView.addItem("shipping", shipping);
		}

		// url
		String uri = getURI(item);
		if (uri != null) {
			itemView.addItem("url", uri);
			// id
			String id = getID(uri);
			if (id != null)
				itemView.addItem("id", id);
		}

		String imageUri = getImageURI(item);
		if (imageUri != null) {
			itemView.addItem("image_url", imageUri);
		} else {
			itemView.addItem("image_url", FileAndPathNames.NO_IMAGE_PATH.toString());
		}

		// bids
		String bids = getBids(item);
		if (bids != null) {
			itemView.addItem("bids", bids);
		}

		return itemView;
	}

}
