package com.espay.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "repositorys")
public class RepositoryDO {
    @Id
    private String id;
    @Field(value = "text")
    private String text;
    @Field(value = "in_response_to")
    private List<ChatbotQuestion> inResponseTo;
    @Field(value = "created_at")
    private Date createAt;
    @Field(value = "update_at")
    private Date updateAt;
    @Field(value = "audit_at")
    private Date auditAt;
    @Field(value = "occurrence")
    private Integer occurrence;
    @Field(value = "extra_data")
    private ExtraData extraData;
    @Field(value = "knowledgeClass")
    private String knowledgeClass;
    @Field(value = "knowledgeSource")
    private String knowledgeSource;
    @Field(value = "status")
    private Integer status;
    @Field(value = "audit_status")
    private Integer auditStatus;
    @Field(value = "created_user_id")
    private String createdUserId;
    @Field(value = "update_user_id")
    private String updateUserId;
    @Field(value = "audit_user_id")
    private String auditUserId;
    @Field(value = "created_user_name")
    private String createdUserName;
    @Field(value = "update_user_name")
    private String updateUserName;
    @Field(value = "audit_user_name")
    private String auditUserName;
    @Field(value = "hot_hit")
    private Integer hotHit;

}
