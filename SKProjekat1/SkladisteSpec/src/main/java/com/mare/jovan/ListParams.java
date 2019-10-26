package com.mare.jovan;

import com.mare.jovan.file.File;
import com.mare.jovan.file.FileType;

public class ListParams {
	
	private String nameFilter = null;
	private String extFilter = null;
	private File path = null;
	private boolean showMetadata = false;
	private FileType typeFilter = null;
	
	public String getNameFilter() {
		return nameFilter;
	}
	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}
	public String getExtFilter() {
		return extFilter;
	}
	public void setExtFilter(String extFilter) {
		this.extFilter = extFilter;
	}
	public File getPath() {
		return path;
	}
	public void setPath(File path) {
		this.path = path;
	}
	public boolean isShowMetadata() {
		return showMetadata;
	}
	public void setShowMetadata(boolean showMetadata) {
		this.showMetadata = showMetadata;
	}
	public FileType getTypeFilter() {
		return typeFilter;
	}
	public void setTypeFilter(FileType typeFilter) {
		this.typeFilter = typeFilter;
	}
	
	
}
