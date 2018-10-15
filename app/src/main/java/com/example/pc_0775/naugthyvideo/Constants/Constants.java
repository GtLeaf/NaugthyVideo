package com.example.pc_0775.naugthyvideo.Constants;

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
    public static final String EUROPE_VIDEO_URL = "http://new.xkapi.xyz/index/g/oumei/";

    /**
     * http://new.xkapi.xyz/index/g/av/?yeshu=1&type=23 cartoon
     * http://new.xkapi.xyz/index/g/av/?yeshu=1&type=21 no
     * http://new.xkapi.xyz/index/g/av/?yeshu=1&type=22 yes
     * http://new.xkapi.xyz/index/g/av/?yeshu=1&type=18 中文
     */
    public static final String CARTOON_VIDEO_URL = "http://new.xkapi.xyz/index/g/av/";
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

    //paramter
    public static final String PAGE_NUMBER = "yeshu";

    //handler的what常量
    public static final int CLASS_ONE_REQUEST = 3101;
    public static final int CLASS_TWO_REQUEST = 3102;
    public static final int CLASS_THREE_REQUEST = 3103;
    public static final int EUROPE_VIDEO_REQUEST = 3104;
    public static final int CARTOON_VIDEO_REQUEST = 3105;
    public static final int LIVE_PLATFORM_REQUEST = 3106;

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
