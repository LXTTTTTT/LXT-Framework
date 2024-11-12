package com.lxt.framework.common.global;

// 项目使用的全局常量
public class Constant {

// 通用常量 ---------------------------------------------------------
    public static final String PLATFORM_IDENTIFIER = "110110110";  // 平台标识
    public static final int DEFAULT_PLATFORM_NUMBER = 15950044;  // 默认平台号码
    public static final int TWO_WAY_PLATFORM_NUMBER = 4207782;  // 默认平台号码
    public static int MESSAGE_TEXT = 1;  // 文本消息
    public static int MESSAGE_VOICE = 2;  // 语音消息
    public static int TYPE_SEND = 1;  // 发送消息
    public static int TYPE_RECEIVE = 2;  // 接收消息
    public static int STATE_SENDING = 20;  // 发送中
    public static int STATE_SUCCESS = 21;  // 发送成功
    public static int STATE_FAILURE = 22;  // 发送失败
// MMKV ---------------------------------------------------------
    public static final String VOICE_COMPRESSION_RATE = "voice_compression_rate";  // 改变压缩码率
    public static final String SYSTEM_NUMBER = "system_number";  // 平台号码
    public static final String ENERGY_SAVING = "energy_saving";  // 节能模式
    public static final String ENERGY_SLEEP_TIME = "energy_sleep_time";  // 睡眠时间
    public static final String VO_ONLINE_ACTIVATION_KEY = "vo_online_activation_key";  // 语音在线激活key
    public static final String PIC_ONLINE_ACTIVATION_KEY = "pic_online_activation_key";  // 图片在线激活key
    public static final String VO_OFF_ACTIVATION_VALUE = "A90A411BDBF02DBEBV";  // 离线语音key
    public static final String PIC_OFF_ACTIVATION_VALUE = "A90A411BDBF02DBEBP";  // 离线图片key
    public static final String SWIFT_MESSAGE = "swift_message";  // 快捷消息
    public static final String SWIFT_MESSAGE_SYMBOL = "/lxt/";

    public static final String USER_INFO_DATA = "user_info_data";  // 用户缓存数据
    public static final String USER_PHONE_NUMBER = "user_phone_number";  // 用户手机号
    public static final String HTTP_COOKIES_INFO = "http_cookies_info";  // cookies缓存
    public static final String SEARCH_HISTORY_INFO = "search_history_info";  // 搜索历史缓存

    public static final String FILE_VIDEO_LIST = "json/video_list.json";  // 视频列表数据json文件
// 网络请求 ---------------------------------------------------------
    public static final String BASE_URL = "https://www.wanandroid.com";
    public static final String PATH_HOTKEY = "/hotkey/json";

// 数据库 ----------------------------------------------------------
    public static final String DATABASE_NAME = "lxt_database";  // 数据库名称
    public static final String TABLE_VIDEO_LIST = "table_video_list";  // 视频列表缓存表
    public static final String MESSAGE_TABLE = "message_table";  // 消息数据表
    public static final String USER_TABLE = "user_table";

}
