package br.ufc.great.cloud.hadoop.wordCount.core;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import br.ufc.great.cloud.hadoop.wordCount.utils.FileUtils;

public class WordCount {
	
	public static class TokenizerMapper extends Mapper<Object, Text, Text, MapWritable> {

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				MapWritable map = new MapWritable();
				MapWritable map2 = new MapWritable();
				
				String token = itr.nextToken().trim();
				map.put(new Text("count"), new IntWritable(1));
				map.put(new Text("length"), new IntWritable(token.length()));
				context.write(new Text(token), map);
				
				map2.put(new Text("length"), new IntWritable(token.length()));
				context.write(new Text("my-avarage"), map2);
			}
		}
	}
	
	public static class IntSumReducer extends Reducer<Text, MapWritable, Text, Text> {

		public void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
			if(key.toString().equals("my-avarage")) {
				int vals = 0, sum = 0;
				for (MapWritable val : values) {
					sum += ((IntWritable) val.get(new Text("length"))).get();
					vals++;
				}
				context.write(key, new Text(sum + ";" + vals + ";" + (sum/vals)));
			}else {
				int count = 0, length = 0;
				for (MapWritable val : values) {
					count += ((IntWritable) val.get(new Text("count"))).get();
					length = ((IntWritable) val.get(new Text("length"))).get();
				}
				context.write(key, new Text(count + ";" + length));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		File outputDIR = new File("D:\\dev\\github\\pedroalmir\\cloud_study\\hadoop\\word-count\\wordCount\\output");
		if(outputDIR.exists()) {
			FileUtils.delete(outputDIR);
		}
		
		Configuration conf = new Configuration();
		Job job = new Job(conf, "word count");
		job.getConfiguration().set("mapreduce.output.basename", "word-count-result.txt");
		job.setJarByClass(WordCount.class);
		job.setMapperClass(TokenizerMapper.class);
		//job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(MapWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}