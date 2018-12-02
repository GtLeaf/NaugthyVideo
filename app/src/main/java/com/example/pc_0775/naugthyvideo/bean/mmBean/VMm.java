package com.example.pc_0775.naugthyvideo.bean.mmBean;

import java.util.List;

/**
 * Created by PC-0775 on 2018/12/2.
 */

public class VMm {

    /**
     * status : 1
     * msg : ok
     * videos : [{"rowNum":1,"id":24,"coverimg":"http://yuntu.dai112.com/3gp_image/oumei_020848/15509d5e42756e0c45f37a58416fe45a.jpg","title":"真實派對 6","vurl":"http://www.aimei6.com/oumei/201808/394062721f90be0a1bcf16c6f592b177/394062721f90be0a1bcf16c6f592b177.m3u8","updatedate":"2018-09-25 23:13:56"}]
     */

    private int status;
    private String msg;
    private List<VideosBean> videos;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<VideosBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideosBean> videos) {
        this.videos = videos;
    }

    public static class VideosBean {
        /**
         * rowNum : 1
         * id : 24
         * coverimg : http://yuntu.dai112.com/3gp_image/oumei_020848/15509d5e42756e0c45f37a58416fe45a.jpg
         * title : 真實派對 6
         * vurl : http://www.aimei6.com/oumei/201808/394062721f90be0a1bcf16c6f592b177/394062721f90be0a1bcf16c6f592b177.m3u8
         * updatedate : 2018-09-25 23:13:56
         */

        private int rowNum;
        private int id;
        private String coverimg;
        private String title;
        private String vurl;
        private String updatedate;

        public int getRowNum() {
            return rowNum;
        }

        public void setRowNum(int rowNum) {
            this.rowNum = rowNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCoverimg() {
            return coverimg;
        }

        public void setCoverimg(String coverimg) {
            this.coverimg = coverimg;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVurl() {
            return vurl;
        }

        public void setVurl(String vurl) {
            this.vurl = vurl;
        }

        public String getUpdatedate() {
            return updatedate;
        }

        public void setUpdatedate(String updatedate) {
            this.updatedate = updatedate;
        }
    }
}
