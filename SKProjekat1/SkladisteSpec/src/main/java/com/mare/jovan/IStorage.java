package com.mare.jovan;

import java.util.List;

import com.mare.jovan.file.Directory;
import com.mare.jovan.file.File;


/**
 * Storage is responsible for data manipulation and manipulation of file structure. 
 */

public interface IStorage {
	/**
	 * Creates an empty directory
	 * @param directory new Directory instance
	 * @return enum value that determines whether the operation was successful or not
	 */
	EProcessResult create(Directory directory);
	/**
	 * Uploads given file on storage using source path.
	 * File path has to be valid, current user must have permission, file has to exist
	 * @param sourcePath path to source file
	 * @param file new File instance
	 * @return enum value that determines whether the operation was successful or not
	 */
	EProcessResult upload(String sourcePath, File file);
	/**
	 * Deletes given file from storage if the file path is valid, current user has permission and if the file exists
	 * @param file a File instance
	 * @return enum value that determines whether the operation was successful or not
	 */
	EProcessResult delete(File file);
	/**
	 * Makes a list of all files which meet given parameters
	 * @param params ListParams instance 
	 * @return list of files which meet given parameters
	 */
	List<File> list(ListParams params);
	/**
	 * Downloads given file to provided destination path.
	 * File path has to be valid, current user must have permission, file has to exist
	 * @param target File instance of the file to be downloaded
	 * @param destinationPath path to destination directory
	 * @return enum value that determines whether the operation was successful or not
	 */
	EProcessResult download(File target, String destinationPath);
}
