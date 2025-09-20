package com.geekstack.cards.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.HololiveCard;
import com.geekstack.cards.service.CardListService;

@RestController()
@RequestMapping("/api/data/hololive")
public class RestControllerHololive {

    @Autowired
    private CardListService cardListService;

    @GetMapping
    public ResponseEntity<List<HololiveCard>> allHololivePage() {
        return new ResponseEntity<List<HololiveCard>>(cardListService.listofhololive().all(), HttpStatus.OK);
    }

    @GetMapping("/{booster}")
    public ResponseEntity<List<HololiveCard>> allHololiveCardsByBooster(@PathVariable String booster) {
        return new ResponseEntity<List<HololiveCard>>(cardListService.listofhololive().byBooster(booster),
                HttpStatus.OK);
    }

    // http//localhost:8080/api/data/hololive/search?{phrase to search for}
    @GetMapping("/search")
    public ResponseEntity<List<HololiveCard>> searchHololiveCard(@RequestParam String term) {
        return new ResponseEntity<List<HololiveCard>>(cardListService.listofhololive().searchDatabase(term),
                HttpStatus.OK);
    }
}
