package com.heima.model.common.constants.article;

public class ArticleConstants {
    public static final Short LOADTYPE_LOAD_MORE = 0;  // 加载更多
    public static final Short LOADTYPE_LOAD_NEW = 1; // 加载最新
    public static final String DEFAULT_TAG = "__all__";

    // 文章行为分值
    public static final Integer HOT_ARTICLE_VIEW_WEIGHT = 1;
    public static final Integer HOT_ARTICLE_LIKE_WEIGHT = 3;
    public static final Integer HOT_ARTICLE_COMMENT_WEIGHT = 5;
    public static final Integer HOT_ARTICLE_COLLECTION_WEIGHT = 8;

    // 存到redis热文章前缀
    public static final String HOT_ARTICLE_FIRST_PAGE = "hot_article_first_page_";
}