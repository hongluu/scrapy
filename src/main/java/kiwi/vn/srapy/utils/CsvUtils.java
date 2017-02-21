package kiwi.vn.srapy.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

import kiwi.vn.scrapy.entity.ProductCsv;

public class CsvUtils {
	public static String writeToCsv(List<ProductCsv> output,String fileName) throws IOException {
		
		File file = new File("./", fileName);
		CSVWriter writer = new CSVWriter(new FileWriter(file));
		ArrayList<String[]> outputData = new ArrayList<String[]>();
		for (ProductCsv product: output){
			outputData.add(product.toCSV());
		}
		writer.writeAll(outputData);
		writer.close();
		return fileName;
	}
	public  static String appendToCsv(ProductCsv input , String fileName) {
		
		File file = new File("./", fileName);
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(file,true));
			ArrayList<String[]> outputData = new ArrayList<String[]>();
			outputData.add(input.toCSV());
			writer.writeAll(outputData);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileName;
	}
	
}
