package com.mare.jovan.file;

import java.io.Serializable;
import java.util.List;

public class File implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private boolean valid;
	private FileMetadata metadata;
	protected FileType type;
	
	public File(String name) {
		this.name = name;
		this.valid = true;
		this.type = FileType.File;
	}
	
	public File(String path, FileMetadata metadata) {
		this(path);
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
	
	public String getName() {
		return name;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof File) {
			File f = (File)obj;
			return f.name.equals(this.name);
		}
		return super.equals(obj);
	}
	
}
