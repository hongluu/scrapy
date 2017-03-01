package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kiwi.vn.scapy.async.RunableCustom;
import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.srapy.utils.CsvUtils;

/**
 * The Class DenzaiScrapy.
 */
public class MitsubishielectricScrapy extends ScrapyAbstract {

	private static final int MAX_THREAD = 80;
	private List<String> allLink;

	/**
	 * Instantiates a new denzai scrapy.
	 */
	public MitsubishielectricScrapy() {
		this.log.debug("Start scrapy with http://www.mitsubishielectric.co.jp/ page");
		this.pageUrl = "http://www.mitsubishielectric.co.jp/ldg/wink/";

	}

	/**
	 * Gets the all item.
	 *
	 * @return the all item
	 */

	public List<ProductCsv> getAllItem() {
		long startTime = System.currentTimeMillis();
		List<ProductCsv> allProducts = new ArrayList<ProductCsv>();
		this.allLink = getAllLinkItem();
		// this.allLink =
		// FileUtils.getListLinkFromFile(TarotoScrapy.class.getClassLoader().getResource(FILE_LINK).getFile());
		int numOfTotalLink = allLink.size();
		System.out.println(numOfTotalLink);
		List<RunableCustom> listRun = new ArrayList<RunableCustom>();
		int nJump = numOfTotalLink / MAX_THREAD;
		for (int ii = 0; ii < MAX_THREAD; ii++) {
			if (ii == MAX_THREAD - 1) {
				listRun.add(new RunableCustom(allProducts, this, ii * (nJump), numOfTotalLink));
			} else {
				listRun.add(new RunableCustom(allProducts, this, ii * (nJump), (ii + 1) * (nJump)));
			}
			listRun.get(ii).start();
		}
		while (true) {
			if (isAllThreadDone(listRun)) {
				System.out
						.println("=====COMPLETE IN ====== :" + (System.currentTimeMillis() - startTime) / 1000 + " s");
				System.out.println("=====    TOTAL   ====== :" + allProducts.size() + "item");
				return allProducts;
			}
		}

	}

	@Override
	public List<ProductCsv> getAllItem(int start, int end) {
		List<ProductCsv> listProducts = new ArrayList<ProductCsv>();
		for (int i = start; i < end; i++) {
			String itemLink = allLink.get(i);
			try {
				Document doc = getDoc(itemLink);
				String category = getCategoryPer(
						getDoc(itemLink.replaceAll("searchProductResult", "searchProductBreadcrumbs")));
				listProducts.addAll(getProductPerLink(doc, category));
			} catch (Exception e) {
			}
		}
		return listProducts;
	}

	private List<ProductCsv> getProductPerLink(Document doc, String category) throws Exception {
		List<ProductCsv> products = new ArrayList<>();
		Elements els = doc.select("#table_product_list tr");
		for (Element element : els) {
			ProductCsv product = getProduct(element, category);
			if (product != null) {
				products.add(product);
			}
		}
		return products;
	}

	private String getCategoryPer(Document doc) {
		String category = "";
		Elements elCates = doc.select("li ");
		int count = 0;
		for (Element element : elCates) {
			if (count == elCates.size()) {
				category += element.text();
				return category;
			}
			if (count > 1) {
				category += element.text() + ">";
			}
			count++;
		}
		return category;
	}

	private ProductCsv getProduct(Element element, String category) throws Exception {
		if (element.select(".sec_product_detail .sec_spec img").size() != 0
				&& "セット".equals(element.select(".sec_product_detail .sec_spec img").attr("alt"))) {
			return null;
		}
		if(StringUtils.isEmpty(element.select("h3 a").text())){
			return null;
		}
		ProductCsv product = new ProductCsv(this.pageUrl);
		Elements elementUrlImg =element.select(".sec_product_img img");
		if(!elementUrlImg.isEmpty()){
			String urlImg =elementUrlImg.attr("src");
			if( elementUrlImg.attr("src").contains("mitsubishielectric.co.jp")){
				product.setImgUrl(urlImg);
			}else{
				product.setImgUrl(this.pageUrl + urlImg);
			}
		}
		String stringPrice = element.select(".txt_detail span:contains(円)").text();
		try{
			if (!StringUtils.isEmpty(stringPrice)){
				stringPrice = stringPrice.replaceAll("[^0-9]","");
			}
			product.setPrice(StringUtils.isEmpty(stringPrice) ? 0 : Integer.parseInt(stringPrice));
		}catch(Exception e){
			stringPrice="";
		}
		String productModel =element.select("h3 a").text();
		product.setProductModel(productModel);
		
		String categoryMorotaro = this.getCategoryMonotaro(productModel);
		if(categoryMorotaro == null){
			product.setCategory(category);
		}else{
			product.setCategory(categoryMorotaro);
		}
		
		String productUrl= this.pageUrl + element.select("h3 a").attr("href");
		String productName = this.getProductNameBy(productUrl);
		
		product.setProductName(productName);
		product.setPublishedDate(element.select(".txt_detail span:contains(発売日)").text().replaceAll("発売日:", ""));

		String productDetailLink = this.pageUrl + element.select(".table_product_status a:contains(仕様表)").attr("href");
		try{
			product.setDescription(getProductDetail(productDetailLink));
		}catch (Exception e) {
			product.setDescription("");
		}
		
		product.setProductUrl(productUrl);
		product.setBrand("三菱電機");
		//CsvUtils.appendToCsv(product, this.getFileName());
		return product;
	}

	public String getProductNameBy(String productUrl) throws IOException {
		Document doc=this.getDoc(productUrl);
		String name = doc.select("#contents_main .hx_lv1 span").text();
		return name;
	}

	private String getProductDetail(String productDetailLink) throws IOException {
		Document doc = getDoc(productDetailLink);
		return doc.select(".data_table").html();
	}

	private List<String> getAllLinkItem() {
		try {
			return getAllLinkBy(getAllCategoryId());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<String> getAllLinkBy(List<String> listCateId) throws IOException {
		List<String> listLink = new ArrayList<>();
		for (String cateId : listCateId) {
			listLink.addAll(getAllLinkBy(cateId));
		}
		return listLink;
	}

	private List<String> getAllLinkBy(String cateId) throws IOException {
		List<String> list_output = new ArrayList<>();
		int pageNum = 1;
		String url1 = getUrlBy(cateId, pageNum);
		Document doc = this.getDoc(url1);
		int maxPage = (doc.select("li a.paging_link").size() == 0
				&& doc.select("li:not(.link_pager_next) a.paging_link").size() == 0) ? pageNum
						: Integer.parseInt(doc.select("li:not(.link_pager_next) a.paging_link").last().text());
		for (int i = 1; i < maxPage + 1; i++) {
			list_output.add(getUrlBy(cateId, i));
		}
		return list_output;
	}

	private String getUrlBy(String cateId, int pageNum) {
		return "http://www.mitsubishielectric.co.jp/ldg/wink/searchProductResult.do?" + "ccd=" + cateId
				+ "&releaseFrom=&releaseTo=&oldProductFlg=&pn=&pns=&pnsCsv=&kwd=&" + "pageNo=" + pageNum
				+ "&log=true&showAll=true&init=false&_=1487475814176";
	}

	private List<String> getAllCategoryId() throws IOException {
		Document doc = this.getDoc(
				"http://www.mitsubishielectric.co.jp/ldg/wink/selectProductCategory.do?ccd=202010&_=1487475210601");
		List<String> listCcd = new ArrayList<>();
		Elements listCcdEl = doc.select("a.selectCcd");
		for (Element element : listCcdEl) {
			listCcd.add(element.attr("ccd"));
		}
		return listCcd;
	}

}
