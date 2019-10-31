package com.mare.jovan;

import java.util.ArrayList;
import java.util.List;

import com.mare.jovan.file.Directory;
import com.mare.jovan.file.File;
import com.mare.jovan.file.FileType;
import com.mare.jovan.user.User;
import com.mare.jovan.util.FileUtil;

public class LocalStorage implements IStorage {

	private static final String ROOT_DIR_PATH = "files.bin";
	
	private User currentUser;
	private Directory rootDir;
	
	private boolean rootInitialized;
		
	protected LocalStorage(User currentUser) {
		this.rootInitialized = false;
		this.currentUser = currentUser;		
		this.rootDir = getDirectory();
		
	}
	
	private void updateFiles() {
		FileUtil.serialize(rootDir, ROOT_DIR_PATH);
	}
	
	private Directory getDirectory() {
		Directory d;
		if(!rootInitialized) {
			d = (Directory) FileUtil.deserialize(ROOT_DIR_PATH);
		}	else {
			d = new Directory("/");
			FileUtil.serialize(d, ROOT_DIR_PATH);
			rootInitialized = true;
		}
		return d;
	}
	
	private File goToPath(String[] path) {
		Directory currDir = rootDir;
		for(int i=0;i<path.length;i++) {
			boolean changed = false;
			for(File f: currDir.getFiles()) {
				if(f.getName().equals(path[i])) {
					if(i==path.length-1)	return f;
					if(f.getFileType() == FileType.Directory) {
						currDir = (Directory) f;
						changed = true;
						break;
					} else {
						return null;
					}
				}
			}
			if(!changed)	return null;
		}
		return null;
	}

	public boolean create(Directory directory) {
		String[] path = directory.getParentPathList();
		File f = this.goToPath(path);
		if(f==null || f.getFileType()!=FileType.Directory)
			return false;
		
		((Directory)f).addFile(directory);
		this.updateFiles();
		return true;
	}

	public boolean upload(String sourcePath, File file) {
		if(!file.isValid() || !FileUtil.isPathValid(sourcePath) || !currentUser.getPermission().create)
			return false;
		
		String[] path = file.getParentPathList();
		File f = this.goToPath(path);
		if(f==null || f.getFileType()!=FileType.Directory)
			return false;
		
		FileUtil.copyFiles(sourcePath, file.getPath());
		((Directory)f).addFile(file);
		this.updateFiles();
		
		return true;
	}

	public boolean delete(File file) {
		if(!file.isValid() || !currentUser.getPermission().delete)
			return false;
		
		String pathList[] = file.getParentPathList();
		File f = goToPath(pathList);
		if(f==null || f.getFileType()!=FileType.Directory)
			return false;
		
		FileUtil.deleteFile(file.getPath());
		((Directory)f).removeFile(file);
		this.updateFiles();
		
		return true;
	}

	public List<File> list(ListParams params) {
		List<File> files = new ArrayList<File>();
		Directory currDir = rootDir;
		if(params.getPath()!=null) {
			File f = goToPath(params.getPath().getPathList());
			if(f!=null && f.getFileType()==FileType.Directory) {
				currDir = (Directory) f;
			} else {
				return files;
			}
		}
		list(params,files,currDir);
		return files;
	}
	
	private void list(ListParams params,List<File> files,File file) {
		if(file.getFileType()==FileType.Directory) {
			if(params.getTypeFilter()!=null && params.getTypeFilter()==FileType.Directory) {
				files.add(file);
			}
			for(File f : ((Directory)file).getFiles()) {
				list(params,files,f);
			}
		} else {
			if(params.getTypeFilter()!=null && params.getTypeFilter()!=FileType.File) {
				return;
			}
			
			if(params.getNameFilter()!=null && !file.getName().contains(params.getNameFilter())) {
				return;
			}
			
			if(params.getExtFilter()!=null) {
				String ext[] = file.getName().split(".");
				if(ext.length<2 || !ext[ext.length-1].equals(params.getExtFilter())) {
					return;
				}
			}
			files.add(file);
		}
	}

	public boolean download(File target, String destinationPath) {
		if(!target.isValid() || !FileUtil.isPathValid(destinationPath) || currentUser.getPermission().download) 
			return false;
		
		FileUtil.copyFiles(target.getPath(), destinationPath);
		return true;
	}
}

