package com.espay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/*该注解能激活Eureka中的DiscoveryClient实现，才能实现Controller中对服务信息的输出。*/

@EnableDiscoveryClient
@SpringBootApplication
public class ProvideApplication {
    /**
     * 此类需要在项目目录下
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(ProvideApplication.class, args);
    }
}
