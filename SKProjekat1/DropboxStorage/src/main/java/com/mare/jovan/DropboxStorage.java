package com.mare.jovan;

import java.util.ArrayList;
import java.util.List;

import com.mare.jovan.file.Directory;
import com.mare.jovan.file.File;
import com.mare.jovan.file.FileType;
import com.mare.jovan.user.User;
import com.mare.jovan.util.FileUtil;

public class DropboxStorage implements IStorage{

	private static final String ROOT_DIR_PATH = "files.bin";
	
	private DropboxTransferProvider provider;
	
	private User currentUser;
	private Directory rootDir;
	
	protected DropboxStorage(User currentUser) {
		provider = DropboxTransferProvider.getInstance();
		this.currentUser = currentUser;
		rootDir = getRootDir();
	}
	
	private boolean updateRootDir() {
		return provider.update(ROOT_DIR_PATH, ROOT_DIR_PATH);
	}
	
	private Directory getRootDir() {
		boolean result = provider.download(ROOT_DIR_PATH, ROOT_DIR_PATH);
		if(result) {
			Directory d = (Directory) FileUtil.deserialize(ROOT_DIR_PATH);
			return d;
		} else {
			Directory d = new Directory("/");
			FileUtil.serialize(d, ROOT_DIR_PATH);
			result = provider.upload(ROOT_DIR_PATH, ROOT_DIR_PATH);
			if(!result) {
				System.out.println("Doslo je do greske prilikom inicijalizacije DropboxStorage.");
				System.exit(1);
			}
			return d;
		}
	}
	
	private File goToPath(String[] path) {
		Directory currDir = rootDir;
		for(int i=0;i<path.length;i++) {
			boolean changed = false;
			for(File f : currDir.getFiles()) {
				if(f.getName().equals(path[i])) {
					if(i==path.length-1) return f;
					if(f.getFileType() == FileType.Directory) {
						currDir = (Directory) f;
						changed = true;
						break;
					} else {
						return null;
					}
				}
			}
			if(!changed) return null;
		}
		return null;
	}
	
	public boolean create(Directory directory) {
		String path[] = directory.getParentPathList();
		File f = goToPath(path);
		if(f==null || f.getFileType()!=FileType.Directory) {
			return false;
		}
		((Directory)f).addFile(directory);
		if(!updateRootDir()) {
			((Directory)f).removeFile(directory);
			return false;
		}
		return true;
	}

	public boolean upload(String sourcePath, File destination) {
		if(!destination.isValid() || !FileUtil.isPathValid(sourcePath) ||
				!currentUser.getPermission().create) return false;
		String pathList[] = destination.getParentPathList();
		File f = goToPath(pathList);
		if(f==null || f.getFileType()!=FileType.Directory) return false;
		((Directory)f).addFile(destination);
		if(!updateRootDir() || !provider.upload(sourcePath, destination.getName())) {
			((Directory)f).removeFile(destination);
			return false;
		}
		return true;
	}

	public boolean download(File target, String destinationPath) {
		if(!target.isValid() || !FileUtil.isPathValid(destinationPath) ||
				currentUser.getPermission().download) return false;
		return provider.download(target.getName(), destinationPath);
	}
	
	public boolean delete(File file) {
		if(!file.isValid() || !currentUser.getPermission().delete) return false;
		String pathList[] = file.getParentPathList();
		File f = goToPath(pathList);
		if(f==null || f.getFileType()!=FileType.Directory) return false;
		((Directory)f).removeFile(file);
		if(!updateRootDir() || !provider.delete(file.getName())) {
			((Directory)f).addFile(file);
			return false;
		}
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
}
