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

    private String APP_PATH = "MichaelDai/";

    private String DB_PATH = "EZX.db";

    public String getROOT_PATH() {
        if (SystemUtils.currentSystem() == SystemUtils.MacOS) {
            return System.getProperty("user.home") + "/library/Application Support/";
        } else if (SystemUtils.currentSystem() == SystemUtils.Windows) {
            return System.getProperty("java.io.tmpdir") + "/";
        } else {
            return System.getProperty("user.home") + "/";
        }
    }

    public String getAPP_PATH() {
        return getROOT_PATH() + APP_PATH;
    }

    public String getDB_PATH() {
        return getAPP_PATH() + DB_PATH;
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
}
