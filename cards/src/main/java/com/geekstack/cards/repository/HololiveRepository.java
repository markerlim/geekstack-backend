package com.geekstack.cards.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.geekstack.cards.model.HololiveCard;

public interface HololiveRepository extends MongoRepository<HololiveCard, ObjectId> {
    
}
