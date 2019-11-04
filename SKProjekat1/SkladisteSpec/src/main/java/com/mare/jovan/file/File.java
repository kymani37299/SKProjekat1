package com.mare.jovan.file;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import com.mare.jovan.util.FileUtil;

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
		if(tmp.length==0) this.name = "root";
		else this.name = tmp[tmp.length-1];
		this.valid = FileUtil.isPathValid(path);
		this.type = FileType.File;
		
		metadata = new FileMetadata();
		metadata.setDateCreated(new Date(System.currentTimeMillis()));
	}
	
	public File(String path, String description) {
		this(path);
		metadata.setDescription(description);
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
