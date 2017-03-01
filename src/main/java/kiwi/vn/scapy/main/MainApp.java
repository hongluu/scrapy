package kiwi.vn.scapy.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.scrapy.service.DenzaiScrapy;
import kiwi.vn.scrapy.service.MitsubishielectricScrapy;
import kiwi.vn.scrapy.service.MonotaroScrapy;
import kiwi.vn.scrapy.service.PanaScrapy;
import kiwi.vn.scrapy.service.TarotoScrapy;
import kiwi.vn.srapy.utils.CsvUtils;

public class MainApp {
	private static ProgressDialog progressDlg = new ProgressDialog();
	public static void main(String[] args) {
		progressDlg.setVisible(true);
		String fileName =formatFileName();

		List<String> listProductCodeFactory = new ArrayList<>();
		MitsubishielectricScrapy mitsubishielectricScrapy = new MitsubishielectricScrapy();
		mitsubishielectricScrapy.setFileName(fileName);
		
		List<ProductCsv> listMitsubishi = mitsubishielectricScrapy.processPage();	
		for (ProductCsv productCsv : listMitsubishi) {
			CsvUtils.appendToCsv(productCsv, fileName);
			listProductCodeFactory.add(productCsv.getProductModel());
		}
		System.out.println("Complete misubishi :"+listMitsubishi.size());
		PanaScrapy panaScrapy = new PanaScrapy();
		panaScrapy.setFileName(fileName);
		List<ProductCsv> listPana = panaScrapy.processPage();
		for (ProductCsv productCsv : listPana) {
			CsvUtils.appendToCsv(productCsv, fileName);
			listProductCodeFactory.add(productCsv.getProductModel());
		}
		System.out.println("Complete pana :"+listPana.size());
		
//		// 2 trang nay ghi csv ben trong
		DenzaiScrapy denzaiScrapy = new DenzaiScrapy();
		denzaiScrapy.setFileName(fileName);
		denzaiScrapy.setListProductCodeExclude(listProductCodeFactory);
		List<ProductCsv> listdenzai = denzaiScrapy.processPage();
		
		TarotoScrapy tarotoScrapy = new TarotoScrapy();
		tarotoScrapy.setFileName(fileName);
		tarotoScrapy.setListProductCodeExclude(listProductCodeFactory);
		List<ProductCsv> listtaroto = tarotoScrapy.processPage();

		progressDlg.stop("./" +fileName);
	}
	private static  String formatFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_DD hh_mm_ss");
		return "All_Product_" + sdf.format(new Date()) + ".csv";
	}
}
