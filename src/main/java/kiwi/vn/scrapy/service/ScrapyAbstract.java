package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import kiwi.vn.scrapy.entity.ProductCsv;

/**
 * The Class ScrapyAbstract.
 */
public abstract class ScrapyAbstract {
	
	/** The log. */
	protected Log log = LogFactory.getLog(ScrapyAbstract.class);
	
	/** The page url. */
	protected String pageUrl;

	/**
	 * Gets the doc.
	 *
	 * @param pageUrl the page url
	 * @return the doc
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Document getDoc(String pageUrl) throws IOException {
		return Jsoup.connect(pageUrl).header("Accept-Encoding", "gzip, deflate")
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0").maxBodySize(0)
				.timeout(600000).get();
	}

	/**
	 * Process page.
	 *
	 * @return the list
	 */
	public List<ProductCsv> processPage() {
		 List<ProductCsv>	listProduct = getAllItem();
			for (ProductCsv productCsv : listProduct) {
				System.out.println(productCsv.getProductModel());
			}
		return listProduct;
	}

	protected abstract List<ProductCsv> getAllItem() ;

	/**
	 * Gets the page url.
	 *
	 * @return the page url
	 */
	public String getPageUrl() {
		return pageUrl;
	}

	/**
	 * Sets the page url.
	 *
	 * @param pageUrl the new page url
	 */
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public abstract List<? extends ProductCsv> getAllItem(int start, int end) ;

}
