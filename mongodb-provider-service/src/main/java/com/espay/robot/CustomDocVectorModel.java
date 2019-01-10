package com.espay.robot;

import com.espay.constant.DependencySentenceConstant;
import com.espay.pojo.Sentence;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import java.util.List;

public class CustomDocVectorModel extends DocVectorModel {
    private final WordVectorModel wordVectorModel;

    public CustomDocVectorModel(WordVectorModel wordVectorModel) {
        super(wordVectorModel);
        this.wordVectorModel = wordVectorModel;
    }

    /**
     * 将一个文档转为向量
     *
     * @param termList 文档
     * @return 向量
     */
    public Vector query(List<Sentence> termList) {
        if (termList == null || termList.size() == 0) return null;
        Vector result = new Vector(dimension());
        int n = 0;
        String deprel = null;
        float weight = 1.0f;
        for (Sentence term : termList) {
            Vector vector = wordVectorModel.vector(term.getLemma());
            if (vector == null) {
                continue;
            }
            deprel = term.getDeprel();
            ++n;
            Vector newVector = new Vector(dimension());
            newVector.addToSelf(vector);
            weight = 1.0f / DependencySentenceConstant.DEPREL_WEIGHT.getOrDefault(deprel, 1.0f);
            result.addToSelf(newVector.divideToSelf(weight));
        }
        if (n == 0) {
            return null;
        }
        result.normalize();
        return result;
    }

    /**
     * 文档相似度计算
     *
     * @param what
     * @param with
     * @return
     */
    public float similarity(List<Sentence> what, List<Sentence> with) {
        Vector A = query(what);
        if (A == null) return -1f;
        Vector B = query(with);
        if (B == null) return -1f;
        return A.cosineForUnitVector(B);
    }

}


