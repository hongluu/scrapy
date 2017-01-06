package kiwi.vn.scapy.main;

import java.util.List;

import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.scrapy.service.DenzaiScrapy;
import kiwi.vn.scrapy.service.MonotaroScrapy;
import kiwi.vn.scrapy.service.TarotoScrapy;

public class MainApp {
	public static void main(String[] args) {
		DenzaiScrapy denzaiScrapy = new DenzaiScrapy();
		List<ProductCsv> listdenzai = denzaiScrapy.processPage();
		TarotoScrapy tarotoScrapy = new TarotoScrapy();
		List<ProductCsv> listtaroto = tarotoScrapy.processPage();
		
		MonotaroScrapy monoScrapy =new MonotaroScrapy();
		monoScrapy.setListProductCodePrepare(listdenzai,listtaroto);
		monoScrapy.processPage();
	
	}

	
}
