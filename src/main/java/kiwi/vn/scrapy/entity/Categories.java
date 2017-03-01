package kiwi.vn.scrapy.entity;

import java.util.List;

public class Categories {
	/**
	 * 
	 */
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<Categories> getSubCats() {
		return subCats;
	}
	public void setSubCats(List<Categories> subCats) {
		this.subCats = subCats;
	}
	private String path;;
	private List<Categories> subCats;

}
