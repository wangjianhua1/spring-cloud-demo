package com.espay.controller;

import com.alibaba.fastjson.JSONObject;
import com.espay.robot.Word2Vec;
import com.espay.service.first.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EspayController {
    @Autowired
    private RobotService robotService;
    @Autowired
    private Word2Vec word2Vec;

    @GetMapping(value = "submit_form")
    public JSONObject getMessage(String question) {
        if (!StringUtils.hasText(question)) {
            return null;
        }
        //调用接口去调用智能客服引擎拿到应答数据
        JSONObject answer = robotService.getRobotAnswer(question);
        return answer;
    }

    @GetMapping("executeModel")
    public String executeModel(String typeName, String ops) {
        word2Vec.executeModel(typeName, ops);
        return "执行成功";
    }


}
