package com.lxt.framework.common.global;

import android.util.Log;
import com.tencent.mmkv.MMKV;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 项目使用的全局变量
public class Variable {
    private static String TAG = "Variable";
    public static String getSwiftMsg(){return MMKV.defaultMMKV().decodeString(Constant.SWIFT_MESSAGE, "");}
    public static void setSwiftMsg(String commands){MMKV.defaultMMKV().encode(Constant.SWIFT_MESSAGE, commands);}
    public static void addSwiftMsg(String command){
        String commands = command+Constant.SWIFT_MESSAGE_SYMBOL +getSwiftMsg();
        setSwiftMsg(commands);
    }
    public static void removeSwiftMsg(int position){
        String commands_str = getSwiftMsg();
        String new_commands = "";
        Log.e(TAG, "拿到快捷消息: "+commands_str);
        if(commands_str==null||commands_str.equals("")){return;}
        String[] commands_arr = commands_str.split(Constant.SWIFT_MESSAGE_SYMBOL);
        List<String> commands_list = Arrays.asList(commands_arr);
        for (int i = 0; i < commands_list.size(); i++) {
            if(i==position){continue;}
            new_commands += commands_list.get(i)+Constant.SWIFT_MESSAGE_SYMBOL;
        }
        setSwiftMsg(new_commands);
    }
    public static void replaceSwiftMsg(int position, String command){
        String[] messages = getSwiftMsg().split(Constant.SWIFT_MESSAGE_SYMBOL);
        messages[position]=command;
        String newMessages = Arrays.stream(messages)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.joining(Constant.SWIFT_MESSAGE_SYMBOL));
        setSwiftMsg(newMessages);
    }

}
