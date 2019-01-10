package com.espay.dbsource;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MultipleMongoProperties {

    @Bean(name = "firstMongoProperties")
    @Primary
    @ConfigurationProperties(prefix = "mongodb.first")
    public MongoProperties firstMongoProperties() {
        System.out.println("-------------------- 第一个mongodb配置 ---------------------");
        return new MongoProperties();
    }

    @Bean(name = "secondMongoProperties")
    @ConfigurationProperties(prefix = "mongodb.second")
    public MongoProperties secondMongoProperties() {
        System.out.println("-------------------- 第二个mongodb配置 ---------------------");
        return new MongoProperties();
    }
}