package com.mare.jovan.file;

import java.util.ArrayList;
import java.util.List;

/**
 * Directory class is used as logical representation of File's structure.
 */

public class Directory extends File{

	private static final long serialVersionUID = 1L;

	private List<File> files;
	
	/**
	 * Creates new Directory instance using given path. Initializes files list which it contains.
	 * @param path
	 */
	public Directory(String path) {
		super(path);
		this.type = FileType.Directory;
		this.files = new ArrayList<File>();
	}
	
	/**
	 * Returns the list of files which directory contains
	 * @return the list of files which directory contains
	 */
	public List<File> getFiles() {
		return files;
	}
	
	/**
	 * Adds file to directory
	 * @param file
	 */
	public void addFile(File file) {
		files.add(file);
	}
	
	/**
	 * Removes file from directory
	 * @param file
	 */
	public void removeFile(File file) {
		files.remove(file);
	}
	
}
