package com.geekstack.cards.restcontroller;

import static com.geekstack.cards.utils.Constants.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.DuelMastersCard;
import com.geekstack.cards.service.CardListService;

@RestController
@RequestMapping("/api/data/duelmasters")
public class RestControllerDuelMaster {

    @Autowired
    private CardListService cardListService;

    // http//localhost:8080/api/data/duelmasters
    @GetMapping
    public ResponseEntity<List<DuelMastersCard>> allDuelMasterPage() {
        return new ResponseEntity<List<DuelMastersCard>>(cardListService.listofduelmaster().all(), HttpStatus.OK);
    }

    // http//localhost:8080/api/data/duelmasters/{booster}
    @GetMapping("/{booster}")
    public ResponseEntity<List<DuelMastersCard>> allDuelMasterPageByBooster(@PathVariable String booster) {
        return new ResponseEntity<List<DuelMastersCard>>(cardListService.listofduelmaster().byBooster(booster),
                HttpStatus.OK);
    }

    // http//localhost:8080/api/data/duelmasters/search?{phrase to search for}
    @GetMapping("/search")
    public ResponseEntity<List<DuelMastersCard>> searchDuelMaster(@RequestParam(required = false) String term, @RequestParam(required = false) String color) {
        return new ResponseEntity<List<DuelMastersCard>>(cardListService.listofduelmaster().searchDatabase(term, color),
                HttpStatus.OK);
    }

    // http//localhost:8080/api/data/duelmaster/filter/{civilization or
    // cardtype}/{booster}
    @GetMapping("/filter/{field}/{booster}")
    public ResponseEntity<List<String>> DuelMasterFiltersByBooster(@PathVariable String field,
            @PathVariable String booster) {
        if (field.toLowerCase().equals(F_CIVILIZATION)) {
            return new ResponseEntity<List<String>>(cardListService.listofduelmaster().civilizationFilters(booster),
                    HttpStatus.OK);
        }

        if (field.toLowerCase().equals(F_CARDTYPE.toLowerCase())) {
            return new ResponseEntity<List<String>>(cardListService.listofduelmaster().cardTypeFilters(booster),
                    HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/qr")
    public ResponseEntity<List<DuelMastersCard>> setCardListService(@RequestBody String value) {
        return new ResponseEntity<List<DuelMastersCard>>(cardListService.listofduelmaster().deckExtract(value),
                HttpStatus.OK);
    }
}
