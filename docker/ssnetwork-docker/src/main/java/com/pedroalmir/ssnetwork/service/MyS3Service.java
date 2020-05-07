/**
 * 
 */
package com.pedroalmir.ssnetwork.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.pedroalmir.ssnetwork.util.AwsUtils;

/**
 * @author Pedro Almir
 *
 */
public class MyS3Service {
	
	private static final String BUCKET 	= "ssn-docker-bucket";
	
	/**
	 * @param user
	 * @param file
	 * @return
	 */
	public static String uploadImage(File file) {
		try {
        	BasicAWSCredentials awsCreds = AwsUtils.getCredentials();
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            		.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            		.withRegion(Regions.SA_EAST_1)
            		.build();
            
            String fileKey = getMD5(file) + "_" + file.getName().toLowerCase().replaceAll(" ", "").trim();
            
            PutObjectRequest request = new PutObjectRequest(BUCKET, fileKey, file);
            request.withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(request);
            return s3Client.getUrl(BUCKET, fileKey).toExternalForm();
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static String getMD5(File file) throws IOException {
		try (InputStream is = Files.newInputStream(Paths.get(file.getAbsolutePath()))) {
		    return org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
		}
	}
	
}
