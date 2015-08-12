package com.mbbatch.sms;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import com.mbbatch.bean.NotificationResponseBean;


public class SmsService 
{
	public static String protocol = "smtp";
	public static boolean auth = false;
	public boolean verbose = false;
	private static HttpClient client = null;
	private static MultiThreadedHttpConnectionManager connectionManager;
	private final static int RES_CODE = 200; // for new
	private static final int CONNECTION_TIMEOUT = 25000;
	private static final int READ_TIMEOUT = 20000;
	private static SimpleDateFormat ft = new SimpleDateFormat("dd.MMM  hh:mm:ss a '->'");
	static {
		connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setDefaultMaxConnectionsPerHost(60);
		params.setMaxTotalConnections(100);
		params.setConnectionTimeout(CONNECTION_TIMEOUT);
		params.setSoTimeout(READ_TIMEOUT);
		connectionManager.setParams(params);
		client = new HttpClient(connectionManager);
	}

	public NotificationResponseBean sendSms(String mobileNumber, String message)
	{
		NotificationResponseBean response = new NotificationResponseBean();
		long l1 = System.currentTimeMillis();
		String mobileNumberWith91 = "";
		/*String username = "tbsl2";
		String password = "tbsl2";
		String appid = "tbsl2";
		String messagetext = URLEncoder.encode(message);
		String masking = "MagicBrk";
		*/

		String username = "magicbricks_otp";
		String password = "magicbricks@!)!";
		String senderId = "MGCBRK";
		String messagetext = URLEncoder.encode(message);
		PostMethod method = null;

		Date now = Calendar.getInstance().getTime();
		// ---
		mobileNumber = getCustomMobileNumber(mobileNumber);
		try {
		if (mobileNumber.trim().length() == 12
		&& (mobileNumber.trim().charAt(0) == '9' && mobileNumber
		.trim().charAt(1) == '1')) {
			mobileNumberWith91 = mobileNumber.trim();
		} else if (mobileNumber.trim().length() == 11 && (mobileNumber.trim().charAt(0) == '0')) {
			mobileNumberWith91 = "91" + mobileNumber.trim().substring(1);
		} else if (mobileNumber.trim().length() == 10) {
			mobileNumberWith91 = "91" + mobileNumber.trim();
		}
		if (mobileNumberWith91 != null){
		/*String mobileNumberWith91 = "";
		String messagetext= message;
		PostMethod method = null;
		long l1 = System.currentTimeMillis();
		try{
			if(mobileNumber != null && !"".equals(mobileNumber.trim()) && mobileNumber.trim().length()>=10 ){
				int len=mobileNumber.trim().length();
				mobileNumberWith91="91"+mobileNumber.substring(len-10);
			}
			
			if (mobileNumberWith91 != null){					
				// for Normal messages
				String urlStr = "http://activeconnect.in/urlpush/CallURLPush.aspx?auth=%2Be7TxI4lKH87RBTK3sok0vhknUt1fh3V&mobilenumber="
					+ mobileNumberWith91
					+ "&messageText="
					+ messagetext
					+ "&IsUnicode=N&tid=UyuJWBqx6zE%3D&masking=MGCBRK&clientMsgID=&dlrURL=&IsPriority=";*/
				
				String urlStr = "http://activeconnect.in/httpurlpush1/CallURLPush.aspx" +
				"?auth=%2Be7TxI4lKH8t3M5hlD7TJEbsxtQrsPuLZFuQ8v%2BG2dE%3D&mobilenumber=" +
				""+mobileNumberWith91+"&messageText="+messagetext+"&IsUnicode=N&tid=ttOB4rcLVuU%3D&masking" +
				"=MGCBRK&clientMsgID=&dlrURL=&IsPriority=1";
				urlStr = urlStr.replaceAll(" ", "%20");
				
				method = new PostMethod(urlStr);

				int statusCode = client.executeMethod(method);
				System.out.println(statusCode);
				if (statusCode != HttpStatus.SC_OK) {
					System.out.println("WRONG RESPONSE.. "
							+ urlStr + " ]  " + method.getStatusLine());
				}

				System.out.println(ft.format(new Date()) + "   " + mobileNumberWith91 + "  Time To Send... " + (System.currentTimeMillis() - l1) / 1000 + " Sec  ");
			}
		} catch (Exception e) {
			System.out.println(ft.format(new Date()) + " Exception ..." + mobileNumberWith91 + "   " + e.toString());			
			try{
				int statusCode = client.executeMethod(method);
				System.out.println(ft.format(new Date()) + "   " + mobileNumberWith91 + "  Time To Send... " + (System.currentTimeMillis() - l1) / 1000 + " Sec  ");
				if (statusCode != HttpStatus.SC_OK) {
					System.out.println("WRONG RESPONSE.." + method.getStatusLine());
				}
			}catch (Exception e1) {
				System.out.println(ft.format(new Date()) + " Exception in resend ..." + mobileNumberWith91 + "   "+ e.toString());			
			}
			
			e.printStackTrace();
			response.setResponseRemarks(e.getMessage());
			response.setSuccess(false);
			return response;
		} finally {
			try {
				method.releaseConnection();
			} catch (Exception ex) {
				System.out.println(" Exception while closing connection ...: "
						+ ex.toString());
			}
		}
		response.setResponseRemarks("SMS sucessfully delivered to "+mobileNumber);
		response.setSuccess(true);
		return response;
	}
	
	public NotificationResponseBean sendPriorityMessageProcessInternational(String mobileNumber,
			String message, String mobileCode) {
		NotificationResponseBean response = new NotificationResponseBean();
		String mobileNumberWith91 = "";
		String username = "magicbricks_otp";
		String password = "magicbricks@!)!";
		String senderId = "MGCBRK";
		String messagetext = URLEncoder.encode(message);
		PostMethod method = null;
		mobileNumber = getCustomMobileNumber(mobileNumber);
		long l1 = System.currentTimeMillis();
		try {
			mobileNumberWith91 = mobileNumber.trim();
			mobileNumberWith91 = mobileCode + mobileNumberWith91;
			mobileNumberWith91 = mobileNumberWith91.replace("+", "");
			String urlStr = "http://activeconnect.in/MBInternational/CallURLPush.aspx?auth=S0zDrZUsAPrb5bKZFWMkpY3MIg1EnVnQKFfq15kMX2k%" +
							"3D&mobilenumber="+mobileNumberWith91+"&messageText="+messagetext+"&IsUnicode=N&tid=cO0HinBxaiM%3D&masking=MGCBRK&clientMsgID=&dlrURL=&IsPriority=";
			urlStr = urlStr.replaceAll(" ", "%20");
			method = new PostMethod(urlStr);

			try {
				HttpClient client = new HttpClient();
				int statusCode = client.executeMethod(method);

				System.out.println("SendPrioritySmsOnContact ***********" + urlStr + "::" + statusCode + "::" + method.getResponseBodyAsString());
				// Get data as a String
				// OR as a byte array
				byte[] res = method.getResponseBody();
				// write to file
				// FileOutputStream fos= new FileOutputStream("donepage.html");
				// fos.write(res);
				// release connection
				System.out.println(ft.format(new Date()) + "   " + mobileNumberWith91 + "  Time To Send... " + (System.currentTimeMillis() - l1) / 1000 + " Sec  ");
				method.releaseConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println(ft.format(new Date()) + " Exception ..." + mobileNumberWith91 + "   " + e.toString());			
			try{
				int statusCode = client.executeMethod(method);
				System.out.println(ft.format(new Date()) + "   " + mobileNumberWith91 + "  Time To Send... " + (System.currentTimeMillis() - l1) / 1000 + " Sec  ");
				if (statusCode != HttpStatus.SC_OK) {
					System.out.println("WRONG RESPONSE.." + method.getStatusLine());
				}
			}catch (Exception e1) {
				System.out.println(ft.format(new Date()) + " Exception in resend ..." + mobileNumberWith91 + "   "+ e.toString());			
			}
			
			e.printStackTrace();
			response.setResponseRemarks(e.getMessage());
			response.setSuccess(false);
			return response;
		} finally {
			try {
				method.releaseConnection();
			} catch (Exception ex) {
				System.out.println(this.getClass().getName() + " Exception while closing connection SMSSERVICE...: " + this.getClass().getName() + " method httpClient()" + ex);
			}
		}
		response.setResponseRemarks("SMS sucessfully delivered to "+mobileNumber);
		response.setSuccess(true);
		return response;
	}
	
	private String getCustomMobileNumber(String mobileNumber) {
		if (mobileNumber != null && mobileNumber.trim().length() > 0) {
			if (mobileNumber.toString().indexOf("/") != -1) {
				mobileNumber = mobileNumber.toString().substring(0,
						mobileNumber.toString().indexOf("/"));
			} else if (mobileNumber.toString().indexOf(",") != -1) {
				mobileNumber = mobileNumber.toString().substring(0,
						mobileNumber.toString().indexOf(","));
			} 
			mobileNumber = trim091MobileNumber(mobileNumber);
		}
		return mobileNumber;
	}
	
	public static String trim091MobileNumber(String sMobileNumParam) {

		String sMobileNum = sMobileNumParam.trim();
		String mobileNum = sMobileNum;

		int firstMobileIdx = sMobileNum.indexOf('/');
		if(firstMobileIdx > 0){
			mobileNum = sMobileNum.substring(0, firstMobileIdx);
			sMobileNum = sMobileNum.substring(firstMobileIdx);
			}else{
		sMobileNum = "";
		}
		int len = mobileNum.length();
		int indexOf0 = mobileNum.indexOf('0');
		if(len > 10){
			if (len == 13 && (mobileNum.indexOf("091") == 0) || mobileNum.indexOf("+91") == 0){
				mobileNum = mobileNum.substring(3, mobileNum.length());
			} else if (len == 12 && (mobileNum.indexOf("91") == 0)) { 
				mobileNum = mobileNum.substring(2, mobileNum.length());
			} else if (len == 11 && indexOf0 == 0) {
				mobileNum = mobileNum.substring(1, mobileNum.length());
			}
		}

		return mobileNum + sMobileNum;
	}
}
