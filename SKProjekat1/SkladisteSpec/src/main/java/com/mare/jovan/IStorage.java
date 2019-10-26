package com.mare.jovan;

import java.util.List;

import com.mare.jovan.file.File;

public interface IStorage {
	void create(File file);
	void upload(File source, File destination);
	void upload(List<File> files, File destination);
	void delete(File file);
	List<File> list(ListParams params);
	void download(File target, File destination);
}
