package com.example.pc_0775.naugthyvideo.Constants;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import kotlin.text.Regex;
import okhttp3.MediaType;

/**
 * Created by PC-0775 on 2018/8/18.
 */

public class Constants {

    //url
    /**
     *  http://api.xkapi.xyz/index/g/guochan/?yeshu=1
     */
    public static final String CHINA_VIDEO_URL = "http://api.xkapi.xyz/index/g/guochan/";
    /**
     * http://api.xkapi.xyz/index/g2/fenlei4/?leixing=se55&yeshu=1
     */
    public static final String CLASS_ONE_VIDEO_URL = "http://api.xkapi.xyz/index/g2/fenlei4/";
    /**
     * http://api.xkapi.xyz/index/g2/fenlei5/?leixing=movielist1&yeshu=1
     */
    public static final String CLASS_TWO_VIDEO_URL = "http://api.xkapi.xyz/index/g2/fenlei4/";
    /**
     * http://api.xkapi.xyz/index/g/fenlei3/?leixing=toupaizipai&yeshu=1
     */
    public static final String CLASS_THREE_VIDEO_URL = "http://api.xkapi.xyz/index/g2/fenlei4/";
    /**
     * http://new.xkapi.xyz/index/g/oumei/?yeshu=1
     */
//    public static final String EUROPE_VIDEO_URL = "http://new.xkapi.xyz/index/g/oumei/";

    /**
     * http://new.xkapi.xyz/index/g/av/?yeshu=1&type=23 cartoon
     * http://new.xkapi.xyz/index/g/av/?yeshu=1&type=21 no
     * http://new.xkapi.xyz/index/g/av/?yeshu=1&type=22 yes
     * http://new.xkapi.xyz/index/g/av/?yeshu=1&type=18 中文
     */
//    public static final String CARTOON_VIDEO_URL = "http://new.xkapi.xyz/index/g/av/";
    /**
     * http://new.xkapi.xyz/index/g/zhibo/
     */
    public static final String LIVE_PLATFORM_URL = "http://new.xkapi.xyz/index/g/zhibo/";
    /**
     * http://new.xkapi.xyz/index/g/zhubolist/?url=jsonchunban.txt
     */
    public static final String LIVE_ROOM_URL = "http://new.xkapi.xyz/index/g/zhubolist/";
    /**
     * 测试用的静态地址
     */
    public static String TEST_VIDEO_URL = "http://api.xkapi.xyz/index/g/fenlei3/?leixing=toupaizipai&yeshu=1";
    //以上接口已经废弃-------------------------------------------------------------------------------------------------

    //直播
    public static String MIMI_LIVE_URL = "http://mimiapp.hanya168.com/rar.ashx?action=homelive";
    //video地址
    //http://mimiapp.hanya168.com/rar.ashx?action=getv&pagesize=50&pageindex=1&type=1
    public static String EUROPE_VIDEO_URL = "http://mimiapp.hanya168.com/rar.ashx?action=getv&pagesize=50";
    public static String JAPAN_VIDEO_URL = "http://mimiapp.hanya168.com/rar.ashx?action=getv&pagesize=50";
    public static String CARTOON_VIDEO_URL = "http://mimiapp.hanya168.com/rar.ashx?action=getv&pagesize=50";
    //video地址参数
    public static final String VIDEO_PARAMTER_PAGEINDEX = "pageindex";
    public static final String VIDEO_PARAMTER_TYPE = "type";


    //接口
    public static String WEREWOLF_URL = "https://www.86rrxx.com/";

    /**
     * 豆瓣top250 api接口
     */
    public static String DOUBAN_MOVIE_TOP_250_URL = "http://api.douban.com/v2/movie/top250";
    /**
     * 最新电影：https://api.douban.com/v2/movie/in_theaters?start=0&count=10
     */
    public static String DOUBAN_LATEST_MOVIE_URL = "https://api.douban.com/v2/movie/in_theaters";
    /**
     * 电影详情：https://api.douban.com/v2/movie/subject/1292052
     */
    public static String DOUBAN_MOVIE_DETAIL_URL = "https://api.douban.com/v2/movie/subject/";

    /**
     * 登录接口
     */
    public static final String LOGIN_URL = "http://101.201.236.217:8888/login";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded");//; charset=utf-8

    //paramter
    public static final String PAGE_NUMBER = "yeshu";

    //videoType
    public static final int VIDEO_TYPE = 1;
    public static final int LIVE_TYPE = 2;
    //handler的what常量
    public static final int CLASS_ONE_REQUEST = 3101;
    public static final int CLASS_TWO_REQUEST = 3102;
    public static final int CLASS_THREE_REQUEST = 3103;
    public static final int EUROPE_VIDEO_REQUEST = 3104;
    public static final int JAPAN_VIDEO_REQUEST = 3105;
    public static final int CARTOON_VIDEO_REQUEST = 3106;
    public static final int LIVE_PLATFORM_REQUEST = 3107;
    public static final int LIVE_ROOM_REQUEST = 3108;
    public static final int LIVE_PLATFORM_MIMI_REQUEST = 3109;
    public static final int DOUBAN_MOVIE_REQUEST = 3110;
    public static final int DOUBAN_LATEST_MOVIE_REQUEST = 3111;

    //登录的what常量
    public static final int USER_LOGIN = 1011;

    public static final String INTENT_URI = "uri";
    public static final String INTENT_VIDEO_URL = "videoUrl";
    public static final String INTENT_RESULT_LIST = "resultList";

    //intent传输VideoInfo用的标识
    //Map中的资源名称
    public static final String TITLE = "title";
    public static final String IMG = "img";
    public static final String URL = "url";

    //文件存储地址
    public static final String LOCAL_FILE_PATH = "/mnt/sdcard/NaugthyVideo/";
    public static final String FILE_PATH = LOCAL_FILE_PATH + "/file/";
    public static final String VIDEO_PATH = LOCAL_FILE_PATH + "/video/";

    public static enum ITEM_TYPE{
        ITEM_TYPE_INFO,
        ITEM_TYPE_LIST
    }

    //Setting
    //播放模式,0为网页，1为直播播放器
    public static int PLAY_MODE = 0;
    //0为vitamio播放器，1为Ijk播放器
    public static int PLAYER_SELECT = 0;
    //是否边下边播，0为否，1为是
    public static boolean DOWNLOAD_AT_THE_SAME_TIME = true;

    //可以用变长参数来优化，待优化
    public static void createAlertDialog(Context context, String title, String message,
                                         String buttonText, DialogInterface.OnClickListener listener) {
        new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, listener)
                .show();
    }

    public static void createAlertDialog(Context context, String message) {
        new AlertDialog
                .Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }

    public static Boolean isMobileNO(String mobileNums){
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        Regex telRegex = new Regex("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return telRegex.matches(mobileNums);
    }

}
