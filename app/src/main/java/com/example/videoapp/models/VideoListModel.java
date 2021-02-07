package com.example.videoapp.models;

import java.io.Serializable;
import java.util.List;

public class VideoListModel implements Serializable {


    public String msg;
    public Integer code;
    public PageBean page;

    public static class PageBean {
        public Integer totalCount;
        public Integer pageSize;
        public Integer totalPage;
        public Integer currPage;
        public List<VideoModel> list;

//        public static class VideoModel {
//            public Integer vid;
//            public String vtitle;
//            public String author;
//            public String coverurl;
//            public String headurl;
//            public Integer commentNum;
//            public Integer likeNum;
//            public Integer collectNum;
//            public String playurl;
//            public String createTime;
//            public String updateTime;
//            public Integer categoryId;
//            public Object categoryName;
//            public Object videoSocialEntity;
//        }
    }
}
