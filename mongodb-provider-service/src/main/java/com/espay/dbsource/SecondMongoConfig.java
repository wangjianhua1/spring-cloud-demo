package com.espay.dbsource;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.espay.dao.second", mongoTemplateRef = "secondMongo")
public class SecondMongoConfig {

    @Autowired
    @Qualifier("secondMongoProperties")
    private MongoProperties mongoProperties;

    @Bean(name = "secondMongo")
    public MongoTemplate secondMongoTemplate() {
        return new MongoTemplate(secondFactory(this.mongoProperties));
    }

    @Bean
    public MongoDbFactory secondFactory(MongoProperties mongoProperties) {
        ServerAddress address = new ServerAddress(mongoProperties.getUri());
        return new SimpleMongoDbFactory(new MongoClient(address), mongoProperties.getDatabase());
    }
}
