package ebay;

import application.Application;
import util.StringUtils;

class LinkBuilder {
	private StringBuilder sb = new StringBuilder();

	public LinkBuilder() {
		// TODO Auto-generated constructor stub
	}

	String intitialLink(String uri) {
		sb.setLength(0);
		sb.append(uri);

		if (Application.switchedToCoUk()) {
			sb.append("?_sacat=");
			sb.append(StringUtils.getNumberFromURL(uri));
			sb.append("&LH_Auction=1&_ftrv=48&_pgn=1&_ipg=200");
		} else {//LH_Auction=1&LH_Time=1&_ftrt=901&_ftrv=48&_sop=10&_dmd=1
			sb.append("?LH_Auction=1&LH_Time=1&_ftrv=48&_ipg=200");
		}
		return sb.toString();
	}

	String nextLink(String uri) {
		sb.setLength(0);
		sb.append(uri);
		sb.append("&_ipg=200");
		return sb.toString();
	}
}
