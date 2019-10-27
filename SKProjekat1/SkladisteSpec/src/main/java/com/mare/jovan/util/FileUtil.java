package com.mare.jovan.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
	
}
