package com.mbbatch.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;

import com.mbbatch.bean.OrderDetailsBean;
import com.mbbatch.itemprocessor.GOHFMailerSMSProcess;
import com.mbbatch.util.ProcessConstants;

//@Component("gohfDAO")
//@Repository
public class GohfDAO{
	
	private final Logger customLogFactory = Logger.getLogger(GohfDAO.class);
	private static Connection conn;
	/**
	 * Get contact details from gohf_contact with status as blank(i.e. for whom the communication is not sent)
	 * Populate the User details in OrderDetailsBean. Based on the Project id get the project details from gohf_projects
	 * table like developer email, mobile, project description(title & address) & bhk_unit_type.
	 *  
	 * Send the mail & sms to both USER & DEVELOPER. 
	 * Log the incoming mail & SMS message.
	 * Log the response received from the email/sms api to each and every message. 
	 */
	/*public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    String str="+911244";
	    if(str.contains("+")){
	    	System.out.println("true");
	    }
		System.out.println(str.replaceFirst("\\+", ""));
		str.replaceFirst("\\+", "");
		//new GohfDAO().createDBConnection();
	}
	public void createDBConnection(){
		try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	      String url = "jdbc:mysql://dbserver.cs7hgtnps9sx.us-east-1.rds.amazonaws.com:3306/db_gohf";
	      conn = DriverManager.getConnection(url, "gohf_mb", "8MmcDwPMJX3u5RTR");
	      findNewContactLeads(conn);
		} catch (ClassNotFoundException ex) {
			System.err.println(ex.getMessage());
			customLogFactory.error("Exception in creating connection with DB server ",ex);
		}
	    catch (IllegalAccessException ex) {
	    	System.err.println(ex.getMessage());
	    	customLogFactory.error("Exception in creating connection with DB server ",ex);
	    }
	    catch (InstantiationException ex) {
	    	System.err.println(ex.getMessage());
	    	customLogFactory.error("Exception in creating connection with DB server ",ex);
	    }
	    catch (SQLException ex) {
	    	System.err.println(ex.getMessage());
	    	customLogFactory.error("Exception in creating connection with DB server ",ex);
	    }
	}*/
	public List<OrderDetailsBean> findNewContactLeads(Connection conn) {
		OrderDetailsBean retval = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<OrderDetailsBean> contactLeadsList = new ArrayList<OrderDetailsBean>();
	    StringBuffer queryBuffer = new StringBuffer();
	    //Thoughts around contacts to be picked for the same day
	    //queryBuffer.append("SELECT a.id, a.name as 'User_Name', a.email as 'User_Email', a.mobile as 'User_Mobile', a.isd as 'User_Mobile_Isd', b.bhk_unit_type as 'Unit_Type',");
		//queryBuffer.append("c.title as 'Developer_Company_Name', b.project_address, b.title as 'Project_Title', b.email as 'Project_Email', b.mobile as 'Project_Mobile',");
		//queryBuffer.append("b.contact_name as 'Project_user_Name' FROM gohf_contact a, gohf_projects b, gohf_developers c where a.commstatus is NULL and a.project_id = b.id and a.developer_id = c.id");
		//queryBuffer.append("b.contact_name as 'Project_user_Name', i.location_name, j.name as 'City' FROM gohf_contact a, gohf_projects b, gohf_developers c, gohf_location i, gohf_city j");
		//queryBuffer.append(" where a.status = 1 and a.project_id = b.id and a.developer_id = c.id and b.location = i.id and b.city_id = j.id ");
		
	    queryBuffer.append("SELECT a.id, a.name as 'User_Name', a.email as 'User_Email', a.mobile as 'User_Mobile', k.phonecode as 'User_Mobile_Isd', b.bhk_unit_type as 'Unit_Type',");
		queryBuffer.append("c.title as 'Developer_Company_Name', b.project_address, b.title as 'Project_Title', b.email as 'Project_Email', b.mobile as 'Project_Display_Mobile', b.lead_sms_number as 'Project_Lead_SMS_Mobile',");
		queryBuffer.append("b.contact_name as 'Project_user_Name', b.location, j.name as 'City', b.project_offer FROM gohf_contact a, gohf_projects b, gohf_developers c, gohf_city j, gohf_country k");
		queryBuffer.append(" where a.project_id = b.id and b.developer_id = c.id and b.city_id = j.id and a.country_id = k.id and a.id not in (select tab.ref_id from gohf_comm_notification tab where tab.type = 0 and tab.comm_status in ('M','S','D'))");
		
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  retval = new OrderDetailsBean();
		    	  retval.setId(rs.getString("id"));
		    	  retval.setOrderNo(rs.getString("id"));
		    	  retval.setUserName(rs.getString("User_Name"));
		    	  retval.setUserEmail(rs.getString("User_Email"));
		    	  retval.setUserMobile(rs.getString("User_Mobile"));
		    	  retval.setUserMobileIsd(rs.getString("User_Mobile_Isd"));
		    	  retval.setUnitBHkType(rs.getString("Unit_Type"));
		    	  retval.setProjectAddress(rs.getString("location")+", "+rs.getString("City"));
		    	  retval.setLocation(rs.getString("location"));
		    	  retval.setCity(rs.getString("City"));
		    	  retval.setProjectTitle(rs.getString("Project_Title"));
		    	  retval.setDeveloperEmail(rs.getString("Project_Email"));
		    	  retval.setDeveloperDisplayMobile(rs.getString("Project_Display_Mobile"));
		    	  retval.setDeveloperLeadSMSMobile(rs.getString("Project_Lead_SMS_Mobile"));
		    	  retval.setDeveloperCompanyName(rs.getString("Developer_Company_Name"));
		    	  retval.setDeveloperName(rs.getString("Project_user_Name"));
		    	  retval.setOfferApplied(rs.getString("project_offer"));
		    	  contactLeadsList.add(retval);
		      }
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      customLogFactory.error("Exception querying DB",ex);
	    }finally{
	    	try{
		    	if(rs != null){
		    		rs.close();
		    	}
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	    customLogFactory.info("GOHF: contactLeadsList size "+contactLeadsList.size());
		return contactLeadsList;
	}
	/**
	 * 
	 */
	public List<OrderDetailsBean> getNewBookingsData(Connection conn) {
		OrderDetailsBean retval = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<OrderDetailsBean> newBookingsList = new ArrayList<OrderDetailsBean>();
	    StringBuffer queryBuffer = new StringBuffer();
		
	    /*queryBuffer.append("select a.id, a.booking_id, e.title as 'Property_Type',c.title as 'Unit_Size', d.name as 'Unit_Type', b.price, b.booking_amount,");
	    queryBuffer.append("b.offer_price, b.offer_valid_till ,f.project_address, f.title as 'Project_Name', b.offer, b.offer_type,");
		queryBuffer.append("f.email as 'Developer_email', f.mobile as 'Developer_Display_mobile', f.lead_sms_number as 'Developer_Lead_sms_Mobile' ,");
		queryBuffer.append("f.contact_name as 'Developer_Name', g.title as 'Developer_Company_Name', h.name as 'User_Name',");
		queryBuffer.append("h.email as 'User_Email', h.mobile as 'User_Mobile', h.isd as 'User_Mobile_ISD', b.how_to_proceed, b.terms_condition,");
		queryBuffer.append("f.location, j.name as 'City', b.covered_plot_area,	b.covered_area_unit, b.plot_area, b.plot_area_unit ");
		queryBuffer.append("from gohf_booking a, gohf_projects_units b, gohf_unit_size c, gohf_unit_type d, gohf_property_type e, ");
		queryBuffer.append("gohf_projects f, gohf_developers g, gohf_user h, gohf_city j ");
		queryBuffer.append("where a.project_unit_id = b.id and a.uid = h.id and b.project_id = f.id and b.property_type_id = e.id and b.unit_id = d.id ");
		queryBuffer.append("and b.unit_size_id = c.id and f.developer_id = g.id and b.booking_option = 1 and f.city_id = j.id ");
		queryBuffer.append("and a.id not in (select tab.ref_id from gohf_comm_notification tab where tab.type = 1 and tab.comm_status in ('M','S','D'))");
		*/
	    queryBuffer.append("select a.id, a.booking_id, e.title as 'Property_Type',c.title as 'Unit_Size', d.name as 'Unit_Type', b.price, b.booking_amount, ");
		queryBuffer.append("b.offer_price, b.offer_valid_till ,f.project_address, f.title as 'Project_Name', b.offer, b.offer_type, ");
		queryBuffer.append("f.email as 'Developer_email', f.mobile as 'Developer_Display_mobile', f.lead_sms_number as 'Developer_Lead_sms_Mobile' , ");
		queryBuffer.append("f.contact_name as 'Developer_Name', g.title as 'Developer_Company_Name', h.name as 'User_Name', ");
		queryBuffer.append("h.email as 'User_Email', h.mobile as 'User_Mobile', h.isd as 'User_Mobile_ISD', f.how_to_proceed, f.terms_condition, f.slug, ");
		queryBuffer.append("f.location, j.name as 'City', b.covered_plot_area,	b.covered_area_unit, b.plot_area, b.plot_area_unit  ");
		queryBuffer.append("from gohf_booking a, gohf_projects_units b, gohf_unit_size c, gohf_unit_type d, gohf_property_type e,  ");
		queryBuffer.append("gohf_projects f, gohf_developers g, gohf_user h, gohf_city j, gohf_booking_transection k   ");
		queryBuffer.append("where a.project_unit_id = b.id and a.uid = h.id and b.project_id = f.id and b.property_type_id = e.id and b.unit_id = d.id "); 
		queryBuffer.append("and b.unit_size_id = c.id and f.developer_id = g.id and b.booking_option = 1 and f.city_id = j.id and a.id = k.booking_ref_id and k.QSIResponseCode = 0 "); 
		queryBuffer.append("and a.id not in (select tab.ref_id from gohf_comm_notification tab where tab.type = 1 and tab.comm_status in ('M','S','D')) ");
		
		String offerPrice = null;
		String unitPrice = null;
		String bookingAmount = null;
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  retval = new OrderDetailsBean();
		    	  retval.setId(rs.getString("id"));
		    	  retval.setOrderNo(rs.getString("booking_id"));
		    	  
		    	  offerPrice = rs.getString("offer_price");
		    	  if(offerPrice != null && !offerPrice.isEmpty()){
		    		  //offerPrice = ProcessConstants.formatCurrencyInWord(Long.valueOf(offerPrice));
		    		  retval.setOfferPrice(offerPrice);
		    	  }
		    	  
		    	  retval.setOfferValidity(rs.getString("offer_valid_till"));
		    	  retval.setOfferType(rs.getString("offer_type"));
		    	  retval.setLocation(rs.getString("location"));
		    	  retval.setCity(rs.getString("City"));
		    	  retval.setUnitArea(rs.getString("Unit_Size"));
		    	  retval.setUnitBHkType(rs.getString("Unit_Type")+" "+rs.getString("Property_Type"));
		    	  retval.setSlug(rs.getString("slug"));
		    	  unitPrice = rs.getString("price");
		    	  if(unitPrice != null && !unitPrice.isEmpty()){
		    		  //unitPrice = ProcessConstants.formatCurrencyInWord(Long.valueOf(unitPrice));
		    		  retval.setUnitPrice(unitPrice);
		    	  }
		    	  
		    	  bookingAmount = rs.getString("booking_amount");
		    	  if(bookingAmount != null && !bookingAmount.isEmpty()){
		    		  //bookingAmount = ProcessConstants.formatCurrencyInWord(Long.valueOf(bookingAmount));
		    		  retval.setBookingAmount(bookingAmount);
		    	  }
		    	  
		    	  retval.setProjectAddress(rs.getString("location")+", "+rs.getString("City"));
		    	  retval.setProjectTitle(rs.getString("Project_Name"));
		    	  retval.setDeveloperCompanyName(rs.getString("Developer_Company_Name"));
		    	  retval.setDeveloperEmail(rs.getString("Developer_email"));
		    	  retval.setDeveloperDisplayMobile(rs.getString("Developer_Display_mobile"));
		    	  retval.setDeveloperLeadSMSMobile(rs.getString("Developer_Lead_sms_Mobile"));
		    	  retval.setDeveloperName(rs.getString("Developer_Name"));
		    	  retval.setUserEmail(rs.getString("User_Email"));
		    	  retval.setUserMobile(rs.getString("User_Mobile"));
		    	  retval.setUserMobileIsd(trimPlusCharacterFromISDCode(rs.getString("User_Mobile_ISD")));
		    	  retval.setUserName(rs.getString("User_Name"));
		    	  retval.setOfferApplied(rs.getString("offer"));
		    	  retval.setNextSteps(rs.getString("how_to_proceed"));
		    	  retval.setTermsAndConditions(rs.getString("terms_condition"));
		    	  retval.setCoveredAraUnit(rs.getString("covered_area_unit"));
		    	  retval.setCoveredArea(rs.getString("covered_plot_area"));
		    	  retval.setPlotArea(rs.getString("plot_area"));
		    	  retval.setPlotAreaUnit(rs.getString("plot_area_unit"));
		    	  retval.setPropertyType(rs.getString("Property_Type"));
		    	  newBookingsList.add(retval);
		      }
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      customLogFactory.error("Exception querying DB",ex);
	    }finally{
	    	try{
		    	if(rs != null){
		    		rs.close();
		    	}
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	    customLogFactory.info("GOHF: New Booking List size "+newBookingsList.size());
		return newBookingsList;
	}
	
	public List<OrderDetailsBean> getCouponsData(Connection conn) {
		OrderDetailsBean retval = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<OrderDetailsBean> couponsDataList = new ArrayList<OrderDetailsBean>();
	    StringBuffer queryBuffer = new StringBuffer();
	    
	    //queryBuffer.append("select a.coupon_code, a.id, e.title as 'Property_Type',c.title as 'Unit_Size', d.name as 'Unit_Type', b.price, b.booking_amount, f.project_address, f.title as 'Project_Name', b.offer,");
		//queryBuffer.append("f.email as 'Developer_email', f.mobile as 'Developer_mobile', f.contact_name as 'Developer_Name', g.title as 'Developer_Company_Name', a.name as 'User_Name',");
		//queryBuffer.append("a.email as 'User_Email', a.mobile as 'User_Mobile', a.isd as 'User_Mobile_ISD', b.how_to_proceed, b.terms_condition, i.location_name, j.name as 'City', b.covered_area,	b.covered_area_unit, b.plot_area, b.plot_area_unit ");
		//queryBuffer.append("from gohf_coupon a, gohf_projects_units b, gohf_unit_size c, gohf_unit_type d, gohf_property_type e, gohf_projects f, gohf_developers g, gohf_location i, gohf_city j"); 
		//queryBuffer.append(" where a.commstatus is NULL and a.project_unit_id = b.id and b.project_id = f.id and b.property_type_id = e.id and b.unit_id = d.id and b.unit_size_id = c.id and f.developer_id = g.id and b.booking_option = 0");
		//queryBuffer.append(" where a.status = 0 and a.project_unit_id = b.id and b.project_id = f.id and b.property_type_id = e.id and b.unit_id = d.id and b.unit_size_id = c.id and f.developer_id = g.id and b.booking_option = 0 and f.location = i.id and f.city_id = j.id");
	    
		
	    queryBuffer.append("select a.coupon_code, a.id, e.title as 'Property_Type',c.title as 'Unit_Size', d.name as 'Unit_Type', b.price, b.booking_amount, b.offer_price, b.offer_valid_till ");
		queryBuffer.append(", f.project_address, f.title as 'Project_Name', b.offer, b.offer_type,f.email as 'Developer_email', f.mobile as 'Developer_Display_mobile', ");
		queryBuffer.append("f.lead_sms_number as 'Developer_Lead_sms_Mobile', f.contact_name as 'Developer_Name', g.title as 'Developer_Company_Name', u.name as 'User_Name',");
		queryBuffer.append("u.email as 'User_Email', u.mobile as 'User_Mobile', u.isd as 'User_Mobile_ISD', f.how_to_proceed, f.terms_condition, f.slug, f.location, j.name as 'City', ");
		queryBuffer.append("b.covered_plot_area,	b.covered_area_unit, b.plot_area, b.plot_area_unit from gohf_coupon a, gohf_projects_units b, gohf_unit_size c, gohf_unit_type d, ");
		queryBuffer.append("gohf_property_type e, gohf_projects f, gohf_developers g, gohf_city j, gohf_user u where a.project_unit_id = b.id ");
		queryBuffer.append("and b.project_id = f.id and b.property_type_id = e.id and b.unit_id = d.id and b.unit_size_id = c.id and f.developer_id = g.id ");
		queryBuffer.append("and b.booking_option = 0 and f.city_id = j.id and a.uid = u.id ");
		queryBuffer.append("and a.id not in (select tab.ref_id from gohf_comm_notification tab where tab.type = 2 and tab.comm_status in ('M','S','D'))");
		String offerPrice = null;
		String unitPrice = null;
		String bookingAmount = null;
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	rs = stmt.executeQuery();
		      while (rs.next())
		      {
		    	  retval = new OrderDetailsBean();
		    	  retval.setId(rs.getString("id"));
		    	  retval.setOrderNo(rs.getString("coupon_code"));
		    	  
		    	  offerPrice = rs.getString("offer_price");
		    	  if(offerPrice != null && !offerPrice.isEmpty()){
		    		  //offerPrice = ProcessConstants.formatCurrencyInWord(Long.valueOf(offerPrice));
		    		  retval.setOfferPrice(offerPrice);
		    	  }
		    	  retval.setSlug(rs.getString("slug"));
		    	  retval.setOfferValidity(rs.getString("offer_valid_till"));
		    	  retval.setOfferType(rs.getString("offer_type"));
		    	  retval.setLocation(rs.getString("location"));
		    	  retval.setCity(rs.getString("City"));
		    	  retval.setUnitArea(rs.getString("Unit_Size"));
		    	  retval.setUnitBHkType(rs.getString("Unit_Type")+" "+rs.getString("Property_Type"));
		    	  
		    	  unitPrice = rs.getString("price");
		    	  if(unitPrice != null && !unitPrice.isEmpty()){
		    		  //unitPrice = ProcessConstants.formatCurrencyInWord(Long.valueOf(unitPrice));
		    		  retval.setUnitPrice(unitPrice);
		    	  }
		    	  
		    	  bookingAmount = rs.getString("booking_amount");
		    	  if(bookingAmount != null && !bookingAmount.isEmpty()){
		    		 // bookingAmount = ProcessConstants.formatCurrencyInWord(Long.valueOf(bookingAmount));
		    		  retval.setBookingAmount(bookingAmount);
		    	  }
		    	  
		    	  retval.setProjectAddress(rs.getString("location")+", "+rs.getString("City"));
		    	  retval.setProjectTitle(rs.getString("Project_Name"));
		    	  retval.setDeveloperCompanyName(rs.getString("Developer_Company_Name"));
		    	  retval.setDeveloperEmail(rs.getString("Developer_email"));
		    	  retval.setDeveloperDisplayMobile(rs.getString("Developer_Display_mobile"));
		    	  retval.setDeveloperLeadSMSMobile(rs.getString("Developer_Lead_sms_Mobile"));
		    	  retval.setDeveloperName(rs.getString("Developer_Name"));
		    	  retval.setUserEmail(rs.getString("User_Email"));
		    	  retval.setUserMobile(rs.getString("User_Mobile"));
		    	  retval.setUserMobileIsd(trimPlusCharacterFromISDCode(rs.getString("User_Mobile_ISD")));
		    	  retval.setUserName(rs.getString("User_Name"));
		    	  retval.setOfferApplied(rs.getString("offer"));
		    	  retval.setNextSteps(rs.getString("how_to_proceed"));
		    	  retval.setTermsAndConditions(rs.getString("terms_condition"));
		    	  retval.setCoveredAraUnit(rs.getString("covered_area_unit"));
		    	  retval.setCoveredArea(rs.getString("covered_plot_area"));
		    	  retval.setPlotArea(rs.getString("plot_area"));
		    	  retval.setPlotAreaUnit(rs.getString("plot_area_unit"));
		    	  retval.setPropertyType(rs.getString("Property_Type"));
		    	  couponsDataList.add(retval);
		      }
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      customLogFactory.error("Exception querying DB",ex);
	    }finally{
	    	try{
		    	if(rs != null){
		    		rs.close();
		    	}
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	    customLogFactory.info("GOHF: Coupon List size "+couponsDataList.size());
		return couponsDataList;
	}
	
	public void insertContactLeadsCommStatus(List<OrderDetailsBean> orderList, Connection conn){
		PreparedStatement stmt = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    queryBuffer.append("Insert into gohf_comm_notification (ref_id, type, comm_status, comm_remarks, created) values (?, ?, ?, ?, ?)");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	for(OrderDetailsBean bean : orderList){
	    		stmt.setInt(1, Integer.valueOf(bean.getId()));
		    	stmt.setInt(2, 0);
		    	stmt.setString(3, bean.getOrderCommStatus());
		    	stmt.setString(4, bean.getOrderCommStatusRemarks());
		    	stmt.setTimestamp(5, modiDateTimeStamp);
		    	stmt.addBatch();
	    	}
	    	int[] rowUpdated = stmt.executeBatch();
	    	for (int i = 0; i < rowUpdated.length; i++) {
	            System.out.println("gohf_comm_notification table row updated " +rowUpdated[i] + " ");
	    	}
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	}
	
	public void insertNewBookingCommStatus(List<OrderDetailsBean> orderList, Connection conn){
		PreparedStatement stmt = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    queryBuffer.append("Insert into gohf_comm_notification (ref_id, type, comm_status, comm_remarks, created) values (?, ?, ?, ?, ?)");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	for(OrderDetailsBean bean : orderList){
	    		stmt.setInt(1, Integer.valueOf(bean.getId()));
		    	stmt.setInt(2, 1);
		    	stmt.setString(3, bean.getOrderCommStatus());
		    	stmt.setString(4, bean.getOrderCommStatusRemarks());
		    	stmt.setTimestamp(5, modiDateTimeStamp);
		    	stmt.addBatch();
	    	}
	    	int[] rowUpdated = stmt.executeBatch();
	    	for (int i = 0; i < rowUpdated.length; i++) {
	            System.out.println("gohf_comm_notification table row updated " +rowUpdated[i] + " ");
	    	}
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	}
	
	public void insertCouponCommStatus(List<OrderDetailsBean> orderList, Connection conn){
		PreparedStatement stmt = null;
	    StringBuffer queryBuffer = new StringBuffer();
	    Calendar calendar = Calendar.getInstance();
	    Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
	    queryBuffer.append("Insert into gohf_comm_notification (ref_id, type, comm_status, comm_remarks, created) values (?, ?, ?, ?, ?)");
	    try
	    {
	    	stmt = conn.prepareStatement(queryBuffer.toString());
	    	for(OrderDetailsBean bean : orderList){
	    		stmt.setInt(1, Integer.valueOf(bean.getId()));
		    	stmt.setInt(2, 2);
		    	stmt.setString(3, bean.getOrderCommStatus());
		    	stmt.setString(4, bean.getOrderCommStatusRemarks());
		    	stmt.setTimestamp(5, modiDateTimeStamp);
		    	stmt.addBatch();
	    	}
	    	int[] rowUpdated = stmt.executeBatch();
	    	for (int i = 0; i < rowUpdated.length; i++) {
	            System.out.println("gohf_comm_notification table row updated " +rowUpdated[i] + " ");
	    	}
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	      customLogFactory.error("Exception while update in DB",ex);
	    }finally{
	    	try{
		    	if(stmt != null){
		    		stmt.close();
		    	}
		    	if(conn != null){
		    		conn.close();
		    	}
	    	}catch(SQLException sqex){
	    		customLogFactory.error("Exception in closing DB connection objects ",sqex);
	    	}
	    }
	}
	
	private String trimPlusCharacterFromISDCode(String isdCode){
		if(isdCode != null && !isdCode.isEmpty()){
			if(isdCode.contains("+")){
				return isdCode.replaceFirst("\\+", "");
			}
		}
		return isdCode;
	}
}
