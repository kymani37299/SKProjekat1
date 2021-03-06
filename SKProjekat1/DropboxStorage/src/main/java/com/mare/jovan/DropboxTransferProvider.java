package com.mare.jovan;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
/**
 * <p>This class (@code DropboxTransferProvider) is responsible
 * for communicating with Dropbox API</p>
 * @author Marko Sreckovic
 */
public class DropboxTransferProvider {
	
	public static String ACCESS_TOKEN = "";
	private final static boolean DEBUG_MODE = false;
	
	private static DropboxTransferProvider instance;
	
	private DbxClientV2 client;
	private boolean enabled;
	
	private DropboxTransferProvider() {
		DbxRequestConfig config = DbxRequestConfig.newBuilder("SKProjekat1").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);
        enabled = true;
	}
	
	protected static DropboxTransferProvider getInstance() {
		if(instance==null) {
			instance = new DropboxTransferProvider();
		}
		return instance;
	}
	
	protected boolean upload(String sourcePath, String destinationPath) {
		if(!enabled) return false;
		destinationPath = "/" + destinationPath;
		try {
			InputStream uploadFile = new FileInputStream(sourcePath);
			client.files().uploadBuilder(destinationPath).uploadAndFinish(uploadFile);
			uploadFile.close();
          return true;
		} catch (Exception e) {
			if(DEBUG_MODE) {
			System.out.println("Error uploading file " + sourcePath);
			e.printStackTrace();
			}
		}
		
		return false;
    }
	
	protected boolean download(String targetPath, String destinationPath) {
		if(!enabled) return false;
		targetPath ="/" +  targetPath;
		try {
			OutputStream downloadFile = new FileOutputStream(destinationPath);
			client.files().downloadBuilder(targetPath).download(downloadFile);
			downloadFile.close();
			return true;
		} catch (Exception e) {
			if(DEBUG_MODE) {
				System.out.println("Error downloading file " + targetPath);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	protected boolean delete(String targetPath) {
		if(!enabled) return false;
		targetPath ="/" +  targetPath;
		try {
			client.files().delete(targetPath);
			return true;
		} catch (Exception e) {
			if(DEBUG_MODE) {
				System.out.println("Error deleting file " + targetPath);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	protected boolean update(String sourcePath,String targetPath) {
		delete(targetPath);
		return upload(sourcePath,targetPath);
	}
	
	protected void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
