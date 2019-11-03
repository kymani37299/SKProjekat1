package com.mare.jovan.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

	public static boolean isPathValid(String path) {
		// TODO
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
	
	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}
	
	public static boolean isDirectory(String path) {
		File file = new File(path);
		return file.isDirectory();
	}
	
	public static boolean zipFiles(String path) {
		File file = new File(path);
		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
			if(file.isDirectory()) {
				zipDirectory(file,file.getName(),zos);
			} else {
				zipFile(file,zos);
			}
			zos.flush();
			zos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private static void zipDirectory(File folder, String parentFolder,
            ZipOutputStream zos) throws FileNotFoundException, IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, parentFolder + "/" + file.getName(), zos);
                continue;
            }
            zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = bis.read(bytesIn)) != -1) {
                zos.write(bytesIn, 0, read);
            }
            bis.close();
            zos.closeEntry();
        }
    }
	
	private static void zipFile(File file, ZipOutputStream zos)
            throws FileNotFoundException, IOException {
        zos.putNextEntry(new ZipEntry(file.getName()));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = bis.read(bytesIn)) != -1) {
            zos.write(bytesIn, 0, read);
        }
        bis.close();
        zos.closeEntry();
    }
	
}
