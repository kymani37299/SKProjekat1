package com.mare.jovan.file;

import java.io.Serializable;
import java.util.Arrays;

public class File implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String path;
	private boolean valid;
	private FileMetadata metadata;
	protected FileType type;
	
	public File(String path) {
		this.path = path.replace('/', '\'').replace('\\', '\'');
		String tmp[] = this.path.split("\'");
		this.name = tmp[tmp.length-1];
		// TODO CHECK IF VALID
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
	
	public String[] getParentPathList() {
		String[] pathList = getPathList();
		if(pathList.length==1) return new String[0];
		return Arrays.copyOfRange(pathList,0,pathList.length-1);
	}
	
	public String[] getPathList() {
		String[] pathList = path.split("\'");
		if(pathList[0].isBlank()) pathList = Arrays.copyOfRange(pathList,1,pathList.length);
		return pathList;
	}
	
	public String getPath() {
		return path;
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
			return f.path.equals(this.path);
		}
		return super.equals(obj);
	}
	
}
