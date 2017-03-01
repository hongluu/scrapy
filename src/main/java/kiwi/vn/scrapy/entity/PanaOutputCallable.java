package kiwi.vn.scrapy.entity;

import java.util.List;
import java.util.Map;

public class PanaOutputCallable {
	private List<String> listLinklLv;;
	private List<String> allLinkProducts;
	private Map<String,List<String>> mapAllLink;;
	public List<String> getAllLinkProducts() {
		return allLinkProducts;
	}
	public void setAllLinkProducts(List<String> allLinkProducts) {
		this.allLinkProducts = allLinkProducts;
	}
	public List<String> getListLinklLv() {
		return listLinklLv;
	}
	public void setListLinklLv(List<String> listLinklLv) {
		this.listLinklLv = listLinklLv;
	}
	public PanaOutputCallable(List<String> listLinklLv, Map<String, List<String>> map) {
		super();
		this.listLinklLv = listLinklLv;
		this.mapAllLink = map;
	}
	public Map<String,List<String>> getMapAllLink() {
		return mapAllLink;
	}
	public void setMapAllLink(Map<String,List<String>> mapAllLink) {
		this.mapAllLink = mapAllLink;
	}
	

}
