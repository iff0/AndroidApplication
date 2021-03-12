package com.example.project.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.example.project.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class DataManageUtil {
    public static final long MB = 1024 * 1024;

    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(getDiskCacheDir(context));
    }

    public static long getCacheDirSize(Context context) {
        return getSizeOfDirectory(getDiskCacheDir(context));
    }

    private static File assertFile(String path) {
        File tmpFile = new File(path);
        if (!tmpFile.getParentFile().exists()) {
            tmpFile.getParentFile().mkdirs();
        }
        return tmpFile;
    }

    private static File assertDir(String path) {
        File tmpFile = new File(path);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        return tmpFile;
    }

    private static long getSizeOfDirectory(File directory) {
        int res = 0;
        if (directory != null && directory.exists()) {
            if (directory.isFile()) {
                return directory.length();
            }
            else {
                for (File item : directory.listFiles()) {
                    res += getSizeOfDirectory(item);
                }
            }
        }
        return res;
    }

    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                if (item.isFile()) {
                    item.delete();
                }
                else {
                    deleteFilesByDirectory(item);
                }
            }
            directory.delete();
        }
    }

    public static File getDiskCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir();
        } else {
            return context.getCacheDir();
        }
    }

    public static File saveLocalImg(Context context, Bitmap rawImg, Bitmap processedImg) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String imgDirPath = context.getFilesDir().getPath() + File.separator + "image" + File.separator + timeStamp;
        File rawImgFile = assertFile( imgDirPath + File.separator + "raw.png");
        File processedImgFile = assertFile(imgDirPath + File.separator + "processed.png");
        try {
            //文件输出流
            FileOutputStream fileOutputStream1 = new FileOutputStream(rawImgFile);
            //压缩图片，如果要保存png，就用Bitmap.CompressFormat.PNG，要保存jpg就用Bitmap.CompressFormat.JPEG,质量是100%，表示不压缩
            rawImg.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream1);
            fileOutputStream1.flush();
            fileOutputStream1.close();
            if (AppConfig.APP_DEBUG) Toast.makeText(context,"写入成功！目录"+ rawImgFile.getAbsolutePath() ,Toast.LENGTH_SHORT).show();
            FileOutputStream fileOutputStream2 = new FileOutputStream(processedImgFile);
            processedImg.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream2);
            fileOutputStream2.flush();
            fileOutputStream2.close();

            if (AppConfig.APP_DEBUG) Toast.makeText(context,"写入成功！目录"+ processedImgFile.getAbsolutePath() ,Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //失败的提示
            Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return new File(imgDirPath);
    }

    public static void deleteAllLocalImage(Context context) {
        File directory = new File(context.getFilesDir().getPath() + File.separator + "image");
        deleteFilesByDirectory(directory);
    }

    public static ArrayList<File> getImgDirNameList(Context context) {
        File imgDir = assertDir(context.getFilesDir().getPath() + File.separator + "image");
        ArrayList<File> ls = new ArrayList<File>();
        for (File item : imgDir.listFiles()) {
            if (item.isDirectory()) {
                ls.add(item);
            }
        }
        Collections.sort(ls, Collections.reverseOrder());
        return ls;

    }

    public static Bitmap getBitmapByFile(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        Bitmap bitmap  = BitmapFactory.decodeStream(fis);
        return bitmap;
    }
}

