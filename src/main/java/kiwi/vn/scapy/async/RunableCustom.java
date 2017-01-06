package kiwi.vn.scapy.async;

import java.util.List;

import kiwi.vn.scrapy.entity.ProductCsv;
import kiwi.vn.scrapy.service.ScrapyAbstract;

// TODO: Auto-generated Javadoc
/**
 * The Class RunableCustom.
 */
public class RunableCustom implements Runnable{

	/** The all product. */
	private List<ProductCsv> allProduct;
	
	/** The srapy. */
	private ScrapyAbstract srapy;
	
	/** The thread. */
	private Thread thread;
	
	/** The start. */
	private int start;
	
	/** The end. */
	private int end;
	
	/**
	 * Instantiates a new runable custom.
	 *
	 * @param allProduct the all product
	 * @param srapy the srapy
	 * @param start the start
	 * @param end the end
	 */
	public RunableCustom(List<ProductCsv> allProduct, ScrapyAbstract srapy, int start, int end) {
		super();
		this.allProduct = allProduct;
		this.srapy = srapy;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Start.
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
		}
		thread.start();
		

	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
			allProduct.addAll(this.srapy.getAllItem(start,end));
			thread.interrupt();
	}
	
	/**
	 * Gets the all product.
	 *
	 * @return the all product
	 */
	public List<ProductCsv> getAllProduct() {
		return allProduct;
	}
	
	/**
	 * Sets the all product.
	 *
	 * @param allProduct the new all product
	 */
	public void setAllProduct(List<ProductCsv> allProduct) {
		this.allProduct = allProduct;
	}
	
	/**
	 * Gets the srapy.
	 *
	 * @return the srapy
	 */
	public ScrapyAbstract getSrapy() {
		return srapy;
	}
	
	/**
	 * Sets the srapy.
	 *
	 * @param srapy the new srapy
	 */
	public void setSrapy(ScrapyAbstract srapy) {
		this.srapy = srapy;
	}
	
	/**
	 * Checks if is running.
	 *
	 * @return true, if is running
	 */
	public boolean isRunning (){
		return thread.isAlive();
	}

}
