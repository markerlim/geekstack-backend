package com.geekstack.cards.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.BoosterButton;
import com.geekstack.cards.model.DuelMasterBtn;

@Repository
public class BoosterListRepositoryV2 {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public   List<BoosterButton> findByTcg(String tcg){
        Query query = new Query(Criteria.where("tcg").is(tcg));
        return mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC,"order")), BoosterButton.class,"BoosterList");
    }


    public List<DuelMasterBtn> getDuelMasterBooster(){
        Query query = new Query();
        return mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC,"timestamp")), DuelMasterBtn.class, "NewList");
    }
}
