package com.mare.jovan.file;

import java.util.ArrayList;
import java.util.List;

public class Directory extends File{

	private static final long serialVersionUID = 1L;

	private List<File> files;
	
	public Directory(String path) {
		super(path);
		this.type = FileType.Directory;
		this.files = new ArrayList<File>();
	}
	
	public List<File> getFiles() {
		return files;
	}
	
	public void addFile(File file) {
		files.add(file);
	}
	
	public void removeFile(File file) {
		files.remove(file);
	}
	
}
