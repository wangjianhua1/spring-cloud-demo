package com.espay.pojo.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Accessors(chain = true)
@Data
@ToString
public class FrspUser implements Serializable {
    @TableId
    private Integer id;
    /**
     * 用户姓名
     */
    private String username;
    /**
     * 用户密码
     */
    @JsonIgnore
    private String password;
    /**
     * 用户账号
     */
    private String account;
    /**
     * 所属部门(FRSP_ORGANIZATION表ID)
     */
    private Integer orgId;
    /**
     * 上级领导
     */
    private String leader;
    /**
     * 用户性别1-男,0-女
     */
    private Integer sex;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 对应角色表主键
     */
    private Integer roleId;
    /**
     * 用户状态1-有效,0无效
     */
    private Integer status;
    /**
     * 最后一次登录时间
     */
    private Date lastLoginTime;

    /**
     * 创建人，数据创建时必须写
     */
    protected String creator;
    /**
     * 修改人，数据每次修改，都必须带上修改人account
     */

    protected String updator;
    /**
     * 记录创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    protected Date createTime;
    /**
     * 记录修改时间
     */
    protected Date updateTime;
    /**
     * 逻辑删除1-已删除,0-未删除
     */
    protected Integer isDeleted;

}
