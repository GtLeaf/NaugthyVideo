package com.example.pc_0775.naugthyvideo.bean.mmBean

import java.io.Serializable

/**
 * Created by PC-0775 on 2018/12/2.
 */
/**
 * status : 1
 * msg : ok
 * videos : [{"rowNum":1,"id":24,"coverimg":"http://yuntu.dai112.com/3gp_image/oumei_020848/15509d5e42756e0c45f37a58416fe45a.jpg","title":"真實蕩婦派對 6","vurl":"http://www.aimei6.com/oumei/201808/394062721f90be0a1bcf16c6f592b177/394062721f90be0a1bcf16c6f592b177.m3u8","updatedate":"2018-09-25 23:13:56"}]
 */
data class VideoInfoMiMi(var status:Int, var msg:String, var videos:List<VideoBean>):Serializable {

    /**
     * rowNum : 1
     * id : 24
     * coverimg : http://yuntu.dai112.com/3gp_image/oumei_020848/15509d5e42756e0c45f37a58416fe45a.jpg
     * title : 真實派對 6
     * vurl : http://www.aimei6.com/oumei/201808/394062721f90be0a1bcf16c6f592b177/394062721f90be0a1bcf16c6f592b177.m3u8
     * updatedate : 2018-09-25 23:13:56
     */
    data class VideoBean(var rowNum:Int,
                         var id:Int,
                         var coverimg:String,
                         var title:String,
                         var vurl:String,
                         var updatedate:String):Serializable

}