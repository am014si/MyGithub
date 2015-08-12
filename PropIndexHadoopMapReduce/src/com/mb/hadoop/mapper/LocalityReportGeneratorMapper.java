package com.mb.hadoop.mapper;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class LocalityReportGeneratorMapper extends
		Mapper<Object, Text, Text, IntWritable> {
	private Text word = new Text();

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		String[] line = value.toString().split(",");
		System.out.println(value.toString());
		// Locality:City:ListingType
		word.set(removeDoubleQuotes(line[3]) + ":"
				+ removeDoubleQuotes(line[1]) + ":"
				+ removeDoubleQuotes(line[4]));
		if((removeStartingDoubleQuotes(line[4]).equalsIgnoreCase("R") && removeEndingDoubleQuotes(line[5]).equalsIgnoreCase("S")) ||(removeStartingDoubleQuotes(line[4]).equalsIgnoreCase("S") && removeEndingDoubleQuotes(line[5]).equalsIgnoreCase("R")) || (removeStartingDoubleQuotes(line[4]).equalsIgnoreCase("R") && removeEndingDoubleQuotes(line[5]).equalsIgnoreCase("R"))){
			context.write(word, new IntWritable(Integer.parseInt(line[7])));
		} else {
			context.write(word, new IntWritable(Integer.parseInt(line[6])));
		}
	}

	private String removeDoubleQuotes(String inStr) {
		String formattedStr = "";
		if (inStr != null && !inStr.isEmpty()) {
			if (inStr.contains("\"")) {
				formattedStr = inStr.substring(1, inStr.length() - 1);
			} else {
				formattedStr = inStr;
			}
		}
		return formattedStr;
	}
	
	private String removeStartingDoubleQuotes(String inStr) {
		String formattedStr = "";
		if (inStr != null && !inStr.isEmpty()) {
			if (inStr.contains("\"")) {
				formattedStr = inStr.substring(1, inStr.length());
			} else {
				formattedStr = inStr;
			}
		}
		return formattedStr;
	}
	
	private String removeEndingDoubleQuotes(String inStr) {
		String formattedStr = "";
		if (inStr != null && !inStr.isEmpty()) {
			if (inStr.contains("\"")) {
				formattedStr = inStr.substring(0, inStr.length()-1);
			} else {
				formattedStr = inStr;
			}
		}
		return formattedStr;
	}
}
