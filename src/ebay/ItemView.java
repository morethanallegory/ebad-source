package ebay;

import java.util.HashMap;
import java.util.Map;

public class ItemView {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemView other = (ItemView) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}

	private Map<String, String> items = new HashMap<String, String>();

	public void addItem(String key, String value) {
		items.put(key, value);
	}

	public Map<String, String> getItems() {
		return items;
	}

	public String getValue(String key) {
		return (items.get(key) == null) ? "" : this.items.get(key);
	}

	public String toString() {
		return items.toString();
	}

}
