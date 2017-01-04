package kiwi.vn.scrapy.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import kiwi.vn.scrapy.entity.ProductCsv;

/**
 * The Class DenzaiScrapy.
 */
public class DenzaiScrapy extends ScrapyAbstract {

	/** The Constant TOTAL_PAGE_SELECTOR. */
	private static final String TOTAL_PAGE_SELECTOR = ".gaitou .kensu span.red";
	
	/** The Constant MAX_PRODUCT_PERPAGE. */
	private static final int MAX_PRODUCT_PERPAGE = 30;
	
	/** The Constant LINK_ALL_PRODUCTS. */
	private static final String LINK_ALL_PRODUCTS = "http://www.denzai-net.jp/page_system/mod_itemlist.php?&page=";
	
	/** The Constant HOME_PAGE. */
	private static final String HOME_PAGE = "www.denzai-net.jp";

	/**
	 * Instantiates a new denzai scrapy.
	 */
	public DenzaiScrapy() {
		this.log.debug("Start scrapy with denzai-net.jp page");
		this.pageUrl = "http://www.denzai-net.jp";

	}

	/* (non-Javadoc)
	 * @see kiwi.vn.scrapy.service.ScrapyAbstract#processPage()
	 */

	/**
	 * Gets the all item.
	 *
	 * @return the all item
	 */
	protected List<ProductCsv> getAllItem() {
		List<ProductCsv> output = new ArrayList<ProductCsv>();
		int totalPage = 0;
		try {
			totalPage= getTotalPageLink(LINK_ALL_PRODUCTS + 1);
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
		
		for (int i = 1; i < totalPage; i++) {
			try {
			output.addAll(getAllItemsPerPage(LINK_ALL_PRODUCTS + i));
			} catch (Exception e) {
				this.log.debug("Can not connect page" + i+ " cause:" +e.getMessage());
				continue;
			}
		}
		
		return output;
	}

	/**
	 * Gets the all items per page.
	 *
	 * @param url the url
	 * @return the all items per page
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private List<ProductCsv> getAllItemsPerPage(String url) throws IOException {
		List<ProductCsv> listItems = new ArrayList<ProductCsv>();
		Document doc = getDynamicDoc(url);
		Elements els = doc.select(".item_listA form");
		for (Element element : els) {
			if (element != null) {
				listItems.add(getProduct(element));
			}
		}
		return listItems;
	}

	/**
	 * Gets the product.
	 *
	 * @param element the element
	 * @return the product
	 */
	private ProductCsv getProduct(Element element) {
		ProductCsv product = new ProductCsv(HOME_PAGE);
		product.setPrice(Integer.parseInt(element.getElementsByAttributeValue("name", "price").val()));
		product.setProductModel(element.getElementsByAttributeValue("name", "code").val());
		product.setProduct(
				product.getProductModel() + "|" + element.getElementsByAttributeValue("name", "shohin").val());
		product.setQuantity(Integer.parseInt(element.getElementsByAttributeValue("name", "kazu").val()));
		product.setProductUrl(element.getElementsByAttributeValue("name", "item_url").val());
		product.setDescription(element.select("table tr:eq(3)").html());
		product.setMoreInfo(element.select(".piece_text").text());
		product.setCategory(element.select("table td:eq(3)").text());
		return product;
	}

	/**
	 * Gets the total page link.
	 *
	 * @param url the url
	 * @return the total page link
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private int getTotalPageLink(String url) throws IOException {
		int totalItems = Integer.parseInt(getDynamicDoc(url).select(TOTAL_PAGE_SELECTOR).text());
		return totalItems / MAX_PRODUCT_PERPAGE + 1;
	}

	/**
	 * Gets the dynamic doc.
	 *
	 * @param pageUrl the page url
	 * @return the dynamic doc
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Document getDynamicDoc(String pageUrl) throws IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(pageUrl);
		HttpResponse response = httpClient.execute(request);
		BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String inputLine;
		StringBuffer responseString = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseString.append(inputLine);
		}
		in.close();
		JSONObject json = new JSONObject(responseString.toString());

		return Jsoup.parse(json.getString("html"));
	}

}
