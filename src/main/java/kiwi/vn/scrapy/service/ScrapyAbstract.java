package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kiwi.vn.scapy.async.RunableCustom;
import kiwi.vn.scrapy.entity.ProductCsv;

// TODO: Auto-generated Javadoc
/**
 * The Class ScrapyAbstract.
 */
public abstract class ScrapyAbstract {
	
	/** The log. */
	protected Log log = LogFactory.getLog(ScrapyAbstract.class);
	
	/** The page url. */
	protected String pageUrl;
	public ScrapyAbstract(){
	}

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
		return listProduct;
	}

	/**
	 * Format file name.
	 *
	 * @return the string
	 */
	@SuppressWarnings("unused")
	private  String formatFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_DD hh_mm_ss");
		return "Â�â‚¬Â•iÂ�Ã®Â•Ã±_" + sdf.format(new Date()) + ".csv";
	}

	/**
	 * Gets the all item.
	 *
	 * @return the all item
	 */
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
	
	/**
	 * Gets the all item.
	 *
	 * @param start the start
	 * @param end the end
	 * @return the all item
	 */
	public abstract List<? extends ProductCsv> getAllItem(int start, int end) ;
	
	/**
	 * Checks if is all thread done.
	 *
	 * @param listRun the list run
	 * @return true, if is all thread done
	 */
	protected  boolean isAllThreadDone(List<RunableCustom> listRun) {
		for (RunableCustom runableCustom : listRun) {
			if (runableCustom!= null && runableCustom.isRunning()) {
				return false;
			}
		}
		return true;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	private String fileName;
	
	public String getCategoryMonotaro(String productModel ) {
		Document doc;
		try {	
			String url = "https://www.monotaro.com/s/?c=&bn=&pn=&mn=&mp="+productModel+"&pf=&pt=&q=";
			doc = this.getDoc(url);
			String linkDetail = doc.select(".first_item .txt a").attr("href");
			doc = this.getDoc("https://www.monotaro.com/"+linkDetail);
			if(doc.select(".products_details").html().contains(productModel)){
				return this.getC1ategoryMonotaro(doc);
			}else{
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	
	}

	private String getC1ategoryMonotaro(Document doc) {
		Elements cateEls = doc.select(".cl_parents a");
		String cat = "";
		int count = 0;
		for (Element element : cateEls) {
			if(count == cateEls.size()-1 ){
				cat= cat +element.text();
			}
			cat= cat +element.text()+ ">";
			count++;
		}
		return cateEls.size()==0? null : cat;
	}
}
