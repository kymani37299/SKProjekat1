package com.mare.jovan;

import java.util.List;

import com.mare.jovan.file.Directory;
import com.mare.jovan.file.File;

public interface IStorage {
	EProcessResult create(Directory directory);
	EProcessResult upload(String sourcePath, File file);
	EProcessResult delete(File file);
	List<File> list(ListParams params);
	EProcessResult download(File target, String destinationPath);
}
