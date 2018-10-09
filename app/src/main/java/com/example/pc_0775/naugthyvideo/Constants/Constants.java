package com.example.pc_0775.naugthyvideo.Constants;

/**
 * Created by PC-0775 on 2018/8/18.
 */

public class Constants {
    //url
    //http://api.xkapi.xyz/index/g/guochan/?yeshu=1
    public static final String CHINA_VIDEO_URL = "http://api.xkapi.xyz/index/g/guochan/";
    //http://api.xkapi.xyz/index/g2/fenlei4/?leixing=se55&yeshu=1
    public static final String CLASS_ONE_VIDEO_URL = "http://api.xkapi.xyz/index/g2/fenlei4/";
    //http://api.xkapi.xyz/index/g2/fenlei5/?leixing=movielist1&yeshu=1
    public static final String CLASS_TWO_VIDEO_URL = "http://api.xkapi.xyz/index/g2/fenlei4/";
    //http://api.xkapi.xyz/index/g/fenlei3/?leixing=toupaizipai&yeshu=1
    public static final String CLASS_THREE_VIDEO_URL = "http://api.xkapi.xyz/index/g2/fenlei4/";
    public static String TEST_VIDEO_URL = "http://api.xkapi.xyz/index/g/fenlei3/?leixing=toupaizipai&yeshu=1";

    //handler的what常量
    public static final int CLASS_ONE_REQUEST = 3101;
    public static final int CLASS_TWO_REQUEST = 3102;
    public static final int CLASS_THREE_REQUEST = 3103;

    public static final String INTENT_URI = "uri";
    public static final String INTENT_RESULT_LIST = "resultList";

    //intent传输VideoInfo用的标识
    //Map中的资源名称
    public static final String TITLE = "title";
    public static final String IMG = "img";
    public static final String URL = "url";

    public static enum ITEM_TYPE{
        ITEM_TYPE_INFO,
        ITEM_TYPE_LIST
    }

}
