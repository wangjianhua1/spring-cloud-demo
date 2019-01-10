package com.espay.pojo.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Accessors(chain = true)
@Data
@ToString
public class SensitiveWord implements Serializable {
    @TableId
    private Integer id;

    private String typeName;

    private String word;

    private Date createTime;
}
