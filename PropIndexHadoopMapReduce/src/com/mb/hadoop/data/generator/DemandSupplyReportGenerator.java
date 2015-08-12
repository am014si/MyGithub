package com.mb.hadoop.data.generator;

import java.net.URI;
import java.text.DateFormatSymbols;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.mb.hadoop.mapper.DemandSupplyReportGeneratorMapper;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.util.MongoConfigUtil;

public class DemandSupplyReportGenerator extends Configured implements Tool  {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		  int res = ToolRunner.run(conf, new DemandSupplyReportGenerator(), args);
		  System.exit(res);
	}
	
	//This Map Reduce job will take input Year and Month for which we want to generate the report.
	//args[0]=Input file path TPSREC Bedrooms Data <month> - Demand
	//args[1]=Input file path TPSREC Property Wise <month> - Demand
	//args[2]=Input file path TPSREC Budget wise <month> - Demand
	//args[3]=Input file path TPLMT Bedrooms Data <month> - Supply
	//args[4]=Input file path TPLMT Property Wise <month> - Supply
	//args[5]=Input file path TPLMT Budget wise <month> - Supply
	//args[6]=year
	//args[7]=month
	
	//args[0]=Input file path TPSREC monthly data
	//args[1]=Input file path TPPMT monthly data
	//args[2]=year
	//args[3]=month
	public final int run(final String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("year", args[2]);
		conf.set("month", args[3]);
	    Job job1 = new Job(conf);
	    Path directoryPath = new Path("/reportingDB/AllMasterData");
	    FileSystem fs = directoryPath.getFileSystem(getConf());
	    FileStatus[] fileStatus = fs.listStatus(directoryPath);
	    for (FileStatus status : fileStatus) {
	        DistributedCache.addCacheFile(new URI(status.getPath().toString()), job1.getConfiguration());
	    }
	    
	    Path directoryPath1 = new Path("/reportingDB/LocalityMasterData");
	    FileSystem fs1 = directoryPath1.getFileSystem(getConf());
	    FileStatus[] fileStatus1 = fs1.listStatus(directoryPath1);
	    for (FileStatus status : fileStatus1) {
	        DistributedCache.addCacheFile(new URI(status.getPath().toString()), job1.getConfiguration());
	    }
	    
		job1.setJarByClass(DemandSupplyReportGenerator.class);
		job1.setMapperClass(DemandSupplyReportGeneratorMapper.class);
		job1.setMapOutputKeyClass(Text.class);
		job1.setMapOutputValueClass(BSONWritable.class);
		job1.setInputFormatClass(TextInputFormat.class);
		job1.setOutputFormatClass(MongoOutputFormat.class);
		job1.setNumReduceTasks(0);
		FileInputFormat.addInputPath(job1, new Path(args[0]));
	    MongoConfigUtil.setOutputURI(job1.getConfiguration(), "mongodb://10.150.200.92:27017/mbstat.demandSupplyData");
	    job1.waitForCompletion(true);
	  //Second Job - Counts total number of words in a given file

	    
	    Job job2 = new Job(conf);
	    Path dp = new Path("/reportingDB/AllMasterData");
	    FileSystem fss = dp.getFileSystem(getConf());
	    FileStatus[] fileStatuss = fss.listStatus(dp);
	    for (FileStatus status : fileStatuss) {
	        DistributedCache.addCacheFile(new URI(status.getPath().toString()), job2.getConfiguration());
	    }
	    
	    Path dp1 = new Path("/reportingDB/LocalityMasterData");
	    FileSystem fss1 = dp1.getFileSystem(getConf());
	    FileStatus[] fileStatuss1 = fss1.listStatus(dp1);
	    for (FileStatus status : fileStatuss1) {
	        DistributedCache.addCacheFile(new URI(status.getPath().toString()), job2.getConfiguration());
	    }
	    job2.setJarByClass(DemandSupplyReportGenerator.class);
		job2.setMapperClass(DemandSupplyReportGeneratorMapper.class);
		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(BSONWritable.class);
		job2.setInputFormatClass(TextInputFormat.class);
		job2.setOutputFormatClass(MongoOutputFormat.class);
		job2.setNumReduceTasks(0);
		FileInputFormat.addInputPath(job2, new Path(args[1]));
	    MongoConfigUtil.setOutputURI(job2.getConfiguration(), "mongodb://10.150.200.92:27017/mbstat.demandSupplyData");
	    
	    
	    /*Job job3 = new Job(conf);
		job3.setJarByClass(DemandSupplyReportGenerator.class);
		job3.setMapperClass(DemandSupplyReportGeneratorMapper.class);
		job3.setMapOutputKeyClass(Text.class);
		job3.setMapOutputValueClass(BSONWritable.class);
		job3.setInputFormatClass(TextInputFormat.class);
		job3.setOutputFormatClass(MongoOutputFormat.class);
		job3.setNumReduceTasks(0);
		FileInputFormat.addInputPath(job3, new Path(args[2]));
	    MongoConfigUtil.setOutputURI(job3.getConfiguration(), "mongodb://10.150.200.92:27017/mbstat.demandSupplyBudgetWiseData");
	    job3.waitForCompletion(true);
	    
	    Job job4 = new Job(conf);
		job4.setJarByClass(DemandSupplyReportGenerator.class);
		job4.setMapperClass(DemandSupplyReportGeneratorMapper.class);
		job4.setMapOutputKeyClass(Text.class);
		job4.setMapOutputValueClass(BSONWritable.class);
		job4.setInputFormatClass(TextInputFormat.class);
		job4.setOutputFormatClass(MongoOutputFormat.class);
		job4.setNumReduceTasks(0);
		FileInputFormat.addInputPath(job4, new Path(args[3]));
	    MongoConfigUtil.setOutputURI(job4.getConfiguration(), "mongodb://10.150.200.92:27017/mbstat.demandSupplyBHKWiseData");
	    job4.waitForCompletion(true);
	    
	    Job job5 = new Job(conf);
		job5.setJarByClass(DemandSupplyReportGenerator.class);
		job5.setMapperClass(DemandSupplyReportGeneratorMapper.class);
		job5.setMapOutputKeyClass(Text.class);
		job5.setMapOutputValueClass(BSONWritable.class);
		job5.setInputFormatClass(TextInputFormat.class);
		job5.setOutputFormatClass(MongoOutputFormat.class);
		job5.setNumReduceTasks(0);
		FileInputFormat.addInputPath(job5, new Path(args[4]));
	    MongoConfigUtil.setOutputURI(job5.getConfiguration(), "mongodb://10.150.200.92:27017/mbstat.demandSupplyPropertyWiseData");
	    job5.waitForCompletion(true);
	    
	    Job job6 = new Job(conf);
		job6.setJarByClass(DemandSupplyReportGenerator.class);
		job6.setMapperClass(DemandSupplyReportGeneratorMapper.class);
		job6.setMapOutputKeyClass(Text.class);
		job6.setMapOutputValueClass(BSONWritable.class);
		job6.setInputFormatClass(TextInputFormat.class);
		job6.setOutputFormatClass(MongoOutputFormat.class);
		job6.setNumReduceTasks(0);
		FileInputFormat.addInputPath(job6, new Path(args[5]));
	    MongoConfigUtil.setOutputURI(job6.getConfiguration(), "mongodb://10.150.200.92:27017/mbstat.demandSupplyBudgetWiseData");*/
	    //job6.submit();
	    return job2.waitForCompletion(true) ? 0 : 1;
	  }
}
