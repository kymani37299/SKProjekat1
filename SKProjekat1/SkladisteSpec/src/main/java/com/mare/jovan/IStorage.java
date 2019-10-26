package com.mare.jovan;

import java.util.List;

import com.mare.jovan.file.Directory;
import com.mare.jovan.file.File;

public interface IStorage {
	boolean create(Directory directory);
	boolean upload(String sourcePath , File destination);
	boolean upload(List<String> sourcePathList, File destination);
	boolean delete(File file);
	List<File> list(ListParams params);
	boolean download(File target, String destinationPath);
}
