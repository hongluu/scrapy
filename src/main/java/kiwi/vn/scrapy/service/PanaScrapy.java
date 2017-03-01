package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.parser.Feature;

import kiwi.vn.scapy.async.ProductPanaWorker;
import kiwi.vn.scapy.async.RunableCustom;
import kiwi.vn.scapy.async.WorkerThreadGetLinkPana;
import kiwi.vn.scrapy.entity.PanaOutputCallable;
import kiwi.vn.scrapy.entity.ProductCsv;

/**
 * The Class DenzaiScrapy.
 */
public class PanaScrapy extends ScrapyAbstract {
	private Map<String, List<String>> allLink = new HashMap<>();
	private static final int MAX_THREAD = 80;

	public PanaScrapy() {
		this.pageUrl = "http://www2.panasonic.biz";
	}

	@Override
	protected List<ProductCsv> getAllItem() {
		long startTime = System.currentTimeMillis();
		List<ProductCsv> allProducts = new ArrayList<ProductCsv>();

		try {
			synchronized (this.allLink) {
				allLink_buildAllLinkItem();
			}
		} catch (Exception e) {
		}
		int numOfTotalLink = allLink.size();
		System.out.println(numOfTotalLink);

		ExecutorService service = Executors.newFixedThreadPool(MAX_THREAD);
		List<ProductCsv> allProduct = new ArrayList<>();
		List<Future<ProductCsv>> listFuture = new ArrayList<Future<ProductCsv>>();
		allLink.forEach((key, list) -> {
			for (String linkProduct : list) {
				Callable<ProductCsv> productCallable = new ProductPanaWorker() {
					@Override
					public ProductCsv call() throws Exception {
						return this.getScrapy().getProductPerLink(key, linkProduct);
					}
				};
				Future<ProductCsv> future = service.submit(productCallable);
				listFuture.add(future);

			}
		});

		for (Future<ProductCsv> future : listFuture) {
			try {
				allProduct.add(future.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		service.shutdown();
		return allProduct;
	}

	public ProductCsv getProductPerLink(String categorykey, String linkProduct) {
		try {
			ProductCsv product = new ProductCsv(this.pageUrl);
			Document doc = this.getDoc(linkProduct);
			Element element = doc.select(".col5r4").first();
			String productCode = element.select("table tr:eq(0) td:eq(1)").text();
			product.setProductModel(productCode);

			String category = getCategoryMonotaro(productCode);
			if (StringUtils.isEmpty(category)) {
				category = categorykey;
			}
			product.setCategory(category);
			
			product.setProductName(element.select("table tr:eq(1) td:eq(1)").text());

			String productPrice = element.select("table tr:eq(2) td:eq(1) font strong").text();
			if (productPrice.isEmpty()) {
				product.setPrice(0);
			} else {
				product.setPrice(Integer.parseInt(productPrice.replaceAll("[^0-9]", "")));
			}
			
			String publishedDate = element.select("table tr td:contains(発売日)").text(); 
			product.setPublishedDate(publishedDate.replaceAll("発売日：",""));
			
			String detail = element.select("table tr:contains(仕様) td:eq(1)").text(); 
			product.setDescription(detail);
			product.setProductUrl(linkProduct);
			product.setBrand("パナソニック");
			product.setImgUrl(doc.select("#thumbnail_area img").attr("src"));
			// product.setCategory(category);
			return product;
		} catch (IOException e) {
			return new ProductCsv(null);
		}

	}

	private void allLink_buildAllLinkItem() throws Exception {
		String url_getDocLv1 = "http://www2.panasonic.biz/es/ai/products/search/category/index.jsp";
		List<String> listCatLv1 = getCatLv1(url_getDocLv1);

		String selector_lv2 = ".categorytree_box #category_tree05 a";
		List<String> listCatLv2 = getLinkCatLvBy(listCatLv1, selector_lv2);

		String selector_caltLv3 = ".categorytree_box #category_tree06 a";
		List<String> listCatLv3 = getLinkCatLvBy(listCatLv2, selector_caltLv3);

		String selector_caltLv4 = ".categorytree_box #category_tree07 a";
		List<String> listCatLv4 = getLinkCatLvBy(listCatLv3, selector_caltLv4);
		for (String cat_url : listCatLv4) {
			Document doc = this.getDoc(cat_url);
			if (isPageHasProduct(doc)) {
				this.allLink.put(getCatetoryBy(doc), getLinksProductInPage(doc));
			}
		}
	}

	private synchronized List<String>  getLinkCatLvBy(List<String> listCatLv1, String selector) throws Exception {
		List<String> listCat = new ArrayList<>();
		ExecutorService service = Executors.newFixedThreadPool(MAX_THREAD);
		List<Future<PanaOutputCallable>> liset = new ArrayList<>();
		for (String cat_url : listCatLv1) {
			Callable<PanaOutputCallable> worker = new WorkerThreadGetLinkPana(cat_url) {
				@Override
				protected PanaOutputCallable process(PanaScrapy scrapy, String param) {
					List<String> listCatLv2 = new ArrayList<>();
					Map<String, List<String>> map = new HashMap<>();
					List<String> allLink = new ArrayList<>();

					try {
						Document doc = scrapy.getDoc(param);
						map.put(scrapy.getCatetoryBy(doc), allLink);
						if (scrapy.isPageHasProduct(doc)) {
							map.get(param).addAll(scrapy.getLinksProductInPage(doc));

						} else {
							Elements el_catLv1 = doc.select(selector);
							listCatLv2.addAll(scrapy.getListCatBy(el_catLv1));
						}
					} catch (IOException e) {
					}
					return new PanaOutputCallable(listCatLv2, map);

				}
			};
			Future<PanaOutputCallable> future = service.submit(worker);
			liset.add(future);

		}
		while (!service.isTerminated()) {
        }
		for (Future<PanaOutputCallable> future : liset) {
			PanaOutputCallable ret = future.get();
			listCat.addAll(ret.getListLinklLv());
			allLink.putAll(ret.getMapAllLink());
		}
		service.shutdown();
		return listCat;
	}

	protected String getCatetoryBy(Document doc) {
		Elements els = doc.select(".categorytree_box li");
		String cate = "";
		;
		int count = 0;
		for (Element element : els) {
			if (count == els.size() - 1) {
				cate = cate + element.text();
			}
			cate = cate + element.text() + ">";
			count++;
		}
		return cate;
	}

	private List<String> getCatLv1(String url_getDocLv1) throws IOException {
		Document doc = this.getDoc(url_getDocLv1);
		if (isPageHasProduct(doc)) {
			this.allLink.put(getCatetoryBy(doc), getLinksProductInPage(doc));

		} else {
			Elements el_catLv1 = this.getDoc(url_getDocLv1).select(".col5 .col a");
			return getListCatBy(el_catLv1);
		}
		return new ArrayList<>();
	}

	public List<String> getLinksProductInPage(Document doc) {
		List<String> list = new ArrayList<>();
		Elements els = doc.select("table.tableH tr a");
		for (Element element : els) {
			list.add(element.attr("href"));
		}
		return list;
	}

	public boolean isPageHasProduct(Document doc) {
		return !doc.select("table.tableH").isEmpty();
	}

	public List<String> getListCatBy(Elements el_catLv1) {
		List<String> listCatLv1 = new ArrayList<>();
		for (Element element : el_catLv1) {
			listCatLv1.add(this.pageUrl + element.attr("href"));
		}
		return listCatLv1;
	}

	@Override
	public List<? extends ProductCsv> getAllItem(int start, int end) {
		return null;
	}

}
