package com.geekstack.cards.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geekstack.cards.model.BoosterButton;
import com.geekstack.cards.repository.BoosterListRepository;

@Service
public class BoosterListService {

    @Autowired
    private BoosterListRepository boosterListRepository;

    public List<BoosterButton> allBoosterByTcg(String tcg) {
        return boosterListRepository.findByTcg(tcg).stream()
                .sorted(Comparator.comparingInt(BoosterButton::getOrder))
                .toList();
    }
    
}
