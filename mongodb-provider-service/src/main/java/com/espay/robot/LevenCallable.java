package com.espay.robot;

import com.alibaba.fastjson.JSONObject;
import com.espay.pojo.ChatbotQuestion;
import com.espay.pojo.Statement;
import com.espay.util.LimitQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 距离计算
 */
public class LevenCallable implements Callable {
    private static final Log logger = LogFactory.getLog(GetAnswerCallable.class);
    private String statement;
    private List<Statement> statementList;

    public LevenCallable(String statement, List<Statement> statementList) {
        this.statement = statement;
        this.statementList = statementList;
    }

    @Override
    public JSONObject call() throws Exception {
        double maxMatch = 0;
        String answer = "该问题正在学习中";
        String match_question = null;
        String question = statement;
        LimitQueue<String> queue = new LimitQueue<String>(3);
        for (Statement repositoryDO : statementList) {
            for (ChatbotQuestion chatbotQuestion : repositoryDO.getInResponseTo()) {
                String questionText = chatbotQuestion.getText();
                double match = LenvenshteinDistance.lenvenshtein(questionText, question);
                if (match >= maxMatch) {
                    maxMatch = match;
                    answer = repositoryDO.getText();
                    match_question = questionText;
                    queue.offer(match_question + "_" + maxMatch);
                }
            }
        }
        logger.info("=LevenCallable=" + match_question + JSONObject.toJSONString(queue.getQueue()));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("matchQuestion", match_question);
        jsonObject.put("answer", answer);
        jsonObject.put("match", maxMatch);
        return jsonObject;
    }
}
