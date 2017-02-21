package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kiwi.vn.scapy.async.RunableCustom;
import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.srapy.utils.CsvUtils;

public class TarotoScrapy extends ScrapyAbstract {
	private static final int MAX_THREAD = 2;
	private static final String SITE_MAP = "http://www.taroto.jp/sitemap.xml";
	private List<String> allLink;

	// private static final String FILE_LINK = "itemTaro.txt";
	public TarotoScrapy() {
		this.log.debug("Start scrapy with taroto.jp page");
		System.out.println("Start scrapy with taroto.jp page");
		this.pageUrl = "http://www.taroto.jp";

	}

	@Override
	protected List<ProductCsv> getAllItem() {
		long startTime = System.currentTimeMillis();
		List<ProductCsv> allProducts = new ArrayList<ProductCsv>();
		this.allLink = getAllLinkFromSiteMap(SITE_MAP);
<<<<<<< HEAD
		// this.allLink =
		// FileUtils.getListLinkFromFile(TarotoScrapy.class.getClassLoader().getResource(FILE_LINK).getFile());
		int numOfTotalLink = allLink.size();
		System.out.println(numOfTotalLink);
		List<RunableCustom> listRun = new ArrayList<RunableCustom>();
		int nJump = numOfTotalLink / MAX_THREAD;
=======
		//this.allLink = FileUtils.getListLinkFromFile(TarotoScrapy.class.getClassLoader().getResource(FILE_LINK).getFile());
		int numOfTotalLink =allLink.size();
		numOfTotalLink=100;
//		System.out.println(numOfTotalLink);
		List<RunableCustom> listRun= new ArrayList<RunableCustom>();
		int nJump = numOfTotalLink/MAX_THREAD;
>>>>>>> 479c3a2dc10e49add605bd6855f27cb91733f2ef
		for (int ii = 0; ii < MAX_THREAD; ii++) {
			if (ii == MAX_THREAD - 1) {
				listRun.add(new RunableCustom(allProducts, this, ii * (nJump), numOfTotalLink));
			} else {
				listRun.add(new RunableCustom(allProducts, this, ii * (nJump), (ii + 1) * (nJump)));
			}
			listRun.get(ii).start();
		}
<<<<<<< HEAD
		while (true) {
			if (isAllThreadDone(listRun)) {
				System.out
						.println("=====COMPLETE IN ====== :" + (System.currentTimeMillis() - startTime) / 1000 + " s");
				System.out.println("=====    TOTAL   ====== :" + allProducts.size() + "item");
=======
		while(true){	
			if(isAllThreadDone(listRun)){
				System.out.println("=====TAROTO ====== :");
				System.out.println("=====COMPLETE IN ====== :"+(System.currentTimeMillis()-startTime)/1000 + " s");
				System.out.println("=====    TOTAL   ====== :"+allProducts.size() +"item");
>>>>>>> 479c3a2dc10e49add605bd6855f27cb91733f2ef
				return allProducts;
			}
		}
	}

	private List<String> getAllLinkFromSiteMap(String url) {
		List<String> output = new ArrayList<String>();
		try {
			Document doc = getDoc(url);
			Elements AllLink = doc.select("loc");
			for (Element element : AllLink) {
				if (element.text().contains("/item/"))
					output.add(element.text());
			}
		} catch (IOException e) {
<<<<<<< HEAD
			return output;
=======
			System.out.println("Can not connect url: " +e);
			return output ;
>>>>>>> 479c3a2dc10e49add605bd6855f27cb91733f2ef
		}
		return output;
	}

	@Override
	public List<ProductCsv> getAllItem(int start, int end) {
		List<ProductCsv> listProducts = new ArrayList<ProductCsv>();
		for (int i = start; i < end; i++) {
			String itemLink = getItemLink(i);
			try {
				Document doc = getDoc(itemLink);
				ProductCsv product = getProductPerLink(doc);
<<<<<<< HEAD
				if (product != null && product.getProductName()!=null && !product.getProductName().contains("●枚入り")) {
=======
				if (product == null){
					allLink.add(itemLink);
					end++;
				}else{
>>>>>>> 479c3a2dc10e49add605bd6855f27cb91733f2ef
					listProducts.add(product);
				}
			} catch (IOException e) {
				System.out.println("Can not connect page Taro");
				i--;
				continue;
			}
		}
		return listProducts;
	}
	// private boolean isExitedProduct(Document doc) {
	// if (!MESSAGE_NOTFOUND.matches(doc.select("font[size='+2']>b").text())) {
	// return true;
	// }
	// return false;
	// }

	private String getItemLink(int i) {
		return allLink.get(i);
	}

	private synchronized ProductCsv getProductPerLink(Document doc) throws IOException {
<<<<<<< HEAD
		ProductCsv product = new ProductCsv(this.pageUrl);
		if (doc.getElementsByAttributeValue("name", "keywords").attr("content") == "") {
			return null;
		}
		product.setCategory(doc.select("a.crumbsList").text());
		product.setPrice(Integer.parseInt(doc.select("td.Item_price strong").text().replaceAll("円（税込）", "").replaceAll(" ", "").replaceAll(",", "")));
		String productcode =doc.getElementsByAttributeValue("name", "keywords").attr("content");
		productcode.substring(0, productcode.indexOf("_"));
		product.setProductModel(productcode);
		product.setProductName(doc.getElementsByAttributeValue("property", "og:site_name").attr("content"));
=======
		ProductCsv product = new ProductCsv("�^���g�d�@");
		if(doc.getElementsByAttributeValue("name", "keywords").attr("content")==""){
			return null;
		}
		Elements catList = doc.select("a.crumbsList");
		StringBuilder sb = new StringBuilder();
		for (int ii = 0; ii < catList.size(); ii++){
			sb.append(">" + catList.get(ii).text());
		}
		product.setCategory(sb.toString().replace(">TOP>",""));
		product.setPrice(
				Integer.parseInt(doc.select("td.Item_Price strong").text().replaceAll("[^0-9]", "")));
		product.setProductModel(doc.getElementsByAttributeValue("name", "keywords").attr("content"));
		product.setProduct(doc.getElementsByAttributeValue("property", "og:site_name").attr("content"));
>>>>>>> 479c3a2dc10e49add605bd6855f27cb91733f2ef
		product.setQuantity(Integer.parseInt(doc.getElementsByAttributeValue("name", "F_item_num").val()));
		product.setProductUrl(doc.getElementsByAttributeValue("property", "og:url").attr("content"));
		product.setImgUrl(doc.getElementsByAttributeValue("property", "og:image").attr("content"));
		System.out.println(product.getProductUrl());
<<<<<<< HEAD
		product.setDescription(doc.select("p.syousai01").html());
		product.setBrand("未来工業");
=======
		product.setDescription(doc.select("p.syousai01").html().replace("<br><br>", "\n").replaceAll("<br>|</a>", "\n").replaceAll("</?span.*>|<a.*>", ""));
>>>>>>> 479c3a2dc10e49add605bd6855f27cb91733f2ef
		CsvUtils.appendToCsv(product, this.getFileName());
		return product;
	}
	//
	// @Override
	// protected List<ProductCsv> getAllItem() {
	// List<ProductCsv> allProducts = new ArrayList<ProductCsv>();
	// List<String> allCategoriesURLLv2 = new ArrayList<String>();
	//
	// List<String> allCategoriesURLLv1;
	// try {
	// allCategoriesURLLv1 = getAllCategoriesURLLv1(this.pageUrl);
	// } catch (IOException e) {
	// return null;
	// }
	//
	// for (String categoryURLLv1 : allCategoriesURLLv1) {
	// try {
	// //set All link to list allItmes
	// findAllItems(categoryURLLv1);
	// System.out.println(allItmes.size());
	// } catch (IOException e) {
	// continue;
	// }
	// }
	// for (String itemLink : allItmes) {
	// try {
	// allProducts.add(getProductPerLink(itemLink));
	// } catch (IOException e) {
	// log.debug("Link not found:" +itemLink);
	// continue;
	// }
	// }
	// return allProducts;
	// }
	// private List<String> allItmes = new ArrayList<String>();
	// private List<String> listCateIgnore = new ArrayList<String>();
	//
	// private void findAllItems(String categoryURLLv1) throws IOException {
	// System.out.println("Start with Url:" + categoryURLLv1);
	// Document doc = getDoc(categoryURLLv1);
	// Elements els = doc.select("div>table>tbody>tr>td table:eq(3) td:eq(1)
	// table td>a:has(img,font)");
	// reBuildListCatIgnore(doc);
	// if (els.size()!=0 && els.first().attr("href").contains("/item/")) {
	// for (Element element : els) {
	// allItmes.add(getItemLink(element.attr("href")));
	// }
	// } else {
	// try {
	// List<String> listCat = getListCatCurrent(els);
	// for (String cat : listCat) {
	// findAllItems(cat);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// private void reBuildListCatIgnore(Document doc) {
	// Elements els =doc.select("div>table>tbody>tr>td table:eq(3) td[width=200]
	// table td>a:has(img,font)");
	// for (Element element : els) {
	// String linkCat = element.attr("href");
	// if(linkCat.contains("http://www.taroto.jp/category/") &&
	// !isInList(linkCat ,listCateIgnore) ){
	// listCateIgnore.add(linkCat);
	// }
	// }
	//
	// }
	//
	// private List<String> getListCatCurrent(Elements els) {
	// List<String> listCat = new ArrayList<String>();
	// for (Element element : els) {
	// String linkCat = element.attr("href");
	// if(linkCat.contains("http://www.taroto.jp/category/") &&
	// !isInList(linkCat ,listCat) && !isInList(linkCat, listCateIgnore)){
	// listCat.add(linkCat);
	// }
	// }
	// return listCat;
	// }
	//
	// private boolean isInList(String linkCat, List<String> listCat) {
	// for (String cat : listCat) {
	// if(cat.equals(linkCat))
	// return true;
	// }
	// return false;
	// }
	//
	// private String getItemLink(String attr) {
	// if(attr.contains(this.pageUrl)){
	// return attr;
	// }else{
	// return this.pageUrl+attr.replace("..", "");
	// }
	// }
	//
	// private List<String> getAllCategoriesURLLv1(String pageUrl) throws
	// IOException {
	// List<String> output = new ArrayList<String>();
	// Document doc = getDoc(pageUrl);
	// Elements els = doc.select(".r-side a[href]");
	// for (Element element : els) {
	// output.add(element.attr("href"));
	// }
	// return output;
	// }
}
