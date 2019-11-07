package com.mare.jovan.file;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FileMetadata class is used to provide more information about certain File.
 * It describes when file was created, how many times it was downloaded and if there is some description about file.
 */

public class FileMetadata implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private Date dateCreated = null;
	private String description = "No description";
	private int noDownloads = 0;
	
	/**
	 * Initializes new FileMetadata instance
	 */
	public FileMetadata() {
		
	}
	
	/**
	 * Initializes new FileMetadata instance and sets date and description with provided values
	 * @param dateCreated the Date value
	 * @param description description string
	 */
	public FileMetadata(Date dateCreated, String description) {
		super();
		this.dateCreated = dateCreated;
		this.description = description;
	}
	
	/**
	 * Returns the date when file was created
	 * @return the date when file was created
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	
	/**
	 * Sets dateCreated field to provided value
	 * @param dateCreated Date instance
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	/**
	 * Returns the file description
	 * @return the file description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets file description to provided value
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Returns the number of times file was downloaded
	 * @return the number of times file was downloaded
	 */
	public int getNoDownloads() {
		return noDownloads;
	}
	
	/**
	 * Increments number of times the file was downloaded
	 */
	public void incNoDownloads() {
		noDownloads++;
	}
	
	@Override
	public String toString() {
		String output = "Created: " + dateFormat.format(dateCreated);
		output += " Description: " + description;
		output += " No downloads: " + noDownloads;
		return output;
	}
	
}
