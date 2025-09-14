package com.geekstack.cards.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.GundamCard;
import com.geekstack.cards.service.CardListService;

@RestController()
@RequestMapping("/api/data/gundam")
public class RestControllerGundam {

    @Autowired
    private CardListService cardListService;

    @GetMapping
    public ResponseEntity<List<GundamCard>> allHololivePage() {
        return new ResponseEntity<List<GundamCard>>(cardListService.listofgundam().all(), HttpStatus.OK);
    }

    @GetMapping("/{booster}")
    public ResponseEntity<List<GundamCard>> allHololiveCardsByBooster(@PathVariable String booster) {
        return new ResponseEntity<List<GundamCard>>(cardListService.listofgundam().byBooster(booster),
                HttpStatus.OK);
    }

    @GetMapping("/search/{term}")
    public ResponseEntity<List<GundamCard>> searchHololiveCard(@PathVariable String term) {
        return new ResponseEntity<List<GundamCard>>(cardListService.listofgundam().searchDatabase(term),
                HttpStatus.OK);
    }

    @PostMapping("/qr")
    public ResponseEntity<List<GundamCard>> setCardListService(@RequestBody String value) {
        return new ResponseEntity<List<GundamCard>>(cardListService.listofgundam().deckExtract(value), HttpStatus.OK);
    }
}
