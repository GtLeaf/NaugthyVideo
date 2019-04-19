package com.example.pc_0775.naugthyvideo.model.bean.douban;

import java.util.List;

/**
 * Created by PC-0775 on 2018/12/19.
 */

public class DoubanMovieDetail {

    /**
     * rating : {"max":10,"average":7.9,"stars":"40","min":0}
     * reviews_count : 2005
     * wish_count : 206274
     * douban_site :
     * year : 2018
     * images : {"small":"https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2541280047.jpg","large":"https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2541280047.jpg","medium":"https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2541280047.jpg"}
     * alt : https://movie.douban.com/subject/3878007/
     * id : 3878007
     * mobile_url : https://movie.douban.com/subject/3878007/mobile
     * title : 海王
     * do_count : null
     * share_url : https://m.douban.com/movie/subject/3878007
     * seasons_count : null
     * schedule_url : https://movie.douban.com/subject/3878007/cinema/
     * episodes_count : null
     * countries : ["美国","澳大利亚"]
     * genres : ["动作","奇幻","冒险"]
     * collect_count : 379161
     * casts : [{"alt":"https://movie.douban.com/celebrity/1022614/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p32853.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p32853.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p32853.jpg"},"name":"杰森·莫玛","id":"1022614"},{"alt":"https://movie.douban.com/celebrity/1044702/","avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p34697.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p34697.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p34697.jpg"},"name":"艾梅柏·希尔德","id":"1044702"},{"alt":"https://movie.douban.com/celebrity/1010539/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p9206.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p9206.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p9206.jpg"},"name":"威廉·达福","id":"1010539"},{"alt":"https://movie.douban.com/celebrity/1006919/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1386481612.26.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1386481612.26.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1386481612.26.jpg"},"name":"帕特里克·威尔森","id":"1006919"}]
     * current_season : null
     * original_title : Aquaman
     * summary : 华纳兄弟影片公司与导演温子仁联手为您呈现波澜壮阔的动作冒险电影——《海王》！横跨七大洋的广阔海底世界徐徐展开，给观众带来震撼十足的视觉奇观。本片由杰森·莫玛领衔主演，讲述半人半亚特兰蒂斯血统的亚瑟·库瑞踏上永生难忘的征途——他不但需要直面自己的特殊身世，更不得不面对生而为王的考验：自己究竟能否配得上“海王”之名。
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1032122/","avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1509950363.8.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1509950363.8.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1509950363.8.jpg"},"name":"温子仁","id":"1032122"}]
     * comments_count : 114423
     * ratings_count : 275646
     * aka : ["水行侠(港/台)","潜水侠","水人","人鱼哥(豆友译名)"]
     */

    private RatingBean rating;
    private int reviews_count;
    private int wish_count;
    private String douban_site;
    private String year;
    private ImagesBean images;
    private String alt;
    private String id;
    private String mobile_url;
    private String title;
    private Object do_count;
    private String share_url;
    private Object seasons_count;
    private String schedule_url;
    private Object episodes_count;
    private int collect_count;
    private Object current_season;
    private String original_title;
    private String summary;
    private String subtype;
    private int comments_count;
    private int ratings_count;
    private List<String> countries;
    private List<String> genres;
    private List<CastsBean> casts;
    private List<DirectorsBean> directors;
    private List<String> aka;

    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public int getWish_count() {
        return wish_count;
    }

    public void setWish_count(int wish_count) {
        this.wish_count = wish_count;
    }

    public String getDouban_site() {
        return douban_site;
    }

    public void setDouban_site(String douban_site) {
        this.douban_site = douban_site;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getDo_count() {
        return do_count;
    }

    public void setDo_count(Object do_count) {
        this.do_count = do_count;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public Object getSeasons_count() {
        return seasons_count;
    }

    public void setSeasons_count(Object seasons_count) {
        this.seasons_count = seasons_count;
    }

    public String getSchedule_url() {
        return schedule_url;
    }

    public void setSchedule_url(String schedule_url) {
        this.schedule_url = schedule_url;
    }

    public Object getEpisodes_count() {
        return episodes_count;
    }

    public void setEpisodes_count(Object episodes_count) {
        this.episodes_count = episodes_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public Object getCurrent_season() {
        return current_season;
    }

    public void setCurrent_season(Object current_season) {
        this.current_season = current_season;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getRatings_count() {
        return ratings_count;
    }

    public void setRatings_count(int ratings_count) {
        this.ratings_count = ratings_count;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<CastsBean> getCasts() {
        return casts;
    }

    public void setCasts(List<CastsBean> casts) {
        this.casts = casts;
    }

    public List<DirectorsBean> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorsBean> directors) {
        this.directors = directors;
    }

    public List<String> getAka() {
        return aka;
    }

    public void setAka(List<String> aka) {
        this.aka = aka;
    }

    public static class RatingBean {
        /**
         * max : 10
         * average : 7.9
         * stars : 40
         * min : 0
         */

        private int max;
        private double average;
        private String stars;
        private int min;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }

    public static class ImagesBean {
        /**
         * small : https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2541280047.jpg
         * large : https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2541280047.jpg
         * medium : https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2541280047.jpg
         */

        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

    public static class CastsBean {
        /**
         * alt : https://movie.douban.com/celebrity/1022614/
         * avatars : {"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p32853.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p32853.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p32853.jpg"}
         * name : 杰森·莫玛
         * id : 1022614
         */

        private String alt;
        private AvatarsBean avatars;
        private String name;
        private String id;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBean getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBean avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class AvatarsBean {
            /**
             * small : https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p32853.jpg
             * large : https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p32853.jpg
             * medium : https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p32853.jpg
             */

            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }

    public static class DirectorsBean {
        /**
         * alt : https://movie.douban.com/celebrity/1032122/
         * avatars : {"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1509950363.8.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1509950363.8.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1509950363.8.jpg"}
         * name : 温子仁
         * id : 1032122
         */

        private String alt;
        private AvatarsBeanX avatars;
        private String name;
        private String id;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBeanX getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBeanX avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class AvatarsBeanX {
            /**
             * small : https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1509950363.8.jpg
             * large : https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1509950363.8.jpg
             * medium : https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1509950363.8.jpg
             */

            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }
}
