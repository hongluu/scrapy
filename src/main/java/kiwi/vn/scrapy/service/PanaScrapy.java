package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kiwi.vn.scapy.async.RunableCustom;
import kiwi.vn.scrapy.entity.ProductCsv;

/**
 * The Class DenzaiScrapy.
 */
public class PanaScrapy extends ScrapyAbstract {
	private List<String> allLink = new ArrayList<>();
	private static final int MAX_THREAD = 80;

	public PanaScrapy() {
		this.pageUrl = "http://www2.panasonic.biz";
	}

	@Override
	protected List<ProductCsv> getAllItem() {
		long startTime = System.currentTimeMillis();
		List<ProductCsv> allProducts = new ArrayList<ProductCsv>();

		try {
			allLink_buildAllLinkItem();
		} catch (IOException e) {
		}
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

	private void allLink_buildAllLinkItem() throws IOException {
		String url_getDocLv1 ="http://www2.panasonic.biz/es/ai/products/search/category/index.jsp";
		List<String> listCatLv1 = getCatLv1(url_getDocLv1);
		List<String> listCatLv2 = getCatLv2(listCatLv1);
		List<String> listCatLv3 = getCatLv3(listCatLv2);
		List<String> listCatLv4 = getCatLv4(listCatLv3);
		for (String cat_url : listCatLv4) {
			Document doc = this.getDoc(cat_url);
			if (isPageHasProduct(doc)) {
				this.allLink.addAll(getLinksProductInPage(doc));
			} 
		}
	}

	private List<String> getCatLv4(List<String> listCatLv3) throws IOException {
		List<String> listCatLv4 = new ArrayList<>();
		for (String cat_url : listCatLv3) {
			Document doc = this.getDoc(cat_url);
			if (isPageHasProduct(doc)) {
				this.allLink.addAll(getLinksProductInPage(doc));
			} else {
				Elements el_catLv1 = doc.select(".categorytree_box #category_tree07 a");
				listCatLv4.addAll(getListCatBy(el_catLv1));
			}
		}
		return listCatLv3;

	}

	private List<String> getCatLv3(List<String> listCatLv2) throws IOException {
		List<String> listCatLv3 = new ArrayList<>();
		for (String cat_url : listCatLv2) {
			Document doc = this.getDoc(cat_url);
			if (isPageHasProduct(doc)) {
				this.allLink.addAll(getLinksProductInPage(doc));
			} else {
				Elements el_catLv1 = doc.select(".categorytree_box #category_tree06 a");
				listCatLv3.addAll(getListCatBy(el_catLv1));
			}
		}
		return listCatLv3;
	}

	private List<String> getCatLv2(List<String> listCatLv1) throws IOException {
		List<String> listCatLv2 = new ArrayList<>();
		for (String cat_url : listCatLv1) {
			Document doc = this.getDoc(cat_url);
			if (isPageHasProduct(doc)) {
				this.allLink.addAll(getLinksProductInPage(doc));
			} else {
				Elements el_catLv1 = doc.select(".categorytree_box #category_tree05 a");
				listCatLv2.addAll(getListCatBy(el_catLv1));
			}
		}
		return listCatLv2;
	}

	private List<String> getCatLv1(String url_getDocLv1) throws IOException {
		Document doc = this.getDoc(url_getDocLv1);
		if (isPageHasProduct(doc)) {
			this.allLink.addAll(getLinksProductInPage(doc));
		} else {
			Elements el_catLv1 = this.getDoc(url_getDocLv1).select(".col5 .col a");
			return getListCatBy(el_catLv1);
		}
		return new ArrayList<>();
	}

	private List<String> getLinksProductInPage(Document doc) {
		List<String> list = new ArrayList<>();
		Elements els = doc.select("table.tableH tr a");
		for (Element element : els) {
			list.add(element.attr("href"));
		}
		return list;
	}

	private boolean isPageHasProduct(Document doc) {
		return !doc.select("table.tableH").isEmpty();
	}

	private List<String> getListCatBy(Elements el_catLv1) {
		List<String> listCatLv1 = new ArrayList<>();
		for (Element element : el_catLv1) {
			listCatLv1.add(this.pageUrl + element.attr("href"));
		}
		return listCatLv1;
	}

	@Override
	public List<? extends ProductCsv> getAllItem(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

}
