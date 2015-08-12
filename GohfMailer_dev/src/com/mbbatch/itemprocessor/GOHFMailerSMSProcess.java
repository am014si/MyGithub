package com.mbbatch.itemprocessor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sql.DataSource;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import com.mbbatch.bean.NotificationResponseBean;
import com.mbbatch.bean.OrderDetailsBean;
import com.mbbatch.dao.GohfDAO;
import com.mbbatch.mailer.MailService;
import com.mbbatch.sms.SmsService;
import com.mbbatch.util.ProcessConstants;


public class GOHFMailerSMSProcess implements ItemProcessor <String, String> {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
	private SmsService smsservice;
	private DataSource dataSource;
	private MailService mailservice;
	private String executionType;
	private GohfDAO gohfDAO = new GohfDAO();
	//private static Connection conn;
	private String velocityFilename =null;
	private int PARALLEL_THREADS_SIZE = 10;
	private ExecutorService executor = Executors.newFixedThreadPool(PARALLEL_THREADS_SIZE);
	public static final String MAIL_FROM_EMAIL_ID = "projectalerts@magicbricks.com";
	public static final String bccEmail = "mbgohf2015@gmail.com";
	//public static final String MAIL_FROM_EMAIL_ID = "singh.amit@timesgroup.com";
	//private final Logger customLogFactory = Logger.getLogger(GOHFMailerSMSProcess.class);
	private List<OrderDetailsBean> contactOrderDetailsListAftrComm = new ArrayList<OrderDetailsBean>();
	private List<OrderDetailsBean> bookingOrderDetailsListAftrComm = new ArrayList<OrderDetailsBean>();
	private List<OrderDetailsBean> couponOrderDetailsListAftrComm = new ArrayList<OrderDetailsBean>();
	public String getVelocityFilename() {
		return velocityFilename;
	}
	public void setVelocityFilename(String velocityFilename) {
		this.velocityFilename = velocityFilename;
	}
	//String currentDay = decrementDateByNumOfDays(new Date(), MM_DD_YYYY, 0);
	/*public static void main(String[] args) {
		//Calendar calendar = Calendar.getInstance();
	    //Timestamp modiDateTimeStamp = new Timestamp(calendar.getTime().getTime());
		//System.out.println("@@@@@@@@@@ "+formatCurrencyInWord(56117900));
		//new GOHFMailerSMSProcess().createDBConnection();
	}*//*
	public void createDBConnection(){
		try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	      String url = "jdbc:mysql://dbserver.cs7hgtnps9sx.us-east-1.rds.amazonaws.com:3306/db_gohf";
	      conn = DriverManager.getConnection(url, "gohf_mb", "8MmcDwPMJX3u5RTR");
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
	private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
        //System.out.println("@@@@@@@@ "+stepExecution.getJobParameters().getString("executionType"));
        executionType = stepExecution.getJobParameters().getString("executionType");
    }
	@SuppressWarnings("unchecked")
	public String process(String str) throws Exception {
		long l1 = System.currentTimeMillis();
		
		//System.out.println("#### "+executionType);
		System.out.println("Batch Process execution started at "+sdf.format(new Date())+" ExecutionType "+executionType);
		
		//New Booking Mailer & SMS
		if(ProcessConstants.EXECUTION_BOOKING_ONLY.equalsIgnoreCase(executionType)){
			SendMailerSMSForNewBookings();
			
			//Batch Insert New Booking
	        try{
				gohfDAO.insertNewBookingCommStatus(bookingOrderDetailsListAftrComm, dataSource.getConnection());
	        } catch (Exception e) {
				System.out.println(" Exception while adding Booking commstatus row "+e.getMessage());
				e.printStackTrace();
			}
		}
		//Coupon Mailer & SMS
		if(ProcessConstants.EXECUTION_COUPON_ONLY.equalsIgnoreCase(executionType)){
			SendMailerSMSForCoupon();
			
			//Batch Insert Orders for which communication is delivered/failed
			try{
				gohfDAO.insertCouponCommStatus(couponOrderDetailsListAftrComm, dataSource.getConnection());
	        } catch (Exception e) {
				System.out.println(" Exception while adding Coupon commstatus row "+e.getMessage());
				e.printStackTrace();
			}
		}
		//Contact Leads Mailer & SMS
		if(ProcessConstants.EXECUTION_CONTACT_ONLY.equalsIgnoreCase(executionType)){
			SendMailerSMSForContactLeads();
			
			//Batch Insert Contacts
	        try{
				gohfDAO.insertContactLeadsCommStatus(contactOrderDetailsListAftrComm, dataSource.getConnection());
	        } catch (Exception e) {
				System.out.println(" Exception while adding Booking commstatus row "+e.getMessage());
				e.printStackTrace();
			}
		}
		
        System.out.println("Batch Process execution finished at "+sdf.format(new Date())+" Total execution time is "+(System.currentTimeMillis()-l1)+" milliseconds");
		exitBatchProcess();
		return null;
	}
	
	private NotificationResponseBean prepareAndSendContactLeadsMailToUser(OrderDetailsBean odb){
		NotificationResponseBean response = null;
		System.out.println(" Inside prepareAndSendContactLeadsMailToUser method");
		Map<String, Object> displaymap =new HashMap<String, Object>();
		String subjectLine = "GOHF : Enquiry Sent Successfully for "+odb.getProjectTitle();
		displaymap.put("userName",odb.getUserName());
		displaymap.put("projectTitle",odb.getProjectTitle());
		displaymap.put("location",odb.getLocation());
		displaymap.put("city",odb.getCity());
		displaymap.put("developerName",odb.getDeveloperName());
		displaymap.put("developerMobile",odb.getDeveloperDisplayMobile());
		displaymap.put("title",odb.getDeveloperCompanyName());
		displaymap.put("bhkType",odb.getUnitBHkType());
		displaymap.put("price",odb.getUnitPrice());
		//displaymap.put("unitArea",odb.getUnitArea());
		displaymap.put("offerPrice",odb.getOfferPrice());
		displaymap.put("offerText",odb.getOfferApplied());
		displaymap.put("offerValidity",odb.getOfferValidity());
		response = mailservice.sendMail(odb.getUserEmail(),MAIL_FROM_EMAIL_ID, "MagicBricks", subjectLine,"GOHF_contact_leads_mailer_to_user.vm", displaymap, true,null,bccEmail);
		if(response != null && response.isSuccess()){
			System.out.println(" Mail successfully delivered to "+odb.getUserEmail());
		} else {
			System.out.println(" Mail delivery failed to "+odb.getUserEmail());
		}
		return response;
	}
	
	private NotificationResponseBean prepareAndSendContactLeadsMailToDeveloper(OrderDetailsBean odb){
		NotificationResponseBean response = null;
		System.out.println(" Inside prepareAndSendContactLeadsMailToDeveloper method");
		Map<String, Object> displaymap =new HashMap<String, Object>();
		String subjectLine = "GOHF : Response on your Project - "+odb.getProjectTitle();
		displaymap.put("developerName",odb.getDeveloperName());
		displaymap.put("projectTitle",odb.getProjectTitle());
		displaymap.put("location",odb.getLocation());
		displaymap.put("city",odb.getCity());
		displaymap.put("userName",odb.getUserName());
		displaymap.put("userEmail",odb.getUserEmail());
		displaymap.put("userMobile","+"+odb.getUserMobileIsd()+"-"+odb.getUserMobile());
		if(odb != null && odb.getUserName() != null && !odb.getUserName().isEmpty()){
			displaymap.put("showUserName",true);
		} else {
			displaymap.put("showUserName",false);
		}
		if(odb != null && odb.getUserMobile() != null && !odb.getUserMobile().isEmpty() && !odb.getUserMobile().trim().equalsIgnoreCase("0")){
			displaymap.put("showUserMobile",true);
		} else {
			displaymap.put("showUserMobile",false);
		}
		if(odb != null && odb.getUserEmail() != null && !odb.getUserEmail().isEmpty()){
			displaymap.put("showUserEmail",true);
		} else {
			displaymap.put("showUserEmail",false);
		}
		displaymap.put("bhkType",odb.getUnitBHkType());
		response = mailservice.sendMail(odb.getDeveloperEmail(),MAIL_FROM_EMAIL_ID, "MagicBricks", subjectLine,"GOHF_contact_leads_mailer_to_devloper.vm", displaymap, true,null,bccEmail);
		if(response != null && response.isSuccess()){
			System.out.println(" Mail successfully delivered to "+odb.getDeveloperEmail());
		} else {
			System.out.println(" Mail delivery failed to "+odb.getDeveloperEmail());
		}
		return response;
	}
	
	private NotificationResponseBean prepareAndSendNewBookingMailToUser(OrderDetailsBean odb){
		NotificationResponseBean response = null;
		System.out.println(" Inside prepareAndSendNewBookingMailToUser method");
		Map<String, Object> displaymap =new HashMap<String, Object>();
		String subjectLine = "GOHF : Booking done Successfully for "+odb.getUnitBHkType()+" "+odb.getUnitArea()+" in "+odb.getProjectTitle();
		displaymap.put("userName",odb.getUserName());
		displaymap.put("projectTitle",odb.getProjectTitle());
		displaymap.put("bookingId",odb.getOrderNo());
		displaymap.put("projectAddress",odb.getProjectTitle()+" "+odb.getLocation());
		//Currency Formatting required
		displaymap.put("bookingAmount",odb.getBookingAmount());
		displaymap.put("developerCompanyName",odb.getDeveloperCompanyName());
		displaymap.put("bhkType",odb.getUnitBHkType());
		displaymap.put("price",odb.getUnitPrice());
		displaymap.put("unitArea",odb.getUnitArea());
		displaymap.put("offerPrice",odb.getOfferPrice());
		displaymap.put("projectUserMobile",odb.getDeveloperDisplayMobile());
		displaymap.put("projectUserName",odb.getDeveloperName());
		displaymap.put("nextSteps",formatNextSteps(odb.getNextSteps()));
		displaymap.put("termsAndConditions",formatTermsAndConditions(odb.getTermsAndConditions()));
		displaymap.put("offerValidTill",odb.getOfferValidity());
		displaymap.put("offerText",odb.getOfferApplied());
		displaymap.put("offerPrice",odb.getOfferPrice());
		displaymap.put("slug","http://gohf.magicbricks.com/project/"+odb.getSlug());
		if(ProcessConstants.FREEBIE_OFFER_TYPE.equalsIgnoreCase(odb.getOfferType())){
			displaymap.put("freeBieOfferType",true);
			displaymap.put("discountedPriceOfferType",false);
		} else if(ProcessConstants.DISCOUNTED_PRICE_OFFER_TYPE.equalsIgnoreCase(odb.getOfferType())){
			displaymap.put("freeBieOfferType",false);
			displaymap.put("discountedPriceOfferType",true);
		}
		response = mailservice.sendMail(odb.getUserEmail(),MAIL_FROM_EMAIL_ID, "MagicBricks", subjectLine,"GOHF_booking_mailer_to_user.vm", displaymap, true,null,bccEmail);
		if(response != null && response.isSuccess()){
			System.out.println(" Mail successfully delivered to "+odb.getUserEmail());
		} else {
			System.out.println(" Mail delivery failed to "+odb.getUserEmail());
		}
		return response;
	}
	
	private NotificationResponseBean prepareAndSendNewBookingMailToDeveloper(OrderDetailsBean odb){
		NotificationResponseBean response = null;
		System.out.println(" Inside prepareAndSendNewBookingMailToDeveloper method");
		Map<String, Object> displaymap =new HashMap<String, Object>();
		String subjectLine = "GOHF : "+odb.getUnitBHkType()+" "+odb.getUnitArea()+" Booked in "+odb.getProjectTitle(); 
		displaymap.put("projectUserName",odb.getDeveloperName());
		displaymap.put("projectAddress",odb.getProjectAddress());
		displaymap.put("userName",odb.getUserName());
		displaymap.put("userEmail",odb.getUserEmail());
		displaymap.put("userMobile","+"+odb.getUserMobileIsd()+"-"+odb.getUserMobile());
		displaymap.put("bhkType",odb.getUnitBHkType());
		displaymap.put("bookingId",odb.getOrderNo());
		displaymap.put("unitArea",odb.getUnitArea());
		displaymap.put("projectTitle",odb.getProjectTitle());
		displaymap.put("unitPrice",odb.getUnitPrice());
		displaymap.put("offer",odb.getOfferApplied());
		displaymap.put("offerPrice",odb.getOfferPrice());
		if(ProcessConstants.FREEBIE_OFFER_TYPE.equalsIgnoreCase(odb.getOfferType())){
			displaymap.put("freeBieOfferType",true);
			displaymap.put("discountedPriceOfferType",false);
		} else if(ProcessConstants.DISCOUNTED_PRICE_OFFER_TYPE.equalsIgnoreCase(odb.getOfferType())){
			displaymap.put("freeBieOfferType",false);
			displaymap.put("discountedPriceOfferType",true);
		}
		response = mailservice.sendMail(odb.getDeveloperEmail(),MAIL_FROM_EMAIL_ID, "MagicBricks", subjectLine,"GOHF_booking_mailer_to_developer.vm", displaymap, true,null,bccEmail);
		if(response != null && response.isSuccess()){
			System.out.println(" Mail successfully delivered to "+odb.getDeveloperEmail());
		} else {
			System.out.println(" Mail delivery failed to "+odb.getDeveloperEmail());
		}
		return response;
	}
	
	private NotificationResponseBean prepareAndSendCouponMailToUser(OrderDetailsBean odb){
		NotificationResponseBean response = null;
		System.out.println(" Inside prepareAndSendCouponMailToUser method");
		Map<String, Object> displaymap =new HashMap<String, Object>();
		String subjectLine = "GOHF: Coupon for Exclusive Offer on "+odb.getUnitBHkType()+" "+odb.getUnitArea()+" in "+odb.getProjectTitle(); 
		displaymap.put("userName",odb.getUserName());
		displaymap.put("projectTitle",odb.getProjectTitle());
		displaymap.put("bookingId",odb.getOrderNo());
		displaymap.put("projectAddress",odb.getLocation());
		displaymap.put("bookingAmount",odb.getBookingAmount());
		displaymap.put("developerCompanyName",odb.getDeveloperCompanyName());
		displaymap.put("unitBHKType",odb.getUnitBHkType());
		displaymap.put("unitPrice",odb.getUnitPrice());
		displaymap.put("unitArea",odb.getUnitArea());
		displaymap.put("offerPrice",odb.getOfferPrice());
		displaymap.put("projectUserMobile",odb.getDeveloperDisplayMobile());
		displaymap.put("projectUserName",odb.getDeveloperName());
		displaymap.put("couponOfferText",odb.getOfferApplied());
		displaymap.put("couponCode",odb.getOrderNo());
		displaymap.put("nextSteps",formatNextSteps(odb.getNextSteps()));
		displaymap.put("termsAndConditions",formatTermsAndConditions(odb.getTermsAndConditions()));
		displaymap.put("offerValidTill",odb.getOfferValidity());
		if(ProcessConstants.FREEBIE_OFFER_TYPE.equalsIgnoreCase(odb.getOfferType())){
			displaymap.put("freeBieOfferType",true);
			displaymap.put("discountedPriceOfferType",false);
		} else if(ProcessConstants.DISCOUNTED_PRICE_OFFER_TYPE.equalsIgnoreCase(odb.getOfferType())){
			displaymap.put("freeBieOfferType",false);
			displaymap.put("discountedPriceOfferType",true);
		}
		//response = mailservice.sendMail(odb.getUserEmail(),MAIL_FROM_EMAIL_ID, "MagicBricks", "GOHF","GOHF_coupon_mailer_to_user.vm", displaymap, true,null,null);
		response = mailservice.sendMail(odb.getUserEmail(),MAIL_FROM_EMAIL_ID, "MagicBricks", subjectLine,"GOHF_coupon_mailer_to_user.vm", displaymap, true,null,bccEmail);
		if(response != null && response.isSuccess()){
			System.out.println(" Mail successfully delivered to "+odb.getUserEmail());
		} else {
			System.out.println(" Mail delivery failed to "+odb.getUserEmail());
		}
		return response;
	}
	
	private NotificationResponseBean prepareAndSendCouponMailToDeveloper(OrderDetailsBean odb){
		NotificationResponseBean response = null;
		System.out.println(" Inside prepareAndSendCouponMailToDeveloper method");
		Map<String, Object> displaymap =new HashMap<String, Object>();
		String subjectLine = "GOHF : Coupon Downloaded for "+odb.getUnitBHkType()+" "+odb.getUnitArea()+" in "+odb.getProjectTitle(); 
		displaymap.put("projectUserName",odb.getDeveloperName());
		displaymap.put("projectAddress",odb.getProjectAddress());
		displaymap.put("userName",odb.getUserName());
		displaymap.put("userEmail",odb.getUserEmail());
		displaymap.put("userMobile","+"+odb.getUserMobileIsd()+"-"+odb.getUserMobile());
		displaymap.put("unitBHKType",odb.getUnitBHkType());
		displaymap.put("projectTitle",odb.getDeveloperName());
		displaymap.put("unitArea",odb.getUnitArea());
		displaymap.put("projectTitle",odb.getProjectTitle());
		displaymap.put("unitPrice",odb.getUnitPrice());
		displaymap.put("couponOfferText",odb.getOfferApplied());
		displaymap.put("couponCode",odb.getOrderNo());
		displaymap.put("offerPrice",odb.getOfferPrice());
		if(ProcessConstants.FREEBIE_OFFER_TYPE.equalsIgnoreCase(odb.getOfferType())){
			displaymap.put("freeBieOfferType",true);
			displaymap.put("discountedPriceOfferType",false);
		} else if(ProcessConstants.DISCOUNTED_PRICE_OFFER_TYPE.equalsIgnoreCase(odb.getOfferType())){
			displaymap.put("freeBieOfferType",false);
			displaymap.put("discountedPriceOfferType",true);
		}
		response = mailservice.sendMail(odb.getDeveloperEmail(),MAIL_FROM_EMAIL_ID, "MagicBricks", subjectLine,"GOHF_coupon_mailer_to_developer.vm", displaymap, true,null,bccEmail);
		if(response != null && response.isSuccess()){
			System.out.println(" Mail successfully delivered to "+odb.getDeveloperEmail());
		} else {
			System.out.println(" Mail delivery failed to "+odb.getDeveloperEmail());
		}
		return response;
	}
	
	private NotificationResponseBean sendSms(String mobile, String message){
		NotificationResponseBean response = null;
		if(mobile != null && !mobile.isEmpty()){
			System.out.println("In sendSms Mobile: "+mobile+" Message: "+message);
			response = smsservice.sendSms(mobile, message);
		 }
		return response;
	}
	
	private NotificationResponseBean sendInternationalSms(String mobile, String message, String isdCode){
		NotificationResponseBean response = null;
		if(mobile != null && !mobile.isEmpty()){
			System.out.println("In sendSms Mobile: "+mobile+" Message: "+message);
			response = smsservice.sendPriorityMessageProcessInternational(mobile, message, isdCode);
		 }
		return response;
	}
	
	private final class ThreadWorkerForCoupon implements Runnable {
        private OrderDetailsBean detailsBean;

        public ThreadWorkerForCoupon(OrderDetailsBean odb) {
            this.detailsBean = odb;
        }

        public void run() {
        	NotificationResponseBean mailToUserResponseStatus = null;
        	NotificationResponseBean mailToDeveloperResponseStatus = null;
        	NotificationResponseBean smsToUserResponseStatus = null;
        	NotificationResponseBean smsToDeveloperResponseStatus = null;
        	boolean dataPushedInBuilderCRM = false;
        	String userCouponMsg = prepareCouponSMSForUser(detailsBean);
        	String developerCouponMsg = prepareCouponSMSForDeveloper(detailsBean);
	        //System.out.println("Thread " + Thread.currentThread().getName() + " starting");
	        System.out.println(" Inside worker thread "+detailsBean);
	        
	        if(detailsBean != null && detailsBean.getUserEmail() != null && !detailsBean.getUserEmail().isEmpty()){
	        	mailToUserResponseStatus = prepareAndSendCouponMailToUser(detailsBean);
	        }
	        if(detailsBean != null && detailsBean.getDeveloperEmail() != null && !detailsBean.getDeveloperEmail().isEmpty()){
	        	mailToDeveloperResponseStatus = prepareAndSendCouponMailToDeveloper(detailsBean);
	        }
	        if(detailsBean != null && detailsBean.getUserMobile() != null && !detailsBean.getUserMobile().isEmpty() && !detailsBean.getUserMobile().trim().equalsIgnoreCase("0")){
	        	if(detailsBean.getUserMobileIsd() != null && !detailsBean.getUserMobileIsd().isEmpty() && ProcessConstants.INDIA_ISD_CODE.equalsIgnoreCase(detailsBean.getUserMobileIsd())){
        			//smsToUserResponseStatus = sendSms(detailsBean.getUserMobile(),userCouponMsg);
        			smsToUserResponseStatus = sendSms(detailsBean.getUserMobile(),userCouponMsg);
	        	} else if(detailsBean.getUserMobileIsd() != null && !detailsBean.getUserMobileIsd().isEmpty() && !ProcessConstants.INDIA_ISD_CODE.equalsIgnoreCase(detailsBean.getUserMobileIsd())){
	        		//International Number API inclusion
	        		smsToUserResponseStatus = sendInternationalSms(detailsBean.getUserMobile(),userCouponMsg,detailsBean.getUserMobileIsd());
	        	}
	        }
	        if(detailsBean != null && detailsBean.getDeveloperLeadSMSMobile() != null && !detailsBean.getDeveloperLeadSMSMobile().isEmpty()){
	        	//smsToDeveloperResponseStatus = sendSms(detailsBean.getDeveloperLeadSMSMobile(),developerCouponMsg);
	        	smsToDeveloperResponseStatus = sendSms(detailsBean.getDeveloperLeadSMSMobile(),developerCouponMsg);
	        }
	        prepareStatusRemarksForCommunication(mailToUserResponseStatus, mailToDeveloperResponseStatus,smsToUserResponseStatus,smsToDeveloperResponseStatus,detailsBean);
	        if(ProcessConstants.IS_CRM_ENABLED_IN_GOHF){
	        	dataPushedInBuilderCRM = pushCRMLogInDeveloperSystem(detailsBean);
	        }
	        if(dataPushedInBuilderCRM){
	        	System.out.println("Data successfully pushed into builder "+detailsBean.getDeveloperCompanyName()+" CRM system."+" Data: "+detailsBean);
	        }
	        couponOrderDetailsListAftrComm.add(detailsBean);
	        
        }
    }
	
	private final class ThreadWorkerForBooking implements Runnable {
        private OrderDetailsBean detailsBean;

        public ThreadWorkerForBooking(OrderDetailsBean odb) {
            this.detailsBean = odb;
        }

        public void run() {
        	NotificationResponseBean mailToUserResponseStatus = null;
        	NotificationResponseBean mailToDeveloperResponseStatus = null;
        	NotificationResponseBean smsToUserResponseStatus = null;
        	NotificationResponseBean smsToDeveloperResponseStatus = null;
        	boolean dataPushedInBuilderCRM = false;
        	String userNewBookingMsg = prepareNewBookingSMSForUser(detailsBean);
        	String developerNewBookingMsg = prepareNewBookingSMSForDeveloper(detailsBean);
	        //System.out.println("Thread " + Thread.currentThread().getName() + " starting");
	        System.out.println(" Inside worker thread "+detailsBean);
	        if(detailsBean != null && detailsBean.getUserEmail() != null && !detailsBean.getUserEmail().isEmpty()){
	        	mailToUserResponseStatus = prepareAndSendNewBookingMailToUser(detailsBean);
	        }
	        if(detailsBean != null && detailsBean.getDeveloperEmail() != null && !detailsBean.getDeveloperEmail().isEmpty()){
	        	mailToDeveloperResponseStatus = prepareAndSendNewBookingMailToDeveloper(detailsBean);
	        }
	        
	        if(detailsBean != null && detailsBean.getUserMobile() != null && !detailsBean.getUserMobile().isEmpty() && !detailsBean.getUserMobile().trim().equalsIgnoreCase("0")){
	        	if(detailsBean.getUserMobileIsd() != null && !detailsBean.getUserMobileIsd().isEmpty() && ProcessConstants.INDIA_ISD_CODE.equalsIgnoreCase(detailsBean.getUserMobileIsd())){
	        		//smsToUserResponseStatus = sendSms(detailsBean.getUserMobile(),userNewBookingMsg);
	        		smsToUserResponseStatus = sendSms(detailsBean.getUserMobile(),userNewBookingMsg);
	        	} else if(detailsBean.getUserMobileIsd() != null && !detailsBean.getUserMobileIsd().isEmpty() && !ProcessConstants.INDIA_ISD_CODE.equalsIgnoreCase(detailsBean.getUserMobileIsd())){
	        		//Interntional Number API inclusion
	        		smsToUserResponseStatus = sendInternationalSms(detailsBean.getUserMobile(),userNewBookingMsg,detailsBean.getUserMobileIsd());
	        	}
	        }
	        if(detailsBean != null && detailsBean.getDeveloperLeadSMSMobile() != null && !detailsBean.getDeveloperLeadSMSMobile().isEmpty()){
	        	//smsToDeveloperResponseStatus = sendSms(detailsBean.getDeveloperLeadSMSMobile(),developerNewBookingMsg);
	        	smsToDeveloperResponseStatus = sendSms(detailsBean.getDeveloperLeadSMSMobile(),developerNewBookingMsg);
	        }
	        prepareStatusRemarksForCommunication(mailToUserResponseStatus, mailToDeveloperResponseStatus,smsToUserResponseStatus,smsToDeveloperResponseStatus,detailsBean);
	        if(ProcessConstants.IS_CRM_ENABLED_IN_GOHF){
	        	dataPushedInBuilderCRM = pushCRMLogInDeveloperSystem(detailsBean);
	        }
	        if(dataPushedInBuilderCRM){
	        	System.out.println("Data successfully pushed into builder "+detailsBean.getDeveloperCompanyName()+" CRM system."+" Data: "+detailsBean);
	        }
	        bookingOrderDetailsListAftrComm.add(detailsBean);
	        
        }
    }
	
	private final class ThreadWorker implements Runnable {
        private OrderDetailsBean detailsBean;

        public ThreadWorker(OrderDetailsBean odb) {
            this.detailsBean = odb;
        }

        public void run() {
        	boolean dataPushedInBuilderCRM = false;
        	NotificationResponseBean mailToUserResponseStatus = null;
        	NotificationResponseBean mailToDeveloperResponseStatus = null;
        	NotificationResponseBean smsToUserResponseStatus = null;
        	NotificationResponseBean smsToDeveloperResponseStatus = null;
        	try{
        	String userContactLeadsMsg = prepareContactLeadsSMSForUser(detailsBean);
        	String developerContactLeadsMsg = prepareContactLeadsSMSForDeveloper(detailsBean);
	        //System.out.println("Thread " + Thread.currentThread().getName() + " starting");
	        System.out.println(" Inside worker thread "+detailsBean);
	        
	        if(detailsBean != null && detailsBean.getUserEmail() != null && !detailsBean.getUserEmail().isEmpty()){
	        	mailToUserResponseStatus = prepareAndSendContactLeadsMailToUser(detailsBean);
	        }
	        if(detailsBean != null && detailsBean.getDeveloperEmail() != null && !detailsBean.getDeveloperEmail().isEmpty() && ((detailsBean.getUserEmail() != null && !detailsBean.getUserEmail().isEmpty()) || (detailsBean.getUserMobile() != null && !detailsBean.getUserMobile().isEmpty() && !detailsBean.getUserMobile().trim().equalsIgnoreCase("0")))){
	        	mailToDeveloperResponseStatus = prepareAndSendContactLeadsMailToDeveloper(detailsBean);
	        }
	        
	        if(detailsBean != null && detailsBean.getUserMobile() != null && !detailsBean.getUserMobile().isEmpty() && !detailsBean.getUserMobile().trim().equalsIgnoreCase("0")){
	        	if(detailsBean.getUserMobileIsd() != null && !detailsBean.getUserMobileIsd().isEmpty() && ProcessConstants.INDIA_ISD_CODE.equalsIgnoreCase(detailsBean.getUserMobileIsd())){
	        		//smsToUserResponseStatus = sendSms(detailsBean.getUserMobile(),userContactLeadsMsg);
	        		smsToUserResponseStatus = sendSms(detailsBean.getUserMobile(),userContactLeadsMsg);
	        	} else if(detailsBean.getUserMobileIsd() != null && !detailsBean.getUserMobileIsd().isEmpty() && !ProcessConstants.INDIA_ISD_CODE.equalsIgnoreCase(detailsBean.getUserMobileIsd())){
	        		//Interntional Number API inclusion
	        		smsToUserResponseStatus = sendInternationalSms(detailsBean.getUserMobile(),userContactLeadsMsg,detailsBean.getUserMobileIsd());
	        	}
	        }
        	if(detailsBean != null && detailsBean.getDeveloperLeadSMSMobile() != null && !detailsBean.getDeveloperLeadSMSMobile().isEmpty() && ((detailsBean.getUserEmail() != null && !detailsBean.getUserEmail().isEmpty()) || (detailsBean.getUserMobile() != null && !detailsBean.getUserMobile().isEmpty() && !detailsBean.getUserMobile().trim().equalsIgnoreCase("0")))){
        		//smsToDeveloperResponseStatus = sendSms(detailsBean.getDeveloperLeadSMSMobile(),developerContactLeadsMsg);
        		smsToDeveloperResponseStatus = sendSms(detailsBean.getDeveloperLeadSMSMobile(),developerContactLeadsMsg);
			}
	        
        	prepareStatusRemarksForCommunication(mailToUserResponseStatus, mailToDeveloperResponseStatus,smsToUserResponseStatus,smsToDeveloperResponseStatus,detailsBean);
	        
	        if(ProcessConstants.IS_CRM_ENABLED_IN_GOHF){
	        	dataPushedInBuilderCRM = pushCRMLogInDeveloperSystem(detailsBean);
	        }
	        if(dataPushedInBuilderCRM){
	        	System.out.println("Data successfully pushed into builder "+detailsBean.getDeveloperCompanyName()+" CRM system."+" Data: "+detailsBean);
	        }
	        if(detailsBean != null && detailsBean.getOrderCommStatus() != null && !detailsBean.getOrderCommStatus().isEmpty()){
	        	contactOrderDetailsListAftrComm.add(detailsBean);
	        }
        	}catch(Exception e){
        		e.printStackTrace();
        		System.out.println(e.getMessage());
        	}
	        
        }
    }
	
	private void prepareStatusRemarksForCommunication(NotificationResponseBean mailToUserResponseStatus, NotificationResponseBean mailToDeveloperResponseStatus,
	NotificationResponseBean smsToUserResponseStatus, NotificationResponseBean smsToDeveloperResponseStatus, OrderDetailsBean detailsBean){
		String statusField = null;
		String mailToUserRemarks = null;
		if(mailToUserResponseStatus != null){
			mailToUserRemarks = mailToUserResponseStatus.getResponseRemarks();
		}
		String smsToUserRemarks = null;
		if(smsToUserResponseStatus != null){
			smsToUserRemarks = smsToUserResponseStatus.getResponseRemarks();
		}
		String mailToDeveloperRemarks = null;
		if(mailToDeveloperResponseStatus != null){
			mailToDeveloperRemarks = mailToDeveloperResponseStatus.getResponseRemarks();
		}
		String smsToDeveloperRemarks = null;
		if(smsToDeveloperResponseStatus != null){
			smsToDeveloperRemarks = smsToDeveloperResponseStatus.getResponseRemarks();
		}
        if(mailToUserResponseStatus != null && mailToUserResponseStatus.isSuccess() && smsToUserResponseStatus != null && smsToUserResponseStatus.isSuccess()){
        	statusField = ProcessConstants.BOTH_MAILER_SMS_DELIVERED;
        } else if(mailToUserResponseStatus != null && mailToUserResponseStatus.isSuccess()){
        	statusField = ProcessConstants.ONLY_MAIL_DELIVERED;
        } else if(smsToUserResponseStatus != null && smsToUserResponseStatus.isSuccess()){
        	statusField = ProcessConstants.ONLY_SMS_DELIVERED;
        } else if(mailToUserResponseStatus != null && !mailToUserResponseStatus.isSuccess() && smsToUserResponseStatus != null &&  !smsToUserResponseStatus.isSuccess()){
        	statusField = ProcessConstants.MAILER_SMS_FAILED;
        }
        detailsBean.setOrderCommStatus(statusField);
        detailsBean.setOrderCommStatusRemarks("Mail To User:"+mailToUserRemarks+" SMS to User:"+smsToUserRemarks);
        detailsBean.setDeveloperCommStatusRemarks("Mail To Developer:"+mailToDeveloperRemarks+" SMS to Developer:"+smsToDeveloperRemarks);
        System.out.println("Inside prepareStatusRemarksForCommunication detailsBean "+detailsBean);
	}
	
	private String getUserDetails(OrderDetailsBean detailsBean){
		StringBuffer temp = new StringBuffer();
		boolean mobileExists = false;
		if(detailsBean != null){
			if(detailsBean.getUserMobile() != null && !detailsBean.getUserMobile().isEmpty() && !detailsBean.getUserMobile().trim().equalsIgnoreCase("0")){
				temp.append("please call +"+detailsBean.getUserMobileIsd()+"-"+detailsBean.getUserMobile());
				mobileExists = true;
			}
			if(!mobileExists && detailsBean.getUserEmail() != null && !detailsBean.getUserEmail().isEmpty()){
				temp.append("please contact "+detailsBean.getUserEmail());
			}
		}
		return temp.toString();
	}
	
	private String prepareContactLeadsSMSForUser(OrderDetailsBean detailsBean){
		StringBuffer userSMS = new StringBuffer("Thanks for expressing your interest in "+detailsBean.getProjectTitle()+", "+detailsBean.getLocation()+".");
		if(detailsBean != null && detailsBean.getUnitPrice() != null && !detailsBean.getUnitPrice().isEmpty()){
			userSMS.append(" Price: "+detailsBean.getUnitPrice());
		}
		if(detailsBean.getOfferApplied() != null && !detailsBean.getOfferApplied().isEmpty()){
			userSMS.append(" Offer: "+detailsBean.getOfferApplied()+".");
		}
		userSMS.append("For more details, please contact "+detailsBean.getDeveloperName()+", "+detailsBean.getDeveloperDisplayMobile()+", GOHF.magicbricks.com");
    	return userSMS.toString();
	}
	
	private String prepareContactLeadsSMSForDeveloper(OrderDetailsBean detailsBean){
		StringBuffer developerSMS = new StringBuffer(detailsBean.getUserName()+" expressed interest in "+detailsBean.getProjectTitle()+", in "+detailsBean.getLocation()+" on GOHF.Magicbricks.com.");
		developerSMS.append("To get in touch, "+getUserDetails(detailsBean));
    	return developerSMS.toString();
	}
	
	private String prepareNewBookingSMSForUser(OrderDetailsBean detailsBean){
		StringBuffer userSMS = new StringBuffer();
		if(ProcessConstants.APARTMENT.equalsIgnoreCase(detailsBean.getPropertyType())){
			userSMS.append("Congratulations! You have successfully booked "+detailsBean.getUnitBHkType()+" "+detailsBean.getUnitArea());
		} else if(ProcessConstants.PLOT.equalsIgnoreCase(detailsBean.getPropertyType())){
			userSMS.append("Congratulations! You have successfully booked plot "+detailsBean.getPlotArea()+" "+detailsBean.getPlotAreaUnit());
		} else if(ProcessConstants.VILLA.equalsIgnoreCase(detailsBean.getPropertyType())){
			//What will be Area and Unit in case of Villa
			userSMS.append("Congratulations! You have successfully booked villa "+detailsBean.getCoveredArea()+" "+detailsBean.getCoveredAraUnit());
		} else if(ProcessConstants.OFFICE_SPACE.equalsIgnoreCase(detailsBean.getPropertyType())){
			//What will be Area and Unit in case of Office Space
			userSMS.append("Congratulations! You have successfully booked office space "+detailsBean.getCoveredArea()+" "+detailsBean.getCoveredAraUnit());
		}
		userSMS.append(" in "+detailsBean.getProjectTitle()+".");
    	userSMS.append(" Price Rs "+detailsBean.getUnitPrice()+", Booking Amt Rs "+detailsBean.getBookingAmount()+", Booking ID "+detailsBean.getOrderNo()+"  For more details, please contact "+detailsBean.getDeveloperName()+", "+detailsBean.getDeveloperCompanyName()+", "+detailsBean.getDeveloperDisplayMobile()+"  GOHF.magicbricks.com");
    	return userSMS.toString();
	}
	
	private String prepareNewBookingSMSForDeveloper(OrderDetailsBean detailsBean){
		StringBuffer developerSMS = new StringBuffer();
		if(ProcessConstants.APARTMENT.equalsIgnoreCase(detailsBean.getPropertyType())){
			developerSMS.append(detailsBean.getUserName()+" has booked "+detailsBean.getUnitBHkType()+" "+detailsBean.getUnitArea());
		} else if(ProcessConstants.PLOT.equalsIgnoreCase(detailsBean.getPropertyType())){
			developerSMS.append(detailsBean.getUserName()+" has booked plot "+detailsBean.getPlotArea()+" "+detailsBean.getPlotAreaUnit()+")");
		} else if(ProcessConstants.VILLA.equalsIgnoreCase(detailsBean.getPropertyType())){
			//What will be Area and Unit in case of Villa
			developerSMS.append(detailsBean.getUserName()+" has booked villa "+detailsBean.getCoveredArea()+" "+detailsBean.getCoveredAraUnit()+")");
		} else if(ProcessConstants.OFFICE_SPACE.equalsIgnoreCase(detailsBean.getPropertyType())){
			//What will be Area and Unit in case of Office Space
			developerSMS.append(detailsBean.getUserName()+" has booked office space "+detailsBean.getCoveredArea()+" "+detailsBean.getCoveredAraUnit()+")");
		}
		developerSMS.append(" in "+detailsBean.getProjectTitle()+" @ Price Rs "+detailsBean.getUnitPrice()+", Booking Amt Rs "+detailsBean.getBookingAmount()+".");
		developerSMS.append("To contact the buyer, "+getUserDetails(detailsBean)+" GOHF.magicbricks.com");
    	return developerSMS.toString();
	}
	
	//No Details for the user on coupon applicable on Plot or Flat or Villa ?
	private String prepareCouponSMSForUser(OrderDetailsBean detailsBean){
		StringBuffer userSMS = new StringBuffer();
		
		if(ProcessConstants.FREEBIE_OFFER_TYPE.equalsIgnoreCase(detailsBean.getOfferType())){
			userSMS.append("Congratulations! Here is the coupon to avail "+detailsBean.getOfferApplied()+" in "+detailsBean.getProjectTitle()+" Coupon code  "+detailsBean.getOrderNo());
		} else if(ProcessConstants.DISCOUNTED_PRICE_OFFER_TYPE.equalsIgnoreCase(detailsBean.getOfferType())){
			userSMS.append("Congratulations! Here is the coupon to avail discounted price Rs "+detailsBean.getOfferPrice()+" in "+detailsBean.getProjectTitle()+" Coupon code  "+detailsBean.getOrderNo());
		}
		
		userSMS.append("  For more details, please contact "+detailsBean.getDeveloperName()+", "+detailsBean.getDeveloperCompanyName()+", "+detailsBean.getDeveloperDisplayMobile()+" GOHF.magicbricks.com");
    	return userSMS.toString();
	}
	
	private String prepareCouponSMSForDeveloper(OrderDetailsBean detailsBean){
		StringBuffer developerSMS = new StringBuffer("Hi "+detailsBean.getDeveloperName()+", ");
		
		if(ProcessConstants.APARTMENT.equalsIgnoreCase(detailsBean.getPropertyType())){
			developerSMS.append(detailsBean.getUserName()+" has taken a coupon for "+detailsBean.getUnitBHkType()+" "+detailsBean.getUnitArea());
		} else if(ProcessConstants.PLOT.equalsIgnoreCase(detailsBean.getPropertyType())){
			developerSMS.append(detailsBean.getUserName()+" has taken a coupon for "+detailsBean.getPlotArea()+" "+detailsBean.getPlotAreaUnit()+")");
		} else if(ProcessConstants.VILLA.equalsIgnoreCase(detailsBean.getPropertyType())){
			//What will be Area and Unit in case of Villa
			developerSMS.append(detailsBean.getUserName()+" has taken a coupon for "+detailsBean.getCoveredArea()+" "+detailsBean.getCoveredAraUnit()+")");
		} else if(ProcessConstants.OFFICE_SPACE.equalsIgnoreCase(detailsBean.getPropertyType())){
			//What will be Area and Unit in case of Office Space
			developerSMS.append(detailsBean.getUserName()+" has taken a coupon for "+detailsBean.getCoveredArea()+" "+detailsBean.getCoveredAraUnit()+")");
		}
		developerSMS.append(" in "+detailsBean.getProjectTitle()+" @ Price Rs "+detailsBean.getUnitPrice()+", Coupon Code  "+detailsBean.getOrderNo()+".");
		developerSMS.append("To contact the buyer, "+getUserDetails(detailsBean)+", GOHF.magicbricks.com");
    	return developerSMS.toString();
	}
	private void SendMailerSMSForContactLeads() {
		
		long startTime = System.currentTimeMillis();
		List<OrderDetailsBean> ordersList = null;
		try {
			ordersList = (ArrayList<OrderDetailsBean>) gohfDAO.findNewContactLeads(dataSource.getConnection());
			System.out.println(" Inside SendUserMailerForContactLeads. Orders List size "+ordersList.size());
		} catch (Exception e) {
			System.out.println(" Exception while fetching new contact leads "+e.getMessage());
			e.printStackTrace();
		}
		
		for(OrderDetailsBean odb : ordersList){
			executor.execute(new ThreadWorker(odb));
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("\nFinished all threads for new contact leads");
		System.out.println("Total execution time "+(System.currentTimeMillis()-startTime));
		
	}
	
	private void SendMailerSMSForNewBookings(){

		long startTime = System.currentTimeMillis();
		List<OrderDetailsBean> ordersList = null;
		try {
			ordersList = (ArrayList<OrderDetailsBean>) gohfDAO.getNewBookingsData(dataSource.getConnection());
			System.out.println(" Inside SendUserMailerForNewBookings. Orders List size "+ordersList.size());
		} catch (Exception e) {
			System.out.println(" Exception while fetching new booking list "+e.getMessage());
			e.printStackTrace();
		}
		
		for(OrderDetailsBean odb : ordersList){
			executor.execute(new ThreadWorkerForBooking(odb));
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("\nFinished all threads for New Bookings");
		System.out.println("Total execution time "+(System.currentTimeMillis()-startTime));
	
	}
	
	private void SendMailerSMSForCoupon(){

		long startTime = System.currentTimeMillis();
		List<OrderDetailsBean> ordersList = null;
		try {
			ordersList = (ArrayList<OrderDetailsBean>) gohfDAO.getCouponsData(dataSource.getConnection());
			System.out.println(" Inside SendMailerSMSForCoupon. Orders List size "+ordersList.size());
		} catch (Exception e) {
			System.out.println(" Exception while fetching new booking list "+e.getMessage());
			e.printStackTrace();
		}
		
		for(OrderDetailsBean odb : ordersList){
			executor.execute(new ThreadWorkerForCoupon(odb));
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("\nFinished all threads for Coupon");
		System.out.println("Total execution time "+(System.currentTimeMillis()-startTime));
	
	}
	
	private boolean pushCRMLogInDeveloperSystem(OrderDetailsBean odb){
		String developerCompanyName = odb.getDeveloperCompanyName();
		String tempUrlParameter = null; 
		boolean flag = false;
		if(developerCompanyName != null && !developerCompanyName.isEmpty()){
			if(ProcessConstants.IS_CRM_ENABLED(developerCompanyName)){
				if("SUPERTECH".equalsIgnoreCase(developerCompanyName)){
					try{
						URL url = new URL(ProcessConstants.SUPERTECH_CRM_URL);
						System.out.println("HitURl " + url+"&"+tempUrlParameter);
						HttpClient client = new HttpClient();
		
						tempUrlParameter = prepareTempURLParameters(odb);
						//tempUrlParameter.app = "&"+tempUrlParameter;
						
						// Create a method instance.
					    GetMethod method = new GetMethod(url+tempUrlParameter);
					    
					    // Provide custom retry handler is necessary
					    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					    		new DefaultHttpMethodRetryHandler(3, false));
		
					    try {
					      // Execute the method.
					      int statusCode = client.executeMethod(method);
		
					      if (statusCode != HttpStatus.SC_OK) {
					        System.err.println("Method failed: " + method.getStatusLine());
					      }
		
					      // Read the response body.
					      byte[] responseBody = method.getResponseBody();
		
					      // Deal with the response.
					      // Use caution: ensure correct character encoding and is not binary data
					      System.out.println(new String(responseBody));
		
					    } catch (HttpException e) {
					    	System.err.println("Fatal protocol violation: " + e.getMessage());
					    	e.printStackTrace();
					    } catch (IOException e) {
					    	System.err.println("Fatal transport error: " + e.getMessage());
					    	e.printStackTrace();
					    } finally {
					    	// Release the connection.
					    	method.releaseConnection();
					    }
					}catch (Exception e){
						e.printStackTrace();
						return false;
					}
					flag=false;
				}
				if(flag){
					try {
						URL url = new URL(ProcessConstants.SUPERTECH_CRM_URL);
						System.out.println("HitURl " + url+"&"+tempUrlParameter);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setDoOutput(true);
						connection.setDoInput(true);
						connection.setInstanceFollowRedirects(false);
						connection.setRequestMethod("POST");
						//Properties systemProperties = System.getProperties();
						//System.out.println("final parameter " + tempUrlParameter);
						//systemProperties.setProperty("http.proxyHost","dcpfproxy.timesgroup.com");
					  	//systemProperties.setProperty("http.proxyPort", "80");
						connection.setRequestProperty("encoding", "UTF-8");
						//connection.setRequestProperty("charset", "utf-8");
						connection.setRequestProperty("Content-Length",	""+ Integer.toString(tempUrlParameter.getBytes().length));
						connection.setUseCaches(false);

						DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
						wr.writeBytes(tempUrlParameter);
						wr.flush();
						BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String line;
						while ((line = rd.readLine()) != null) {
							System.out.println(line);
						}
						wr.close();
						rd.close();
						connection.disconnect();
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private String formatNextSteps(String nextSteps){
		if(nextSteps != null && !nextSteps.isEmpty()){
			nextSteps = nextSteps.replaceAll("<p>", "<li style=\"color: #d74434;  line-height: 23px;\"><span style=\"color: #333; font-size:12px;\">");
			nextSteps = nextSteps.replaceAll("</p>","</span></li>");
		}
		return nextSteps;
	}
	
	private String formatTermsAndConditions(String textTC){
		if(textTC != null && !textTC.isEmpty()){
            if(textTC.indexOf("<p>")>=0){
                textTC = textTC.substring(0, textTC.indexOf("<p>"));
            }
			//textTC = textTC.substring(0, textTC.indexOf("<p>"));
			textTC = textTC.replaceAll("<li>", "<li style=\"color: #d74434;  line-height: 23px;\"><span style=\"color: #333; font-size:12px;\">");
			textTC = textTC.replaceAll("</li>","</span></li>");
			textTC = textTC.replaceAll("<ul class=\"termsList\">", "");
			textTC = textTC.replaceAll("</ul>", "");
		}
		return textTC;
	}
	
	private String prepareTempURLParameters(OrderDetailsBean odb){
		StringBuffer temp = new StringBuffer();
		temp.append("&userName="+odb.getUserName());
		temp.append("&userMobile="+odb.getUserMobile());
		temp.append("&userEmail="+odb.getUserEmail());
		//Add more fields as per CRM integration details from Builder 
		return temp.toString();
	}
	
	public void setMailservice(MailService mailservice) {
		this.mailservice = mailservice;
	}
	public void setSmsservice(SmsService smsservice) {
		this.smsservice = smsservice;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	private void exitBatchProcess(){
		System.exit(0);
	}
}
