package br.ufc.great.hadoop.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import br.ufc.great.hadoop.model.Tweet;
import br.ufc.great.hadoop.utils.MyFileUtils;
import br.ufc.great.hadoop.utils.ReadTSV;

public class HashTagCountByDay {
	
	public static class TokenizerMapper extends Mapper<Object, Text, Text, Text> {
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			Tweet tweet = ReadTSV.parse(value.toString());
			ArrayList<String> tags = tweet.getHashTags();
			for (String tag : tags) {
				context.write(new Text(tag), new Text(tweet.getFormattedDate()));
			}
		}
	}
	
	public static class IntSumReducer extends Reducer<Text, Text, Text, NullWritable> {

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			HashMap<String, Integer> map = new HashMap<>();
			for (Text val : values) {
				String mkey = val.toString();
				if(map.containsKey(mkey)) {
					map.put(mkey, map.get(mkey) + 1);
				}else {
					map.put(mkey, 1);
				}
			}
			for(String mkey : map.keySet()) {
				context.write(new Text(key + "\t" + mkey + "\t" + map.get(mkey)), null);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String inputDIR = "D:/dev/github/pedroalmir/cloud_study/hadoop/tests/tweets/input";
		String outputDIR = "D:/dev/github/pedroalmir/cloud_study/hadoop/tests/tweets/output";
		
		File outputDIRFile = new File(outputDIR);
		if(outputDIRFile.exists()) {
			MyFileUtils.delete(outputDIRFile);
		}
		
		Configuration conf = new Configuration();
		Job job = new Job(conf, "word count");
		job.setJarByClass(HashTagCountByDay.class);
		job.setMapperClass(TokenizerMapper.class);
		//job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(inputDIR));
		FileOutputFormat.setOutputPath(job, new Path(outputDIR));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}