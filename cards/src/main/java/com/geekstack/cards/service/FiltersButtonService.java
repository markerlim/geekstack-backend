package com.geekstack.cards.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geekstack.cards.model.FiltersButton;
import com.geekstack.cards.repository.FiltersButtonRepo;

@Service
public class FiltersButtonService {

    @Autowired
    private FiltersButtonRepo filtersButtonRepo;

    public Optional<FiltersButton> findByParam(String param) {
        return filtersButtonRepo.findByParam(param);
    }
}
