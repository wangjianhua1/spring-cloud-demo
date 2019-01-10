package com.espay.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoliBot {
    private static Logger logger = LoggerFactory.getLogger(MoliBot.class);
    private static final String url = "http://i.itpk.cn/api.php";
    private static final String question = "&question=";
    private static final String apiKey = "api_key=02f03523aa004717ece959fc272e64b9";
    private static final String apiSecret = "&api_secret=byso7yaf52v9";
    private static final String returnType = "&type=json";
    private static final String UTF8 = "UTF-8";
    private static final String botName = "金小北";

    public static String getAnswer(String questionStr) {
        System.out.println(Charset.defaultCharset().toString());
        String param = null;
        String answer = null;
        try {
            param = apiKey + apiSecret + returnType + question + URLEncoder.encode(questionStr, UTF8);
            answer = doGet(url, param);
            answer = answer.replace("茉莉", botName);
        } catch (Exception e) {
            logger.error("请求茉莉机器人出错：", e);
            answer = "网络异常，请稍后再试";
        }
        return answer;
    }

    public static String doGet(String url, String param) {
        String srtResult = "";
        CloseableHttpClient httpCilent2 = HttpClients.createDefault();
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) // 设置连接超时时间
                    .setConnectionRequestTimeout(5000) // 设置请求超时时间
                    .setSocketTimeout(5000).setRedirectsEnabled(true)// 默认允许自动重定向
                    .build();
            HttpGet httpGet2 = new HttpGet(url + "?" + param);
            httpGet2.setConfig(requestConfig);

            HttpResponse httpResponse = httpCilent2.execute(httpGet2);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = httpResponse.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, UTF8));
                srtResult = IOUtils.toString(br);
                if (srtResult.indexOf("title") > -1) {
                    srtResult = unicodeToString(srtResult);
                }
            } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                srtResult = "400";
            } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                srtResult = "400";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpCilent2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return srtResult;
    }

    /**
     * Unicode转 汉字字符串
     *
     * @param str \u6728
     * @return '木' 26408
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            // group 6728
            String group = matcher.group(2);
            // ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            // group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;

    }
}

