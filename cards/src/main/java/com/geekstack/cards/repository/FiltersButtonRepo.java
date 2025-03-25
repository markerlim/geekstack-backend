package com.geekstack.cards.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.FiltersButton;

@Repository
public interface FiltersButtonRepo extends MongoRepository<FiltersButton, ObjectId>{
    
    Optional<FiltersButton> findByParam(String param);

}
