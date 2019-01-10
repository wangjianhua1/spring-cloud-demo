package com.espay.robot;

import com.espay.pojo.Sentence;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;

import java.util.ArrayList;
import java.util.List;

public class HanLPUtil {
    public static List<Sentence> getDependencySentence(String text) {
        CoNLLSentence sentence = HanLP.parseDependency(text);
        List<Sentence> wordList = new ArrayList<>(sentence.word.length);
        Sentence sentenceTransform = null;
        for (CoNLLWord word : sentence) {
            sentenceTransform = new Sentence(word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
            wordList.add(sentenceTransform);
        }
        return wordList;
    }
}
