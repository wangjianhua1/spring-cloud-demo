package com.espay.robot;

import com.alibaba.fastjson.JSONObject;
import com.espay.config.MatchDegreeConfig;
import com.espay.constant.BotAnswerSourceConstant;
import com.espay.constant.RepositoryConstant;
import com.espay.pojo.Sentence;
import com.espay.pojo.Statement;
import com.espay.util.BotMatchComparator;
import com.espay.util.ListUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class CalculationManage {
    public static ExecutorService cachedThreadPool = new ThreadPoolExecutor(16, 256, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());

    /**
     * 向量计算
     *
     * @param question
     * @param statementList
     * @return
     * @throws Exception
     */
    public static List<JSONObject> vectorCalculate(String question, List<Statement> statementList) throws Exception {
        List<Future<JSONObject>> futures = new ArrayList<>();
        List<Sentence> questionParseList = HanLPUtil.getDependencySentence(question);//依存句法分析
        int financeExistCount = 0;
        int zhExistCount = 0;
        int wbExistCount = 0;
        int existCount = 0;
        int tencentCount = 0;
        int total = 0;
        for (Sentence sentence : questionParseList) {
            total++;
            String word = sentence.getLemma();
            if (Word2Vec.financeWordVectorModel != null && Word2Vec.financeWordVectorModel.query(word) != null) {
                financeExistCount++;
            }
            if (Word2Vec.zhihuWordVectorModel != null && Word2Vec.zhihuWordVectorModel.query(word) != null) {
                zhExistCount++;
            }
            if (Word2Vec.weiboWordVectorModel != null && Word2Vec.weiboWordVectorModel.query(word) != null) {
                wbExistCount++;
            }
            if (Word2Vec.wordVectorModel != null && Word2Vec.wordVectorModel.query(word) != null) {
                existCount++;
            }
            if (Word2Vec.tencentWordVectorModel != null && Word2Vec.tencentWordVectorModel.query(word) != null) {
                tencentCount++;
            }

        }

        //金融新闻
        if (CollectionUtils.isNotEmpty(statementList) && total != 0 && financeExistCount >= 0.5 * total) {
            int financeCopies = statementList.size() / RepositoryConstant.CONFIG.FINANCE_COPY_NUM + 1;
            List<List<Statement>> financeCopiesList = ListUtil.averageAssign(statementList, financeCopies);
            for (List<Statement> financeStatementList : financeCopiesList) {
                GetAnswerCallable financeAnswerCallable = new GetAnswerCallable(question, questionParseList,
                        financeStatementList, Word2Vec.financeDocVectorModel, RepositoryConstant.CONFIG.FINANCE);
                Future<JSONObject> financeFuture = cachedThreadPool.submit(financeAnswerCallable);
                futures.add(financeFuture);
            }
        }
        //知乎
        if (CollectionUtils.isNotEmpty(statementList) && total != 0 && zhExistCount >= 0.5 * total) {
            int zhCopies = statementList.size() / RepositoryConstant.CONFIG.ZH_COPY_NUM + 1;
            List<List<Statement>> zhCopiesList = ListUtil.averageAssign(statementList, zhCopies);
            for (List<Statement> zhStatementList : zhCopiesList) {
                GetAnswerCallable zhAnswerCallable = new GetAnswerCallable(question, questionParseList,
                        zhStatementList, Word2Vec.zhihuDocVectorModel, RepositoryConstant.CONFIG.ZHIHU);
                Future<JSONObject> zhFuture = cachedThreadPool.submit(zhAnswerCallable);
                futures.add(zhFuture);
            }
        }
        //微博
        if (CollectionUtils.isNotEmpty(statementList) && total != 0 && wbExistCount >= 0.5 * total) {
            int wbCopies = statementList.size() / RepositoryConstant.CONFIG.WB_COPY_NUM + 1;
            List<List<Statement>> wbCopiesList = ListUtil.averageAssign(statementList, wbCopies);
            for (List<Statement> wbStatementList : wbCopiesList) {
                GetAnswerCallable wbAnswerCallable = new GetAnswerCallable(question, questionParseList,
                        wbStatementList, Word2Vec.weiboDocVectorModel, RepositoryConstant.CONFIG.WEIBO);
                Future<JSONObject> wbFuture = cachedThreadPool.submit(wbAnswerCallable);
                futures.add(wbFuture);
            }
        }
        //综合
        if (CollectionUtils.isNotEmpty(statementList) && total != 0 && existCount >= 0.5 * total) {
            int allCopies = statementList.size() / RepositoryConstant.CONFIG.ALL_COPY_NUM + 1;
            List<List<Statement>> integrateCopiesList = ListUtil.averageAssign(statementList, allCopies);
            for (List<Statement> integrateStatementList : integrateCopiesList) {
                GetAnswerCallable getAnswerCallable = new GetAnswerCallable(question, questionParseList,
                        integrateStatementList, Word2Vec.docVectorModel, RepositoryConstant.CONFIG.ALL);
                Future<JSONObject> allFuture = cachedThreadPool.submit(getAnswerCallable);
                futures.add(allFuture);
            }
        }
        //腾讯
        if (CollectionUtils.isNotEmpty(statementList) && total != 0 && tencentCount >= 0.5 * total) {
            int allCopies = statementList.size() / RepositoryConstant.CONFIG.TENCENT_COPY_NUM + 1;
            List<List<Statement>> integrateCopiesList = ListUtil.averageAssign(statementList, allCopies);
            for (List<Statement> integrateStatementList : integrateCopiesList) {
                GetAnswerCallable getAnswerCallable = new GetAnswerCallable(question, questionParseList,
                        integrateStatementList, Word2Vec.tencentDocVectorModel, RepositoryConstant.CONFIG.TENCENT);
                Future<JSONObject> allFuture = cachedThreadPool.submit(getAnswerCallable);
                futures.add(allFuture);
            }
        }

        List<JSONObject> resultList = new ArrayList<>(futures.size());
        for (Future<JSONObject> future : futures) {
            resultList.add(future.get());
        }
        return resultList;
    }

    /**
     * 距离匹配度计算
     *
     * @param question
     * @param statementList
     * @return
     * @throws Exception
     */
    public static List<JSONObject> lenvenshteinCalculate(String question, List<Statement> statementList) throws Exception {
        int allCopies = statementList.size() / RepositoryConstant.CONFIG.ALL_COPY_NUM + 1;
        List<List<Statement>> allCopiesList = ListUtil.averageAssign(statementList, allCopies);
        List<Future<JSONObject>> levenFutures = new ArrayList<>(allCopies);
        for (List<Statement> allStatementList : allCopiesList) {
            LevenCallable levenCallable = new LevenCallable(question, allStatementList);
            Future<JSONObject> levenFuture = cachedThreadPool.submit(levenCallable);
            levenFutures.add(levenFuture);
        }
        List<JSONObject> levenResultList = new ArrayList<>(levenFutures.size());
        for (Future<JSONObject> future : levenFutures) {
            JSONObject result = future.get();
            levenResultList.add(result);
        }
        return levenResultList;
    }

    public static List<JSONObject> importantHighFrequency(String question, List<Statement> statementList) throws Exception {
        JSONObject answerJson = null;
        //向量匹配
        List<JSONObject> resultList = CalculationManage.vectorCalculate(question, statementList);
        //添加匹配方式来源
        BotAnswerSourceConstant.addSource(resultList, BotAnswerSourceConstant.IMPORTANT_VECTOR);
        Collections.sort(resultList, new BotMatchComparator());
        if (resultList.size() > 0) {
            answerJson = resultList.get(0);
        }
        //距离匹配
        if (answerJson == null || answerJson.getDouble("match") <= MatchDegreeConfig.LENVENSHTEIN_VALVE) {
            List<JSONObject> lenvenResult = CalculationManage.lenvenshteinCalculate(question, statementList);
            //添加匹配方式来源
            BotAnswerSourceConstant.addSource(lenvenResult, BotAnswerSourceConstant.IMPORTANT_LENVEN);
            resultList.addAll(lenvenResult);
        }

        return resultList;
    }
}
