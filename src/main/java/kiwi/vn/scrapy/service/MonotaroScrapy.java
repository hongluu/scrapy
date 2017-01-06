package kiwi.vn.scrapy.service;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;

import kiwi.vn.scapy.main.ProgressDialog;
import kiwi.vn.scrapy.entity.ProductCsv;


public class MonotaroScrapy extends ScrapyAbstract{
	private static ProgressDialog progressDlg = new ProgressDialog();


	public static void main(String[] args) throws Exception{
		progressDlg.setVisible(true);
		List<ProductCsv> output = new ArrayList<ProductCsv>();
		// use for loop here
		try{
			
			List<ProductCsv> ret = new MonotaroScrapy().searchProduct("600VCV2SQx2C");
			output.addAll(ret);
			new MonotaroScrapy().searchProduct("WT57511W");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_DD hh_mm_ss");
		String fileName = "ﾂ鞘ぎﾂ品ﾂ湘ｮﾂ陛ｱ_" + sdf.format(new Date()) + ".csv";
		File file = new File("./", fileName);
		CSVWriter writer = new CSVWriter(new FileWriter(file));
		ArrayList<String[]> outputData = new ArrayList<String[]>();
		for (ProductCsv product: output){
			outputData.add(product.toCSV());
		}
		writer.writeAll(outputData);
		writer.close();
		
		progressDlg.stop("./" +fileName);
	}
	
	private List<ProductCsv> searchProduct(String productModel) throws Exception{
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
				
				String category = element.select(".item_node_path").get(0).text().replaceAll("<.*>", "");
				
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
					ret.add(product);
				}
			}
		}
		else{
			return java.util.Collections.emptyList();
		}
		
		return ret;
	}

	public void setListProductCodePrepare(List<ProductCsv> listdenzai, List<ProductCsv> listtaroto) {
		List<String> output = new ArrayList<String>();
		listdenzai.forEach(x -> output.add(x.getProductModel()));
		for (ProductCsv productCsv : listtaroto) {
			String productCode = productCsv.getProductModel();
			if(!isInList(productCode, output)){
				output.add(productCode);
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

	public List<ProductCsv> processPage() {
		
		return null;
	}

	@Override
	protected List<ProductCsv> getAllItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ProductCsv> getAllItem(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}
}