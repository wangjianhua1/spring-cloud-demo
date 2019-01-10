package com.espay.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.espay.pojo.entity.FrspUser;
import com.espay.service.RobotService;
import com.espay.service.SensitiveWordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ComputeController {

    private final Log logger = LogFactory.getLog(ComputeController.class);

    @Autowired
    private DiscoveryClient client;
    @Autowired
    private RobotService robotService;
    @Autowired
    private SensitiveWordService sensitiveWordService;


    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Integer add(@RequestParam Integer a, @RequestParam Integer b) {
        List<ServiceInstance> instance = client.getInstances("compute-service");
        Integer r = a + b;
        FrspUser frspUser = robotService.selectById(1);
        int sensitiveWord = sensitiveWordService.selectCount(new EntityWrapper<>());
        logger.info("/add, host:" + instance.get(0).getHost() + ", service_id:" + instance.get(0).getServiceId() + ", result:" + r);
        return r;
    }


}