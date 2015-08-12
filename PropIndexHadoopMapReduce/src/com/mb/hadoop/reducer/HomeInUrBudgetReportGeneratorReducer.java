package com.mb.hadoop.reducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BasicBSONObject;
import com.mb.hadoop.bean.HomeInUrBudgetBean;
import com.mongodb.hadoop.io.BSONWritable;

public class HomeInUrBudgetReportGeneratorReducer extends Reducer<Text,IntWritable,Text,BSONWritable>{

	private static List<String> rfNumCodesList = new ArrayList<String>();
	private static Text word = new Text();
	private int year;
	private int month;
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		try{
		Configuration conf = context.getConfiguration();
		year = Integer.parseInt(conf.get("year"));
		month = Integer.parseInt(conf.get("month"));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, (month-1));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.YEAR, year);
        Date date = cal.getTime();
		int sum = 0;
		for (IntWritable val : values) {
			sum += val.get();
		}
		boolean cityFound = false;
		boolean propTypeFound = false;
		String[] lrtArray = key.toString().split(":");
		HomeInUrBudgetBean homeInUrBudgetBean = new HomeInUrBudgetBean();
		homeInUrBudgetBean.setSalePriceBucket(lrtArray[2]);
		
		for(int i=0;i<rfNumCodesList.size();i++){
			
			String rfLine = rfNumCodesList.get(i);
			String[] rfLineArray = rfLine.split(",");
			if(!checkNullOrEmpty(rfLineArray[0]) && rfLineArray[0].equalsIgnoreCase(lrtArray[0]) && !lrtArray[0].equalsIgnoreCase("null")){
				homeInUrBudgetBean.setLocalityName(rfLineArray[1]);
				
				//Get City Name from rfCodesList
				for(String rfStr:rfNumCodesList){
					String[] rfStrArray = rfStr.split(",");
					if(rfLineArray[2].equalsIgnoreCase(rfStrArray[0])){
						homeInUrBudgetBean.setCity(rfStrArray[1]);
						cityFound = true;
						break;
					}
				}
				
				/*//Get Property Type from rfCodesList
				for(String rfProptype:rfNumCodesList){
					String[] rfProptypeArray = rfProptype.split(",");
					if(lrtArray[1].equalsIgnoreCase(rfProptypeArray[0])){
						homeInUrBudgetBean.setPropertyType(rfProptypeArray[1]);
						propTypeFound = true;
						break;
					}
				}*/
				if(cityFound){
					break;
				}
			}
			
		}
		
		homeInUrBudgetBean.setCount(sum);
		if(!homeInUrBudgetBean.isDataNullOrEmpty()){
			word.set(getRandomIdValue());
			BasicBSONObject map = new BasicBSONObject();
			map.put("Locality",homeInUrBudgetBean.getLocalityName());
			map.put("City",homeInUrBudgetBean.getCity());
			//map.put("PropertyType",homeInUrBudgetBean.getPropertyType());
			map.put("Date", date);
			map.put("SalePriceBucket", homeInUrBudgetBean.getSalePriceBucket());
			map.put("Count", homeInUrBudgetBean.getCount());
			context.write(word, new BSONWritable( map ));
		}}catch(Exception ex){
			System.out.println(ex);
		}
		
	}
	
	public void setup(Context context) throws IOException, InterruptedException {
	    Configuration conf = context.getConfiguration();
	    FileSystem fs = FileSystem.getLocal(conf);
	    String line = null;
	    boolean isHeader = true;
	    Path[] dataFile = DistributedCache.getLocalCacheFiles(conf);
	    // [0] index for rfNumCodes file.
	    BufferedReader cacheReader = null;
	    for(int i=0;i<dataFile.length;i++){
		    cacheReader = new BufferedReader(new InputStreamReader(fs.open(dataFile[i])));
		    while((line = cacheReader.readLine()) != null){
		    	if(!isHeader){
		    		if(i==0){
		    			rfNumCodesList.add(line);
		    		} 
	    		}
		    	else if(isHeader){
		    		isHeader = false;
		    	}
		    }
	    }
	    System.out.println("## RfnumcodesList Size "+rfNumCodesList.size());
	}
	
	public String getRandomIdValue() {
		Random randomService = new Random();
		StringBuilder sb = new StringBuilder();
		while (sb.length() < 10) {
		    sb.append(Integer.toHexString(randomService.nextInt()));
		}
		sb.setLength(10);
		return sb.toString();
	}
	
	private boolean checkNullOrEmpty(String inStr){
		return (inStr == null && inStr.isEmpty() && inStr.equalsIgnoreCase("null"));
	}
}
