package com.espay.constant;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class BotAnswerSourceConstant {

    public static final String ERROR = "问答出错";             //回答出错
    public static final String DEFAULT = "默认回答";            //未找到符合要求答案
    public static final String IMPORTANT_VECTOR = "重要部分向量匹配";   //北金所和高热度的向量匹配
    public static final String IMPORTANT_LENVEN = "重要部分编辑距离匹配";   //北金所和高热度的编辑距离匹配
    public static final String OTHER_VECTOR = "其他部分向量匹配";       //其它的向量匹配
    public static final String OTHER_LENVEN = "其他部分编辑距离匹配";       //其它的编辑距离匹配
    public static final String BLUR_GUIDANCE = "模糊引导";
    public static final String MOLI_BOT = "茉莉机器人";                     //茉莉机器人回答

    public static void addSource(List<JSONObject> list, String source) {
        for (JSONObject jsonObject : list) {
            jsonObject.put("source", source);
        }
    }

}
