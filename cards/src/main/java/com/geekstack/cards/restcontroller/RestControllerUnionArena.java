package com.geekstack.cards.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.UnionArenaCard;
import com.geekstack.cards.model.UnionArenaCardDTO;
import com.geekstack.cards.service.CardListService;

import static com.geekstack.cards.utils.Constants.*;

@RestController
@RequestMapping("/api/data/unionarena")
public class RestControllerUnionArena {

    @Autowired
    private CardListService cardListService;

    // http://localhost:8080/api/data/unionarena
    @GetMapping()
    public ResponseEntity<List<UnionArenaCard>> allUnionArenaPage() {
        return new ResponseEntity<List<UnionArenaCard>>(cardListService.listofunionarena().all(), HttpStatus.OK);
    }

    // http://localhost:8080/api/data/unionarena/{animecode}
    @GetMapping("/{animecode}")
    public ResponseEntity<List<UnionArenaCard>> allUnionArenaPageByAnimeCode(@PathVariable String animecode) {
        return new ResponseEntity<List<UnionArenaCard>>(cardListService.listofunionarena().byAnimeCode(animecode),
                HttpStatus.OK);
    }

    // http://localhost:8080/api/data/unionarena/search/{phrase to search for}
    @GetMapping("/search/{term}")
    public ResponseEntity<List<UnionArenaCard>> searchUnionArena(@PathVariable String term) {
        return new ResponseEntity<List<UnionArenaCard>>(cardListService.listofunionarena().searchDatabaseFull(term),
                HttpStatus.OK);
    }

    // http://localhost:8080/api/data/unionarena/search/{phrase to search for}
    @GetMapping("/prompts/{term}")
    public ResponseEntity<List<UnionArenaCardDTO>> searchUnionArenaPrompts(@PathVariable String term) {
        return new ResponseEntity<List<UnionArenaCardDTO>>(cardListService.listofunionarena().searchDatabase(term),
                HttpStatus.OK);
    }

    // http://localhost:8080/api/data/unionarena/filter/{booster or color or
    // rarity}/{animecode}
    @GetMapping("/filter/{field}/{animecode}")
    public ResponseEntity<List<String>> UnionArenaFiltersByAnimeCode(@PathVariable String field,
            @PathVariable String animecode) {
        if (field.toLowerCase().equals(F_BOOSTER)) {
            return new ResponseEntity<List<String>>(cardListService.listofunionarena().boosterFilters(animecode),
                    HttpStatus.OK);
        }

        if (field.toLowerCase().equals(F_COLOR)) {
            return new ResponseEntity<List<String>>(cardListService.listofunionarena().colorFilters(animecode),
                    HttpStatus.OK);
        }

        if (field.toLowerCase().equals(F_RARITY)) {
            return new ResponseEntity<List<String>>(cardListService.listofunionarena().rarityFilters(animecode),
                    HttpStatus.OK);
        }
        return null;
    }

}
