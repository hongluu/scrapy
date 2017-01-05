package kiwi.vn.scapy.async;

import java.util.List;

import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.scrapy.service.ScrapyAbstract;

public class RunableCustom implements Runnable{

	private List<ProductCsv> allProduct;
	private ScrapyAbstract srapy;
	private Thread thread;
	private int start;
	private int end;
	
	public RunableCustom(List<ProductCsv> allProduct, ScrapyAbstract srapy, int start, int end) {
		super();
		this.allProduct = allProduct;
		this.srapy = srapy;
		this.start = start;
		this.end = end;
	}
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
		}
		thread.start();
		

	}
	public void run() {
			allProduct.addAll(this.srapy.getAllItem(start,end));
			thread.interrupt();
	}
	public List<ProductCsv> getAllProduct() {
		return allProduct;
	}
	public void setAllProduct(List<ProductCsv> allProduct) {
		this.allProduct = allProduct;
	}
	public ScrapyAbstract getSrapy() {
		return srapy;
	}
	public void setSrapy(ScrapyAbstract srapy) {
		this.srapy = srapy;
	}
	public boolean isRunning (){
		return thread.isAlive();
	}

}
