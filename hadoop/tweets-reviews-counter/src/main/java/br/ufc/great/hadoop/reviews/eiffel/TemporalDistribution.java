package br.ufc.great.hadoop.reviews.eiffel;

import java.io.File;
import java.io.IOException;

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

import br.ufc.great.hadoop.commons.model.Review;
import br.ufc.great.hadoop.commons.utils.MyEnv;
import br.ufc.great.hadoop.commons.utils.MyFileUtils;
import br.ufc.great.hadoop.commons.utils.ReadJson;

public class TemporalDistribution {
	
	public static class MyMapper extends Mapper<Object, Text, Text, IntWritable> {
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			Review review = ReadJson.getReview(value.toString());
			context.write(new Text(review.getFormattedDate()), new IntWritable(1));
		}
	}
	
	public static class MyReducer extends Reducer<Text, IntWritable, Text, NullWritable> {
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int count = 0;
			for (IntWritable val : values) {
				count += val.get();
			}
			context.write(new Text(key + "\t" + count), null);
		}
	}

	public static void main(String[] args) throws Exception {
		String inputDIR = null, outputDIR = null;
		if(MyEnv.isDevelopment) {
			// Only in development environment...
			inputDIR = "D:/dev/github/pedroalmir/cloud_study/hadoop/tests/reviews";
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
		Job job = new Job(conf, "q3d-temporal-distribution");
		job.setJarByClass(TemporalDistribution.class);
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(inputDIR));
		FileOutputFormat.setOutputPath(job, new Path(outputDIR));
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}