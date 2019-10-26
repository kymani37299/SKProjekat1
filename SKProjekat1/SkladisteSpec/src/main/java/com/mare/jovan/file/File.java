package com.mare.jovan.file;

import java.io.Serializable;

public class File implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String path;
	private FileType type;
	private boolean valid;
	private FileMetadata metadata;
	
	public File(String path,FileType type) {
		this.path = path;
		this.valid = true;
		this.type = type;
	}
	
	public File(String path,FileType type,FileMetadata metadata) {
		this(path,type);
		this.metadata = metadata;
	}
	
	public FileMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(FileMetadata metadata) {
		this.metadata = metadata;
	}

	public FileType getFileType() {
		return type;
	}
	
	public String getPath() {
		return path;
	}
	
	public boolean isValid() {
		return valid;
	}
	
}
