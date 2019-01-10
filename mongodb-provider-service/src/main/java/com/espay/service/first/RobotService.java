package com.espay.service.first;

import com.alibaba.fastjson.JSONObject;

public interface RobotService {
    /**
     * 拿到机器客服返回的信息
     *
     * @param question 用户向机器客服提出的问题
     * @return 机器客服向用户应答的JSON
     */
    JSONObject getRobotAnswer(String question);

}
