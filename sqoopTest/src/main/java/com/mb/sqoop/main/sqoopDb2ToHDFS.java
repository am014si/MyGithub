package com.mb.sqoop.main;

import java.text.DateFormatSymbols;
import java.util.List;
import org.apache.sqoop.client.SqoopClient;
import org.apache.sqoop.model.MConnection;
import org.apache.sqoop.model.MConnectionForms;
import org.apache.sqoop.model.MForm;
import org.apache.sqoop.model.MInput;
import org.apache.sqoop.model.MJob;
import org.apache.sqoop.model.MJobForms;
import org.apache.sqoop.model.MSubmission;
import org.apache.sqoop.submission.counter.Counter;
import org.apache.sqoop.submission.counter.CounterGroup;
import org.apache.sqoop.submission.counter.Counters;
import org.apache.sqoop.validation.Status;

public class sqoopDb2ToHDFS {
	private static String url = "http://10.150.200.78:12000/sqoop/";
	private static SqoopClient client = new SqoopClient(url); 
	private static void createConnection(){
		//Dummy connection object
		MConnection newCon = client.newConnection(1);
		//Get connection and framework forms. Set name for connection
		MConnectionForms conForms = newCon.getConnectorPart();
		MConnectionForms frameworkForms = newCon.getFrameworkPart();
		newCon.setName("MyConnection");
		//UAT DB properties
		//conForms.getStringInput("connection.connectionString").setValue("jdbc:db2://10.150.200.201:50000/mbprod");
		//conForms.getStringInput("connection.username").setValue("property");
		//conForms.getStringInput("connection.password").setValue("property");
		conForms.getStringInput("connection.connectionString").setValue("jdbc:db2://115.112.206.242:50000/mblivedb");
		conForms.getStringInput("connection.jdbcDriver").setValue("com.ibm.db2.jcc.DB2Driver");
		conForms.getStringInput("connection.username").setValue("property");
		conForms.getStringInput("connection.password").setValue("tpmbd@t@");
		frameworkForms.getIntegerInput("security.maxConnections").setValue(0);
		Status status  = client.createConnection(newCon);
		if(status.canProceed()) {
		 System.out.println("Created. New Connection ID : " +newCon.getPersistenceId());
		} else {
			 System.out.println("Check for status and forms error ");
		}
		printMessage(newCon.getConnectorPart().getForms());
		printMessage(newCon.getFrameworkPart().getForms());
	}

	//args[0]=year
	//args[1]=month
	public static void main(String[] args) {
		
		String year = "2014";
		String month = "4";
		String tplmtSql = "select lmtrfnum,lmtcndzip,lmtcndstate,lmtcndcountry,lmtcndcity,lmtname,lmtdisc,deleted,createdby,exfield1,exfield2,lmtisactive,longitude,latitude,lmtnearbyrfnum,lmtalias1,lmtalias2,lmtpdfavail,lmtlmtrfnum,lmtrestrict,lmtpopular,lmtdispname,lmtadvice,lmtiswap from tplmt lmt where ${CONDITIONS}";
		String tpcndSql = "select cndrfnum,cndcode,cnddesc,cndcndrfnum,cndfieldtype,cndgroup,cndseqnum,createdby,deleted,exfield1,exfield2,isactive from tpcnd cnd where ${CONDITIONS}";
		String tppmtSql = "select pmtrfnum,pmtubirfnum,pmtlisttype,pmtcndproptype,pmtlmtrfnum,pmtsaleprice,pmtrentprice,pmtislisted,pmtisactive,pmtmoderstatus,pmtmoderresult,deleted,pmtcndbedrms,pmtcndlisttype  from tppmt tppmt where year(tppmt.createdate) = "+year+" and month(tppmt.createdate) = "+month+" and ${CONDITIONS}";
		String tplrtSql = "select lrtrfnum,lrtcndrfnum,lrtpriceunit,lrtlmtrfnum,lrtisrent,lrtminprice,lrtmaxprice,lrfavgprice,deleted,createdby,lrtoldminprice,lrtoldmaxprice,lrtoldavgprice,lrtyeildprice  from tplrt lrt where year(lrt.modidate) = "+year+" and month(lrt.modidate) = "+month+" and ${CONDITIONS}";
		String tpsrecSql = "select srrfnum,srcatg,srcndcity,srlocality,srcndproptype,srcndbedrms,srcndplunit,srcndlistingposted,srubirfnum,srhitcnt,srresultcount,srresultids from tpsrec tpsrec where year(tpsrec.createdate) = "+year+" and month(tpsrec.createdate) = "+month+" and ${CONDITIONS}";
		
		MConnection newCon = client.getConnection(1);
		if(newCon == null){
			createConnection();
		}
		createJob(newCon.getPersistenceId(),tplmtSql, "lmtrfnum", "/reportingDB/LocalityMasterData");
		createJob(newCon.getPersistenceId(),tpcndSql, "cndrfnum", "/reportingDB/AllMasterData");
		createJob(newCon.getPersistenceId(),tppmtSql, "pmtrfnum", "/reportingDB/LocalityWiseListingData/"+year+"/"+new DateFormatSymbols().getMonths()[ Integer.valueOf(month)-1]);
		createJob(newCon.getPersistenceId(),tplrtSql, "lrtrfnum", "/reportingDB/LocalityWiseCapitalData/"+year+"/"+new DateFormatSymbols().getMonths()[ Integer.valueOf(month)-1]);
		createJob(newCon.getPersistenceId(),tpsrecSql, "srrfnum", "/reportingDB/LocalityWiseSearchesData/"+year+"/"+new DateFormatSymbols().getMonths()[ Integer.valueOf(month)-1]);
	}
	
	private static void createJob(long connPersistenceId, String sqlString, String partitionColumn, String outputDir){
		//Creating dummy job object
		MJob newjob = client.newJob(connPersistenceId, org.apache.sqoop.model.MJob.Type.IMPORT);
		MJobForms connectorForm = newjob.getConnectorPart();
		MJobForms frameworkForm = newjob.getFrameworkPart();

		newjob.setName("ImportJob");
		
		connectorForm.getStringInput("table.sql").setValue(sqlString);
		connectorForm.getStringInput("table.partitionColumn").setValue(partitionColumn);
		//Set boundary value only if required
		//connectorForm.getStringInput("table.boundaryQuery").setValue("SELECT MIN(SRRFNUM), MAX(SRRFNUM) FROM TPSREC tpsrec where date(tpsrec.createdate) between '2014-04-01' and '2014-06-30'");

		//Output configurations
		frameworkForm.getEnumInput("output.storageType").setValue("HDFS");
		frameworkForm.getEnumInput("output.outputFormat").setValue("TEXT_FILE");//Other option: SEQUENCE_FILE
		frameworkForm.getStringInput("output.outputDirectory").setValue(outputDir);

		//Job resources
		frameworkForm.getIntegerInput("throttling.extractors").setValue(20);
		frameworkForm.getIntegerInput("throttling.loaders").setValue(20);

		Status status = client.createJob(newjob);
		if(status.canProceed()) {
		 System.out.println("New Job ID: "+ newjob.getPersistenceId());
		 submitJob(newjob.getPersistenceId());
		} else {
		 System.out.println("Check for status and forms error ");
		}

		//Print errors or warnings
		printMessage(newjob.getConnectorPart().getForms());
		printMessage(newjob.getFrameworkPart().getForms());
	}
	
	private static void submitJob(long jobPersistenceId){
		//Job submission start
		MSubmission submission = client.startSubmission(jobPersistenceId);
		System.out.println("Status : " + submission.getStatus());
		if(submission.getStatus().isRunning() && submission.getProgress() != -1) {
		  System.out.println("Progress : " + String.format("%.2f %%", submission.getProgress() * 100));
		}
		System.out.println("Hadoop job id :" + submission.getExternalId());
		System.out.println("Job link : " + submission.getExternalLink());
		Counters counters = submission.getCounters();
		if(counters != null) {
		  System.out.println("Counters:");
		  for(CounterGroup group : counters) {
		    System.out.print("\t");
		    System.out.println(group.getName());
		    for(Counter counter : group) {
		      System.out.print("\t\t");
		      System.out.print(counter.getName());
		      System.out.print(": ");
		      System.out.println(counter.getValue());
		    }
		  }
		}
		if(submission.getExceptionInfo() != null) {
		  System.out.println("Exception info : " +submission.getExceptionInfo());
		}


		//Check job status
		MSubmission submission1 = client.getSubmissionStatus(1);
		if(submission1.getStatus().isRunning() && submission1.getProgress() != -1) {
		  System.out.println("Progress : " + String.format("%.2f %%", submission1.getProgress() * 100));
		}

		//Stop a running job
		//submission1.stopSubmission(jid);
	}
	
	private static void printMessage(List<MForm> formList) {
		  for(MForm form : formList) {
		    List<MInput<?>> inputlist = form.getInputs();
		    if (form.getValidationMessage() != null) {
		      System.out.println("Form message: " + form.getValidationMessage());
		    }
		    for (MInput minput : inputlist) {
		      if (minput.getValidationStatus() == Status.ACCEPTABLE) {
		        System.out.println("Warning:" + minput.getValidationMessage());
		      } else if (minput.getValidationStatus() == Status.UNACCEPTABLE) {
		        System.out.println("Error:" + minput.getValidationMessage());
		      }
		    }
		  }
	}
}
