package com.espay.pojo;

import lombok.Data;

/**
 * 返回类型
 */
@Data
public class ResultInfo {
    private Integer statusCode;
    private Object data;
    private String message;
    private String navTabId;
    private String rel;
    private String callbackType;
    private String forwardUrl;
}
