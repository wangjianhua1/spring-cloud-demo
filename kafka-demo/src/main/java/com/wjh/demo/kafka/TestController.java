package com.wjh.demo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private kafkaTest kafkaTest;

    @GetMapping("/hello")
    public String hello() {
        kafkaTest.send();
        return "success";
    }

}
