package com.lxt.framework.common.utils.File;

import android.util.Log;

import com.lxt.framework.common.utils.DataUtils;

import java.io.File;
import java.io.RandomAccessFile;

// 文件写入（txt文本）
public class FileUtils3 {

    private static String TAG = "FileUtils3";

    public static void recordError(String filePath,String content) {
        try {
            String today = DataUtils.getDateString();
            String info = "**********************************\n" ;
            String curTime = DataUtils.getTimeString();  // yyyy-MM-dd HH:mm:ss
            info += curTime + "\n" ;
            info += content + "\n";
            final File kmlFile = new File(filePath+"LOG_ERROR_" + today + ".txt");
            if (!kmlFile.exists()) {
                kmlFile.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(kmlFile, "rw");
            randomAccessFile.seek(kmlFile.length());
            randomAccessFile.write(info.getBytes());
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "recordError 失败");
        }
    }

    // 记录北斗连接日志
    public static void recordBDLog(String filePath, String content) {
        try {
            String today = DataUtils.getDateString();
            String info = "**********************************\n" ;
            String curTime = DataUtils.getTimeString();  // yyyy-MM-dd HH:mm:ss
            info += curTime + "\n" ;
            info += content + "\n";
            final File kmlFile = new File(filePath+"LOG_BD_" +today+ ".txt");
            if (!kmlFile.exists()) {
                kmlFile.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(kmlFile, "rw");
            randomAccessFile.seek(kmlFile.length());
            randomAccessFile.write(info.getBytes());
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "recordLog 失败");
        }
    }

    public static void recordLog(String filePath,String logName,String content) {
        try {
            String today = DataUtils.getDateString();
            String info = "**********************************\n" ;
            String curTime = DataUtils.getTimeString();  // yyyy-MM-dd HH:mm:ss
            info += curTime + "\n" ;
            info += content + "\n";
            final File kmlFile = new File(filePath+logName+today+ ".txt");
            if (!kmlFile.exists()) {
                kmlFile.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(kmlFile, "rw");
            randomAccessFile.seek(kmlFile.length());
            randomAccessFile.write(info.getBytes());
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "recordLog 失败");
        }
    }
}
