package com.geekstack.cards.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.OnePieceCard;
import com.geekstack.cards.service.CardListService;

import static com.geekstack.cards.utils.Constants.*;

@RestController
@RequestMapping("/api/data/onepiece")
public class RestControllerOnePiece {
    
    @Autowired
    private CardListService cardListService;

    // http//localhost:8080/data/onepiece
    @GetMapping
    public ResponseEntity<List<OnePieceCard>> allOnePiecePage() {
        return new ResponseEntity<List<OnePieceCard>>(cardListService.listofonepiece().all(), HttpStatus.OK);
    }

    // http//localhost:8080/data/onepiece/{booster}
    @GetMapping("/{booster}")
    public ResponseEntity<List<OnePieceCard>> allOnePieceBYBooster(@PathVariable String booster) {
        return new ResponseEntity<List<OnePieceCard>>(cardListService.listofonepiece().byBooster(booster),
                HttpStatus.OK);
    }
        
    // http//localhost:8080/data/unionarena/search/{phrase to search for}
    @GetMapping("/search/{term}")
    public ResponseEntity<List<OnePieceCard>> searchOnePiece(@PathVariable String term) {
        return new ResponseEntity<List<OnePieceCard>>(cardListService.listofonepiece().searchDatabase(term),
                HttpStatus.OK);
    }
    // http//localhost:8080/data/onepiece/filter/{category or color or rarity}/{booster}
    @GetMapping("/filter/{field}/{booster}")
    public ResponseEntity<List<String>> OnePieceFiltersByBooster(@PathVariable String field,
            @PathVariable String booster) {
        if (field.toLowerCase().equals(F_CATEGORY)) {
            return new ResponseEntity<List<String>>(cardListService.listofonepiece().categoryFilters(booster),
                    HttpStatus.OK);
        }

        if (field.toLowerCase().equals(F_COLOR)) {
            return new ResponseEntity<List<String>>(cardListService.listofonepiece().colorFilters(booster),
                    HttpStatus.OK);
        }

        if (field.toLowerCase().equals(F_RARITY)) {
            return new ResponseEntity<List<String>>(cardListService.listofonepiece().rarityFilters(booster),
                    HttpStatus.OK);
        }
        return null;
    }
}
