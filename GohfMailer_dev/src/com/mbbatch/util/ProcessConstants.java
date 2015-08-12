package com.mbbatch.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProcessConstants {
	
	public final static String EXECUTION_BOOKING_ONLY = "BK";
	public final static String EXECUTION_CONTACT_ONLY = "CO";
	public final static String EXECUTION_COUPON_ONLY = "CP";
	
	public final static String ONLY_MAIL_DELIVERED = "M";
	public final static String ONLY_SMS_DELIVERED = "S";
	public final static String BOTH_MAILER_SMS_DELIVERED = "D";
	public final static String MAILER_SMS_FAILED = "F";
	
	public final static String ACTION_CONTACT = "C";
	public final static String ACTION_BOOKING = "B";
	public final static String ACTION_COUPON = "F";
	
	public final static String APARTMENT = "Apartment";
	public final static String VILLA = "Villa";
	public final static String PLOT = "Plot";
	public final static String OFFICE_SPACE = "Office Space";
	
	public final static String FREEBIE_OFFER_TYPE = "0";
	public final static String DISCOUNTED_PRICE_OFFER_TYPE = "1";
	
	public final static String INDIA_ISD_CODE="91";
	public final static String DEFAULT_ZERO = "0";
	public final static String COMMA = ",";
	//CRM Integration constants
	public final static boolean IS_CRM_ENABLED_IN_GOHF = false;
	public final static List<String> CRM_ENABLED_BUILDERS_LIST = new ArrayList<String>(){{ add("SUPERTECH");add("BUILDER1");add("BUILDER2"); }};
	
	//public final static boolean SUPERTECH = true;
	public final static String SUPERTECH_CRM_URL = "https://www.salesforce.com/servlet/servlet.WebToLead?encoding=UTF-8";
	public final static String USER_NAME = "userName";
	public final static String USER_MOBILE = "userMobile";
	public final static String USER_EMAIL = "userEmail";
	public final static String PROJECT_TITLE = "projectInterestedIn";
	public final static String BHK_TYPE = "bhkType";
	public final static String URL = "url";
	
	//public final static boolean BUILDER2_CRM_ENABLED = true;
	public final static String BUILDER2_CRM_URL = "https://www.salesforce.com/servlet/servlet.WebToLead?encoding=UTF-8";
	
	public static boolean IS_CRM_ENABLED(String developerName) {
		boolean crmEnabled = false;
		if(CRM_ENABLED_BUILDERS_LIST.contains(developerName)){
			crmEnabled = true;
		}
		return crmEnabled;
	}
	public static String formatCurrencyInWord(long number) {
		try {
			if (number == 0) {
				return ProcessConstants.DEFAULT_ZERO;
			} else if (number < 1000){
				return String.valueOf(number);
			}else if(number >= 10000000){
				// In Crore(s)
				long croresAmt = number/10000000;
				int lacsAmt = (int) (number%10000000);
				StringBuffer buffer = new StringBuffer(String.valueOf(croresAmt));
				buffer.append(".");
				String lacsString = String.valueOf(lacsAmt);
				if (!chkNull(lacsString)){
					if(lacsString.length() == 6){
						buffer.append(ProcessConstants.DEFAULT_ZERO+lacsString.substring(0,1));
					}else if(lacsString.length() == 7){
						buffer.append(lacsString.substring(0,2));
					}else{
						buffer.append(ProcessConstants.DEFAULT_ZERO);
					}
				} else {
					buffer.append(lacsString);
				}
				/*if(buffer.toString().equals("999999.0")) {
					buffer.replace(0, 11, "5+");
				}*/
				//buffer.append(" Cr");
				/*if(buffer.toString().equals("100.0 Cr")) {
					return "10+ Lacs";
				}*/
				//float val=Math.round(Float.parseFloat(buffer.toString())*10)/10f;
				return buffer.toString()+" Cr";
			}else if(number >= 100000 && number < 10000000){
				// In Lac(s)
				long lacsAmt = number/100000;
				int thousandAmt = (int) (number%100000);
				StringBuffer buffer = new StringBuffer(String.valueOf(lacsAmt));
				buffer.append(".");
				String thousandString = String.valueOf(thousandAmt);
				if (!chkNull(thousandString)){
					if(thousandString.length() == 4){
						buffer.append(ProcessConstants.DEFAULT_ZERO+thousandString.substring(0,1));
					}else if(thousandString.length() == 5){
						buffer.append(thousandString.substring(0,2));
					}else{
						buffer.append(ProcessConstants.DEFAULT_ZERO);
					}
				} else {
					buffer.append(thousandString);
				}
				float val=Math.round(Float.parseFloat(buffer.toString())*10)/10f;
				
				//buffer.append(" Lacs");
				return val+" Lac";
			} else {
				String numberString = String.valueOf(number);
				StringBuffer buffer = new StringBuffer(numberString);
				int length = buffer.length();
				length = length - 3;
				buffer.insert(length, ProcessConstants.COMMA);
				while (length > 2) {
					length = length - 2;
					buffer.insert(length, ProcessConstants.COMMA);
				}
				
				return buffer.toString();
			}
		} catch (Exception e) {
			//LOGGER.error("Exception in formatCurrencyInWord", e);
		}
		return number+"";
	}
	
	private static boolean chkNull(Object value){
		if(value == null){
			return true;
		}
		String strValue = null;
		if( value instanceof Integer){
			strValue = value.toString();
		} else if( value instanceof Long) {
			strValue = value.toString();
		} else if( value instanceof Double) {
			strValue = value.toString();
		} else if(value instanceof String) {
			strValue = value.toString();
		} else if(value instanceof List) {
			List list = (List) value;
			return list.isEmpty();
		} else if(value instanceof Map) {
			Map map = (Map) value;
			return map.isEmpty();
		}else if(value instanceof Set) {
			Set set = (Set) value;
			return set.isEmpty();
		}else if( value instanceof Float) {
			strValue = value.toString();
		}else{
			strValue = value.toString();
		}
		//strValue will never null in or condition...removed null check
		if(strValue==null || "undefined".equals(strValue.trim())|| "null".equals(strValue.trim()) || "-1".equals(strValue.trim()) || strValue.trim().length()==0){
			return true;
		}
		return false;

	}
}
