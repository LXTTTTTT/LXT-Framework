package com.lxt.framework.common.global;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.Arrays;

public enum Path {

    MAIN_PATH("",null, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LXT"),

    DCM_PATH("dcm", MAIN_PATH),
    LOGGER_PATH("logger", MAIN_PATH),
    MMKV_PATH("mmkv", MAIN_PATH),
    IMAGE_PATH("image", MAIN_PATH),
    TEST_PATH("test", MAIN_PATH),
    TEMP_PATH("temp",MAIN_PATH);

    private final String name;
    private final Path parent;
    private String path;

    Path(String name, Path parent) {
        this(name,parent,"");
    }

    Path(String name, Path parent, String path) {
        this.name = name;
        this.parent = parent;
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    private static final String TAG = "Path";
    public static void init(){
        Arrays.stream(values())
                .filter(value ->
                        // 过滤条件：名称不为空且路径为空或路径不存在
                        !TextUtils.isEmpty(value.name) && (TextUtils.isEmpty(value.path) || !isFileExist(value.path))
                )
                .peek(value -> {
                    // 创建文件夹并设置路径
                    File file = new File(value.parent.getPath(), value.name);
                    boolean orExistsDir = file.exists() || file.mkdirs();
                    value.setPath(file.getPath());
                    Log.d(TAG, "需要创建目录：" + value.parent.name + "/" + value.name + " --- " + orExistsDir);
                })
                .forEach(value -> {
                    // 输出跳过创建的日志
                    Log.d(TAG, "跳过创建：" + value.name());
                });
    }

    public static boolean isFileExist(String path){
        File file = new File(path);
        return file.exists();
    }

    public static String getFileName(String path){
        File file = new File(path);
        return file.exists()? file.getPath():"";
    }

    public static String getDCMPath(){
        return DCM_PATH.getPath();
    }
    public static String getLOGGERPath(){
        return LOGGER_PATH.getPath();
    }
    public static String getMMKVPath(){
        return MMKV_PATH.getPath();
    }
    public static String getIMAGEPath(){
        return IMAGE_PATH.getPath();
    }
    public static String getTEMPPath(){
        return TEMP_PATH.getPath();
    }
    public static String getTESTPPath(){
        return TEST_PATH.getPath();
    }
}
