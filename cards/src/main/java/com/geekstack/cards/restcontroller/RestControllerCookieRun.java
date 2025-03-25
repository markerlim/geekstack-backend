package com.geekstack.cards.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.CookieRunCard;
import com.geekstack.cards.service.CardListService;

@RestController
@RequestMapping("/api/data/cookierunbraverse")
public class RestControllerCookieRun {

    @Autowired
    private CardListService cardListService;

    // http//localhost:8080/data/cookierun
    @GetMapping
    public ResponseEntity<List<CookieRunCard>> allCookieRunPage() {
        return new ResponseEntity<List<CookieRunCard>>(cardListService.listofcookierun().all(), HttpStatus.OK);
    }

    // http//localhost:8080/data/cookierun/{booster}
    @GetMapping("/{booster}")
    public ResponseEntity<List<CookieRunCard>> allCookieRunPageByBooster(@PathVariable String booster) {
        return new ResponseEntity<List<CookieRunCard>>(cardListService.listofcookierun().byBooster(booster),
                HttpStatus.OK);
    }
}
