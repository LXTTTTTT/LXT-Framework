package com.lxt.framework.common.global;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class Permissions {
    private static String TAG = "Permissions";
// 必要权限 -------------------------------------------------------
    public static final String[] PERMISSION_LIST = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
    };
// 权限检测 -------------------------------------------------------
    // 所有文件权限
    public static boolean checkFilePermission(){
        boolean had = false;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            if(!Environment.isExternalStorageManager()){
                Log.e(TAG, "没有文件权限");
                had=false;
            }else{
                had=true;
                Log.e(TAG, "已有文件权限");
            }
        }else{
            had=true;
            Log.e(TAG, "已有文件权限");
        }
        return had;
    }

    // 安装权限
    public static boolean checkInstallPermission(Activity activity){
        boolean had = false;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            if(!activity.getPackageManager().canRequestPackageInstalls()){
                Log.e(TAG, "没有安装权限");
                had=false;
            }else{
                had=true;
                Log.e(TAG, "已有安装权限");
            }
        }else{
            had=true;
            Log.e(TAG, "已有安装权限");
        }
        return had;
    }
// 请求码 ---------------------------------------------------------
    public static final int PERMISSION_CODE_FILE = 10086;
    public static final int PERMISSION_CODE_INSTALL = 10087;


}
