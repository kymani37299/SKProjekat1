package com.mare.jovan;

import com.mare.jovan.file.File;
import com.mare.jovan.file.FileType;

/**
 * ListParams class is used to determine which filters should be included while
 * listing files and directories and what are their values
 *
 */

public class ListParams {
	
	private String nameFilter = null;
	private String extFilter = null;
	private File path = null;
	private boolean showMetadata = false;
	private FileType typeFilter = null;
	
	/**
	 * Returns null if name filter is not set up
	 * @return null if name filter is not set up
	 */
	public String getNameFilter() {
		return nameFilter;
	}
	/**
	 * Sets name filter to provided value
	 * @param nameFilter the nameFilter string
	 */
	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}
	/**
	 * Returns null if extension filter is not set up
	 * @return null if extension filter is not set up
	 */
	public String getExtFilter() {
		return extFilter;
	}
	/**
	 * Sets extension filter to provided value
	 * @param extFilter the extFilter string
	 */
	public void setExtFilter(String extFilter) {
		this.extFilter = extFilter;
	}
	/**
	 * Returns null if path is not set up
	 * @return null if path is not set up
	 */
	public File getPath() {
		return path;
	}
	/**
	 * Sets file path to provided value
	 * @param path File instance
	 */
	public void setPath(File path) {
		this.path = path;
	}
	/**
	 * Returns true if showMetadata filter is set to true
	 * @return returns true if showMetadata filter is set to true;otherwise returns false
	 */
	public boolean isShowMetadata() {
		return showMetadata;
	}
	/**
	 * Sets showMetadata filter value to provided value
	 * @param showMetadata
	 */
	public void setShowMetadata(boolean showMetadata) {
		this.showMetadata = showMetadata;
	}
	/**
	 * Returns null if file type filter is not set up
	 * @return null if file type filter is not set up
	 */
	public FileType getTypeFilter() {
		return typeFilter;
	}
	/**
	 * Sets file type filter to provided value
	 * @param typeFilter FileType instance
	 */
	public void setTypeFilter(FileType typeFilter) {
		this.typeFilter = typeFilter;
	}
	
	
}
