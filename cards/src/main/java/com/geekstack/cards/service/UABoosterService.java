package com.geekstack.cards.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geekstack.cards.model.UnionArenaBooster;
import com.geekstack.cards.repository.UABoosterRepository;

@Service
public class UABoosterService {

    @Autowired
    private UABoosterRepository uaBoosterRepository;

    public List<UnionArenaBooster> allBooster() {
        return uaBoosterRepository.findAll();
    }
}
