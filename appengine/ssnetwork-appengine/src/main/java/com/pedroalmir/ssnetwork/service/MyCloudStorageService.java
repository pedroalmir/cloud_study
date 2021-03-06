/**
 * 
 */
package com.pedroalmir.ssnetwork.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.pedroalmir.ssnetwork.util.AppEngineUtils;

/**
 * @author Pedro Almir
 */
public class MyCloudStorageService {
	
	/**
	 * @param filename
	 * @param content
	 * @return
	 */
	public static String uploadImage(String filename, byte[] content) {
		Map<String, String> storageConfig;
		try {
			storageConfig = AppEngineUtils.getStorageConfig();
			String md5 = getMD5(content);
			String fileKey = String.join("", new String[] {md5, "_", filename.toLowerCase().replaceAll(" ", "").trim()});

			Storage storage = StorageOptions.newBuilder().setProjectId(storageConfig.get("projectId")).build().getService();
			BlobId blobId = BlobId.of(storageConfig.get("bucketName"), fileKey);
			BlobInfo blobInfo = BlobInfo
					.newBuilder(blobId)
					.setContentType("image/" + filename.split("\\.")[1].toLowerCase().trim())
					.setMd5(md5)
					.build();
			
			storage.create(blobInfo, content);
			storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
			
			return AppEngineUtils.getBucketBaseURL() + fileKey;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param user
	 * @param file
	 * @return
	 */
	public static String uploadImage(File file) {
		Map<String, String> storageConfig;
		try {
			storageConfig = AppEngineUtils.getStorageConfig();
			String fileKey = getMD5(file) + "_" + file.getName().toLowerCase().replaceAll(" ", "").trim();

			Storage storage = StorageOptions.newBuilder().setProjectId(storageConfig.get("projectId")).build().getService();
			BlobId blobId = BlobId.of(storageConfig.get("bucketName"), fileKey);
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
			
			byte[] bytesArray = new byte[(int) file.length()]; 
			FileInputStream fis = new FileInputStream(file);
			fis.read(bytesArray); fis.close();
			
			Blob created = storage.create(blobInfo, bytesArray);
			storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
			
			return created.getMediaLink();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param content
	 * @return
	 * @throws IOException
	 */
	private static String getMD5(byte[] content) throws IOException {
		return org.apache.commons.codec.digest.DigestUtils.md5Hex(content);
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
