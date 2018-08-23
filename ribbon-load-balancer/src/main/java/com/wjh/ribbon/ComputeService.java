package com.wjh.ribbon;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 改造消费模式
 *
 * @see com.wjh.ribbon.ConsumerController
 */
@Service
public class ComputeService {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 在使用ribbon消费服务的函数上增加@HystrixCommand注解来指定回调方法
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "addServiceFallback")
    public String addService() {
        return restTemplate.getForEntity("http://COMPUTE-SERVICE/add?a=10&b=20", String.class).getBody();
    }

    public String addServiceFallback() {
        return "error";
    }

}