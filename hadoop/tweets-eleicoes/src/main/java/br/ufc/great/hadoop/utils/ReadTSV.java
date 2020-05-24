/**
 * 
 */
package br.ufc.great.hadoop.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import br.ufc.great.hadoop.model.Tweet;

/**
 * @author Pedro Almir
 */
public class ReadTSV {
	
	private static final String SAMPLE_CSV_FILE_PATH = "D:/dev/github/pedroalmir/cloud_study/hadoop/tests/tweets/input/tweets.tsv";
	
	public static void main(String[] args) throws IOException {
        try (
            Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter('\t'));
        ) {
            for (CSVRecord csvRecord : csvParser) {
            	Tweet tweet = new Tweet(csvRecord.get(0), csvRecord.get(1), csvRecord.get(7));
            	System.out.println(tweet);
            }
        }
    }
	
	/**
	 * @param line
	 * @return
	 * @throws IOException
	 */
	public static Tweet parse(String line) throws IOException {
		try (
			Reader reader = new StringReader(line);
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter('\t'));
		){
			CSVRecord csvRecord = csvParser.iterator().next();
			return new Tweet(csvRecord.get(0), csvRecord.get(1), csvRecord.get(7));
		}
	}
}
