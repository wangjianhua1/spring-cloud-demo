package com.espay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@SpringBootApplication
public class MongodbProviderApplication {

    /**
     * 此类需要在项目目录下
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(MongodbProviderApplication.class, args);
    }
}
