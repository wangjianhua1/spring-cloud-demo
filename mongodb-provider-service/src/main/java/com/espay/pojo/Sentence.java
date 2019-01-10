package com.espay.pojo;

import lombok.Data;

@Data
public class Sentence {
    private String lemma;
    private String deprel;
    private String headLemma;

    public Sentence(String lemma, String deprel, String headLemma) {
        this.lemma = lemma;
        this.deprel = deprel;
        this.headLemma = headLemma;
    }
}
