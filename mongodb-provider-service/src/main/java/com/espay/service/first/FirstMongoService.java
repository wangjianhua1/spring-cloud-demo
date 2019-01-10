package com.espay.service.first;

import com.espay.pojo.Statement;

import java.util.List;

public interface FirstMongoService {

    void importantHighFrequency(List<Statement> statementList);

    void statementHandle(List<Statement> zhList, List<Statement> wbList, List<Statement> integrateList);

    void saveMoliBotCorpus(String question, String answer);
}
