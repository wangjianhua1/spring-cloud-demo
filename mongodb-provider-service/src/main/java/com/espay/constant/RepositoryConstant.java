package com.espay.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RepositoryConstant {
    static {
        AUDIT_OPEN = getAuditOpenStatus();
    }

    //问答对知识状态
    public interface STATUS {
        public static Integer STOP = 0; //暂停
        public static Integer START = 1; //启用
        public static Integer DELETE = 3; //回收
    }

    public interface QUESTION_STATUS {
        public static Integer START = 1; //有效， 用于去重只要不是进回收站，都是有效状态，
        public static Integer DELETE = 3; //回收  进回收站后不参与重复判断等
    }

    //审核状态
    public interface AUDIT_STATUS {
        public static Integer NOTAUDIT = 0; //未提交
        public static Integer WAITAUDIT = 1; //待审核
        public static Integer PASS = 2; //已审核
    }

    //热度排序
    public interface FREQUENCY {
        public static Integer BASE = 0; //初始热度
        public static Integer ASC = 0; //升序
        public static Integer DESC = 1; //降序
    }

    //知识最大长度
    public static final Integer MAXLENGTH = 10000;

    //审核流程是否开启
    public static Boolean AUDIT_OPEN;

    private static Boolean getAuditOpenStatus() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = RepositoryConstant.class.getResourceAsStream("/repository.properties");
            properties.load(inputStream);
            String status = properties.getProperty("audit_open");
            if ("open".equals(status.trim())) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    //配置
    public interface CONFIG {
        int FINANCE_COPY_NUM = 5000;//金融语料分页数量
        int ZH_COPY_NUM = 5000;//知乎
        int WB_COPY_NUM = 5000;//微博
        int ALL_COPY_NUM = 5000;//所有的
        int TENCENT_COPY_NUM = 5000;//所有的

        String WEIBO="WEIBO";
        String ZHIHU="ZHIHU";
        String FINANCE="FINANCE";
        String ALL="ALL";
        String TENCENT="TENCENT";
    }

}
