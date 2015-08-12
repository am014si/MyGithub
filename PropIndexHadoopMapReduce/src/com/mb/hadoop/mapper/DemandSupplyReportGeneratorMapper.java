package com.mb.hadoop.mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.bson.BasicBSONObject;

import com.mb.hadoop.bean.Tpcnd;
import com.mb.hadoop.bean.Tplmt;
import com.mb.hadoop.bean.Tplrt;
import com.mongodb.hadoop.io.BSONWritable;

public class DemandSupplyReportGeneratorMapper extends
		Mapper<Object, Text, Text, BSONWritable> {
	private static List<String> rfNumCodesList = new ArrayList<String>();
	private static List<Tpcnd> tpcndList = new ArrayList<Tpcnd>();
	private static List<Tplmt> tplmtList = new ArrayList<Tplmt>();
	private Text word = new Text();
	private int year;
	private int month;

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		try {
			Configuration conf = context.getConfiguration();
			year = Integer.parseInt(conf.get("year"));
			month = Integer.parseInt(conf.get("month"));
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.MONTH, (month-1));
	        cal.set(Calendar.DAY_OF_MONTH, 1);
	        cal.set(Calendar.YEAR, year);
	        Date date = cal.getTime();
			
			//System.out.println("## Date "+date);
			int propCount =0;
			String[] line = value.toString().split(",");
			FileSplit fileSplit = (FileSplit) context.getInputSplit();
			String filename = fileSplit.getPath().getName();
			System.out.println(" filename "+filename);
			BasicBSONObject map = new BasicBSONObject();
			if (filename.contains("Bedroom")) {
				if (filename.contains("TPSREC")) {
					map.put("Type", "Demand");
				} else if (filename.contains("TPLMT")) {
					map.put("Type", "Supply");
				}
				map.put("State", removeDoubleQuotes(line[0]));
				map.put("City", removeDoubleQuotes(line[1]));
				map.put("Zone", removeDoubleQuotes(line[2]));
				map.put("Locality", removeDoubleQuotes(line[3]));
				map.put("ListingType", removeDoubleQuotes(line[4]));
				map.put("PropertyType", removeDoubleQuotes(line[5]));
				map.put("NoOfBedrooms", removeDoubleQuotes(line[6]));
				map.put("Count", removeDoubleQuotes(line[7]));
			} else if (filename.contains("Property")) {
				if (filename.contains("TPSREC")) {
					map.put("Type", "Demand");
				} else if (filename.contains("TPLMT")) {
					map.put("Type", "Supply");
				}
				map.put("State", removeDoubleQuotes(line[0]));
				map.put("City", removeDoubleQuotes(line[1]));
				map.put("Zone", removeDoubleQuotes(line[2]));
				map.put("Locality", removeDoubleQuotes(line[3]));
				map.put("ListingType", removeDoubleQuotes(line[4]));
				map.put("PropertyType", removeDoubleQuotes(line[5]));
				map.put("Count", removeDoubleQuotes(line[6]));
				//System.out.println("#### PropCount "+propCount++);
			} else if (filename.contains("Budget")) {
				System.out.println("@@@ Budget Data @@@"+line);
				if (filename.contains("TPSREC")) {
					map.put("Type", "Demand");
				} else if (filename.contains("TPLMT")) {
					map.put("Type", "Supply");
				}
				map.put("State", removeDoubleQuotes(line[0]));
				map.put("City", removeDoubleQuotes(line[1]));
				map.put("Zone", removeDoubleQuotes(line[2]));
				map.put("Locality", removeDoubleQuotes(line[3]));
				map.put("ListingType", removeDoubleQuotes(line[4]));
				map.put("PropertyType", removeDoubleQuotes(line[5]));
				if (line[4] != null && !line[4].isEmpty()) {
					if (removeDoubleQuotes(line[4]).equalsIgnoreCase("S")) {
						map.put("Budget", removeDoubleQuotes(line[6]));
						long rangeVal = Long.parseLong(line[6]);
						map.put("Range", checkSaleRange(rangeVal));
					} else if (removeDoubleQuotes(line[4]).equalsIgnoreCase("R")) {
						map.put("Budget", removeDoubleQuotes(line[6]));
						long rangeVal = Long.parseLong(line[6]);
						map.put("Range", checkRentRange(rangeVal));
					}
				}
				map.put("Count", removeDoubleQuotes(line[7]));
			}
			map.put("Date", date);
			word.set(getRandomIdValue());
			context.write(word, new BSONWritable(map));
		} catch (Exception ex) {
			System.out.println(ex);
		}
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

	private String checkSaleRange(long val) {
		String range = null;
		if (val > 1000000 && val < 2000000) {
			range = "Rs 10-20";
		} else if (val >= 2000000 && val < 4000000) {
			range = "Rs 20-40";
		} else if (val >= 4000000 && val < 6000000) {
			range = "Rs 40-60";
		} else if (val >= 6000000 && val < 10000000) {
			range = "Rs 60-100";
		} else if (val >= 10000000) {
			range = "> Rs 100";
		}
		return range;
	}

	private String checkRentRange(long val) {
		String range = null;
		if (val < 10000) {
			range = "Rs 5-10";
		} else if (val >= 10000 && val < 15000) {
			range = "Rs 10-15";
		} else if (val >= 15000 && val < 20000) {
			range = "Rs 15-20";
		} else if (val >= 20000 && val < 25000) {
			range = "Rs 20-25";
		} else if (val >= 25000 && val < 30000) {
			range = "Rs 25-30";
		} else if (val >= 30000 && val < 35000) {
			range = "Rs 30-35";
		} else if (val >= 35000 && val < 40000) {
			range = "Rs 35-40";
		} else if (val >= 40000 && val < 45000) {
			range = "Rs 40-45";
		} else if (val >= 45000 && val < 50000) {
			range = "Rs 45-50";
		} else if (val >= 50000) {
			range = "> Rs 50";
		}
		return range;
	}
	
	private String removeDoubleQuotes(String inStr){
		String formattedStr = "";
		if(inStr != null && !inStr.isEmpty()){
			if(inStr.contains("\"")){
				formattedStr = inStr.substring(1, inStr.length()-1);
			} else {
				formattedStr = inStr;
			}
		}
		return formattedStr;
	}
	
	public void setup(Context context) throws IOException, InterruptedException {
	    Configuration conf = context.getConfiguration();
	    FileSystem fs = FileSystem.getLocal(conf);
	    Path[] dataFile = DistributedCache.getLocalCacheFiles(conf);
	    // [0] index for rfNumCodes file.
	    // [1] index for Locality monthly Report file.
	    
	    Tplrt tplrt = null;
	    for(int i=0;i<dataFile.length;i++){
	    	if(dataFile[i].toUri().getPath().contains("AllMasterData")){
	    		//Add to TPCND list
	    		addDataToList(fs,dataFile[i],"AllMasterData");
	    	}
	    	if(dataFile[i].toUri().getPath().contains("LocalityMasterData")){
	    		//Add to TPLMT list
	    		addDataToList(fs,dataFile[i],"LocalityMasterData");
	    	}
		    
		    //System.out.println("###tplrtList.size() "+tplrtList.size());
	    }
	}
	
	public void addDataToList(FileSystem fs, Path dataFile, String type){
		String line = null;
		BufferedReader cacheReader = null;
		Tpcnd tpcnd = null;
		Tplmt tplmt = null;
		cacheReader = new BufferedReader(new InputStreamReader(fs.open(dataFile)));
	    while((line = cacheReader.readLine()) != null){
			String[] params = line.split(",");
			if(type.equalsIgnoreCase("AllMasterData")){
				tpcnd = new Tpcnd();
				/*lmtrfnum,lmtcndzip,lmtcndstate,lmtcndcountry,lmtcndcity,lmtname,lmtdisc,deleted,createdby,exfield1,exfield2,
				lmtisactive,longitude,latitude,lmtnearbyrfnum,lmtalias1,lmtalias2,lmtpdfavail,lmtlmtrfnum,lmtrestrict,lmtpopular,
				lmtdispname,lmtadvice,lmtiswap*/
				
				
				/*cndrfnum,cndcode,cnddesc,cndcndrfnum,cndfieldtype,cndgroup,cndseqnum,createdby,deleted,exfield1,exfield2,isactive*/
				for(int j=0;j<params.length;j++){
					if(j == 0 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCndrfnum(Long.valueOf(params[j]));
					}
					if(j==1 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCndcode(params[j]);
					}
					if(j==2 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCnddesc(params[j]);
					}
					if(j==3 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCndcndrfnum(Long.valueOf(params[j]));
					}
					if(j==4 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCndfieldtype(params[j]);
					}
					if(j==5 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCndgroup(params[j]);
					}
					if(j==6 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCndseqnum(Double.valueOf(params[j]));
					}
					if(j==7 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCreatedby(Long.valueOf(params[j]));
					}
					if(j==8 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setDeleted(params[j]);
					}
					if(j==9 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setExfield1(Long.valueOf(params[j]));
					}
					if(j==10 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setExfield2(params[j]);
					}
					if(j==11 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setIsactive(params[j]);
					}
				}
				tpcndList.add(tpcnd);
			}
			if(type.equalsIgnoreCase("LocalityMasterData")){
				tplmt = new Tplmt();
				/*lmtrfnum,lmtcndzip,lmtcndstate,lmtcndcountry,lmtcndcity,lmtname,lmtdisc,deleted,createdby,exfield1,exfield2,
				lmtisactive,longitude,latitude,lmtnearbyrfnum,lmtalias1,lmtalias2,lmtpdfavail,lmtlmtrfnum,lmtrestrict,lmtpopular,
				lmtdispname,lmtadvice,lmtiswap*/
				
				
				/*cndrfnum,cndcode,cnddesc,cndcndrfnum,cndfieldtype,cndgroup,cndseqnum,createdby,deleted,exfield1,exfield2,isactive*/
				for(int j=0;j<params.length;j++){
					if(j == 0 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tplmt.setLmtrfnum(Long.valueOf(params[j]));
					}
					if(j==1 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tplmt.setLmtcndzip(Long.parseLong(params[j]));
					}
					if(j==2 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tplmt.setLmtcndstate(Long.parseLong(params[j]));
					}
					if(j==3 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tplmt.setLmtcndcountry(Long.valueOf(params[j]));
					}
					if(j==4 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tplmt.setLmtcndcity(Long.valueOf(params[j]));
					}
					if(j==5 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tplmt.setLmtname(params[j]);
					}
					if(j==6 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCndseqnum(Double.valueOf(params[j]));
					}
					if(j==7 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setCreatedby(Long.valueOf(params[j]));
					}
					if(j==8 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setDeleted(params[j]);
					}
					if(j==9 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setExfield1(Long.valueOf(params[j]));
					}
					if(j==10 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setExfield2(params[j]);
					}
					if(j==11 && params[j] != null && !params[j].equalsIgnoreCase("null")){
						tpcnd.setIsactive(params[j]);
					}
				}
				tpcndList.add(tpcnd);
			}
			
	    }
		    				if((tplrt.getLrtminprice() == null || tplrt.getLrtminprice()==0) || (tplrt.getLrtmaxprice() == null || tplrt.getLrtmaxprice()==0)){
		    					//System.out.println("NO Capital Value Data ");
		    					continue;
		    				}
	    				tplrtList.add(tplrt);
	    			}
	    			}
	    		}
	    	else if(isHeader){
	    		isHeader = false;
	    	}
	    }
	}
}
