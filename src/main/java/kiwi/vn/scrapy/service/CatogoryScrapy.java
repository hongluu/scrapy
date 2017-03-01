package kiwi.vn.scrapy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

import kiwi.vn.scrapy.entity.Categories;
import kiwi.vn.scrapy.repo.CategoryMongoRepo;

public class CatogoryScrapy {

	public Document getDoc(String pageUrl) throws IOException {
		return Jsoup.connect(pageUrl).header("Accept-Encoding", "gzip, deflate")
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0").maxBodySize(0)
				.timeout(600000).get();
	}

	public static void main(String[] args) throws IOException {
		CategoryMongoRepo repo = new CategoryMongoRepo();
		CatogoryScrapy scrapy = new CatogoryScrapy();
		List<Categories> allCats = new ArrayList<>();
		allCats.addAll(scrapy.getCatMitsu());

		allCats.addAll(scrapy.getCatPana("http://www2.panasonic.biz/es/ai/products/search/category/index.jsp",
				".col5 .col", "h2", "ul>p"));
		allCats.addAll(scrapy.getCatDenzai("http://www.denzai-net.jp/category/index.htm"));
		allCats.addAll(scrapy.getCatPana("https://www.monotaro.com/", ".category_3column_table dl", "dt", "dd"));
		allCats.addAll(scrapy.getCatTatoro("http://www.taroto.jp/"));
		List<String> outputJson = new ArrayList<>();
		for (Categories cat : allCats) {
			String json = JSON.toJSONString(cat);
			outputJson.add(JSON.toJSONString(cat));
			repo.insert(json);
		}
	}

	private List<Categories> getCatTatoro(String Url) throws IOException {
		Document doc = this.getDoc(Url);
		List<Categories> output = new ArrayList<>();
		Elements elsCatLv1 = doc.select(".center h2");
		for (int i = 0; i < elsCatLv1.size(); i++) {
			Categories catLv1 = new Categories();
			String catNameLv1 = elsCatLv1.get(i).select("img").attr("alt");
			catLv1.setName(catNameLv1);
			catLv1.setPath(catNameLv1);
			List<Categories> subcats = new ArrayList<>();
			Element elTopCatLV2 = doc.select(".center ul").get(i);
			Elements elsCatLV2 = elTopCatLV2.select("a");
			for (Element element : elsCatLV2) {
				Categories catLv2 = new Categories();
				String catNameLv2 = element.text();
				catLv2.setName(catNameLv2);
				catLv2.setPath(catNameLv1 + ">" + catNameLv2);
				subcats.add(catLv2);
			}
			catLv1.setSubCats(subcats);
			output.add(catLv1);
		}

		return output;
	}

	private List<Categories> getCatDenzai(String Url) throws IOException {
		Document doc = this.getDoc(Url);
		List<Categories> output = new ArrayList<>();
		Elements elsCatLv1 = doc.select("#cat_all_list h2");
		for (int i = 0; i < elsCatLv1.size(); i++) {
			Categories catLv1 = new Categories();
			String catNameLv1 = elsCatLv1.get(i).text();
			catLv1.setName(catNameLv1);
			catLv1.setPath(catNameLv1);
			List<Categories> subcats = new ArrayList<>();
			Element elTopCatLV2 = doc.select("#cat_all_list .all_list_area").get(i);
			Elements elsCatLV2 = elTopCatLV2.select("dl");
			for (Element element : elsCatLV2) {
				Categories catLv2 = new Categories();
				String catNameLv2 = element.select(".td").text();
				catLv2.setName(catNameLv2);
				catLv2.setPath(catNameLv1 + ">" + catNameLv2);
				subcats.add(catLv2);
			}
			catLv1.setSubCats(subcats);
			output.add(catLv1);
		}

		return output;
	}

	private List<Categories> getCatPana(String Url, String el1Selector, String el1NameSelector, String el2Selector)
			throws IOException {
		Document doc = this.getDoc(Url);
		List<Categories> output = new ArrayList<>();
		Elements elsCatLv1 = doc.select(el1Selector);
		for (Element elCatLv1 : elsCatLv1) {
			Categories catLv1 = new Categories();
			String catNameLv1 = elCatLv1.select(el1NameSelector).text();
			catLv1.setName(catNameLv1);
			catLv1.setPath(catNameLv1);
			List<Categories> subcats = new ArrayList<>();
			Elements elsCatLV2 = elCatLv1.select(el2Selector);
			for (Element element : elsCatLV2) {
				Categories catLv2 = new Categories();
				String catNameLv2 = element.text();
				catLv2.setName(catNameLv2);
				catLv2.setPath(catNameLv1 + ">" + catNameLv2);
				subcats.add(catLv2);
			}
			catLv1.setSubCats(subcats);
			output.add(catLv1);
		}

		return output;
	}

	public List<Categories> getCatMitsu() throws IOException {
		Document doc = this.getDoc(
				"http://www.mitsubishielectric.co.jp/ldg/wink/selectProductCategory.do?ccd=202010&_=1487475210601");
		List<Categories> output = new ArrayList<>();
		Element elDN = doc.select("table").first();
		Elements elsDNLv1 = elDN.select("th");
		output.addAll(getCatsMitsuBy(elDN, elsDNLv1, "[業務用]"));

		elDN = doc.select("table").last();
		elsDNLv1 = elDN.select("th");
		output.addAll(getCatsMitsuBy(elDN, elsDNLv1, "[住宅用]"));
		return output;
	}

	private List<Categories> getCatsMitsuBy(Element elDN, Elements elsDNLv1, String prefix) {
		List<Categories> output = new ArrayList<>();
		for (int i = 0; i < elsDNLv1.size(); i++) {
			Categories cate = new Categories();
			String catNameLv1 = prefix + elsDNLv1.get(i).text();
			cate.setName(catNameLv1);
			cate.setPath(catNameLv1);
			List<Categories> subcats = new ArrayList<Categories>();
			Elements elsCatLv2 = elDN.select("tr td:eq(" + i + ") ul li");
			for (Element element : elsCatLv2) {
				Categories cateLv2 = new Categories();
				String catNameLv2 = element.text();
				cateLv2.setName(catNameLv2);
				cateLv2.setPath(catNameLv1 + ">" + catNameLv2);
				subcats.add(cateLv2);
			}
			cate.setSubCats(subcats);
			output.add(cate);
		}
		return output;
	}
}
