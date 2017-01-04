package kiwi.vn.scrapy.service;

import java.util.List;

import kiwi.vn.scrapy.entity.ProductCsv;

public class TarotoScrapy extends ScrapyAbstract {
	public TarotoScrapy() {
		this.log.debug("Start scrapy with taroto.jp page");
		this.pageUrl = "";

	}

	@Override
	protected List<ProductCsv> getAllItem() {
		return null;
	}
}
