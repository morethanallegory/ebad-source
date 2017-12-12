package ebay;

import org.jsoup.nodes.Element;

class USAItemTransform extends ItemTransform {

	public USAItemTransform() {
		// TODO Auto-generated constructor stub
	}

	@Override
	String getShipping(Element item) {
		String s=null;
		Element el = item.select(".lvdetails").first();
		if (el != null) {
			el = el.select("li:nth-child(2)").first();
			if (el != null) {
				if( el.text().length()<= 50){
					s= el.text();
				}
			}
		}
		return s;
	}
}
