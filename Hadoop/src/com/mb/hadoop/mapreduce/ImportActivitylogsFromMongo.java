package com.mb.hadoop.mapreduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.MongoUpdateWritable;
import com.mongodb.hadoop.util.MongoConfigUtil;

public class ImportActivitylogsFromMongo extends Configured implements Tool {
	
	public static class ReadActivitylogsFromMongo extends
			Mapper<Object, BSONObject, Text, Text> {
		private Text keyword = new Text();
		private Text word = new Text();
		public void map(Object key, BSONObject value, Context context)
				throws IOException, InterruptedException {
			String day = String.valueOf(((Date)value.get("dateTimeTTL")).getDate());
			String month = String.valueOf(((Date)value.get("dateTimeTTL")).getMonth());
			String year = String.valueOf(((Date)value.get("dateTimeTTL")).getYear());
			String action = String.valueOf((value.get("action")));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
			String date = String.valueOf(sdf.format((Date)value.get("dateTimeTTL")));
			//keyword.set(day+":"+month+":"+year+","+action);
			keyword.set(date+","+action);
			word.set(value.toString());
			context.write(keyword, word);
		}
	}
	public static class WriteCollectionToMongo extends
			Reducer<Text, Text, Text, MongoUpdateWritable> {
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			int count = 0;
			BasicBSONObject query = new BasicBSONObject("_id", key.toString());
			for(Text value: values){
				count++;
			}
			String[] str = key.toString().split(",");
			Map<String,String> map = new HashMap<String,String>();
			if(str[1] == null || str[1].equalsIgnoreCase("Property Search - Refine") || str[1].equalsIgnoreCase("Project Search - Refine") || str[1].equalsIgnoreCase("Agent Search - Refine")){
				map.put("date",str[0]);
				map.put("action",str[1]);
				map.put("dailyCount",String.valueOf(count));
				BasicBSONObject update = new BasicBSONObject(map);
				//BasicBSONObject action = new BasicBSONObject("action", str[1]);
				//BasicBSONObject dailyCount = new BasicBSONObject("dailyCount", count);
				context.write(key, new MongoUpdateWritable(query, update, true, false));
				//context.write(key, new IntWritable(count));
			}
		}
	}
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		  int res = ToolRunner.run(conf, new ImportActivitylogsFromMongo(), args);
		  System.exit(res);
	}
	public final int run(final String[] args) throws Exception {
	    Job job = new Job(super.getConf());
		MongoConfigUtil.setInputURI(job.getConfiguration(), "mongodb://10.150.200.78:27017/mbstat.activitylog?readPreference=secondary");
		MongoConfigUtil.setCreateInputSplits(job.getConfiguration(), true);
		MongoConfigUtil.setOutputURI(job.getConfiguration(), "mongodb://10.150.200.78:27017/mbstat.activityLogCount");
		job.setJarByClass(ImportActivitylogsFromMongo.class);
		job.setMapperClass(ReadActivitylogsFromMongo.class);
		job.setCombinerClass(WriteCollectionToMongo.class);
		job.setReducerClass(WriteCollectionToMongo.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MongoUpdateWritable.class);
		job.setInputFormatClass(MongoInputFormat.class);
		job.setOutputFormatClass(MongoOutputFormat.class);
	    return job.waitForCompletion(true) ? 0 : 1;
	  }
}
