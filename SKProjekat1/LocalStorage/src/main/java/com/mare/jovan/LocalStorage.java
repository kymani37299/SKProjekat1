package com.mare.jovan;

import java.util.ArrayList;
import java.util.List;

import com.mare.jovan.file.Directory;
import com.mare.jovan.file.File;
import com.mare.jovan.file.FileType;
import com.mare.jovan.user.User;
import com.mare.jovan.util.FileUtil;
import com.mare.jovan.util.ZipUtil;

public class LocalStorage implements IStorage {

	private ArrayList<String> invalidExtensions = new ArrayList<String>();
	
	protected static String ROOT_DIR_PATH = System.getProperty("user.home").concat("/Desktop/root");
	private static String FILES_PATH = ROOT_DIR_PATH.concat("/files.bin");
	
	private User currentUser;
	private Directory rootDir;
	
	protected LocalStorage(User currentUser) {
		this.currentUser = currentUser;		
		this.rootDir = getDirectory();
		
	}
	
	protected static void updateRootPath(String path) {
		if(path.charAt(path.length()-1)!='/' || path.charAt(path.length()-1)!='\\') {
			path += "/";
		}
		ROOT_DIR_PATH = path;
		FILES_PATH = ROOT_DIR_PATH + "files.bin";
	}
	
	private void updateFiles() {
		FileUtil.serialize(rootDir, FILES_PATH);
	}
	
	private Directory getDirectory() {
		Directory d;
		if(FileUtil.fileExists(FILES_PATH)) {
			d = (Directory) FileUtil.deserialize(FILES_PATH);
		}	else {
			d = new Directory("/");
			FileUtil.serialize(d, FILES_PATH);
		}
		return d;
	}
	
	private File goToPath(String[] path) {
		if(path.length==0) return rootDir;
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

	public EProcessResult create(Directory directory) {
		String[] path = directory.getParentPathList();
		File f = this.goToPath(path);
		if(f==null || f.getFileType()!=FileType.Directory)
			return EProcessResult.DEST_NOT_VALID;
		
		((Directory)f).addFile(directory);
		this.updateFiles();
		return EProcessResult.PROCESS_SUCCESS;
	}

	public EProcessResult upload(String sourcePath, File file) {
		if(!file.isValid()) {
			return EProcessResult.DEST_NOT_VALID;
		}
		if(!FileUtil.isPathValid(sourcePath)) {
			return EProcessResult.SOURCE_NOT_VALID;
		}
		if(!currentUser.getPermission().create) {
			return EProcessResult.DENIED_ACCESS;
		}
		if(checkExtension(file.getName())) {
			return EProcessResult.EXTENSION_FORBIDDEN;
		}
		String[] path = file.getParentPathList();
		File f = this.goToPath(path);
		if(f==null || f.getFileType()!=FileType.Directory)
			return EProcessResult.DEST_NOT_VALID;
		
		boolean isDirectory = FileUtil.isDirectory(sourcePath);
		
		if(isDirectory) {
			ZipUtil.zipFolder(sourcePath);
			sourcePath += ".zip";
		}
		
		if(!FileUtil.copyFiles(sourcePath, ROOT_DIR_PATH.concat(file.getPath())))
			return EProcessResult.PROCESS_FAILED;
		
		if(isDirectory) {
			FileUtil.deleteFile(sourcePath);
		}
		
		((Directory)f).addFile(file);
		this.updateFiles();
		
		return EProcessResult.PROCESS_SUCCESS;
	}
	
	
	public EProcessResult delete(File file) {
		if(!file.isValid()) {
			return EProcessResult.DEST_NOT_VALID;
		}
		if(!currentUser.getPermission().delete) {
			return EProcessResult.DENIED_ACCESS;
		}
		String parentPathList[] = file.getParentPathList();
		String pathList[] = file.getPathList();
		file = goToPath(pathList);
		if(file.getFileType()==FileType.Directory) {
			ArrayList<File> files = new ArrayList<File>();
			for(File child : ((Directory)file).getFiles()) {
				files.add(child);
			}
			for(File child : files) {
				if(delete(child)!=EProcessResult.PROCESS_SUCCESS) return EProcessResult.PROCESS_FAILED;
			}
		}
		File f = goToPath(parentPathList);
		if(f==null || f.getFileType()!=FileType.Directory)
			return EProcessResult.DEST_NOT_VALID;
		if(!FileUtil.deleteFile(ROOT_DIR_PATH.concat(file.getPath())))
			return EProcessResult.PROCESS_FAILED;
		((Directory)f).removeFile(file);
		this.updateFiles();
		return EProcessResult.PROCESS_SUCCESS;
		
	}

	public List<File> list(ListParams params) {
		List<File> files = new ArrayList<File>();
		Directory currDir = rootDir;
		if(params.getPath()!=null) {
			File f = goToPath(params.getPath().getPathList());
			if(f!=null && f.getFileType()==FileType.Directory) {
				currDir = (Directory) f;
			}
		}
		list(params,files,currDir);
		return files;
	}
	
	private void list(ListParams params,List<File> files,File file) {
		if(file.getFileType()==FileType.Directory) {
			for(File f : ((Directory)file).getFiles()) {
				list(params,files,f);
			}
			if(params.getTypeFilter()!=null && params.getTypeFilter()==FileType.Directory) {
				return;
			}
			if(params.getNameFilter()!=null && !file.getName().contains(params.getNameFilter())) {
				return;
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
		}
		files.add(file);
	}

	public EProcessResult download(File target, String destinationPath) {
		destinationPath = destinationPath.concat("/"+target.getName());
		if(!target.isValid()) {
			return EProcessResult.DEST_NOT_VALID;
		}
		if(!FileUtil.isPathValid(destinationPath)) {
			return EProcessResult.SOURCE_NOT_VALID;
		}
		if(!currentUser.getPermission().download) {
			return EProcessResult.DENIED_ACCESS;
		}
		
		if(!FileUtil.copyFiles(ROOT_DIR_PATH.concat(target.getPath()), destinationPath)) {
			return EProcessResult.PROCESS_FAILED;
		}
		
		File file = goToPath(target.getPathList());
		if(file!=null) {
			file.getMetadata().incNoDownloads();
			updateFiles();
		}
		return EProcessResult.PROCESS_SUCCESS;
	}
	
	public void addExtension(String ext) {
		this.invalidExtensions.add(ext);
	}
	
	public boolean checkExtension(String name) {
		String[] paths = name.split("\\.");
		if(paths.length==0) return false;
		return invalidExtensions.contains(paths[paths.length-1]);
	}

}
