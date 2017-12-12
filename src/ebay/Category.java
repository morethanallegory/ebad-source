package ebay;

import java.util.LinkedList;
import java.util.List;

public class Category {
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	private List<Category> categories = new LinkedList<Category>();
	private String name;
	private String id, url;
	private boolean active;
	private Object[] pathSteps;
	
	String pathSteps(){
		if( pathSteps == null)return "Path not available";
		StringBuilder builder = new StringBuilder();
		for(int i =1; i < pathSteps.length; i++){			
			builder.append(pathSteps[i]);
			if(i < pathSteps.length-1)builder.append("/");
		}
		return builder.toString();
	}
	
	public Object[] getPathSteps() {
		return pathSteps;
	}

	public void setPathSteps(Object[] pathSteps) {
		this.pathSteps = pathSteps;
	}


	public Category(String name, String url, boolean active, String id) {
		this.name = name;
		this.url = url;
		this.active = active;
		this.id = id;
	}

	public String getName() {
		return name;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Category(String name) {
		this.name = name;
	}

	public Category() {
		// TODO Auto-generated constructor stub
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public boolean hasChildren() {
		return !this.categories.isEmpty();
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public Category add(Category category) {
		this.categories.add(category);
		return this;
	}
}
