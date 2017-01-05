//package kiwi.vn.scrapy.service;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import com.opencsv.CSVWriter;
//
//import kiwi.vn.scapy.main.ProgressDialog;
//import kiwi.vn.scrapy.entity.ProductCsv;
//
//
//public class MonotaroScrapy {
//	private static ProgressDialog progressDlg = new ProgressDialog();
//
//
//	public static void main(String[] args) throws Exception{
//		progressDlg.setVisible(true);
//		List<ProductCsv> output = new ArrayList<ProductCsv>();
//		// use for loop here
//		try{
//			
//			List<ProductCsv> ret = new MonotaroScrapy().searchProduct("600VCV2SQx2C");
//			output.addAll(ret);
//			new MonotaroScrapy().searchProduct("WT57511W");
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//		}
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_DD hh_mm_ss");
//		String fileName = "ﾂ鞘ぎﾂ品ﾂ湘ｮﾂ陛ｱ_" + sdf.format(new Date()) + ".csv";
//		File file = new File("./", fileName);
//		CSVWriter writer = new CSVWriter(new FileWriter(file));
//		ArrayList<String[]> outputData = new ArrayList<String[]>();
//		for (ProductCsv product: output){
//			outputData.add(product.toCSV());
//		}
//		writer.writeAll(outputData);
//		writer.close();
//		
//		progressDlg.stop("./" +fileName);
//	}
//	
//	private List<ProductCsv> searchProduct(String productModel) throws Exception{
//		ArrayList<ProductCsv> ret = new ArrayList<ProductCsv>();
////		Document document = Jsoup.connect("https://www.monotaro.com/s/?c=&q=600VCV2SQx2C&swc=0")
//		Document document = Jsoup.connect("https://www.monotaro.com/s/?c=&q=" + productModel + "&swc=0")
//				.method(Connection.Method.GET).get();
//		Elements elements = document.select(".txt");
//		if (!elements.isEmpty()){
//			for (Element element: elements){
//				Elements e1 = element.select("a.product_name");
//				String href = e1.attr("href");
//				String productName = e1.text();
//				
//				String category = element.select(".item_node_path").get(0).text().replaceAll("<.*>", "");
//				
//				Document doc = Jsoup.connect("https://www.monotaro.com" + href)
//						.method(Connection.Method.GET).get();
//				StringBuilder commonDesc = new StringBuilder();
//				
//				Elements abtest = doc.select(".product_data-property abtest");
//				for (Element el: abtest){
//					commonDesc.append(el.text() +"\r\n");
//				}
//				
//				Elements labels = doc.select(".product_data_label");
//				Elements labelValues = doc.select("dd.product-inline");
//				int size = labels.size();
//				for (int ii = 0;ii < size;  ii ++){
//					commonDesc.append(labels.get(ii).text() +" ");
//					commonDesc.append(labelValues.get(ii).text() + " ");
//				}
//				commonDesc.append("\r\n");
//				
//				Elements properties = doc.select(".data-ee-sku");
//				for (Element el : properties){
//					StringBuilder desc = new StringBuilder(commonDesc.toString());
//					String ﾂ陳債閉ｶﾂコﾂーﾂド = el.select(".pd_list_monotaro_no").text();
//					Elements els = el.select(".pd_list");
//					String ﾂ品ﾂ氾� = els.get(1).text();
//					String ﾂ全ﾂ陳ｷ = els.get(2).text();
//					String ﾂ芯ﾂ青� = els.get(3).text();
//					String ﾂ素ﾂ静ｼﾂ径 = els.get(4).text();
//					String ﾂ篠ｯﾂ陛�  = els.get(5).text();
//					String ﾂ静｢ﾂ可渉妥個古ｺﾂつｳ= els.get(6).text();
//					
//					desc.append("ﾂ陳債閉ｶﾂコﾂーﾂド"+ ﾂ陳債閉ｶﾂコﾂーﾂド + "\r\n");
//					desc.append("ﾂ品ﾂ氾�"+ ﾂ品ﾂ氾� + "\r\n");
//					desc.append("ﾂ全ﾂ陳ｷ(m)"+ ﾂ全ﾂ陳ｷ + "\r\n");
//					desc.append("ﾂ芯ﾂ青�"+ ﾂ芯ﾂ青� + "\r\n");
//					desc.append("ﾂ素ﾂ静ｼﾂ径(mm)"+ ﾂ素ﾂ静ｼﾂ径 + "\r\n");
//					desc.append("ﾂ篠ｯﾂ陛�"+ ﾂ篠ｯﾂ陛� + "\r\n");
//					desc.append("ﾂ静｢ﾂ可渉妥個古ｺﾂつｳ(mm)"+ ﾂ静｢ﾂ可渉妥個古ｺﾂつｳ + "\r\n");
//					
//					String priceTxt = el.select(".pd_sp_price").text().replace("ﾂ�ﾂ�","").replace(",", "");
//					long price = (long)(1.08* Double.parseDouble(priceTxt));
//					
//					String daynum = el.select(".shipping_icons_s").attr("src").replaceAll(".*ship_day", "").replaceAll("[^0-9]", "");
//					String delivery = "";
//					if ("1".equals(daynum)){
//						delivery = "ﾂ督鳴禿ｺ";
//					}
//					else{
//						delivery = daynum + "ﾂ禿ｺ";
//					}
//					
//					ProductCsv product = new ProductCsv("ﾂδつノﾂタﾂδ債ウ", productName, productModel, desc.toString(), price, -1, delivery, "https://www.monotaro.com" +href, "");
//					ret.add(product);
//				}
//			}
//		}
//		else{
//			return java.util.Collections.emptyList();
//		}
//		
//		return ret;
//	}
//}