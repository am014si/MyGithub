package com.mb.hadoop.mapper;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HomeInUrBudgetReportGeneratorMapper extends Mapper<Object, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String[] line = value.toString().split(",");
		String saleBucket = null;
		try{
			//PMTLMTRFNUM:PMTCNDPROPTYPE:PMTLISTTYPE,1
	  if(!line[0].equalsIgnoreCase("PMTRFNUM") && line[5] != null && line[5].equalsIgnoreCase("S")){
		  if(line[6] != null && !line[6].isEmpty() && !line[6].equalsIgnoreCase("null")){
			  long salePrice = Long.parseLong(line[6]);
			  if(salePrice <= 2000000){
				  saleBucket = "Upto Rs 20 Lakh";
			  } else if(salePrice > 2000000 && salePrice < 4000000){
				  saleBucket = "Rs 20-40 Lakh";
			  } else if(salePrice >= 4000000 && salePrice < 6000000){
				  saleBucket = "Rs 40-60 Lakh";
			  } else if(salePrice >= 6000000 && salePrice < 10000000){
				  saleBucket = "Rs 60-100 Lakh";
			  } else if(salePrice >= 10000000){
				  saleBucket = "Rs 1 Crore & Above";
			  }
		  }
		  if(!checkNullOrEmpty(line[3]) && !checkNullOrEmpty(line[5])){
			  word.set(line[3]+":"+line[5]+":"+saleBucket);
			  System.out.println(" ## Map Output "+word);
			  context.write(word, one);
		  }
	  }}catch(ArrayIndexOutOfBoundsException ex){
		  
	  }
  }
	
	private boolean checkNullOrEmpty(String inStr){
		 if(inStr == null){
			 return true;
		 } else if(inStr.isEmpty() || inStr.equalsIgnoreCase("null")){
			return true;
		}
		 return false;
	}
}
