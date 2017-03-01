package kiwi.vn.scapy.async;

import java.util.List;
import java.util.concurrent.Callable;

import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.scrapy.service.PanaScrapy;

public class ProductPanaWorker implements Callable<ProductCsv> {

	private PanaScrapy scrapy;
	public ProductPanaWorker(PanaScrapy scrapy) {
		super();
		this.scrapy = scrapy;
	}
	public ProductPanaWorker( ) {
		this.scrapy = new PanaScrapy();
	}
	@Override
	public ProductCsv call() throws Exception {
		return null;
	}
	public PanaScrapy getScrapy() {
		return scrapy;
	}
	public void setScrapy(PanaScrapy scrapy) {
		this.scrapy = scrapy;
	}

}
