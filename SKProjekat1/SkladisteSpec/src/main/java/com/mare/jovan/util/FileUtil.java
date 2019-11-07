package com.mare.jovan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;

/**
 * FileUtil class is used for various file manipulation functions, such as:
 * - Files serialization and deserialization
 * - Copying,deleting and downloading files
 * - Checking if file exists, is directory or if pathname is valid
 */
public class FileUtil {
	
	/**
	 * Checks if given pathname has invalid characters accordingly to various operating systems.
	 * @param path
	 * @return false if pathname has invalid characters accordingly to various operating systems;otherwise true
	 */
	public static boolean isPathValid(String path) {
		String[] illegal = {"\n","\r","\t","\0","\f","`","?","*","\\","<",">","|","\""};
		for(String str:illegal) {
			if(path.contains(str)) {
				return false;
			}
		}
		return true;
	}

	
	public static boolean serialize(Serializable target, String path) {
		try {    
            FileOutputStream file = new FileOutputStream(path); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
              
            out.writeObject(target); 
            out.close(); 
            file.close(); 
            return true;
        } 
        catch(IOException ex) { } 
		return false;
	}
	
	public static Object deserialize(String path) {
		Object o = null;
        try {    
            FileInputStream file = new FileInputStream(path); 
            ObjectInputStream in = new ObjectInputStream(file); 
            
            o = in.readObject(); 
              
            in.close(); 
            file.close();  
        } catch(Exception e) {}
        return o;
	}
	/**
	 * Copies file from given source path to destination path. Returns false if error occurs
	 * @param sourcePath the source pathname
	 * @param destinationPath the destination pathname
	 * @return false if error occurs;otherwise true
	 */
	public static boolean copyFiles(String sourcePath,String destinationPath) {
		File sourceFile = new File(sourcePath);
		File destFile = new File(destinationPath);

		try {
			Files.copy(sourceFile.toPath(), destFile.toPath());
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Deletes file from given pathname. Returns false if error occurs
	 * @param path the pathname to file
	 * @return false if error occurs;otherwise true
	 */
	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}
	
	/**
	 * Checks if file on given pathname is directory
	 * @param path the pathname to file
	 * @return yes if file on given pathname is directory;otherwise false
	 */
	public static boolean isDirectory(String path) {
		File file = new File(path);
		return file.isDirectory();
	}
	
	/**
	 * Checks if file exist on given pathname
	 * @param path the pathname to file
	 * @return true if file exists on given pathname;otherwise false
	 */
	public static boolean fileExists(String path) {
		File file = new File(path);
		return file.exists();
	}
}
