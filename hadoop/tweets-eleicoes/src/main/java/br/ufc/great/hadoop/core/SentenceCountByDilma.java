package br.ufc.great.hadoop.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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

public class SentenceCountByDilma {
	
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			Tweet tweet = ReadTSV.parse(value.toString());
			if(tweet.isRelatedDilma()) {
				ArrayList<String> nGrams = tweet.getNGrams();
				for (String ngram : nGrams) {
					context.write(new Text(ngram), new IntWritable(1));
				}
			}
		}
	}
	
	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, NullWritable> {
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int count = 0;
			for (IntWritable val : values) {
				count += val.get();
			}
			if(count > 5) {
				int length = new StringTokenizer(key.toString()).countTokens();
				context.write(new Text(key + "\t" + length + "\t" + count), null);
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
		job.setJarByClass(SentenceCountByDilma.class);
		job.setMapperClass(TokenizerMapper.class);
		//job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(inputDIR));
		FileOutputFormat.setOutputPath(job, new Path(outputDIR));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}