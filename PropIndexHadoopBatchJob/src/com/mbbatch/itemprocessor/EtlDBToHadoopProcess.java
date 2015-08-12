package com.mbbatch.itemprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import com.mbbatch.dao.PropIndexDaoHibernate;
import com.mbbatch.domain.Tplrt;
import com.mbbatch.domain.Tppmt;
import com.mbbatch.domain.Tpsrec;

public class EtlDBToHadoopProcess implements ItemProcessor <String, String> {
	
	private static Properties _pro = new Properties();
	private static final String _locality = "LOCALITY";
	private static final String _rfNumCodes = "RFNumCodes";
	private static final String _localityListing = "LocalityListing";
	private static final String _localitySearches = "LocalitySearches";
	private static final Logger _logger = Logger.getLogger(EtlDBToHadoopProcess.class);
	private static String[] _monthArray;
	private static String[] _yearArray;
	static {
		try {
			_pro.load(new FileInputStream("src/propIndex.properties"));
			_monthArray = _pro.getProperty("month").split(",");
			_yearArray = _pro.getProperty("year").split(",");
		} catch (IOException e) {
			System.out.println("Property  file ====== " + e);
		}
	}
	private PropIndexDaoHibernate propIndexDaoHibernate;
	
	public PropIndexDaoHibernate getPropIndexDaoHibernate() {
		return propIndexDaoHibernate;
	}

	public void setPropIndexDaoHibernate(PropIndexDaoHibernate propIndexDaoHibernate) {
		this.propIndexDaoHibernate = propIndexDaoHibernate;
	}

	private void writeFilesToDisk(List list, String type, String header, String fileName, String filePath){
		PrintWriter out = null;
		FileWriter fos = null;
		String dataToBeWritten = null;
		File file = null;
		try {
			file = new File(filePath+fileName);
			if(file.exists() && file.isFile()){
				System.out.println("File "+fileName+" exists at location "+filePath);
				return;
			}
			if(!file.exists()){
				System.out.println("Creating the file directory"+filePath);
				new File(filePath).mkdirs();
				file.createNewFile();
			}
			System.out.println("File Created"+file.getAbsolutePath());
			fos = new FileWriter(filePath+fileName);
			out = new PrintWriter(fos);
			out.write(header+"\n");
			for (int i = 0; i < list.size(); i++) {
				if(type.equalsIgnoreCase(_locality)){
					Tplrt tplrt = (Tplrt)list.get(i);
					dataToBeWritten = tplrt.toString();
				}
				if(type.equalsIgnoreCase(_rfNumCodes)){
					dataToBeWritten = (String)list.get(i);
				}
				if(type.equalsIgnoreCase(_localityListing)){
					Tppmt tppmt = (Tppmt)list.get(i);
					dataToBeWritten = tppmt.toString();
				}
				if(type.equalsIgnoreCase(_localitySearches)){
					Tpsrec tpsrec = (Tpsrec)list.get(i);
					//TPSREC data to be written as semicolon separated txt file becoz property type and bedrooms are itself comma seperated
					dataToBeWritten = tpsrec.toString();
				}
				out.write(dataToBeWritten+"\n");
			}
			out.close();
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
			_logger.debug(ie.getMessage());
		} finally{
			if(out != null){
				out.close();
			}
			if(fos != null){
				try{
					fos.close();
				}catch(IOException ioe){
					_logger.debug(ioe.getMessage());
				}
			}
		}
	}

	private void getLocalityReportMonthlyDataFromDBToDisk(){
		List<Tplrt> tplrtListPerYearPerMonth = null;
		System.out.println("In getLocalityReportMonthlyDataFromDBToDisk method");
		String tplrtHeaderText = "LRTRFNUM,LRTCNDRFNUM,LRTPRICEUNIT,LRTLMTRFNUM,LRTISRENT,LRTMINPRICE,LRTMAXPRICE,LRFAVGPRICE,CREATEDATE,MODIDATE,DELETED,CREATEDBY,LRTOLDMINPRICE,LRTOLDMAXPRICE,LRTOLDAVGPRICE,LRTYEILDPRICE";
		for(String year:_yearArray){
			for(String month:_monthArray){
				tplrtListPerYearPerMonth = propIndexDaoHibernate.generateLocalityReportData(Integer.valueOf(year),Integer.valueOf(month));
				System.out.println("TPLRT list size "+tplrtListPerYearPerMonth.size());
				writeFilesToDisk(tplrtListPerYearPerMonth,_locality,tplrtHeaderText, "LOCALITY_REPORT_" + new DateFormatSymbols().getMonths()[ Integer.valueOf(month)-1] +".CSV","/opt/propIndexData/liveDB/LocalityReport/" + year + "/input/");
			}
		}
	}
	
	private void getLocalityWiseListingDataFromDB(){
		List<Tppmt> tppmtListPerLocality = null;
		System.out.println("In getLocalityWiseListingDataFromDB method");
		String tppmtHeaderText = "PMTRFNUM,PMTUBIRFNUM,PMTCDMRFNUM,PMTLMTRFNUM,PMTCNDPROPTYPE,PMTLISTTYPE,PMTSALEPRICE,PMTRENTPRICE,PMTBRIEFDISC,PMTISLISTED,PMTISACTIVE,PMTMODERSTATUS,PMTSTARTDATE,PMTENDDATE,CREATEDATE,MODIDATE,DELETED,RANKDATE,LOCRANKDATE,PMTCNDLISTTYPE";
		for(String year:_yearArray){
			for(String month:_monthArray){
				tppmtListPerLocality = propIndexDaoHibernate.getLocalityWiseListingData(Integer.valueOf(year),Integer.valueOf(month));
				System.out.println("TPPMT list size "+tppmtListPerLocality.size());
				writeFilesToDisk(tppmtListPerLocality,_localityListing,tppmtHeaderText, "LOCALITY_WISE_LISTING_REPORT_" + new DateFormatSymbols().getMonths()[ Integer.valueOf(month)-1] +".CSV","/opt/propIndexData/liveDB/LocalityWisePropertyListingReport/" + year + "/input/");
			}
		}
	}
	
	private void getSearchesLocalityDataFromDB(){
		List<Tpsrec> tpsrecListPerLocality = null;
		System.out.println("In getSearchesLocalityDataFromDB method");
		String tpsrecHeaderText = "SRRFNUM,SRCATG,SRCNDCITY,SRLOCALITY,SRCNDPROPTYPE,SRCNDBEDRMS,SRCNDPLUNIT,SRCNDLISTINGPOSTED,SRUBIRFNUM,CREATEDATE,SRHITCNT,SRRESULTCOUNT,SRRESULTIDS";
		for(String year:_yearArray){
			for(String month:_monthArray){
				tpsrecListPerLocality = propIndexDaoHibernate.getLocalityWiseSearchesData(Integer.valueOf(year),Integer.valueOf(month));
				System.out.println("TPSREC list size "+tpsrecListPerLocality.size());
				writeFilesToDisk(tpsrecListPerLocality,_localitySearches,tpsrecHeaderText, "LOCALITY_WISE_SEARCHES_REPORT_" + new DateFormatSymbols().getMonths()[ Integer.valueOf(month)-1] +".txt","/opt/propIndexData/liveDB/LocalityWisePropertySearchesReport/" + year + "/input/");
			}
		}
	}
	
	private void getRFNumCodesFromDB(){
		List<String> rfNumCodesList = null;
		String headerText = "Code,Name,Description";
		rfNumCodesList = propIndexDaoHibernate.getRFCodesLookupData();
		writeFilesToDisk(rfNumCodesList, "RFNumCodes", headerText, "RFNumCodesFile.CSV", "/opt/propIndexData/liveDB/RFCodesLookup/");
	}
	
	@SuppressWarnings("unchecked")
	public String process(String str)  {
		System.out.println("In process method");
		try{
		//get RF Num Codes & create Lookup file.
		//getRFNumCodesFromDB();
		
		//get Locality Listing data from TPPMT
		//getLocalityWiseListingDataFromDB();
		
		//Locality Report Data
		//getLocalityReportMonthlyDataFromDBToDisk();
		
		//get Searches data from TPSREC
		getSearchesLocalityDataFromDB();

		//For Exiting Batch Process
		}catch(Exception ex){
			System.out.println(ex);
			ex.printStackTrace();
		}
		exitBatchProcess();
		return null;
	}
		
	private void exitBatchProcess(){
		System.exit(0);
	}
}
