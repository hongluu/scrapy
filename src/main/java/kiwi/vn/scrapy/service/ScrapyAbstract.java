package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import kiwi.vn.scapy.async.RunableCustom;
import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.srapy.utils.CsvUtils;

// TODO: Auto-generated Javadoc
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
		String fileName  =formatFileName(); 
		List<ProductCsv>	listProduct = getAllItem();
		try {
			CsvUtils.writeToCsv(listProduct, fileName);
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
		return listProduct;
	}

	/**
	 * Format file name.
	 *
	 * @return the string
	 */
	private  String formatFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_DD hh_mm_ss");
		return "€iîñ_" + sdf.format(new Date()) + ".csv";
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
			if (runableCustom.isRunning()) {
				return false;
			}
		}
		return true;
	}

}
