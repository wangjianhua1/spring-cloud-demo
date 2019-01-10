package com.espay.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Document(collection = "statements")
public class Statement implements Serializable {
    @Id
    private String id;
    @Field(value = "text")
    private String text;
    @Field(value = "in_response_to")
    private List<ChatbotQuestion> inResponseTo;
    @Field(value = "occurrence")
    private Integer occurrence;
    @Field(value = "extra_data")
    private ExtraData extraData;
    @Field(value = "knowledgeClass")
    private String knowledgeClass;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ChatbotQuestion> getInResponseTo() {
        return inResponseTo;
    }

    public void setInResponseTo(List<ChatbotQuestion> inResponseTo) {
        this.inResponseTo = inResponseTo;
    }

    public Integer getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Integer occurrence) {
        this.occurrence = occurrence;
    }

    public ExtraData getExtraData() {
        return extraData;
    }

    public void setExtraData(ExtraData extraData) {
        this.extraData = extraData;
    }

    public String getKnowledgeClass() {
        return knowledgeClass;
    }

    public void setKnowledgeClass(String knowledgeClass) {
        this.knowledgeClass = knowledgeClass;
    }
}
