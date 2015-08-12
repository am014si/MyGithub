package com.mb.hadoop.data.generator;

import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import com.mb.hadoop.mapper.HomeInUrBudgetReportGeneratorMapper;
import com.mb.hadoop.reducer.HomeInUrBudgetReportGeneratorReducer;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.util.MongoConfigUtil;

public class HomeInUrBudgetReportGenerator extends Configured implements Tool  {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		  int res = ToolRunner.run(conf, new HomeInUrBudgetReportGenerator(), args);
		  System.exit(res);
	}
	
	//This Map Reduce job will take input Year and Month for which we want to generate the report.
	//args[0]=Input file path i.e. Locality Wise Listing monthly report
	//args[1]=year
	//args[2]=month
	public final int run(final String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("year", args[1]);
		conf.set("month", args[2]);
	    Job job = new Job(conf);
	    DistributedCache.addCacheFile(new URI("/propIndexData/RFCodesLookup/RFNumCodesFile.CSV"),job.getConfiguration());
		job.setJarByClass(HomeInUrBudgetReportGenerator.class);
		job.setMapperClass(HomeInUrBudgetReportGeneratorMapper.class);
		job.setReducerClass(HomeInUrBudgetReportGeneratorReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BSONWritable.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(MongoOutputFormat.class);
		job.setNumReduceTasks(20);
		FileInputFormat.addInputPath(job, new Path(args[0]));
	    MongoConfigUtil.setOutputURI(job.getConfiguration(), "mongodb://10.150.200.92:27017/mbstat.homeInUrBudgetCollection");
	    return job.waitForCompletion(true) ? 0 : 1;
	  }
}
