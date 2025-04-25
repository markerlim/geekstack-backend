package com.geekstack.cards.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.TwitterApiResponse;

@Repository
public class GenericMongoRepo {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveTwitterAPIResponse(TwitterApiResponse twitterApiResponse){
        mongoTemplate.save(twitterApiResponse);
    }
}
