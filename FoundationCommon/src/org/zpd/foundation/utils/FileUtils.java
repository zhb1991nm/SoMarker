package org.zpd.foundation.utils;

import java.io.*;

/**
 * @User: hb.zhang
 * @Date: 2015/11/4
 * @Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class FileUtils {

    static FileUtils INSTANCE = null;

    static public FileUtils getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new FileUtils();
        }
        INSTANCE.init();
        return INSTANCE;
    }

    private FileUtils() {

    }

    public static final String csvNullString = "null";

    private final String APP_PATH = "SoMarker" + File.separator;

    private final String DB_PATH = "SoMarker.db";

    private final String IMAGE_PATH = "images" + File.separator;

    public String getROOT_PATH() {
        if (SystemUtils.currentSystem().equals(SystemUtils.MacOS)) {
            return System.getProperty("user.home") + "/library/Application Support/";
        } else if (SystemUtils.currentSystem().equals(SystemUtils.Windows)) {
//            return System.getProperty("java.io.tmpdir") + "/";
            return System.getenv("APPDATA") + File.separator;
        } else {
            return System.getProperty("user.home") + File.separator;
        }
    }

    public String getAPP_PATH() {
        return getROOT_PATH() + APP_PATH;
    }

    public String getDB_PATH() {
        return getAPP_PATH() + DB_PATH;
    }

    public String getIMAGE_PATH() {
        return getAPP_PATH() + IMAGE_PATH;
    }

    private void init() {
        makeDir(getAPP_PATH());
    }

    private boolean makeDir(String path) {
        File file = new File(path);
        if (!file.exists())
            return file.mkdir();
        return true;
    }

    private void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }


    static public void writeToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        int ch = 0;
        while ((ch = is.read()) != -1) {
            fos.write(ch);
        }
        is.close();
        fos.close();
    }

    static public boolean copyFile(File fromFile, File toFile) {
        int byteread = 0;
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(fromFile);//可替换为任何路径何和文件名
            out = new FileOutputStream(toFile);//可替换为任何路径何和文件名
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static public boolean copyFile(InputStream in, File toFile) {
        int byteread = 0;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(toFile);//可替换为任何路径何和文件名
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearAppDirectory(){
        clearDirectory(new File(this.getAPP_PATH()));
    }

    public static void clearDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 如果是子目录，递归调用clearDirectory
                        clearDirectory(file);
                    } else {
                        // 删除文件
                        if (!file.delete()) {
                            System.out.println("无法删除文件: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}
