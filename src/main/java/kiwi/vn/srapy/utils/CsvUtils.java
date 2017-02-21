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
<<<<<<< HEAD
	public  static String appendToCsv(ProductCsv input , String fileName) {
		
=======
	public static String appendToCsv(ProductCsv input , String fileName) {
		System.out.println("Write product: "+ input.getProductModel());
>>>>>>> 479c3a2dc10e49add605bd6855f27cb91733f2ef
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
	
public static String appendToCsv(List<ProductCsv> input , String fileName) {
		
	File file = new File("./", fileName);
	CSVWriter writer;
	ArrayList<String[]> outputData = new ArrayList<String[]>();
	try {
		writer = new CSVWriter(new FileWriter(file,true));
		for (ProductCsv product: input){
			System.out.println("Write product: "+ product.getProductModel());
			outputData.add(product.toCSV());
		}
		writer.writeAll(outputData);
		writer.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	
	return fileName;
	}
	
}
