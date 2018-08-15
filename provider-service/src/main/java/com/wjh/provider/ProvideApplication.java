package com.wjh.provider;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ProvideApplication {
    /**
     * 此类需要在项目目录下
     *
     * @param args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(ProvideApplication.class).web(true).run(args);
    }
}
