package com.mare.jovan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZipUtil class provides file zipping functionality
 *
 */
public class ZipUtil {

    private static List<String> fileList;

    /**
     * Zips file from given pathname and creates new instance
     * @param sourcePath the pathname to file
     */
    public static void zipFolder(String sourcePath) {
    	fileList = new ArrayList<String>();
    	generateFileList(sourcePath,new File(sourcePath));
    	zipIt(sourcePath,sourcePath+".zip");
    }
    
    private static void generateFileList(String sourcePath,File node) {
        if (node.isFile()) {
            fileList.add(generateZipEntry(sourcePath,node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(sourcePath,new File(node, filename));
            }
        }
    }

    private static String generateZipEntry(String sourcePath,String file) {
        return file.substring(sourcePath.length() + 1, file.length());
    }

    private static void zipIt(String sourcePath,String zipFile) {
        byte[] buffer = new byte[1024];
        String source = new File(sourcePath).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            FileInputStream in = null;

            for (String file: ZipUtil.fileList) {
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(sourcePath + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}