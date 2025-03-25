package com.geekstack.cards.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.BoosterButton;
import com.geekstack.cards.model.FiltersButton;
import com.geekstack.cards.model.UnionArenaBooster;
import com.geekstack.cards.service.BoosterListService;
import com.geekstack.cards.service.CardListService;
import com.geekstack.cards.service.FiltersButtonService;
import com.geekstack.cards.service.UABoosterService;

@RestController
@RequestMapping("/api/boosterlist")
public class BoosterController {

    @Autowired
    private UABoosterService uaBoosterService;
    @Autowired
    private BoosterListService boosterListService;
    @Autowired
    private FiltersButtonService filtersButtonService;
    @Autowired
    private CardListService cardListService;


    @GetMapping("/{tcg}")
    public ResponseEntity<List<BoosterButton>> getUAList(@PathVariable String tcg){
        return new ResponseEntity<List<BoosterButton>>(boosterListService.allBoosterByTcg(tcg),HttpStatus.OK);
    }

    @GetMapping("/unionarena/{animecode}")
    public ResponseEntity<List<UnionArenaBooster>> getAllUABooster(@PathVariable String animecode) {
        
        return new ResponseEntity<List<UnionArenaBooster>>(uaBoosterService.allBooster(), HttpStatus.OK);
    }

    @GetMapping("/filters/{param}")
    public ResponseEntity<Optional<FiltersButton>> getFilters(@PathVariable String param){
        return new ResponseEntity<Optional<FiltersButton>>(filtersButtonService.findByParam(param),HttpStatus.OK);
    }

    @GetMapping("/duelmasters")
    public ResponseEntity<List<String>> getDMList(){
        return new ResponseEntity<List<String>>(cardListService.listofduelmaster().boosterFilters(),HttpStatus.OK);
    }
}
