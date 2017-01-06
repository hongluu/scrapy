package kiwi.vn.srapy.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
 public  static List<String> getListLinkFromFile(String filePath){
	 FileInputStream fstream;
	 List<String> output = new ArrayList<String>() ;
	try {
		fstream = new FileInputStream(filePath);
		 BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		 String strLine;
		 while ((strLine = br.readLine()) != null)   {
			 output.add(strLine);
		 }
		 br.close();
	} catch (FileNotFoundException e) {
		return output;
	} catch (IOException e) {
		return output;
	}
	
	 return output;
 }
}
