package br.ufc.great.hadoop.text.books;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

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

import br.ufc.great.hadoop.commons.utils.MyEnv;
import br.ufc.great.hadoop.commons.utils.MyFileUtils;

public class WordLength {
	
	public static class MyMapper extends Mapper<Object, Text, Text, MapWritable> {

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
				context.write(new Text("my-word-average"), map2);
			}
		}
	}
	
	public static class MyReducer extends Reducer<Text, MapWritable, Text, NullWritable> {

		public void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
			if(key.toString().equals("my-word-average")) {
				int vals = 0, sum = 0;
				for (MapWritable val : values) {
					sum += ((IntWritable) val.get(new Text("length"))).get();
					vals++;
				}
				
				context.write(new Text(String.join("\t", new String[] {key.toString(), sum + "", vals + "", (sum/vals)  + ""})), null);
			}else {
				int count = 0, length = 0;
				for (MapWritable val : values) {
					count += ((IntWritable) val.get(new Text("count"))).get();
					length = ((IntWritable) val.get(new Text("length"))).get();
				}
				context.write(new Text(String.join("\t", new String[] {key.toString(), count + "", length + ""})), null);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String inputDIR = null, outputDIR = null;
		if(MyEnv.isDevelopment) {
			// Only in development environment...
			inputDIR = "D:/dev/github/pedroalmir/cloud_study/hadoop/tests/books";
			outputDIR = "D:/dev/github/pedroalmir/cloud_study/hadoop/tests/output";
			
			File outputDIRFile = new File(outputDIR);
			if(outputDIRFile.exists()) {
				MyFileUtils.delete(outputDIRFile);
			}
		}else {
			inputDIR = args[0];
			outputDIR = args[1];
		}
		
		Configuration conf = new Configuration();
		Job job = new Job(conf, "q1ab-word-length-average");
		job.setJarByClass(WordLength.class);
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(MapWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(inputDIR));
		FileOutputFormat.setOutputPath(job, new Path(outputDIR));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}