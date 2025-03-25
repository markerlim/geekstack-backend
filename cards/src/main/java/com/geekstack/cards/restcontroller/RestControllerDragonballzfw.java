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

import com.geekstack.cards.model.DragonBallzFWCard;
import com.geekstack.cards.service.CardListService;

@RestController
@RequestMapping("/api/data/dragonballzfw")
public class RestControllerDragonballzfw {

    @Autowired
    private CardListService cardListService;

    // http//localhost:8080/api/data/dragonballzfw
    @GetMapping
    public ResponseEntity<List<DragonBallzFWCard>> allDragonBallzFWPage() {
        return new ResponseEntity<List<DragonBallzFWCard>>(cardListService.listofdragonballzfw().all(), HttpStatus.OK);
    }

    // http//localhost:8080/api/data/dragonballzfw/{booster}
    @GetMapping("/{booster}")
    public ResponseEntity<List<DragonBallzFWCard>> allDragonBallzFWBYBooster(@PathVariable String booster) {
        return new ResponseEntity<List<DragonBallzFWCard>>(cardListService.listofdragonballzfw().byBooster(booster),
                HttpStatus.OK);
    }

        // http//localhost:8080/data/dragonballzfw/filter/{category or color or rarity}/{booster}
        @GetMapping("/filter/{field}/{booster}")
        public ResponseEntity<List<String>> OnePieceFiltersByBooster(@PathVariable String field,
                @PathVariable String booster) {
            if (field.toLowerCase().equals(F_CARDTYPE.toLowerCase())) {
                return new ResponseEntity<List<String>>(cardListService.listofdragonballzfw().cardtypeFilters(booster),
                        HttpStatus.OK);
            }
    
            if (field.toLowerCase().equals(F_COLOR)) {
                return new ResponseEntity<List<String>>(cardListService.listofdragonballzfw().colorFilters(booster),
                        HttpStatus.OK);
            }
    
            if (field.toLowerCase().equals(F_RARITY)) {
                return new ResponseEntity<List<String>>(cardListService.listofdragonballzfw().rarityFilters(booster),
                        HttpStatus.OK);
            }
            return null;
        }

}
