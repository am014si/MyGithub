package com.mb.hadoop.reducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BasicBSONObject;

import com.mb.hadoop.bean.PreferredLocalityReportBean;
import com.mb.hadoop.bean.Tplrt;
import com.mongodb.hadoop.io.BSONWritable;


public class LocalityReportGeneratorReducer extends Reducer<Text,IntWritable,Text,BSONWritable>{
	
	private static List<String> rfNumCodesList = new ArrayList<String>();
	private static List<Tplrt> tplrtList = new ArrayList<Tplrt>();
	private static SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd");
	private Text word = new Text();
	private int year;
	private int month;
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		//Locality:City:ListingType
		String[] line = key.toString().split(":");
		int sum = 0;
		for (IntWritable val : values) {
			sum += val.get();
		}
		Configuration conf = context.getConfiguration();
		year = Integer.parseInt(conf.get("year"));
		month = Integer.parseInt(conf.get("month"));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, (month-1));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.YEAR, year);
        Date date = cal.getTime();
		try{
		  PreferredLocalityReportBean preferredLocalityReportBean = new PreferredLocalityReportBean();
		  if(line[0] != null){
			//Get Locality Details from rfCodesList
				for(String rfStr:rfNumCodesList){
					String[] rfStrArray = rfStr.split(",");
					if(line[0].equalsIgnoreCase(rfStrArray[1])){
						//get locality Code rfStrArray[0]
						preferredLocalityReportBean.setLocalityName(line[0]);
						preferredLocalityReportBean.setCity(line[1]);
						preferredLocalityReportBean.setSaleOrRent(line[2]);
						preferredLocalityReportBean.setModiDate(date);
						preferredLocalityReportBean.setListingCount(sum);
						List<PreferredLocalityReportBean> tempList = new ArrayList<PreferredLocalityReportBean>();
						PreferredLocalityReportBean tempBean = null;
						for(Tplrt tplrtbean : tplrtList){
							if(tplrtbean.getLrtlmtrfnum() == Integer.parseInt(rfStrArray[0]) && line[2] != null && line[2].equalsIgnoreCase(tplrtbean.getLrtisrent())){
								tempBean = new PreferredLocalityReportBean();
								tempBean.setMinCapitalValue(Math.round(tplrtbean.getLrtminprice()));
								tempBean.setMaxCapitalValue(Math.round(tplrtbean.getLrtmaxprice()));
								tempBean.setAvgCapitalValue(Math.round(tplrtbean.getLrfavgprice()));
								tempList.add(tempBean);
							}
						}
						long minCapSum = 0;
						long maxcapSum = 0;
						long avgCapSum = 0;
						for(PreferredLocalityReportBean tBean: tempList){
							minCapSum = minCapSum + Math.round(tBean.getMinCapitalValue());
							maxcapSum = maxcapSum + Math.round(tBean.getMaxCapitalValue());
							avgCapSum = avgCapSum + Math.round(tBean.getAvgCapitalValue());
						}
						preferredLocalityReportBean.setMinCapitalValue(calculateAvg(minCapSum, tempList.size()));
						preferredLocalityReportBean.setMaxCapitalValue(calculateAvg(maxcapSum, tempList.size()));
						preferredLocalityReportBean.setAvgCapitalValue(calculateAvg(avgCapSum, tempList.size()));
						break;
					}
				}
		  }
		  
		  if(!preferredLocalityReportBean.isDataNullOrEmpty()){
				System.out.println("## In write "+preferredLocalityReportBean);
				word.set(getRandomIdValue());
				BasicBSONObject map = new BasicBSONObject();
				map.put("Locality",preferredLocalityReportBean.getLocalityName());
				map.put("City",preferredLocalityReportBean.getCity());
				map.put("SaleOrRent",preferredLocalityReportBean.getSaleOrRent());
				if(preferredLocalityReportBean.getSaleOrRent().equals("R")){
					map.put("MinCapitalValue", Math.round(preferredLocalityReportBean.getMinCapitalValue())*1000);
					map.put("MaxCapitalValue", Math.round(preferredLocalityReportBean.getMaxCapitalValue())*1000);
					map.put("AvgCapitalValue", Math.round(preferredLocalityReportBean.getAvgCapitalValue())*1000);
				} else if(preferredLocalityReportBean.getSaleOrRent().equals("S")){
					map.put("MinCapitalValue", Math.round(preferredLocalityReportBean.getMinCapitalValue()));
					map.put("MaxCapitalValue", Math.round(preferredLocalityReportBean.getMaxCapitalValue()));
					map.put("AvgCapitalValue", Math.round(preferredLocalityReportBean.getAvgCapitalValue()));
				}
				map.put("Date", preferredLocalityReportBean.getModiDate());
				map.put("Count", preferredLocalityReportBean.getListingCount());
				context.write(word, new BSONWritable( map ));
		  }
		  
		}catch(Exception ex){
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
		    // [1] index for Locality monthly Report file.
		    BufferedReader cacheReader = null;
		    Tplrt tplrt = null;
		    for(int i=0;i<dataFile.length;i++){
			    cacheReader = new BufferedReader(new InputStreamReader(fs.open(dataFile[i])));
			    while((line = cacheReader.readLine()) != null){
			    	if(!isHeader){
			    		if(i==0){
			    			rfNumCodesList.add(line);
			    		} else if(i==1){
			    			String[] tplrtParams = line.split(",");
			    			if(tplrtParams[0] != null && !tplrtParams[0].equalsIgnoreCase("LRTRFNUM")){
			    				tplrt =  new Tplrt();
				    				for(int j=0;j<tplrtParams.length;j++){
				    					if(j == 0 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    						tplrt.setLrtrfnum(Long.valueOf(tplrtParams[j]));
				    					}
				    					if(j==1 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    						tplrt.setLrtcndrfnum(Long.valueOf(tplrtParams[j]));
				    					}
				    					if(j==2 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
			    							tplrt.setLrtpriceunit(Long.valueOf(tplrtParams[j]));
				    					}
				    					if(j==3 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    						tplrt.setLrtlmtrfnum(Long.valueOf(tplrtParams[j]));
				    					}
				    					if(j==4 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    						tplrt.setLrtisrent(tplrtParams[j]);
				    					}
				    					if(j==5 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    						tplrt.setLrtminprice(Double.valueOf(tplrtParams[j]));
				    					}
				    					if(j==6 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    						tplrt.setLrtmaxprice(Double.valueOf(tplrtParams[j]));
				    					}
				    					if(j==7 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    						tplrt.setLrfavgprice(Double.valueOf(tplrtParams[j]));
				    					}
				    				try{
				    					if(j==8 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    						tplrt.setCreatedate(formatter.parse(tplrtParams[j]));
				    					}
				    					if(j==9 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    						//System.out.println("@@@ "+tplrtParams[j]+" @@@");
				    						tplrt.setModidate(formatter.parse(tplrtParams[j]));
				    					}
				    				}catch(ParseException pex){
				    					System.out.println(" $%#@ pex "+pex.getMessage());
				    				}
				    				if(j==10 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    					tplrt.setDeleted(tplrtParams[j]);
				    				}
				    				if(j==11 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    					tplrt.setCreatedby(Long.valueOf(tplrtParams[j]));
				    				}
				    				if(j==12 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    					tplrt.setLrtminpriceOld(Double.valueOf(tplrtParams[j]));
				    				}
				    				if(j==13 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    					tplrt.setLrtmaxpriceOld(Double.valueOf(tplrtParams[j]));
				    				}
				    				if(j==14 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    					tplrt.setLrfavgpriceOld(Double.valueOf(tplrtParams[j]));
				    				}
				    				if(j==15 && tplrtParams[j] != null && !tplrtParams[j].equalsIgnoreCase("null")){
				    					tplrt.setLrtYieldPrice(Double.valueOf(tplrtParams[j]));
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
			    //System.out.println("###tplrtList.size() "+tplrtList.size());
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
	
		private long calculateAvg(long sum, int size){
			return (sum / size);
		}

}
