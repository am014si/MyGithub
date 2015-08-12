package com.mbbatch.mailer;

import java.util.Map;
import java.util.Properties;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;
import com.mbbatch.bean.NotificationResponseBean;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.sun.mail.smtp.SMTPAddressFailedException;



public class MailService{

	//public static final String CUSTOMIZED_SMTP = "smtp.timesgroup.com"; 	//For local deployment
	public static final String CUSTOMIZED_SMTP = "pune.tbsl.in";
	public static final SendGrid sendgrid = new SendGrid("gohf", "Times@123");
	//public static final String CUSTOMIZED_SMTP = "mailsrvr";

	public static String protocol = "smtp";

	public static boolean auth = false;

	public static boolean verbose = false;

	private VelocityEngine velocityEngine = new VelocityEngine();

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

	public String getMessageContentVm(String vmfilename, Map obj){
		String result = null;
		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			velocityEngine.init(p);
			result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "VM/" + vmfilename, obj);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in velocity engine");
		}
		return null;
	}
	public NotificationResponseBean sendMail(String toemail, String fromemail,String fromName, String subject, String msgContent, boolean textFormat, String cc, String bcc){
		NotificationResponseBean response = new NotificationResponseBean();
		String[] bccArray = new String[] {bcc};
		System.out.println("Inside MailService sendMail method");
		try {
			
			//SendGrid sendgrid = new SendGrid("gohf", "Times@123");
			SendGrid.Email email = new SendGrid.Email();
		    email.addTo(toemail);
		    email.setFromName(fromName);
		    email.setFrom(fromemail);
		    email.setSubject(subject);
		    email.setHtml(msgContent);
		    email.setBcc(bccArray);
		    Long start = System.currentTimeMillis();
		    SendGrid.Response sgResponse = sendgrid.send(email);
		    System.out.println("Mail going transport "+ (System.currentTimeMillis() - start)+" Mail Response "+sgResponse.getMessage());
			response.setResponseRemarks("Mail was sent successfully to "+ toemail);
			response.setSuccess(true);
			return response;

		}
		catch (SendGridException e) {
			System.out.println("Exception during sending mail ::"+e);
			e.printStackTrace();
			response.setResponseRemarks(e.getMessage());
			response.setSuccess(false);
			return response;
	    }
		catch (Exception e){
			System.out.println("Exception during sending mail ::"+e);
			e.printStackTrace();
			if(e instanceof SMTPAddressFailedException){
				SMTPAddressFailedException ex = (SMTPAddressFailedException)e;
				System.out.println("Internet Address ::"+ex.getAddress());
			}
			response.setResponseRemarks(e.getMessage());
			response.setSuccess(false);
			return response;
		}
	}

	public NotificationResponseBean sendMail(String toemail, final String fromemail, final String fromName,
			final String subject, final String vmfilename, final Map obj, final boolean textFormat,
			final String cc, final String bcc) {
		System.out.println("Inside sendMail method");
		String msgContent = getMessageContentVm(vmfilename, obj);
		if(msgContent != null){
			
			return sendMail(toemail, fromemail, fromName,subject, msgContent, textFormat, cc, bcc);
		}else
			return new NotificationResponseBean("msgContent is Null", false);
		}

}
