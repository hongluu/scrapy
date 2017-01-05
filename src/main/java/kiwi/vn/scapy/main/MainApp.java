package kiwi.vn.scapy.main;

import kiwi.vn.scrapy.service.DenzaiScrapy;
import kiwi.vn.scrapy.service.TarotoScrapy;

public class MainApp {
	public static void main(String[] args) {
		DenzaiScrapy denzaiScrapy = new DenzaiScrapy();
		denzaiScrapy.processPage();
	}
}
