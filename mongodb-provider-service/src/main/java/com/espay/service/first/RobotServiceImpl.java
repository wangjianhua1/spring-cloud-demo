package com.espay.service.first;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.espay.config.MatchDegreeConfig;
import com.espay.constant.BotAnswerSourceConstant;
import com.espay.pojo.Statement;
import com.espay.robot.CalculationManage;
import com.espay.util.BotMatchComparator;
import com.espay.util.ListUtil;
import com.espay.util.MoliBot;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class RobotServiceImpl implements RobotService {
    private static Log logger = LogFactory.getLog(RobotServiceImpl.class);
    @Autowired
    private FirstMongoService firstMongoService;

    @Override
    public JSONObject getRobotAnswer(String question) {
        JSONObject answerJson = null;
        try {
            //综合
            List<Statement> financeList = new ArrayList<>();
            List<Statement> zhList = new ArrayList<>();
            List<Statement> wbList = new ArrayList<>();
            List<Statement> integrateList = new ArrayList<>();
            Set<String> questionList = new HashSet<>();
            List<JSONObject> importantHighList = new ArrayList<>();
            firstMongoService.importantHighFrequency(financeList);
            //重要专业问题
            List<JSONObject> resultList = CalculationManage.importantHighFrequency(question, financeList);
            Collections.sort(resultList, new BotMatchComparator());
            if (resultList.size() > 0) {
                answerJson = resultList.get(0);
                importantHighList.addAll(resultList);
                logger.info("=重要专业问题=向量=" + JSON.toJSONString(answerJson));
            }
            if (answerJson == null || answerJson.getDouble("match") < MatchDegreeConfig.CHAT_VALVE) {
                firstMongoService.statementHandle(zhList, wbList, integrateList);
                List<Statement> statementAll = ListUtil.merge(zhList, wbList, integrateList);
                resultList = CalculationManage.vectorCalculate(question, statementAll);
                //添加其他部分向量匹配方式来源
                BotAnswerSourceConstant.addSource(resultList, BotAnswerSourceConstant.OTHER_VECTOR);
                //编辑距离
                Collections.sort(resultList, new BotMatchComparator());
                if (resultList.size() > 0) {
                    answerJson = resultList.get(0);
                }
                if (answerJson == null || answerJson.getDouble("match") <= MatchDegreeConfig.LENVENSHTEIN_VALVE) {
                    List<JSONObject> lenvenResult = CalculationManage.lenvenshteinCalculate(question, ListUtil.merge(zhList, wbList, integrateList));
                    //添加编辑距离匹配来源
                    BotAnswerSourceConstant.addSource(lenvenResult, BotAnswerSourceConstant.OTHER_LENVEN);
                    resultList.addAll(lenvenResult);
                }
                Collections.sort(resultList, new BotMatchComparator());

                if (resultList.size() > 0) {
                    answerJson = resultList.get(0);
                    logger.info("=重要专业问题=向量+距离=" + JSON.toJSONString(answerJson));
                }
                //最终查询结果匹配度过低，则给出重要问答对中匹配到的问答对返回
                //若匹配度太低则请求茉莉机器人给出回答
                double match = answerJson.getDouble("match");
                if (match < MatchDegreeConfig.BLUR_GUIDE_VALVE) {
                    if (match > MatchDegreeConfig.THIRD_PARTY_BOT_VALVE) {
                        for (int i = 0; i < importantHighList.size(); i++) {
                            String questionItem = importantHighList.get(i).getString("matchQuestion");
                            if (StringUtils.hasText(questionItem)) {
                                questionList.add(questionItem);
                            }
                        }
                        if (questionList.size() > 0) {
                            answerJson.put("questionList", questionList);
                            answerJson.put("source", BotAnswerSourceConstant.BLUR_GUIDANCE);
                        } else {
                            answerJson.put("answer", "该问题正在学习");
                            answerJson.put("source", BotAnswerSourceConstant.DEFAULT);
                        }
                    } else {
                        String answer = MoliBot.getAnswer(question);
                        answerJson.put("answer", answer);
                        answerJson.put("source", BotAnswerSourceConstant.MOLI_BOT);
                        logger.info("=茉莉机器回答=" + JSON.toJSONString(answerJson));
                        firstMongoService.saveMoliBotCorpus(question, answer);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("机器客服引擎出错：", e);
            answerJson = new JSONObject();
            answerJson.put("answer", "系统繁忙，请稍后再试");
            answerJson.put("question", question);
            answerJson.put("source", BotAnswerSourceConstant.ERROR);
        }
        answerJson.put("question", question);
        answerJson.put("answer", answerJson.getString("answer"));
        answerJson.remove("source");
        return answerJson;
    }


}
