package com.geekstack.cards.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.BoosterButton;

@Repository
public interface BoosterListRepository extends MongoRepository<BoosterButton, ObjectId>{

    @Query("{ 'tcg': ?0 }")
    List<BoosterButton> findByTcg(String tcg);

}
