package com.mare.jovan.file;

import java.io.Serializable;
import java.util.Date;

public class FileMetadata implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Date dateCreated = null;
	private String owner = null;
	private String description = "No description";
	private int noDownloads = 0;
	
	public FileMetadata() {
		
	}
	
	public FileMetadata(Date dateCreated, String owner, String description) {
		super();
		this.dateCreated = dateCreated;
		this.owner = owner;
		this.description = description;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
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
	public void setNoDownloads(int noDownloads) {
		this.noDownloads = noDownloads;
	}
	
	
	
}
