package com.espay.config;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class MatchDegreeConfig {
    private static final Logger logger = LoggerFactory.getLogger(MatchDegreeConfig.class);
    /**
     * 专业问题匹配和非专业问题根据匹配度是否进行编辑距离算法的阀值
     */
    public static volatile double LENVENSHTEIN_VALVE = 0.75;
    /**
     * 匹配完专业问题后根据匹配度是否进行非专业问题匹配的阀值
     */
    public static volatile double CHAT_VALVE = 0.75;
    /**
     * 匹配完所有的问题后根据匹配度是否进行模糊引导范围的上限
     */
    public static volatile double BLUR_GUIDE_VALVE = 0.8;
    /**
     * 匹配完所有的问题后根据匹配度是否进行模糊引导范围的下限，若低于此值则由第三方机器人回答
     */
    public static volatile double THIRD_PARTY_BOT_VALVE = 0.7;

    @PostConstruct
    public static void initialization() {
        InputStream config = null;
        BufferedReader bufferedReader = null;
        try {
            config = MatchDegreeConfig.class.getClassLoader().getResourceAsStream("match-degree.config");
            bufferedReader = new BufferedReader(new InputStreamReader(config, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            str = sb.toString();
            JSONObject configJson = JSONObject.parseObject(str);
            LENVENSHTEIN_VALVE = configJson.getDouble("lenvenshteinValve");
            CHAT_VALVE = configJson.getDouble("chatValve");
            BLUR_GUIDE_VALVE = configJson.getDouble("blurGuideValve");
            THIRD_PARTY_BOT_VALVE = configJson.getDouble("thirdPartyBotValve");
        } catch (Exception e) {
            writeConfig();
            logger.info("配置文件不存在,采用默认配置");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.error("流关闭异常");
                }
            }
            if (config != null) {
                try {
                    config.close();
                } catch (IOException e) {
                    logger.error("流关闭异常");
                }
            }
        }
    }

    public static void writeConfig() {
        String path = MatchDegreeConfig.class.getResource("/").getPath() + "match-degree.config";
        FileWriter fileWriter = null;
        try {
            JSONObject config = new JSONObject();
            config.put("lenvenshteinValve", LENVENSHTEIN_VALVE);
            config.put("chatValve", CHAT_VALVE);
            config.put("blurGuideValve", BLUR_GUIDE_VALVE);
            config.put("thirdPartyBotValve", THIRD_PARTY_BOT_VALVE);
            fileWriter = new FileWriter(path);
            fileWriter.write(config.toJSONString());
            fileWriter.flush();
        } catch (IOException e1) {
            logger.error("不能创建配置文件");
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    logger.error("流关闭异常");
                }
            }
        }
    }
}
