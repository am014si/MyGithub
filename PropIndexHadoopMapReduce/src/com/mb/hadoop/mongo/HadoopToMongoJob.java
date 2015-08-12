package com.mb.hadoop.mongo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.bson.BasicBSONObject;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.io.MongoUpdateWritable;
import com.mongodb.hadoop.util.MongoConfigUtil;

public class HadoopToMongoJob extends Configured implements Tool {

		public static void main(String[] args) throws Exception {
			Configuration conf = new Configuration();
			  int res = ToolRunner.run(conf, new HadoopToMongoJob(), args);
			  System.exit(res);
		}
		
		//This Map Reduce job will take input Year and Month for which we want to generate the report.
		//args[0]=Input file path i.e. Locality Wise Listing monthly report
		public final int run(final String[] args) throws Exception {
		    Job job = new Job(super.getConf());
			job.setJarByClass(HadoopToMongoJob.class);
			job.setMapperClass(WriteIntoMongoCollection.class);
			job.setNumReduceTasks(0);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(BSONWritable.class);
			/*job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(NullWritable.class);*/
			job.setInputFormatClass(TextInputFormat.class);
			//job.setOutputFormatClass(TextOutputFormat.class);
			job.setOutputFormatClass(MongoOutputFormat.class);
			FileInputFormat.addInputPath(job, new Path(args[0]));
		    //FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    MongoConfigUtil.setOutputURI(job.getConfiguration(), "mongodb://10.150.200.92:27017/mbstat.preferredLocalityReportOutput");
		    return job.waitForCompletion(true) ? 0 : 1;
		  }
		
	public static class WriteIntoMongoCollection extends
			Mapper<Object, Text, Text, BSONWritable> {
		private Text keyword = new Text();
		private Text word = new Text();
		private static NullWritable nw = NullWritable.get();

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			//word.set(value.toString());
			BasicBSONObject query = new BasicBSONObject("_id", key.toString());
			word.set(key.toString());
			String[] str = value.toString().split(",");
			//Map<String,String> map = new HashMap<String,String>();
			//if(str[1] == null || str[1].equalsIgnoreCase("Property Search - Refine") || str[1].equalsIgnoreCase("Project Search - Refine") || str[1].equalsIgnoreCase("Agent Search - Refine")){
			/*map.put("Locality",str[0]);
			map.put("City",str[1]);
			map.put("PropertyType",str[2]);
			map.put("SaleOrRent",str[3]);
			map.put("MinCapitalValue", str[4]);
			map.put("MaxCapitalValue", str[5]);
			map.put("Date", str[6]);
			map.put("ListingCount", str[7]);*/
			//BasicBSONObject update = new BasicBSONObject(map);
			//BSONWritable pkeyOut = new BSONWritable(update);
			
			
			BasicBSONObject map = new BasicBSONObject();
			map.put("Locality",str[0]);
			map.put("City",str[1]);
			map.put("PropertyType",str[2]);
			map.put("SaleOrRent",str[3]);
			map.put("MinCapitalValue", new Double(str[4]));
			map.put("MaxCapitalValue", new Double(str[5]));
			map.put("Date", str[6]);
			map.put("ListingCount", new Integer(str[7]));
	        //output.put("avg", avg);
			context.write( word, new BSONWritable( map ) );
			
			
			//BasicBSONObject action = new BasicBSONObject("action", str[1]);
			//BasicBSONObject dailyCount = new BasicBSONObject("dailyCount", count);
			//context.write(word, new MongoUpdateWritable(query, update, true, false));
			//context.write(key, new IntWritable(count));
		}
	}
}

