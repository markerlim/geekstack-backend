package com.geekstack.cards.restcontroller;

import static com.geekstack.cards.utils.Constants.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.DuelMasterCard;
import com.geekstack.cards.service.CardListService;

@RestController
@RequestMapping("/api/data/duelmasters")
public class RestControllerDuelMaster {

    @Autowired
    private CardListService cardListService;

    // http//localhost:8080/data/duelmaster
    @GetMapping
    public ResponseEntity<List<DuelMasterCard>> allDuelMasterPage() {
        return new ResponseEntity<List<DuelMasterCard>>(cardListService.listofduelmaster().all(), HttpStatus.OK);
    }

    // http//localhost:8080/data/duelmaster/{booster}
    @GetMapping("/{booster}")
    public ResponseEntity<List<DuelMasterCard>> allDuelMasterPageByBooster(@PathVariable String booster) {
        return new ResponseEntity<List<DuelMasterCard>>(cardListService.listofduelmaster().byBooster(booster),
                HttpStatus.OK);
    }
        // http//localhost:8080/data/duelmaster/search/{phrase to search for}
    @GetMapping("/search/{term}")
    public ResponseEntity<List<DuelMasterCard>> searchDuelMaster(@PathVariable String term) {
        return new ResponseEntity<List<DuelMasterCard>>(cardListService.listofduelmaster().searchDatabase(term),
                HttpStatus.OK);
    }
    // http//localhost:8080/data/duelmaster/filter/{civilization or cardtype}/{booster}
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
}
