package com.wjh.demo.kafka;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    @ApiModelProperty(value = "topic", example = "tp-1")
    private String topic;
    @ApiModelProperty(value = "msg", example = "测试")
    private String msg;

}
