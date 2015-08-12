import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.sendgrid.SendGrid;


public class GOHFReportGenerator {
	private Connection conn;
	private static Calendar cal = Calendar.getInstance();
	private static int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
	private static String dateField = "2015-07-"+dayOfMonth+"_"+cal.getTime().getHours()+"_"+cal.getTime().getMinutes();
	public static final SendGrid sendgrid = new SendGrid("gohf", "Times@123");
	public static final String bookingFileName = "GOHF_Booking_Report_"+dateField+".csv";
	public static final String couponfileName = "GOHF_Coupon_Report_"+dateField+".csv";
	public static final String contactfileName = "GOHF_Contact_Report_"+dateField+".csv";
	public static final String totalCountsFileName = "Total_Counts "+dateField+".csv";
	public static void main(String[] args) throws Exception {
		
		//System.out.println(dateField);
		
		GOHFReportGenerator gohfReportGenerator = new GOHFReportGenerator();
		gohfReportGenerator.createDBConnection();
		gohfReportGenerator.createBookingReport();
		gohfReportGenerator.createCouponReport();
		gohfReportGenerator.createContactReport();
		gohfReportGenerator.createTotalReport();
		
		String[] bccArray = new String[] {"mbgohf2015@gmail.com"};
		String[] ccArray = new String[] {"Roushan.Singh@timesgroup.com","Rahul.Goyal@timesgroup.com","Puneet.Kukreja@timesgroup.com","Dipti.Tandon@timesgroup.com","Subodh.Kumar2@timesgroup.com","singh.amit@timesgroup.com"};
		//String[] ccArray = new String[] {};
		SendGrid.Email email = new SendGrid.Email();
	    email.addTo("Amit.Khanna1@timesgroup.com");
		//email.addTo("singh.amit@timesgroup.com");
	    email.setFromName("MagicBricks GOHF Reports");
	    email.setFrom("projectalerts@magicbricks.com");
	    email.setSubject("MagicBricks GOHF Reports");
	    email.setText("PFA reports");
	    email.setCc(ccArray);
	    email.setBcc(bccArray);
	    email.addAttachment(bookingFileName, new File("/home/batch/temp/"+bookingFileName));
	    email.addAttachment(couponfileName, new File("/home/batch/temp/"+couponfileName));
	    email.addAttachment(contactfileName, new File("/home/batch/temp/"+contactfileName));
	    email.addAttachment(totalCountsFileName, new File("/home/batch/temp/"+totalCountsFileName));
	    try{
	    
	    SendGrid.Response sgResponse = sendgrid.send(email);
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
	    }
		//gohfReportGenerator.createReport1();
		//gohfReportGenerator.createReport2();
		//Calendar cal = Calendar.getInstance();
		//int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		//System.out.println(dateField);
	}
	
	public void createDBConnection(){
		try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	      String url = "jdbc:mysql://10.240.78.224:3306/gohfstagingdb";
	      conn = DriverManager.getConnection(url, "gohfproduser", "Rt2g39assWd");
		
			//String url = "jdbc:mysql://dbserver.cs7hgtnps9sx.us-east-1.rds.amazonaws.com:3306/db_gohf";
	      //conn = DriverManager.getConnection(url, "gohf_mb", "8MmcDwPMJX3u5RTR");
		} catch (ClassNotFoundException ex) {
			System.err.println(ex.getMessage());
			//customLogFactory.error("Exception in creating connection with DB server ",ex);
		}
	    catch (IllegalAccessException ex) {
	    	System.err.println(ex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    }
	    catch (InstantiationException ex) {
	    	System.err.println(ex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    }
	    catch (SQLException ex) {
	    	System.err.println(ex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    }
	}
	
	public List<String> getReport2Data(Connection conn){
		PreparedStatement stmt = null;
		PreparedStatement bookingStmt = null;
		
		StringBuffer contactStr = new StringBuffer("Date,Developer Name,Project Name,Project Price Range,City,Locality,Total Contacts,Total Booking,Total Coupons,Total Failed Booking,Unique Contacts,Unique Coupons\n");
		//StringBuffer contactStr = new StringBuffer();
		List<String> dailyContactDataList = new ArrayList<String>();
		dailyContactDataList.add(contactStr.toString());
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    long cnt = 9999;
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    String devName = null;
	    String prjName = null;
	    String prjPricerange = null;
	    String contactsTotal = null;
	    String bookingTotal = null;
	    String couponsTotal = null;
	    String failedBookingTotal = null;
	    String uniqueContacts = null;
	    String uniqueCoupons = null;
	    String city = null;
	    String usrName = null;
	    String usrEmail = null; String usrMobile = null; String usrISD = null; String projectName = null; String developerName = null; String locality = null;
	    String created = null; String type = null; String bookingAmount = null; String unit = null; String offer = null;
	    ResultSet rs = null;
	    ResultSet bookingRS = null;
	    queryBuffer.append("select prj.title as Project_Name, dev.title as Developer_Name, prj.project_address, prj.display_price_range, ");
		queryBuffer.append("(select count(DISTINCT email) from gohf_contact con where con.project_id = prj.id and con.created like ?) as Unique_Contacts, ");
		queryBuffer.append("(select count(1) from gohf_contact con where con.project_id = prj.id and con.created like ?) as Total_Contacts , ");
		queryBuffer.append("(select count(1) from gohf_coupon cp, gohf_projects_units prju where cp.project_unit_id = prju.id and prju.project_id = prj.id ");
		queryBuffer.append("and cp.created like ?) as Total_Coupons, (select count(DISTINCT usr.email) from gohf_coupon cp, gohf_projects_units prju, ");
		queryBuffer.append("gohf_user usr where cp.project_unit_id = prju.id and cp.uid = usr.id and prju.project_id = prj.id and cp.created like ?) ");
		queryBuffer.append("as Unique_Coupons, (select count(1) from gohf_booking bk, gohf_booking_transection bktr, gohf_projects_units prju ");
		queryBuffer.append("where bk.project_unit_id = prju.id and prju.project_id = prj.id and bk.id = bktr.booking_ref_id and bktr.QSIResponseCode = 0 ");
		queryBuffer.append("and bk.created like ?) as Total_Successful_Booking, (select count(1) from gohf_booking bk, gohf_booking_transection bktr, ");
		queryBuffer.append("gohf_projects_units prju, gohf_user usr where bk.project_unit_id = prju.id and bk.uid = usr.id and prju.project_id = prj.id ");
		queryBuffer.append("and bktr.QSIResponseCode != 0 and bk.created like ?) as Total_Failed_Booking, ci.name from gohf_projects prj, gohf_city ci, gohf_developers dev ");
		queryBuffer.append("where prj.developer_id = dev.id and prj.city_id = ci.id;");
	    
	    /*StringBuffer bookingQueryBuffer = new StringBuffer();
	    bookingQueryBuffer.append("SELECT usr.email as User_Email, usr.name as User_Name, usr.mobile as User_Mobile, usr.isd as User_Mobile_ISD, ");
	    bookingQueryBuffer.append("prj.title as Project_Title , dev.title as Developer_Title, prj.project_address Project_Address, prjunit.booking_amount, ");
	    bookingQueryBuffer.append(" prjunit.title as Unit, prjunit.offer FROM gohfstagingdb.gohf_user usr, ");
	    bookingQueryBuffer.append("gohfstagingdb.gohf_projects prj, gohfstagingdb.gohf_developers dev, gohfstagingdb.gohf_projects_units prjunit ");
	    bookingQueryBuffer.append(" where usr.id = ? and prjunit.id = ? and prjunit.project_id = prj.id and prj.developer_id = dev.id;");*/
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	stmt.setString(1, dateField);
	    	stmt.setString(2, dateField);
	    	stmt.setString(3, dateField);
	    	stmt.setString(4, dateField);
	    	stmt.setString(5, dateField);
	    	stmt.setString(6, dateField);
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  created = dateField;
		    	  developerName = rs.getString("Developer_Name");
		    	  projectName = rs.getString("Project_Name");
		    	  prjPricerange = rs.getString("display_price_range");
		    	  uniqueContacts = rs.getString("Unique_Contacts");
		    	  contactsTotal = rs.getString("Total_Contacts");
		    	  couponsTotal = rs.getString("Total_Coupons");
		    	  uniqueCoupons = rs.getString("Unique_Coupons");
		    	  bookingTotal = rs.getString("Total_Successful_Booking");
		    	  failedBookingTotal = rs.getString("Total_Failed_Booking");
		    	  locality = rs.getString("project_address");
		    	  if(locality != null && !locality.isEmpty() && locality.contains(",")){
		    		  locality = locality.split(",")[0];
		    	  }
		    	  city = rs.getString("name");
		    	  //contactStr.append(created+","+developerName+","+projectName+","+prjPricerange+","+city+","+locality+","+contactsTotal+","+bookingTotal+","+couponsTotal+","+failedBookingTotal+","+uniqueContacts+","+uniqueCoupons+"\n");
		    	  //System.out.println("Report2 String "+contactStr);
		    	  if(developerName != null && !developerName.isEmpty()){
		    		  dailyContactDataList.add(created+","+developerName+","+projectName+","+prjPricerange+","+city+","+locality+","+contactsTotal+","+bookingTotal+","+couponsTotal+","+failedBookingTotal+","+uniqueContacts+","+uniqueCoupons+"\n");
		    	  }
	    	  }
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	    return dailyContactDataList;
	}
	
	public List<String> getTotalReportData(Connection conn){
		PreparedStatement stmt = null;
		PreparedStatement bookingStmt = null;
		
		StringBuffer contactStr = new StringBuffer("Project_ID,Developer_Name,Project_Name,Price_Range,City,Locality,Total_Contact,Total_Booking,Total_Coupon,Success_Booking,Failed_Booking,Unique_Contact\n");
		//StringBuffer contactStr = new StringBuffer();
		//
		List<String> dailyContactDataList = new ArrayList<String>();
		dailyContactDataList.add(contactStr.toString());
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    long cnt = 9999;
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    String devName = null;
	    String prjName = null;
	    String prjPricerange = null;
	    String contactsTotal = null;
	    String bookingTotal = null;
	    String couponsTotal = null;
	    String failedBookingTotal = null;
	    String uniqueContacts = null;
	    String uniqueCoupons = null;
	    String city = null;
	    String usrName = null;
	    String usrEmail = null; String usrMobile = null; String usrISD = null; String projectName = null; String developerName = null; String locality = null;
	    String created = null; String type = null; String bookingAmount = null; String unit = null; String offer = null;
	    ResultSet rs = null;
	    ResultSet bookingRS = null;
	    /*queryBuffer.append("select prj.title as Project_Name, dev.title as Developer_Name, prj.project_address, prj.display_price_range, ");
		queryBuffer.append("(select count(DISTINCT email) from gohf_contact con where con.project_id = prj.id and con.created like ?) as Unique_Contacts, ");
		queryBuffer.append("(select count(1) from gohf_contact con where con.project_id = prj.id and con.created like ?) as Total_Contacts , ");
		queryBuffer.append("(select count(1) from gohf_coupon cp, gohf_projects_units prju where cp.project_unit_id = prju.id and prju.project_id = prj.id ");
		queryBuffer.append("and cp.created like ?) as Total_Coupons, (select count(DISTINCT usr.email) from gohf_coupon cp, gohf_projects_units prju, ");
		queryBuffer.append("gohf_user usr where cp.project_unit_id = prju.id and cp.uid = usr.id and prju.project_id = prj.id and cp.created like ?) ");
		queryBuffer.append("as Unique_Coupons, (select count(1) from gohf_booking bk, gohf_booking_transection bktr, gohf_projects_units prju ");
		queryBuffer.append("where bk.project_unit_id = prju.id and prju.project_id = prj.id and bk.id = bktr.booking_ref_id and bktr.QSIResponseCode = 0 ");
		queryBuffer.append("and bk.created like ?) as Total_Successful_Booking, (select count(1) from gohf_booking bk, gohf_booking_transection bktr, ");
		queryBuffer.append("gohf_projects_units prju, gohf_user usr where bk.project_unit_id = prju.id and bk.uid = usr.id and prju.project_id = prj.id ");
		queryBuffer.append("and bktr.QSIResponseCode != 0 and bk.created like ?) as Total_Failed_Booking, ci.name from gohf_projects prj, gohf_city ci, gohf_developers dev ");
		queryBuffer.append("where prj.developer_id = dev.id and prj.city_id = ci.id;");*/
	    queryBuffer.append("SELECT prj.id as Project_ID, dev.title as Developer_Name, prj.title as Project_Name, prj.display_price_range as Price_Range, ");
		queryBuffer.append("(select name from gohfstagingdb.gohf_city where id = prj.city_id) as City, prj.location as Locality,");
		queryBuffer.append("(select count(1) from gohfstagingdb.gohf_contact where project_id = prj.id) as Total_Contact, ");
		queryBuffer.append("(select count(1) from gohfstagingdb.gohf_projects_units prju, gohfstagingdb.gohf_booking gb where gb.project_unit_id = prju.id and prju.project_id = prj.id) as Total_Booking,"); 
		queryBuffer.append("(select count(1) from gohfstagingdb.gohf_projects_units prju, gohfstagingdb.gohf_coupon gc where gc.project_unit_id = prju.id and prju.project_id = prj.id) as Total_Coupon, ");
		queryBuffer.append("(select count(1) from gohfstagingdb.gohf_projects_units prju, gohfstagingdb.gohf_booking gb, gohfstagingdb.gohf_booking_transection gbt where gbt.booking_ref_id = gb.id and gb.project_unit_id = prju.id and gbt.qsiresponsecode = 0 and prju.project_id = prj.id) as Success_Booking,"); 
		queryBuffer.append("(select count(1) from gohfstagingdb.gohf_projects_units prju, gohfstagingdb.gohf_booking gb, gohfstagingdb.gohf_booking_transection gbt where gbt.booking_ref_id = gb.id and gb.project_unit_id = prju.id and gbt.qsiresponsecode != 0 and prju.project_id = prj.id) as Failed_Booking, ");
		queryBuffer.append("(select count(distinct email+mobile) from gohfstagingdb.gohf_contact where project_id = prj.id) as Unique_Contact ");
		queryBuffer.append(" FROM gohfstagingdb.gohf_projects prj, gohfstagingdb.gohf_developers dev where dev.id = prj.developer_id ");
	    /*StringBuffer bookingQueryBuffer = new StringBuffer();
	    bookingQueryBuffer.append("SELECT usr.email as User_Email, usr.name as User_Name, usr.mobile as User_Mobile, usr.isd as User_Mobile_ISD, ");
	    bookingQueryBuffer.append("prj.title as Project_Title , dev.title as Developer_Title, prj.project_address Project_Address, prjunit.booking_amount, ");
	    bookingQueryBuffer.append(" prjunit.title as Unit, prjunit.offer FROM gohfstagingdb.gohf_user usr, ");
	    bookingQueryBuffer.append("gohfstagingdb.gohf_projects prj, gohfstagingdb.gohf_developers dev, gohfstagingdb.gohf_projects_units prjunit ");
	    bookingQueryBuffer.append(" where usr.id = ? and prjunit.id = ? and prjunit.project_id = prj.id and prj.developer_id = dev.id;");*/
	    String projectId = null; String couponTotal = null; String successBookingTotal = null; String uniqueContact = null;
		try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	/*stmt.setString(1, dateField);
	    	stmt.setString(2, dateField);
	    	stmt.setString(3, dateField);
	    	stmt.setString(4, dateField);
	    	stmt.setString(5, dateField);
	    	stmt.setString(6, dateField);*/
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  created = dateField;
		    	  projectId = rs.getString("Project_ID");
		    	  developerName = rs.getString("Developer_Name");
		    	  projectName = rs.getString("Project_Name");
		    	  prjPricerange = rs.getString("Price_Range");
		    	  city = rs.getString("City");
		    	  locality = rs.getString("Locality");
		    	  contactsTotal = rs.getString("Total_Contact");
		    	  bookingTotal = rs.getString("Total_Booking");
		    	  couponTotal = rs.getString("Total_Coupon");
		    	  successBookingTotal = rs.getString("Success_Booking");
		    	  failedBookingTotal = rs.getString("Failed_Booking");
		    	  uniqueContact = rs.getString("Unique_Contact");
		    	  if(locality == null || locality.isEmpty()){
		    		  locality = " ";
		    	  }
		    	  locality = locality.replaceAll(",", "-");
		    	  //city = rs.getString("name");
		    	  //contactStr.append(created+","+developerName+","+projectName+","+prjPricerange+","+city+","+locality+","+contactsTotal+","+bookingTotal+","+couponsTotal+","+failedBookingTotal+","+uniqueContacts+","+uniqueCoupons+"\n");
		    	  //System.out.println("Report2 String "+contactStr);
		    	  //if(developerName != null && !developerName.isEmpty()){
		    		  dailyContactDataList.add(projectId+","+developerName+","+projectName+","+prjPricerange+","+city+","+locality+","+contactsTotal+","+bookingTotal+","+couponTotal+","+successBookingTotal+","+failedBookingTotal+","+uniqueContact+"\n");
		    	 // }
	    	  }
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	    return dailyContactDataList;
	}
	
	public void createReport2(){
		FileWriter fileWriter = null;
		try{
	      List<String> report2List = getReport2Data(conn);
	      
	      System.out.println("##### report2 List Size "+report2List.size());
	      fileWriter = new FileWriter("GOHF_Report2_"+dateField+".csv");
			for(String str: report2List){
				fileWriter.write(str);
			}
	      //InsertBookingRecords(conn);
	      //InsertCouponRecords(conn);
	      //InsertContactRecords(conn);
		} catch(IOException ioex) {
	    	System.err.println(ioex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    } 
	    finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
	
	public void createTotalReport(){
		FileWriter fileWriter = null;
		try{
	      List<String> report2List = getTotalReportData(conn);
	      
	      System.out.println("##### report2 List Size "+report2List.size());
	      fileWriter = new FileWriter(totalCountsFileName);
			for(String str: report2List){
				fileWriter.write(str);
			}
	      //InsertBookingRecords(conn);
	      //InsertCouponRecords(conn);
	      //InsertContactRecords(conn);
		} catch(IOException ioex) {
	    	System.err.println(ioex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    } 
	    finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
	
	/*public void createReport1(){
		FileWriter fileWriter = null;
		try{
	      List<String> bookingList = getBookingReportingDataForGOHF(conn);
	      List<String> couponList = getCouponReportingDataForGOHF(conn);
	      List<String> contactList = getContactReportingDataForGOHF(conn);
	      
	      System.out.println("##### report1 BookingList Size "+bookingList.size());
	      System.out.println("##### report1 couponList Size "+couponList.size());
	      System.out.println("##### report1 contactList Size "+contactList.size());
	      
	      fileWriter = new FileWriter("GOHF_Report1_"+dateField+".csv");
			for(String str: bookingList){
				fileWriter.write(str);
			}
			for(String str: couponList){
				fileWriter.write(str);
			}
			for(String str: contactList){
				fileWriter.write(str);
			}
	      //InsertBookingRecords(conn);
	      //InsertCouponRecords(conn);
	      //InsertContactRecords(conn);
		} catch(IOException ioex) {
	    	System.err.println(ioex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    } 
	    finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}*/
	
	
	public void createBookingReport(){
		FileWriter fileWriter = null;
		try{
	      List<String> report2List = getBookingReportData(conn);
	      
	      System.out.println("##### report2 List Size "+report2List.size());
	      //bookingFileName = "GOHF_Booking_Report_"+dateField+".csv";
	      fileWriter = new FileWriter(bookingFileName);
			for(String str: report2List){
				fileWriter.write(str);
			}
	      //InsertBookingRecords(conn);
	      //InsertCouponRecords(conn);
	      //InsertContactRecords(conn);
		} catch(IOException ioex) {
	    	System.err.println(ioex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    } 
	    finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
	
	public void createCouponReport(){
		FileWriter fileWriter = null;
		try{
	      //List<String> bookingList = getBookingReportingDataForGOHF(conn);
	      List<String> couponList = getCouponReportData(conn);
	      //List<String> contactList = getContactReportingDataForGOHF(conn);
	      
	      //System.out.println("##### report1 BookingList Size "+bookingList.size());
	      System.out.println("##### report1 couponList Size "+couponList.size());
	     // System.out.println("##### report1 contactList Size "+contactList.size());
	      
	      fileWriter = new FileWriter(couponfileName);
			/*for(String str: bookingList){
				fileWriter.write(str);
			}*/
			for(String str: couponList){
				fileWriter.write(str);
			}
			/*for(String str: contactList){
				fileWriter.write(str);
			}*/
	      //InsertBookingRecords(conn);
	      //InsertCouponRecords(conn);
	      //InsertContactRecords(conn);
		} catch(IOException ioex) {
	    	System.err.println(ioex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    } 
	    finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
	
	public void createContactReport(){
		FileWriter fileWriter = null;
		try{
	      //List<String> bookingList = getBookingReportingDataForGOHF(conn);
	      //List<String> couponList = getCouponReportingDataForGOHF(conn);
	      List<String> contactList = getContactReportData(conn);
	      
	      //System.out.println("##### report1 BookingList Size "+bookingList.size());
	      //System.out.println("##### report1 couponList Size "+couponList.size());
	      System.out.println("##### report1 contactList Size "+contactList.size());
	      
	      fileWriter = new FileWriter(contactfileName);
			/*for(String str: bookingList){
				fileWriter.write(str);
			}
			for(String str: couponList){
				fileWriter.write(str);
			}*/
			for(String str: contactList){
				fileWriter.write(str);
			}
	      //InsertBookingRecords(conn);
	      //InsertCouponRecords(conn);
	      //InsertContactRecords(conn);
		} catch(IOException ioex) {
	    	System.err.println(ioex.getMessage());
	    	//customLogFactory.error("Exception in creating connection with DB server ",ex);
	    } 
	    finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
	
	public List<String> getBookingReportData(Connection conn){
		PreparedStatement stmt = null;
		PreparedStatement bookingStmt = null;
		
		StringBuffer bookingStr = new StringBuffer("id,uid,booking_id,project_unit_id,date(gb.created),Unit_Title,Project_Title,booking_amount,city_name,Booking_resp_code,PGI_transaction_id,name,email,isd,mobile,Developer_Name\n");
		List<String> dailyBookingDataList = new ArrayList<String>();
		dailyBookingDataList.add(bookingStr.toString());
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    long cnt = 9999;
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    String userId = null;
	    String bookingId = null;
	    String city = null;
	    String prjUnitId = null;
	    String qsiRespCode = null;
	    String txnDesc = null;
	    String usrName = null;
	    String usrEmail = null; String usrMobile = null; String usrISD = null; String projectName = null; String developerName = null; String locality = null;
	    Timestamp created = null; String type = null; String bookingAmount = null; String unit = null; String offer = null;
	    ResultSet rs = null;
	    ResultSet bookingRS = null;
	    //queryBuffer.append("select bk.uid, bk.booking_id, bk.project_unit_id, bk.created FROM gohf_booking bk where bk.created like (?)");
	    //System.out.println("Inside Booking datefield "+dateField);
	    StringBuffer bookingQueryBuffer = new StringBuffer();
	    /*bookingQueryBuffer.append("SELECT usr.email as User_Email, usr.name as User_Name, usr.mobile as User_Mobile, usr.isd as User_Mobile_ISD, ");
	    bookingQueryBuffer.append("prj.title as Project_Title , dev.title as Developer_Title, prj.project_address Project_Address, prjunit.booking_amount, ");
	    bookingQueryBuffer.append(" prjunit.title as Unit, ci.name as city_name, prjunit.offer FROM gohf_user usr, ");
	    bookingQueryBuffer.append("gohf_projects prj, gohf_developers dev, gohf_projects_units prjunit, gohf_city ci ");
	    bookingQueryBuffer.append(" where usr.id = ? and prjunit.id = ? and prjunit.project_id = prj.id and prj.developer_id = dev.id and prj.city_id = ci.id;");
	    */
	    bookingQueryBuffer.append("SELECT gb.id, gb.uid, gb.booking_id, gb.project_unit_id, date(gb.created), prju.title as Unit_Title, prj.title as Project_Title, prju.booking_amount, "); 
		bookingQueryBuffer.append("(select name from gohfstagingdb.gohf_city where id = prj.city_id) as city_name,  ");
		bookingQueryBuffer.append("(select QSIResponseCode from gohfstagingdb.gohf_booking_transection where booking_ref_id = gb.id) as Booking_resp_code,  ");
		bookingQueryBuffer.append("(select merorder from gohfstagingdb.gohf_booking_transection where booking_ref_id = gb.id) as PGI_transaction_id,  ");
		bookingQueryBuffer.append("gu.name, gu.email, gu.isd, gu.mobile, dev.title as Developer_Name FROM gohfstagingdb.gohf_projects prj, gohfstagingdb.gohf_developers dev,  ");
		bookingQueryBuffer.append("gohfstagingdb.gohf_projects_units prju, gohfstagingdb.gohf_booking gb, gohfstagingdb.gohf_user gu where gu.id = gb.uid and dev.id = prj.developer_id  ");
		bookingQueryBuffer.append("and prj.id = prju.project_id and gb.project_unit_id = prju.id ");
		String prjTitle = null; String bkAmt = null;String mobile = null; String devName = null;String cpnCde = null;
	    String name = null;String email = null;String isd = null; String id = null; String uid = null; String createDate = null; String unitTitle = null;
	    String bkRespcode = null; String pgiTxnId = null;
	    try
	    {
	    	stmt = conn.prepareStatement(bookingQueryBuffer.toString());
	    	//stmt.setString(1, dateField);
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  id = rs.getString("id"); uid = rs.getString("uid"); createDate = rs.getString("date(gb.created)"); unitTitle = rs.getString("Unit_Title");
		    	  prjTitle = rs.getString("Project_Title"); bkAmt = rs.getString("booking_amount"); city = rs.getString("city_name");
		    	  bkRespcode = rs.getString("Booking_resp_code"); pgiTxnId = rs.getString("PGI_transaction_id");
		    	  mobile = rs.getString("mobile"); devName = rs.getString("Developer_Name"); 
		    	  name = rs.getString("name");
		    	  email = rs.getString("email");
		    	  isd = rs.getString("isd");
		    	  bookingId = rs.getString("booking_id");
		    	  userId = rs.getString("uid");
		    	  prjUnitId = rs.getString("project_unit_id");
		    	  
			      //bookingStr.append(usrName+","+usrEmail+","+usrISD+","+usrMobile+","+projectName+","+developerName+","+locality+","+city+","+created+","+type+","+bookingAmount+","+unit+","+offer+"\n");
			      //System.out.println("Booking String "+bookingStr);
			      //id,uid,booking_id,project_unit_id,date(gb.created),Unit_Title,Project_Title,booking_amount,city_name,Booking_resp_code,PGI_transaction_id,name,email,isd,mobile,Developer_Name\n
			      //if(developerName != null && !developerName.isEmpty()){
			    	  dailyBookingDataList.add(id+","+userId+","+bookingId+","+prjUnitId+","+createDate+","+unitTitle+","+prjTitle+","+bkAmt+","+city+","+bkRespcode+","+pgiTxnId+","+name+","+email+","+isd+","+mobile+","+devName+"\n");
			     // }
	    	}
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	/*try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}*/
	    }
	    return  dailyBookingDataList;
	}
	
	public List<String> getCouponReportData(Connection conn){
		PreparedStatement stmt = null;
		PreparedStatement bookingStmt = null;
		
		StringBuffer couponStr = new StringBuffer("Coupon_ID,User,Project_Unit_ID,Coupon_Date,Project_ID,Unit_Title,Project_Title,Project_City_ID,Booking_Amount,City,name,email,isd,mobile,Developer_Name,Coupon_Code\n");
		//StringBuffer couponStr = new StringBuffer();
		List<String> dailyCouponDataList = new ArrayList<String>();
		dailyCouponDataList.add(couponStr.toString());
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    long cnt = 9999;
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    String userId = null;
	    String city = null;
	    String couponCode = null;
	    String bookingId = null;
	    String prjUnitId = null;
	    String qsiRespCode = null;
	    String txnDesc = null;
	    String usrName = null;
	    String usrEmail = null; String usrMobile = null; String usrISD = null; String projectName = null; String developerName = null; String locality = null;
	    Timestamp created = null; String type = null; String bookingAmount = null; String unit = null; String offer = null;
	    ResultSet rs = null;
	    ResultSet bookingRS = null;
	    /*queryBuffer.append("select cpn.uid, cpn.booking_id, cpn.project_unit_id, cpn.created, cpn.coupon_code FROM gohf_coupon cpn where cpn.created like (?)");
	    */
	    StringBuffer bookingQueryBuffer = new StringBuffer();
	    /*bookingQueryBuffer.append("SELECT usr.email as User_Email, usr.name as User_Name, usr.mobile as User_Mobile, usr.isd as User_Mobile_ISD, ");
	    bookingQueryBuffer.append("prj.title as Project_Title , dev.title as Developer_Title, prj.project_address Project_Address, prjunit.booking_amount, ");
	    bookingQueryBuffer.append(" prjunit.title as Unit, ci.name as city_name, prjunit.offer FROM gohf_user usr, ");
	    bookingQueryBuffer.append("gohf_projects prj, gohf_developers dev, gohf_projects_units prjunit, gohf_city ci ");
	    bookingQueryBuffer.append(" where usr.id = ? and prjunit.id = ? and prjunit.project_id = prj.id and prj.developer_id = dev.id and prj.city_id = ci.id;");
	    */
	    
	    bookingQueryBuffer.append("SELECT gc.id as Coupon_ID, gc.uid as User, prju.id as Project_Unit_ID, date(gc.created) as Coupon_Date, prj.id as Project_ID,  ");
		bookingQueryBuffer.append("prju.title as Unit_Title, prj.title as Project_Title, prj.city_id as Project_City_ID, prju.booking_amount as Booking_Amount,  ");
		bookingQueryBuffer.append("(select name from gohfstagingdb.gohf_city where id = prj.city_id) as City,  ");
		bookingQueryBuffer.append("gu.name, gu.email, gu.isd, gu.mobile, dev.title as Developer_Name, gc.coupon_code as Coupon_Code  ");
		bookingQueryBuffer.append("FROM gohfstagingdb.gohf_projects prj, gohfstagingdb.gohf_developers dev, gohfstagingdb.gohf_projects_units prju,  ");
		bookingQueryBuffer.append("gohfstagingdb.gohf_coupon gc, gohfstagingdb.gohf_user gu  where gu.id = gc.uid and dev.id = prj.developer_id and  ");
		bookingQueryBuffer.append("prj.id = prju.project_id and gc.project_unit_id = prju.id ");
	    String cpnId = null;String user = null;String cpndate = null; String prjId = null;String unTitle = null;String prjcityId = null;
	    String prjTitle = null; String bkAmt = null;String mobile = null; String devName = null;String cpnCde = null;
	    String name = null;String email = null;String isd = null;
	    try
	    {
	    	stmt = conn.prepareStatement(bookingQueryBuffer.toString());
	    	//stmt.setString(1, dateField);
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  cpnId = rs.getString("Coupon_ID");
		    	  user = rs.getString("User");
		    	  prjUnitId = rs.getString("project_unit_id");
		    	  cpndate = rs.getString("Coupon_Date");
		    	  couponCode = rs.getString("coupon_code");
		    	  prjId = rs.getString("Project_ID");
		    	  unTitle = rs.getString("Unit_Title");
		    	  prjTitle = rs.getString("Project_Title");
		    	  prjcityId = rs.getString("Project_City_ID");
		    	  bkAmt = rs.getString("Booking_Amount");
		    	  city = rs.getString("City"); mobile = rs.getString("mobile"); devName = rs.getString("Developer_Name"); cpnCde = rs.getString("Coupon_Code");
		    	  name = rs.getString("name");
		    	  email = rs.getString("email");
		    	  isd = rs.getString("isd");
		    	  type = "COUPON";
		    	  if(unTitle == null || unTitle.isEmpty()){
		    		  unTitle = " ";
		    	  }
		    	  unTitle = unTitle.replaceAll(",", ".");
			      //couponStr.append(usrName+","+usrEmail+","+usrISD+","+usrMobile+","+projectName+","+developerName+","+locality+","+city+","+created+","+type+","+couponCode+","+unit+","+offer+"\n");
			      //System.out.println("couponStr String "+couponStr);
			      //if(developerName != null && !developerName.isEmpty()){
		    	  //Coupon_ID,User,Project_Unit_ID,Coupon_Date,Project_ID,Unit_Title,Project_Title,Project_City_ID,Booking_Amount,City,name,email,isd,mobile,Developer_Name,Coupon_Code\n
			    	  dailyCouponDataList.add(cpnId+","+user+","+prjUnitId+","+cpndate+","+prjId+","+unTitle+","+prjTitle+","+prjcityId+","+bkAmt+","+city+","+name+","+email+","+isd+","+mobile+","+devName+","+cpnCde+"\n");
			      //}
		      }
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	/*try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}*/
	    }
	    return dailyCouponDataList;
	}
	
	public List<String> getContactReportData(Connection conn){
		PreparedStatement stmt = null;
		PreparedStatement bookingStmt = null;
		
		StringBuffer contactStr = new StringBuffer("Contact_ID,Contact_Date,Project_ID,Project_Title,Project_City_ID,City,Name,Email,ISD,Mobile,Developer_Name\n");
		//StringBuffer contactStr = new StringBuffer();
		List<String> dailyContactDataList = new ArrayList<String>();
		dailyContactDataList.add(contactStr.toString());
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    long cnt = 9999;
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    String userId = null;
	    String couponCode = null;
	    String bookingId = null;
	    String prjUnitId = null;
	    String qsiRespCode = null;
	    String txnDesc = null;
	    String city = null;
	    String usrName = null;
	    String usrEmail = null; String usrMobile = null; String usrISD = null; String projectName = null; String developerName = null; String locality = null;
	    Timestamp created = null; String type = null; String bookingAmount = null; String unit = null; String offer = null;
	    ResultSet rs = null;
	    ResultSet bookingRS = null;
	    /*queryBuffer.append("SELECT co.name as User_Name, co.email as User_Email, co.mobile as User_Mobile, country.phonecode, co.created,");
		queryBuffer.append(" prj.title as Project_Title, prj.project_address, dev.title as Developer_Title, ci.name as city_name ");
		queryBuffer.append(" FROM gohf_contact co, gohf_projects prj, gohf_country country,");
		queryBuffer.append(" gohf_developers dev, gohf_city ci where co.project_id = prj.id and prj.developer_id = dev.id and co.country_id = country.id and prj.city_id = ci.id and co.created like ?");
	    */
		queryBuffer.append("SELECT gc.id as Contact_ID, date(gc.created) as Contact_Date, prj.id as Project_ID, ");
		queryBuffer.append("prj.title as Project_Title, prj.city_id as Project_City_ID,  ");
		queryBuffer.append("(select name from gohfstagingdb.gohf_city where id = prj.city_id) as City,  ");
		queryBuffer.append("gc.name as Name, gc.email as Email, gc.country_id as ISD, gc.mobile as Mobile, dev.title as Developer_Name  ");
		queryBuffer.append("FROM gohfstagingdb.gohf_projects prj, gohfstagingdb.gohf_developers dev, gohfstagingdb.gohf_contact gc  ");
		queryBuffer.append("where dev.id = prj.developer_id and gc.project_id = prj.id ");
		
	    /*StringBuffer bookingQueryBuffer = new StringBuffer();
	    bookingQueryBuffer.append("SELECT usr.email as User_Email, usr.name as User_Name, usr.mobile as User_Mobile, usr.isd as User_Mobile_ISD, ");
	    bookingQueryBuffer.append("prj.title as Project_Title , dev.title as Developer_Title, prj.project_address Project_Address, prjunit.booking_amount, ");
	    bookingQueryBuffer.append(" prjunit.title as Unit, prjunit.offer FROM gohfstagingdb.gohf_user usr, ");
	    bookingQueryBuffer.append("gohfstagingdb.gohf_projects prj, gohfstagingdb.gohf_developers dev, gohfstagingdb.gohf_projects_units prjunit ");
	    bookingQueryBuffer.append(" where usr.id = ? and prjunit.id = ? and prjunit.project_id = prj.id and prj.developer_id = dev.id;");*/
	    String conDate = null; String conId = null; String name = null; String email = null; String isd = null; String prjCityId = null;
	    String prjId = null;
		try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	//stmt.setString(1, dateField);
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  //created = rs.getTimestamp("created");
		    	  type = "CONTACT";
		    	  conDate = rs.getString("Contact_Date");
		    	  conId = rs.getString("Contact_ID");
		    	  //usrEmail = rs.getString("User_Email");
		    	  usrMobile = rs.getString("Mobile");
		    	  prjId = rs.getString("Project_ID");
		    	  projectName = rs.getString("Project_Title");
		    	  city = rs.getString("City");
		    	  name = rs.getString("Name");
		    	  email = rs.getString("Email");
		    	  isd = rs.getString("ISD");
		    	  developerName = rs.getString("Developer_Name");
		    	  prjCityId = rs.getString("Project_City_ID");
		    	  /*if(locality != null && !locality.isEmpty() && locality.contains(",")){
		    		  locality = locality.split(",")[0];
		    	  }*/
		    	  //city = rs.getString("city_name");
		    	  //contactStr.append(usrName+","+usrEmail+","+usrISD+","+usrMobile+","+projectName+","+developerName+","+locality+","+city+","+created+","+type+","+couponCode+","+unit+","+offer+"\n");
		    	  //System.out.println("Contact String "+contactStr);
		    	 // if(developerName != null && !developerName.isEmpty()){
		    		  dailyContactDataList.add(conId+","+conDate+","+prjId+","+projectName+","+prjCityId+","+city+","+name+","+email+","+isd+","+usrMobile+","+developerName+"\n");
		    	 // }
	    	  }
		    
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	/*try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}*/
	    }
	    return dailyContactDataList;
	}
	
	public static void InsertBookingRecords(Connection conn){
		PreparedStatement stmt = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    long cnt = 9999;
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    queryBuffer.append("Insert into gohf_booking (uid, booking_id, project_unit_id, created, updated, status) values (26, ?, 3, ?, ?, 0)");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	for(int i=1;i<=100;i++){
	    		//stmt.setInt(1, Integer.valueOf(bean.getId()));
		    	//stmt.setInt(2, 2);
		    	stmt.setString(1, "MB-BK"+cnt);
		    	//stmt.setString(4, bean.getOrderCommStatusRemarks());
		    	stmt.setTimestamp(2, modiDateTimeStamp);
		    	stmt.setTimestamp(3, modiDateTimeStamp);
		    	stmt.addBatch();
		    	cnt++;
	    	}
	    	int[] rowUpdated = stmt.executeBatch();
	    	for (int i = 0; i < rowUpdated.length; i++) {
	            System.out.println("gohf_comm_notification table row updated " +rowUpdated[i] + " ");
	    	}
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	}
	
	public static void InsertCouponRecords(Connection conn){
		PreparedStatement stmt = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    long cnt = 9999;
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    queryBuffer.append("Insert into gohf_coupon (uid, coupon_code, project_unit_id, created, updated, status) values (26, ?, 1, ?, ?, 0)");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	for(int i=1;i<=100;i++){
	    		//stmt.setInt(1, Integer.valueOf(bean.getId()));
		    	//stmt.setInt(2, 2);
		    	stmt.setString(1, "CP"+cnt);
		    	//stmt.setString(4, bean.getOrderCommStatusRemarks());
		    	stmt.setTimestamp(2, modiDateTimeStamp);
		    	stmt.setTimestamp(3, modiDateTimeStamp);
		    	stmt.addBatch();
		    	cnt++;
	    	}
	    	int[] rowUpdated = stmt.executeBatch();
	    	/*for (int i = 0; i < rowUpdated.length; i++) {
	            System.out.println("gohf_comm_notification table row updated " +rowUpdated[i] + " ");
	    	}*/
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	/*try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}*/
	    }
	}

	public static void InsertContactRecords(Connection conn){
		PreparedStatement stmt = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    long cnt = 9999;
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    queryBuffer.append("Insert into gohf_contact (developer_id, project_id, property_type_id, name, email, " +
	    		"country_id, mobile, action, page_url, created, modified_date, status) " +
	    		"values (1,1,3, 'Amit', 'abc@gmail.com',1,1234,0, 'http://gohf.chutneyweb.com/contac/onelia-nest-2', ?, ?, 0)");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	for(int i=1;i<=100;i++){
	    		//stmt.setInt(1, Integer.valueOf(bean.getId()));
		    	//stmt.setInt(2, 2);
		    	//stmt.setString(1, "CP"+cnt);
		    	//stmt.setString(4, bean.getOrderCommStatusRemarks());
		    	stmt.setTimestamp(1, modiDateTimeStamp);
		    	stmt.setTimestamp(2, modiDateTimeStamp);
		    	stmt.addBatch();
		    	cnt++;
	    	}
	    	int[] rowUpdated = stmt.executeBatch();
	    	/*for (int i = 0; i < rowUpdated.length; i++) {
	            System.out.println("gohf_comm_notification table row updated " +rowUpdated[i] + " ");
	    	}*/
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      //customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		//customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	}
}
