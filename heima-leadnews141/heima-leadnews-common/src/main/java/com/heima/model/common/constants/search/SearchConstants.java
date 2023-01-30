package com.heima.model.common.constants.search;

/**
 * @author mrchen
 * @date 2022/5/8 10:45
 */
public class SearchConstants {
    public static final String ARTICLE_INDEX_NAME = "app_info_article";

    public static final String ARTICLE_INDEX_MAPPING = "{\n" +
            "    \"mappings\":{\n" +
            "        \"properties\":{\n" +
            "            \"id\":{\n" +
            "                \"type\":\"long\"\n" +
            "            },\n" +
            "            \"publishTime\":{\n" +
            "                \"type\":\"date\"\n" +
            "            },\n" +
            "            \"layout\":{\n" +
            "                \"type\":\"integer\"\n" +
            "            },\n" +
            "            \"images\":{\n" +
            "                \"type\":\"keyword\",\n" +
            "                \"index\": false\n" +
            "            },\n" +
            "           \"staticUrl\":{\n" +
            "                \"type\":\"keyword\",\n" +
            "                \"index\": false\n" +
            "            },\n" +
            "            \"authorId\": {\n" +
            "          \t\t\"type\": \"long\"\n" +
            "       \t\t},\n" +
            "          \"title\":{\n" +
            "            \"type\":\"text\",\n" +
            "            \"analyzer\":\"ik_smart\"\n" +
            "          }\n" +
            "        }\n" +
            "    }\n" +
            "}";
}
