package com.espay.dao.first;

import com.espay.pojo.RepositoryDO;

import java.util.List;

public interface ChatterBotDao {

    List<RepositoryDO> listStatementNotClass(String knowledgeClass);

    Boolean isExistText(List<String> texts);

    void saveRepositorys(List<RepositoryDO> repositoryDOS);

}
