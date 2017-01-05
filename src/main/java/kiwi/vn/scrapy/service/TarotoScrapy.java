package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kiwi.vn.scrapy.entity.ProductCsv;

public class TarotoScrapy extends ScrapyAbstract {
	public TarotoScrapy() {
		this.log.debug("Start scrapy with taroto.jp page");
		this.pageUrl = "http://www.taroto.jp";

	}

	@Override
	protected List<ProductCsv> getAllItem() {
		List<ProductCsv> allItems = new ArrayList<ProductCsv>();
		List<String> allCategoriesURLLv2 = new ArrayList<String>();

		List<String> allCategoriesURLLv1;
		try {
			allCategoriesURLLv1 = getAllCategoriesURLLv1(this.pageUrl);
		} catch (IOException e) {
			return null;
		}

		for (String categoryURLLv1 : allCategoriesURLLv1) {
			try {
				findAllItems(categoryURLLv1);
			} catch (IOException e) {
				continue;
			}
		}
		return allItems;
	}

	private List<String> allItmes = new ArrayList<String>();

	private void findAllItems(String categoryURLLv1) throws IOException {
		Document doc = getDoc(categoryURLLv1);
		Elements els = doc.select("div>table>tbody>tr>td table:eq(3) td:eq(1) table td>a:has(img,font)");
		if (els.first().attr("href").contains("/item/")) {
			for (Element element : els) {
				allItmes.add(getItemLink(element.attr("href")));
			}
		} else {
			try {
				List<String> listCat = getListCatCurrent(els);
				for (String cat : listCat) {
					findAllItems(cat);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private List<String> getListCatCurrent(Elements els) {
		List<String> listCat = new ArrayList<String>();
		for (Element element : els) {
			String linkCat = element.attr("href");	
			if(linkCat.contains("http://www.taroto.jp/category/") && !isInListCat(linkCat ,listCat) ){
				listCat.add(linkCat);
			}
		}
		return listCat;
	}

	private boolean isInListCat(String linkCat, List<String> listCat) {
		for (String cat : listCat) {
			if(cat.equals(linkCat))
				return true;
		}
		return false;
	}

	private String getItemLink(String attr) {
		if(attr.contains(this.pageUrl)){
			return attr;
		}else{
			return this.pageUrl+attr.replace("..", "");
		}
	}

	private List<String> getAllCategoriesURLLv1(String pageUrl) throws IOException {
		List<String> output = new ArrayList<String>();
		Document doc = getDoc(pageUrl);
		Elements els = doc.select(".r-side a[href]");
		for (Element element : els) {
			output.add(element.attr("href"));
		}
		return output;
	}
}
