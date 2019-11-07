package com.mare.jovan;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

/*
 * (@code ConnectionFactory) is responsible for instantiating
 * implementations of this specification
 * 
 */
public class ConnectionFactory {
	
	private static String implementationPath;
	private static String repositoryClass;
	private static String accessToken;
	
	private ConnectionFactory() {}
	
	/*
	 * Returns the implementation instance of IConnection
	 * 
	 * @param configPath local or absolute path to configuration of implementation
	 * @return instance of implementation of IConnection
	 */
	public static IConnection getConnection(String configPath) {
		try {
			parseFile(configPath);
			if(implementationPath==null || repositoryClass==null) return null;
			if(accessToken!=null) {
				return newInstance(accessToken);
			}else {
				return newInstance();
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	/*
	 * Returns the implementation instance of IConnection
	 * 
	 * @param configPath local or absolute path to configuration of implementation
	 * @param arg argument for implementation constructor
	 * @return instance of implementation of IConnection
	 */
	public static IConnection getConnection(String configPath,String arg) {
		try {
			parseFile(configPath);
			if(implementationPath==null || repositoryClass==null) return null;
			IConnection connection = newInstance(arg);
			return connection;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static void parseFile(String configPath) throws FileNotFoundException {
		implementationPath = null;
		repositoryClass = null;
		File configFile = new File(configPath);
		Scanner sc = new Scanner(configFile);
		while(sc.hasNext()) {
			String line = sc.nextLine();
			String split[] = line.split("=");
			if(split.length<2) continue;
			if(split[0].equals("implementationPath")) {
				implementationPath = split[1];
			}else if(split[0].equals("repositoryClass")) {
				repositoryClass = split[1];
			}
		}
		sc.close();
	}
	
	@SuppressWarnings("resource")
	private static IConnection newInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		File implementationFile = new File(implementationPath);
		URL url[] = {implementationFile.toURI().toURL()};
		ClassLoader cl = new URLClassLoader(url);
		IConnection connection = (IConnection) cl.loadClass(repositoryClass).getConstructor().newInstance();
		return connection;
	}
	
	@SuppressWarnings("resource")
	private static IConnection newInstance(String arg) throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		File implementationFile = new File(implementationPath);
		URL url[] = {implementationFile.toURI().toURL()};
		ClassLoader cl = new URLClassLoader(url); 
		Constructor<?> constructor = cl.loadClass(repositoryClass).getConstructor(String.class);
		IConnection connection = (IConnection) constructor.newInstance(arg);
		return connection;
	}
	
}
