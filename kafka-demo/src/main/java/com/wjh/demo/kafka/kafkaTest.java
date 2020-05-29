package com.wjh.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 消息
 */
@Component
@Slf4j
public class kafkaTest {
    // 泛型与生产者key,value配置对应
    @Resource
    KafkaTemplate<String, String> template;

    /**
     * kafka消息发送
     */
    public void send() {
        log.info(LocalDateTime.now() + "发送消息");
        template.send("thfx-test00", "2", "你好00");
        template.send("thfx-test01", "2", "你好01");
    }

    // 泛型与消费者key,value配置对应
    @KafkaListener(topics = {"thfx-test00"})
    public void consumerthfx0(ConsumerRecord<String, String> record) {
        log.info("接收x0 : " + record.value());

    }

    // 泛型与消费者key,value配置对应
    @KafkaListener(topics = {"thfx-test01"})
    public void consumerthfx1(ConsumerRecord<String, String> record) {
        log.info("接收x1 : " + record.value());
    }

    @KafkaListener(topics = {"PT-MDC-XSHG-INDEXTYPE"})
    public void consumerRecordPT(ConsumerRecord<String, String> record) {
        log.info("接收消息PT" + record.key() + "  " + record.value());
    }
}
