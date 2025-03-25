package com.geekstack.cards.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.CardPriceFULLA;
import com.geekstack.cards.model.CardPriceYYT;
import com.geekstack.cards.service.CardListService;

@RestController
@RequestMapping("/api/prices")
public class CardsController {

    @Autowired
    private CardListService cardListService;

    // http//localhost:8080/yyt/{priceYytId}
    @GetMapping("/yyt/{priceYytId}")
    public ResponseEntity<CardPriceYYT> getOneYYTCard(@PathVariable String priceYytId) {
        return new ResponseEntity<CardPriceYYT>(cardListService.cardprices().byYuyutei(priceYytId),HttpStatus.OK);
    }

    // http//localhost:8080/fulla/{priceFullaId}
    @GetMapping("/fulla/{priceFullaId}")
    public ResponseEntity<CardPriceFULLA> getOneFullaCard(@PathVariable String priceFullaId) {
        return new ResponseEntity<CardPriceFULLA>(cardListService.cardprices().byFulla(priceFullaId),HttpStatus.OK);
    }

}