package com.mare.jovan;

import java.util.ArrayList;
import java.util.List;

import com.mare.jovan.file.Directory;
import com.mare.jovan.file.File;
import com.mare.jovan.file.FileType;
import com.mare.jovan.user.User;
import com.mare.jovan.util.FileUtil;
import com.mare.jovan.util.ZipUtil;


/**
 * <p>This class (@code DropboxStorage) implements (@code IStorage) and 
 * is responsible for data manipulation and manipulation of file structure.</p>
 * @author Marko Sreckovic
 */
public class DropboxStorage implements IStorage{

	private static final String ROOT_DIR_PATH = "files.bin";
	
	private DropboxTransferProvider provider;

	private User currentUser;

	private Directory rootDir;

	private static final String forbiddenExstensions[] = {"exe","pdf"};
	
	/**
	 * Gets existing file structure or initializes new if it doesn't exist.
	*	@param currentUser represents current logged in user info
	*/
	protected DropboxStorage(User currentUser) {
		provider = DropboxTransferProvider.getInstance();
		this.currentUser = currentUser;
		rootDir = getRootDir();
	}
	
	private boolean validExstension(String name) {
		String extSplit[] = name.split("\\.");
		if(extSplit.length==0) return true;
		String ext = extSplit[extSplit.length-1];
		for(int i=0;i<forbiddenExstensions.length;i++) {
			if(ext.equals(forbiddenExstensions[i])) return false;
		}
		return true;
	}
	
	private boolean updateRootDir() {
		FileUtil.serialize(rootDir, ROOT_DIR_PATH);
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
		if(path.length==0) return rootDir;
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
	
	/**
	 * {@inheritDoc}
	 */
	public EProcessResult create(Directory directory) {
		String path[] = directory.getParentPathList();
		File f = goToPath(path);
		if(f==null || f.getFileType()!=FileType.Directory) {
			return EProcessResult.DEST_NOT_VALID;
		}
		((Directory)f).addFile(directory);
		if(!updateRootDir()) {
			((Directory)f).removeFile(directory);
			return EProcessResult.PROCESS_FAILED;
		}
		return EProcessResult.PROCESS_SUCCESS;
	}

	/**
	 * {@inheritDoc}
	 */
	public EProcessResult upload(String sourcePath, File destination) {
		if(!destination.isValid()) {
			return EProcessResult.DEST_NOT_VALID;
		}
		if(!FileUtil.isPathValid(sourcePath)) {
			return EProcessResult.SOURCE_NOT_VALID;
		}
		if(!currentUser.getPermission().create) {
			return EProcessResult.DENIED_ACCESS;
		}
		if(!validExstension(destination.getName())) {
			return EProcessResult.EXTENSION_FORBIDDEN;
		}
		String parentPathList[] = destination.getParentPathList();
		File f = goToPath(parentPathList);
		if(f==null || f.getFileType()!=FileType.Directory) return EProcessResult.DEST_NOT_VALID;
		((Directory)f).addFile(destination);
		boolean isDirectory = FileUtil.isDirectory(sourcePath);
		if(isDirectory) {
			ZipUtil.zipFolder(sourcePath);
			sourcePath += ".zip";
		}
		if(!updateRootDir() || !provider.upload(sourcePath, destination.getPath())) {
			((Directory)f).removeFile(destination);
			updateRootDir();
			return EProcessResult.PROCESS_FAILED;
		}
		
		FileUtil.deleteFile(sourcePath);
		
		return EProcessResult.PROCESS_SUCCESS;
	}


	/**
	 * {@inheritDoc}
	 */
	public EProcessResult download(File target, String destinationPath) {
		if(!target.isValid()) {
			return EProcessResult.DEST_NOT_VALID;
		}
		if(!FileUtil.isPathValid(destinationPath)) {
			return EProcessResult.SOURCE_NOT_VALID;
		}
		if(!currentUser.getPermission().download) {
			return EProcessResult.DENIED_ACCESS;
		}
		if(!provider.download(target.getPath(), destinationPath + "/" + target.getName())) {
			return EProcessResult.PROCESS_FAILED;
		}
		File file = goToPath(target.getPathList());
		if(file!=null) {
			file.getMetadata().incNoDownloads();
			updateRootDir();
		}
		return EProcessResult.PROCESS_SUCCESS;
	}
	
	/**
	 * {@inheritDoc}
	 */
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
		if(f==null || f.getFileType()!=FileType.Directory) return EProcessResult.DEST_NOT_VALID;
		((Directory)f).removeFile(file);
		if(!updateRootDir()) {
			return EProcessResult.PROCESS_FAILED;
		}
		if(file.getFileType()==FileType.File && !provider.delete(file.getPath())) {
			((Directory)f).addFile(file);
			updateRootDir();
			return EProcessResult.PROCESS_FAILED;
		}
		
		return EProcessResult.PROCESS_SUCCESS;
	}
	
	/**
	 * {@inheritDoc}
	 */
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
			if(params.getTypeFilter()!=null && params.getTypeFilter()==FileType.File) {
				return;
			}
			if(params.getNameFilter()!=null && !file.getName().contains(params.getNameFilter())) {
				return;
			}
			if(params.getExtFilter()!=null) {
				return;
			}
		} else {
			if(params.getTypeFilter()!=null && params.getTypeFilter()==FileType.Directory) {
				return;
			}
			
			if(params.getNameFilter()!=null && !file.getName().contains(params.getNameFilter())) {
				return;
			}
			if(params.getExtFilter()!=null) {
				String extList[] = file.getName().split("\\.");
				if(extList.length==0)  {
					return;
				}
				if(!extList[extList.length-1].equals(params.getExtFilter())) {
					return;
				}
			}
		}
		files.add(file);
	}
}


