package com.wjh.eureka;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/*注解启动一个服务注册中心提供给其他应用进行对话*/
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {
    /**
     * 此类需要在项目目录下
     *
     * @param args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaApplication.class).web(true).run(args);
    }
}
