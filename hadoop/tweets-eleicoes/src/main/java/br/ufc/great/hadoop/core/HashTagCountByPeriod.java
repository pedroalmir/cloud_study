package br.ufc.great.hadoop.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
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

public class HashTagCountByPeriod {
	
	public static class TokenizerMapper extends Mapper<Object, Text, Text, MapWritable> {
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			Tweet tweet = ReadTSV.parse(value.toString());
			ArrayList<String> tags = tweet.getHashTags();
			for (String tag : tags) {
				MapWritable map = new MapWritable();
				map.put(new Text("morning"), (tweet.isMorning()) ? new IntWritable(1) : new IntWritable(0));
				map.put(new Text("afternoon"), (tweet.isAfternoon()) ? new IntWritable(1) : new IntWritable(0));
				map.put(new Text("night"), (tweet.isNight()) ? new IntWritable(1) : new IntWritable(0));
				context.write(new Text(tag), map);
			}
		}
	}
	
	public static class IntSumReducer extends Reducer<Text, MapWritable, Text, NullWritable> {

		public void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
			int morning = 0, afternoon = 0, night = 0;
			for (MapWritable val : values) {
				morning   += ((IntWritable) val.get(new Text("morning"))).get();
				afternoon += ((IntWritable) val.get(new Text("afternoon"))).get();
				night 	  += ((IntWritable) val.get(new Text("night"))).get();
			}
			context.write(new Text(key + "\t" + morning + "\t" + afternoon + "\t" + night), null);
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
		job.setJarByClass(HashTagCountByPeriod.class);
		job.setMapperClass(TokenizerMapper.class);
		//job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(MapWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(inputDIR));
		FileOutputFormat.setOutputPath(job, new Path(outputDIR));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}