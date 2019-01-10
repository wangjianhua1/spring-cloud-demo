package com.espay.dao.first;

import com.espay.constant.RepositoryConstant;
import com.espay.pojo.RepositoryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatterBotDaoImpl implements ChatterBotDao {
    @Autowired
    @Qualifier("firstMongo")
    private MongoTemplate fistMongo;


    @Override
    public List<RepositoryDO> listStatementNotClass(String knowledgeClass) {
        Query query = new Query();
        query.addCriteria(Criteria.where("occurrence").exists(false));
        query.addCriteria(Criteria.where("status").is(RepositoryConstant.STATUS.START));
        if (knowledgeClass != null) {
            query.addCriteria(Criteria.where("knowledgeClass").ne(knowledgeClass));
        }
        return fistMongo.find(query, RepositoryDO.class);
    }

    @Override
    public Boolean isExistText(List<String> texts) {
        Query query = new Query();
        if (texts != null && texts.size() > 0) {
            //只查询未被回收的问答对和单独问题
            query.addCriteria(Criteria.where("status").ne(RepositoryConstant.QUESTION_STATUS.DELETE));
            query.addCriteria(Criteria.where("text").in(texts));
        } else {
            return false;
        }
        Long count = fistMongo.count(query, RepositoryDO.class);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void saveRepositorys(List<RepositoryDO> repositoryDOS) {
        fistMongo.insertAll(repositoryDOS);
    }


}
