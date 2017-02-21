package kiwi.vn.scrapy.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import kiwi.vn.scapy.async.RunableCustom;
import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.srapy.utils.CsvUtils;


public class MonotaroScrapy extends ScrapyAbstract{
	public MonotaroScrapy(){
		super();
		this.log.debug("Start scrapy with Monotaro page");
		System.out.println("Start scrapy with Monotaro page");
		this.pageUrl = "https://www.monotaro.com";
	}
	private  List<ProductCsv> searchProduct(String productModel) throws Exception{
		ArrayList<ProductCsv> ret = new ArrayList<ProductCsv>();
//		Document document = Jsoup.connect("https://www.monotaro.com/s/?c=&q=600VCV2SQx2C&swc=0")
		Document document = Jsoup.connect("https://www.monotaro.com/s/?c=&q=" + productModel + "&swc=0")
				.method(Connection.Method.GET).get();
		Elements elements = document.select(".txt");
		if (!elements.isEmpty()){
			for (Element element: elements){
				Elements e1 = element.select("a.product_name");
				String href = e1.attr("href");
				String productName = e1.text();
				
				//String category = element.select(".item_node_path").get(0).text().replaceAll("<.*>", "");
				
				Document doc = Jsoup.connect("https://www.monotaro.com" + href)
						.method(Connection.Method.GET).get();
				StringBuilder commonDesc = new StringBuilder();
				
				Elements abtest = doc.select(".product_data-property abtest");
				for (Element el: abtest){
					commonDesc.append(el.text() +"\r\n");
				}
				
				Elements labels = doc.select(".product_data_label");
				Elements labelValues = doc.select("dd.product-inline");
				int size = labels.size();
				for (int ii = 0;ii < size;  ii ++){
					commonDesc.append(labels.get(ii).text() +" ");
					commonDesc.append(labelValues.get(ii).text() + " ");
				}
				
				Elements properties = doc.select(".data-ee-sku");
				for (Element el : properties){
					StringBuilder desc = new StringBuilder(commonDesc.toString());
					String 注文コード = el.select(".pd_list_monotaro_no").text();
					Elements els = el.select(".pd_list");
					String 品番 = els.get(1).text();
					String 全長 = els.get(2).text();
					String 芯数 = els.get(3).text();
					String 素線径 = els.get(4).text();
					String 識別  = els.get(5).text();
					String 絶縁体厚さ= els.get(6).text();
					
					desc.append("注文コード"+ 注文コード + "\r\n");
					desc.append("品番"+ 品番 + "\r\n");
					desc.append("全長(m)"+ 全長 + "\r\n");
					desc.append("芯数"+ 芯数 + "\r\n");
					desc.append("素線径(mm)"+ 素線径 + "\r\n");
					desc.append("識別"+ 識別 + "\r\n");
					desc.append("絶縁体厚さ(mm)"+ 絶縁体厚さ + "\r\n");
					
					String priceTxt = el.select(".pd_sp_price").text().replace("￥","").replace(",", "");
					double price = (double)(1.08* Double.parseDouble(priceTxt));
					
					String daynum = el.select(".shipping_icons_s").attr("src").replaceAll(".*ship_day", "").replaceAll("[^0-9]", "");
					String delivery = "";
					if ("1".equals(daynum)){
						delivery = "当日";
					}
					else{
						delivery = daynum + "日";
					}
					
					ProductCsv product = new ProductCsv("モノタロウ", productName, productModel, desc.toString(), price, -1, delivery, href, "");
					product.setCategory(category);
					ret.add(product);
				}
			}
		}
		else{
			return java.util.Collections.emptyList();
		}
		
		return ret;
	}
	private List<String> listProductCodePrepare = new ArrayList<>();
	
	public void setListProductCodePrepare(List<ProductCsv> listdenzai, List<ProductCsv> listtaroto) {
		listdenzai.forEach(x -> listProductCodePrepare.add(x.getProductModel()));
		for (ProductCsv productCsv : listtaroto) {
			String productCode = productCsv.getProductModel();
			if(!isInList(productCode, listProductCodePrepare)){
				listProductCodePrepare.add(productCode);
			}
		}
		
	}
	private boolean isInList(String productCode, List<String> output) {
		if(productCode == null){
			return false;
		}
		for (String string : output) {
			if(productCode.equals(string)){
				return true;
			}
		}
		return false;
	}

	public List<String> getListProductCodePrepare(List<ProductCsv> listdenzai, List<ProductCsv> listtaroto) {
		return null;
	}


	@Override
	protected List<ProductCsv> getAllItem() {
		int MAX_THREAD = 10;
		long startTime = System.currentTimeMillis();
		List<ProductCsv> allProducts = new ArrayList<ProductCsv>();
		int numOfTotalLink =this.listProductCodePrepare.size();
		System.out.println("Total Item: " + numOfTotalLink);
		List<RunableCustom> listRun= new ArrayList<RunableCustom>();
		int nJump = numOfTotalLink/MAX_THREAD;
		for (int ii = 0; ii < MAX_THREAD; ii++) {
			if(ii == MAX_THREAD-1){
				listRun.add(new RunableCustom(allProducts, this,ii*(nJump),numOfTotalLink));
			}else{
				listRun.add(new RunableCustom(allProducts, this,ii*(nJump),(ii+1)*(nJump)));
			}
			listRun.get(ii).start();
		}
		while(true){	
			if(isAllThreadDone(listRun)){
				System.out.println("=====COMPLETE IN ====== :"+(System.currentTimeMillis()-startTime)/1000 + " s");
//				System.out.println("=====    TOTAL   ====== :"+allProducts.size() +"item");
				return allProducts;
			}
		}
	}

	@Override
	public List<? extends ProductCsv> getAllItem(int start, int end) {
		List<ProductCsv> listProducts = new ArrayList<ProductCsv>();
		for (int i = start; i < end; i++) {
			String itemLink = listProductCodePrepare.get(i);
			try {
				 List<ProductCsv> productList = searchProduct(itemLink);
				 CsvUtils.appendToCsv(productList, this.getFileName());
				 listProducts.addAll(productList);
			} catch (Exception e) {
				continue;
			}
		}
		return listProducts;
	}
}