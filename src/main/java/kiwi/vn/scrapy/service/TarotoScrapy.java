package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;

import kiwi.vn.scapy.async.RunableCustom;
import kiwi.vn.scrapy.entity.ProductCsv;

public class TarotoScrapy extends ScrapyAbstract {
	private static final String ALL_ITEM_LINK = "http://www.taroto.jp/item/";
	private static final int MAX_THREAD = 3;
	private static final int MAX_TOTAL_LINK = 300000;

	public TarotoScrapy() {
		this.log.debug("Start scrapy with taroto.jp page");
		this.pageUrl = "http://www.taroto.jp";

	}

	@Override
	protected List<ProductCsv> getAllItem() {
		long startTime = System.currentTimeMillis();
		List<ProductCsv> allProducts = new ArrayList<ProductCsv>();
		List<RunableCustom> listRun= new ArrayList<RunableCustom>();
		for (int ii = 0; ii < MAX_THREAD; ii++) {
			listRun.add(new RunableCustom(allProducts, this,ii*(MAX_TOTAL_LINK/MAX_THREAD),(ii+1)*(MAX_TOTAL_LINK/MAX_THREAD)));
			listRun.get(ii).start();
		}
		while(true){	
			if(isAllThreadDone(listRun)){
				System.out.println("=====COMPLETE IN ====== :"+(System.currentTimeMillis()-startTime)/1000 + " s");
				System.out.println("=====    TOTAL   ====== :"+allProducts.size() +"item");
				return allProducts;
			}
		}
	}

	@Override
	public List<ProductCsv> getAllItem(int start, int end) {
		List<ProductCsv> listProducts = new ArrayList<ProductCsv>();
		for (int i = start; i < end; i++) {
			String itemLink = getItemLink(i);
			try {
				Document doc = getDoc(itemLink);
				listProducts.add(getProductPerLink(doc));
			} catch (IOException e) {
				continue;
			}
		}
		return listProducts;
	}
//	private boolean isExitedProduct(Document doc) {
//		if (!MESSAGE_NOTFOUND.matches(doc.select("font[size='+2']>b").text())) {
//			return true;
//		}
//		return false;
//	}

	private String getItemLink(int i) {
		return ALL_ITEM_LINK + i + ".html";
	}

	private synchronized ProductCsv getProductPerLink(Document doc) throws IOException {
		ProductCsv product = new ProductCsv(this.pageUrl);
		if(doc.getElementsByAttributeValue("name", "keywords").attr("content")==""){
			return null;
		}
		product.setCategory(doc.select("a.crumbsList").text());
		product.setPrice(
				Integer.parseInt(doc.select("td.Item_price strong").text().replace("�~�i�ō��j", "").replace(",", "")));
		product.setProductModel(doc.getElementsByAttributeValue("name", "keywords").attr("content"));
		product.setProduct(doc.getElementsByAttributeValue("property", "og:site_name").attr("content"));
		product.setQuantity(Integer.parseInt(doc.getElementsByAttributeValue("name", "F_item_num").val()));
		product.setProductUrl(doc.getElementsByAttributeValue("property", "og:url").attr("content"));
		System.out.println(product.getProductUrl());
		product.setDescription(doc.select("p.syousai01").html());
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
