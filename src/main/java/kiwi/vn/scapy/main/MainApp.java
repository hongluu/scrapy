package kiwi.vn.scapy.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.scrapy.service.DenzaiScrapy;
import kiwi.vn.scrapy.service.MonotaroScrapy;
import kiwi.vn.scrapy.service.TarotoScrapy;

public class MainApp {
	private static ProgressDialog progressDlg = new ProgressDialog();
	public static void main(String[] args) {
		progressDlg.setVisible(true);
		String fileName =formatFileName();
		DenzaiScrapy denzaiScrapy = new DenzaiScrapy();
		denzaiScrapy.setFileName(fileName);
		List<ProductCsv> listdenzai = denzaiScrapy.processPage();
		
		TarotoScrapy tarotoScrapy = new TarotoScrapy();
		tarotoScrapy.setFileName(fileName);
		List<ProductCsv> listtaroto = tarotoScrapy.processPage();
		
		MonotaroScrapy monoScrapy =new MonotaroScrapy();
		monoScrapy.setFileName(fileName);
		monoScrapy.setListProductCodePrepare(listdenzai,listtaroto);
		monoScrapy.processPage();
		progressDlg.stop("./" +fileName);
	}
	private static  String formatFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_DD hh_mm_ss");
		return "All_Product_" + sdf.format(new Date()) + ".csv";
	}
	
}
