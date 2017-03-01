package kiwi.vn.scapy.async;
import java.util.concurrent.Callable;

import kiwi.vn.scrapy.entity.PanaOutputCallable;
import kiwi.vn.scrapy.service.PanaScrapy;

public  class WorkerThreadGetLinkPana implements Callable<PanaOutputCallable >{

	private PanaScrapy scrapy;
	private String param;
	
	public WorkerThreadGetLinkPana(String param) {
		this.scrapy = new PanaScrapy();
		this.param = param;
	}




	@Override
	public PanaOutputCallable call() throws Exception {
		 return process(scrapy, param);
	}




	protected PanaOutputCallable  process(PanaScrapy scrapy, String param) {
		return null;
		
	}

}
