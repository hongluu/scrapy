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

import kiwi.vn.scapy.async.RunableCustom;
import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.srapy.utils.CsvUtils;

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
	private static final String HOME_PAGE = ".dÞƒlƒbƒg";
	private static final int MAX_THREAD = 10;

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
	
	public List<ProductCsv> getAllItem() {
		List<ProductCsv> output = new ArrayList<ProductCsv>();
		    int totalPage = 0;
			long startTime = System.currentTimeMillis();
			try {
				totalPage= getTotalPageLink(LINK_ALL_PRODUCTS + 1);
				totalPage=100;
				System.out.println(totalPage);
			} catch (IOException e) {
				log.debug(e.getMessage());
			}
			List<RunableCustom> listRun= new ArrayList<RunableCustom>();
			int nJump =totalPage/MAX_THREAD;
			for (int ii = 0; ii < MAX_THREAD; ii++) {
				if(ii == MAX_THREAD-1){
					listRun.add(new RunableCustom(output, this,ii*(nJump),totalPage));
				}else{
					listRun.add(new RunableCustom(output, this,ii*(nJump),(ii+1)*(nJump)));
				}
				listRun.get(ii).start();
			}
			
			while(true){	
				if(isAllThreadDone(listRun)){
					System.out.println("=====DENZAI ====== ");
					System.out.println("=====COMPLETE IN ====== :"+(System.currentTimeMillis()-startTime)/1000 + " s");
					System.out.println("=====    TOTAL   ====== :"+output.size() +"item");
					return output;
				}
			}
			
	}
	@Override
	public List<ProductCsv> getAllItem(int start, int end) {
		List<ProductCsv> output = new ArrayList<ProductCsv>();
		for (int i = start+1; i <= end; i++) {
			try {
			output.addAll(getAllItemsPerPage(LINK_ALL_PRODUCTS + i));
			} catch (Exception e) {
				this.log.debug("Can not connect page" + i+ " cause:" +e.getMessage());
				i--;
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
		//System.out.println(url);
		List<ProductCsv> listItems = new ArrayList<ProductCsv>();
		Document doc = getDynamicDoc(url);
		Elements els = doc.select(".item_listA form");
		for (Element element : els) {
			if (element != null) {
				ProductCsv product = getProduct(element);
				listItems.add(product);
				CsvUtils.appendToCsv(product, this.getFileName());
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
	private  ProductCsv getProduct(Element element) {
		ProductCsv product = new ProductCsv(HOME_PAGE);
			product.setPrice(Integer.parseInt(element.getElementsByAttributeValue("name", "price").val()));
			product.setProductModel(element.getElementsByAttributeValue("name", "code").val());
			product.setProduct(
					product.getProductModel() + "|" + element.getElementsByAttributeValue("name", "shohin").val());
			product.setQuantity(Integer.parseInt(element.getElementsByAttributeValue("name", "kazu").val()));
			product.setProductUrl(element.getElementsByAttributeValue("name", "item_url").val());
			product.setDescription(element.select("table tr:eq(3)").html().replaceAll("<th>.*</th>|<td>|</td>", "").replace("<br>", "\n"));
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
