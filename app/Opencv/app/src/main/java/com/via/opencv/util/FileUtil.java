package com.via.opencv.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nick on 17-12-1.
 */

public class FileUtil {

    public static void rename(String path,String append)
    {
        File file = new File(path);
        String newFileName = file.getParent() + "/" + append + file.getName();
        System.out.println("file path: " + path + "\n" + "rename path: " + newFileName);
        file.renameTo(new File(newFileName));
    }

    public static void write(byte[] datas, String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            out.write(datas);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }





}
