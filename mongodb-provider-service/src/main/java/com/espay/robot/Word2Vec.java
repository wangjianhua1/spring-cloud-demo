package com.espay.robot;

import com.espay.config.RobotConfig;
import com.espay.constant.RepositoryConstant;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

//import com.espay.service.ProperNounsService;

@Component
public class Word2Vec {
    private static final Log log = LogFactory.getLog(Word2Vec.class);
    public static WordVectorModel wordVectorModel = null;
    public static WordVectorModel financeWordVectorModel = null;
    public static WordVectorModel zhihuWordVectorModel = null;
    public static WordVectorModel weiboWordVectorModel = null;
    public static CustomDocVectorModel docVectorModel = null;
    public static CustomDocVectorModel financeDocVectorModel = null;
    public static CustomDocVectorModel zhihuDocVectorModel = null;
    public static CustomDocVectorModel weiboDocVectorModel = null;

    public static WordVectorModel tencentWordVectorModel = null;
    public static CustomDocVectorModel tencentDocVectorModel = null;

    @Autowired
    private RobotConfig robotConfig;
    @Value("${word2vec.type}")
    private String types;

    private boolean loading = true;


    @PostConstruct
    public void Initialization() {
        CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getModelFileName(), RepositoryConstant.CONFIG.ALL));
        CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getFinanceModel(), RepositoryConstant.CONFIG.FINANCE));
        CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getWeiboModel(), RepositoryConstant.CONFIG.WEIBO));
        CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getZhihuModel(), RepositoryConstant.CONFIG.ZHIHU));
        CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getTencentModel(), RepositoryConstant.CONFIG.TENCENT));
    }

    public void executeModel(String typeName, String ops) {
        log.info("=====executeModel=====" + ops + "===" + typeName + "===" + loading);
        if (StringUtils.isNotBlank(typeName) && StringUtils.isNotBlank(ops)) {
            if ("add".equals(ops) && !loading) {
                if (RepositoryConstant.CONFIG.ALL.equals(typeName)) {
                    types = RepositoryConstant.CONFIG.ALL;
                    CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getModelFileName(), RepositoryConstant.CONFIG.ALL));
                }
                if (RepositoryConstant.CONFIG.FINANCE.equals(typeName)) {
                    types = RepositoryConstant.CONFIG.FINANCE;
                    CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getModelFileName(), RepositoryConstant.CONFIG.FINANCE));
                }
                if (RepositoryConstant.CONFIG.WEIBO.equals(typeName)) {
                    types = RepositoryConstant.CONFIG.WEIBO;
                    CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getModelFileName(), RepositoryConstant.CONFIG.WEIBO));
                }
                if (RepositoryConstant.CONFIG.ZHIHU.equals(typeName)) {
                    types = RepositoryConstant.CONFIG.ZHIHU;
                    CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getModelFileName(), RepositoryConstant.CONFIG.ZHIHU));
                }
                if (RepositoryConstant.CONFIG.TENCENT.equals(typeName)) {
                    types = RepositoryConstant.CONFIG.TENCENT;
                    CalculationManage.cachedThreadPool.execute(new ModelThread(robotConfig.getModelFileName(), RepositoryConstant.CONFIG.TENCENT));
                }
            }
            if ("sub".equals(ops)) {
                if (RepositoryConstant.CONFIG.ALL.equals(typeName)) {
                    wordVectorModel = null;
                    docVectorModel = null;
                }
                if (RepositoryConstant.CONFIG.FINANCE.equals(typeName)) {
                    financeWordVectorModel = null;
                    financeDocVectorModel = null;
                }
                if (RepositoryConstant.CONFIG.WEIBO.equals(typeName)) {
                    weiboWordVectorModel = null;
                    weiboDocVectorModel = null;
                }
                if (RepositoryConstant.CONFIG.ZHIHU.equals(typeName)) {
                    zhihuWordVectorModel = null;
                    zhihuDocVectorModel = null;
                }
                if (RepositoryConstant.CONFIG.TENCENT.equals(typeName)) {
                    tencentWordVectorModel = null;
                    tencentDocVectorModel = null;
                }
            }
        }
    }

    class ModelThread implements Runnable {
        private String modelPath;
        private String type;

        ModelThread(String modelPath, String type) {
            this.modelPath = modelPath;
            this.type = type;
        }

        @Override
        public void run() {
            try {
                log.info("-------------------加载词向量模型-------" + type + "-------------" + types);
                if (types.contains(type)) {
                    if (RepositoryConstant.CONFIG.WEIBO.equals(type)) {
                        weiboWordVectorModel = new WordVectorModel(modelPath);
                        weiboDocVectorModel = new CustomDocVectorModel(weiboWordVectorModel);
                    }
                    if (RepositoryConstant.CONFIG.ZHIHU.equals(type)) {
                        zhihuWordVectorModel = new WordVectorModel(modelPath);
                        zhihuDocVectorModel = new CustomDocVectorModel(zhihuWordVectorModel);
                    }
                    if (RepositoryConstant.CONFIG.FINANCE.equals(type)) {
                        financeWordVectorModel = new WordVectorModel(modelPath);
                        financeDocVectorModel = new CustomDocVectorModel(financeWordVectorModel);
                    }
                    if (RepositoryConstant.CONFIG.ALL.equals(type)) {
                        wordVectorModel = new WordVectorModel(modelPath);
                        docVectorModel = new CustomDocVectorModel(wordVectorModel);
                    }
                    if (RepositoryConstant.CONFIG.TENCENT.equals(type)) {
                        tencentWordVectorModel = new WordVectorModel(modelPath);
                        tencentDocVectorModel = new CustomDocVectorModel(tencentWordVectorModel);
                    }
                    log.info("-------------------加载完成---------------" + type + "-----");
                    loading = false;
                }

            } catch (IOException e) {
                log.error(e);
            }
        }
    }
}
