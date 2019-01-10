package com.espay.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Comparator;

/**
 * 按照匹配度最高来排序
 */
public class BotMatchComparator implements Comparator<JSONObject> {
    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        Double match_1 = o1.getDouble("match");
        Double match_2 = o2.getDouble("match");
        if(match_2 > match_1){
            return 1;
        }else if(match_2 < match_1){
            return -1;
        }else{
            return 0;
        }
    }
}
