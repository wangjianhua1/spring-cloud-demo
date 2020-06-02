package com.wjh.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

/**
 * 单分区时，多个消费者只会有一个成功，且消息是顺序的
 * 当分区数多于消费者数的时候，有的消费者对应多个分区
 * 当分区数等于消费者数，每个消费者对应一个分区数
 * 启动多个组，相同的数据会被不同组的消费者消费多次
 *
 * 相同的groupId消费记录是同一个offset,多个地方使用相同的groupId可能消费不到
 */
@Component
@Slf4j
public class kafkaService {

    @Resource
    KafkaTemplate<String, String> template;

    /**
     * kafka消息发送
     * Kafka commitId : c0518aa65f25317e
     */
    public void send() {
        log.info(LocalDateTime.now() + "发送消息");
        for (int i = 0; i < 100; i++) {
            template.send("thfx-test00", "2", "-first-" + i);
            try {
                //同步发送
                SendResult<String, String> sendResult = template.send("thfx-test01", "2", "-second-" + i).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 单个消费
     * 有批量消费,此消费就不会接收到消息
     *
     * @param record
     */
    @KafkaListener(topics = {"thfx-test00"})
    public void single_thfx0(ConsumerRecord<String, String> record) {
        log.info("单个接收x0 : " + record.value());
    }

//    /**
//     * 批量消费
//     * https://www.jianshu.com/p/5370fff55cff
//     *
//     * @param records
//     */
//    @KafkaListener(topics = {"thfx-test00"}, containerFactory = "batchFactory")
//    public void batch_thfx00(List<ConsumerRecord<String, String>> records) {
//        for (ConsumerRecord<String, String> record : records) {
//            log.info("批量接收x0 : " + record.key() + "==" + record.value());
//        }
//    }

//    // 泛型与消费者key,value配置对应
//    @KafkaListener(topics = {"thfx-test01"})
//    public void consumerthfx1(ConsumerRecord<String, String> record) {
//        log.info("接收x1 : " + record.key() + "==" + record.value());
//    }

    /**
     * 正则表达式消费topic
     * 1.第一个没有消费，thfx-test01有，2个都被正则消费
     * 2.thfx-test00有单个和批量，只有thfx-test00被正则消费，thfx-test01没有
     * 3.两个都是单个消费，有正则，消费thfx-test01
     * 正则>批量>单个
     *
     * @param record
     */
    @KafkaListener(topicPattern = "thfx-.*")
    public void thfx_pattern(ConsumerRecord<String, String> record) {
        log.info("正则接收: " + record.topic() + "--" + record.offset() + "--" + record.key() + "--" + record.value());
    }

    /**
     * 没有生产的topic
     *
     * @param record
     */
    @KafkaListener(topics = {"PT-MDC-XSHG-INDEXTYPE"})
    public void consumerRecordPT(ConsumerRecord<String, String> record) {
        log.info("接收消息PT" + record.key() + "  " + record.value());
    }
}
