package com.geekstack.cards.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.UnionArenaBooster;

@Repository
public interface UABoosterRepository extends MongoRepository<UnionArenaBooster, ObjectId>  {

}
