package com.mare.jovan.file;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import com.mare.jovan.util.FileUtil;

/**
 * File class is used as logical representation of files in storage.
 */
public class File implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String path;
	private boolean valid;
	private FileMetadata metadata;
	protected FileType type;
	
	/**
	 * Creates new File instance using given path.
	 * @param path
	 */
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
	
	/**
	 * Creates new File instance with description using given path.
	 * @param path
	 * @param description
	 */
	public File(String path, String description) {
		this(path);
		metadata.setDescription(description);
	}
	
	/**
	 * Returns metadata about file
	 * @return metadata about file
	 */
	public FileMetadata getMetadata() {
		return metadata;
	}

	/**
	 * Sets file metadata to provided value
	 * @param metadata FileMetadata instance
	 */
	public void setMetadata(FileMetadata metadata) {
		this.metadata = metadata;
	}

	/**
	 * Returns FileType value
	 * @return FileType value
	 */
	public FileType getFileType() {
		return type;
	}
	
	/**
	 * Returns the list of parent folders
	 * @return the list of parent folders
	 */
	public String[] getParentPathList() {
		String[] pathList = getPathList();
		if(pathList.length==1) return new String[0];
		return Arrays.copyOfRange(pathList,0,pathList.length-1);
	}
	
	/**
	 * Returns the list of files from root to file
	 * @return the list of files from root to file
	 */
	public String[] getPathList() {
		String[] pathList = path.split("\'");
		if(pathList[0].isEmpty()) pathList = Arrays.copyOfRange(pathList,1,pathList.length);
		return pathList;
	}
	
	/**
	 * Returns the file path
	 * @return the file path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Returns the file name
	 * @return the file name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns if file is valid. 
	 * File is valid if pathname is using valid characters accordingly to various operating systems
	 * @return true if file is valid;otherwise false
	 */
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
