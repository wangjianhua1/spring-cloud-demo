package com.espay.service.first;

import com.espay.constant.RepositoryConstant;
import com.espay.dao.first.ChatterBotDao;
import com.espay.pojo.*;
import com.espay.pojo.view.QuestionAnswerVO;
import com.espay.robot.HanLPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.*;

@Service
public class FirstMongoServiceImpl implements FirstMongoService {
    @Autowired
    private ChatterBotDao chatterBotDao;

    private static Map<String, List<Statement>> statementListMap;

    @PostConstruct
    private void init() {
        statementListMap = new HashMap<>();
        reloadRedisData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        reloadRedisData(new ArrayList<>());
    }

    @Override
    public void importantHighFrequency(List<Statement> statementList) {
        Set<String> statementDOListMapKeySet = statementListMap.keySet();
        if (statementDOListMapKeySet.contains("importantHighFrequency")) {
            statementList.addAll(statementListMap.get("importantHighFrequency"));
        } else {
            reloadRedisData(statementList);
        }
    }


    @Override
    public void statementHandle(List<Statement> zhList, List<Statement> wbList, List<Statement> integrateList) {
        Set<String> statementDOListMapKeySet = statementListMap.keySet();
        if (statementDOListMapKeySet.contains("cfae")) {
            if (statementDOListMapKeySet.contains("zhihu")) {
                zhList.addAll(statementListMap.get("zhihu"));
            }
            if (statementDOListMapKeySet.contains("weibo")) {
                wbList.addAll(statementListMap.get("weibo"));
            }
            if (statementDOListMapKeySet.contains("integrate")) {
                integrateList.addAll(statementListMap.get("integrate"));
            }
        } else {
            reloadRedisData(zhList, wbList, integrateList);
        }
    }

    @Override
    public void saveMoliBotCorpus(String question, String answer) {
        QuestionAnswerVO questionAnswerVO = new QuestionAnswerVO();
        questionAnswerVO.setAnswer(answer);
        String[] questions = {question};
        questionAnswerVO.setUserId("moli_bot");
        questionAnswerVO.setUserName("茉莉机器人");
        questionAnswerVO.setQuestions(questions);
        questionAnswerVO.setStatus("0");
        saveRepository(questionAnswerVO);
    }

    public ResultInfo saveRepository(QuestionAnswerVO questionAnswerVO) {
        String[] questions = questionAnswerVO.getQuestions();
        String answer = questionAnswerVO.getAnswer().trim();
        ResultInfo resultInfo = new ResultInfo();
        // 校验数据格式
        String errorInfo = checkQusetionAnswer(questions, answer);
        if (!"200".equals(errorInfo)) {
            resultInfo.setStatusCode(300);
            resultInfo.setMessage("保存失败" + errorInfo);
            return resultInfo;
        }
        // 检查是否有已存在的问题或答案
        String[] texts = Arrays.copyOf(questions, questions.length + 1);
        texts[texts.length - 1] = answer;
        List<String> textList = new ArrayList<>(Arrays.asList(texts));
        Boolean existText = chatterBotDao.isExistText(textList);
        if (existText) {
            resultInfo.setStatusCode(300);
            resultInfo.setMessage("保存失败(问题或答案已经存在)");
            return resultInfo;
        }

        List<RepositoryDO> questionEntities = getRepositoryDOs(questionAnswerVO);
        chatterBotDao.saveRepositorys(questionEntities);
        resultInfo.setStatusCode(200);
        resultInfo.setMessage("保存成功！");
        return resultInfo;
    }


    private List<RepositoryDO> getRepositoryDOs(QuestionAnswerVO questionAnswerVO) {
        String[] questions = questionAnswerVO.getQuestions();
        String answer = questionAnswerVO.getAnswer();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // 问答对
        RepositoryDO repositoryDO = new RepositoryDO();
        // 问答对里的inResponseTo
        List<ChatbotQuestion> inResponseTos = new ArrayList<>();
        // 单独的问题记录d
        List<RepositoryDO> questionEntities = new ArrayList<>();
        for (int i = 0; i < questions.length; i++) {
            ChatbotQuestion inResponseTo = new ChatbotQuestion();
            inResponseTo.setCreateAt(timestamp);
            inResponseTo.setOccurrence(1);
            inResponseTo.setText(questions[i]);
            List<Sentence> wordList = HanLPUtil.getDependencySentence(questions[i]);
            inResponseTo.setSentence(wordList);
            inResponseTos.add(inResponseTo);
        }
        // 填充问答对对象
        repositoryDO.setText(answer);
        repositoryDO.setInResponseTo(inResponseTos);
        repositoryDO.setKnowledgeClass("");
        ExtraData extraData = new ExtraData();
        repositoryDO.setExtraData(extraData);
        repositoryDO.setCreateAt(timestamp);
        repositoryDO.setUpdateAt(timestamp);
        repositoryDO.setCreatedUserId(questionAnswerVO.getUserId());
        repositoryDO.setCreatedUserName(questionAnswerVO.getUserName());
        repositoryDO.setUpdateUserId(questionAnswerVO.getUserId());
        repositoryDO.setUpdateUserName(questionAnswerVO.getUserName());
        repositoryDO.setKnowledgeClass(questionAnswerVO.getStatus());
        if (RepositoryConstant.AUDIT_OPEN) {
            repositoryDO.setStatus(RepositoryConstant.STATUS.STOP);
            repositoryDO.setAuditStatus(RepositoryConstant.AUDIT_STATUS.WAITAUDIT);
        } else {
            repositoryDO.setStatus(RepositoryConstant.STATUS.START);
            repositoryDO.setAuditStatus(RepositoryConstant.AUDIT_STATUS.PASS);
        }
        repositoryDO.setHotHit(RepositoryConstant.FREQUENCY.BASE);
        // 批量保存
        questionEntities.add(repositoryDO);
        return questionEntities;
    }

    /**
     * 校验问题和回答的格式，去除字段为空的问题
     *
     * @param questions
     * @param answer
     * @return
     */
    private String checkQusetionAnswer(String[] questions, String answer) {
        if (StringUtils.isEmpty(answer)) {
            return "（答案不能为空）";
        }
        if (answer.length() > RepositoryConstant.MAXLENGTH) {
            return "（答案长度不能大于300）";
        }
        if (questions.length < 1) {
            return "至少需要插入一个问题";
        }
        for (int i = 0; i < questions.length; i++) {
            if (StringUtils.isEmpty(questions[i])) {
                return "（问题不能为空）";
            }
            if (questions[i].length() > RepositoryConstant.MAXLENGTH) {
                return "（问题长度不能大于300）";
            }
            if (Objects.equals(answer, questions[i])) {
                return "（问题不能与答案相同）";
            }
            questions[i] = questions[i].trim();
        }
        return "200";
    }


    public void reloadRedisData(List<Statement> zhList, List<Statement> wbList, List<Statement> integrateList) {
        List<RepositoryDO> repositoryDOList = chatterBotDao.listStatementNotClass("1");
        List<Statement> statementList = getStatementByRepository(repositoryDOList);
        for (Statement statement : statementList) {
            if ("2".equals(statement.getKnowledgeClass())) {
                zhList.add(statement);
                continue;
            } else if ("3".equals(statement.getKnowledgeClass())) {
                wbList.add(statement);
                continue;
            } else {
                integrateList.add(statement);
            }
        }
        if (!statementList.isEmpty()) {
            statementListMap.put("cfae", new ArrayList<>());
        }
        if (!zhList.isEmpty()) {
            statementListMap.put("zhihu", zhList);
        }
        if (!wbList.isEmpty()) {
            statementListMap.put("weibo", wbList);
        }
        if (!integrateList.isEmpty()) {
            statementListMap.put("integrate", integrateList);
        }
    }

    private List<Statement> getStatementByRepository(List<RepositoryDO> repositoryDOList) {
        List<Statement> statementList = new ArrayList<>();
        for (RepositoryDO repositoryDO : repositoryDOList) {
            Statement statement = new Statement();
            statement.setId(repositoryDO.getId());
            statement.setText(repositoryDO.getText());
            statement.setInResponseTo(repositoryDO.getInResponseTo());
            statement.setExtraData(repositoryDO.getExtraData());
            statement.setKnowledgeClass(repositoryDO.getKnowledgeClass());
            if (repositoryDO.getOccurrence() != null) {
                statement.setOccurrence(repositoryDO.getOccurrence());
            }
            statementList.add(statement);
        }
        return statementList;
    }

    public void reloadRedisData(List<Statement> statementList) {
        List<RepositoryDO> repositoryDOList = chatterBotDao.listStatementNotClass("1");
        List<Statement> dbStatementList = getStatementByRepository(repositoryDOList);
        statementList.addAll(dbStatementList);
        if (!dbStatementList.isEmpty()) {
            statementListMap.put("importantHighFrequency", dbStatementList);
        }
    }

}
