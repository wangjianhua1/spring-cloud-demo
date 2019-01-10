package com.espay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "robot.config")
public class RobotConfig {
    private List<String> submit_url;
    private String pinyinToChinese;
    private String chatterbot_match;
    private String modelFileName;
    private String financeModel;
    private String zhihuModel;
    private String weiboModel;
    private String tencentModel;
}
