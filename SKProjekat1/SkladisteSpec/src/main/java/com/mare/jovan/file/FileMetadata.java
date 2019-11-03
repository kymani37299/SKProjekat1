package com.mare.jovan.file;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileMetadata implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private Date dateCreated = null;
	private String description = "No description";
	private int noDownloads = 0;
	
	public FileMetadata() {
		
	}
	
	public FileMetadata(Date dateCreated, String description) {
		super();
		this.dateCreated = dateCreated;
		this.description = description;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getNoDownloads() {
		return noDownloads;
	}
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
