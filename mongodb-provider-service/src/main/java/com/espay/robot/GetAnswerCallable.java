package com.espay.robot;

import com.alibaba.fastjson.JSONObject;
import com.espay.pojo.ChatbotQuestion;
import com.espay.pojo.Sentence;
import com.espay.pojo.Statement;
import com.espay.util.LimitQueue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 异步计算词向量的匹配度，并且找到问题答案
 */
public class GetAnswerCallable implements Callable<JSONObject> {
    private static final Log logger = LogFactory.getLog(GetAnswerCallable.class);
    private String statement;
    private List<Sentence> questionParseList;
    private List<Statement> statementList;
    private CustomDocVectorModel docVectorModel;
    private String type;

    public GetAnswerCallable(String statement, List<Sentence> questionParseList, List<Statement> statementList,
                             CustomDocVectorModel docVectorModel, String type) {
        this.statement = statement;
        this.questionParseList = questionParseList;
        this.statementList = statementList;
        this.docVectorModel = docVectorModel;
        this.type = type;
    }

    @Override
    public JSONObject call() throws Exception {
        if (docVectorModel == null) {
            return new JSONObject();
        }
        float maxMatch = 0.0f;
        String answer = "该问题正在学习中";
        String match_question = null;
        String question = statement;
        String questionText;
        float match = -1.0f;
        LimitQueue<String> queue = new LimitQueue<String>(3);
        for (Statement repositoryDO : statementList) {
            for (ChatbotQuestion chatbotQuestion : repositoryDO.getInResponseTo()) {
                questionText = chatbotQuestion.getText();
                List<Sentence> questionTextParseList = chatbotQuestion.getSentence();
                if (CollectionUtils.isNotEmpty(questionTextParseList)) {
                    match = docVectorModel.similarity(questionParseList, questionTextParseList);
                } else {
                    match = docVectorModel.similarity(question, questionText);
                }
                if (match >= maxMatch) {
                    maxMatch = match;
                    answer = repositoryDO.getText();
                    match_question = questionText;
                    queue.offer(match_question + "_" + maxMatch);
                }
            }
        }
        logger.info("=GetAnswerCallable=" + type + "==" + match_question + JSONObject.toJSONString(queue.getQueue()));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("matchQuestion", match_question);
        jsonObject.put("answer", answer);
        jsonObject.put("match", maxMatch);
        return jsonObject;
    }
}
