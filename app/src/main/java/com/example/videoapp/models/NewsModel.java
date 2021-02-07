package com.example.videoapp.models;

import java.util.List;

public class NewsModel {
    public Integer type;
    public Integer newsId;
    public String newsTitle;
    public String authorName;
    public String headerUrl;
    public Integer commentCount;
    public String releaseDate;
    public Object imgList;
    public List<ThumbEntitiesBean> thumbEntities;

    public static class ThumbEntitiesBean {
        public Integer thumbId;
        public String thumbUrl;
        public Integer newsId;
    }
}
