package com.geekstack.cards.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.BoosterButton;
import com.geekstack.cards.model.ChangeLog;
import com.geekstack.cards.model.DuelMasterBtn;
import com.geekstack.cards.model.FiltersButton;
import com.geekstack.cards.model.UnionArenaBooster;
import com.geekstack.cards.repository.BoosterListRepositoryV2;
import com.geekstack.cards.repository.ChangeLogRepository;
import com.geekstack.cards.service.FiltersButtonService;
import com.geekstack.cards.service.UABoosterService;

@RestController
@RequestMapping("/api/boosterlist")
public class BoosterController {

    @Autowired
    private UABoosterService uaBoosterService;
    @Autowired
    private FiltersButtonService filtersButtonService;
    @Autowired
    private BoosterListRepositoryV2 boosterListRepositoryV2;

    @GetMapping("/{tcg}")
    public ResponseEntity<List<BoosterButton>> getUAList(@PathVariable String tcg){
        return new ResponseEntity<List<BoosterButton>>(boosterListRepositoryV2.findByTcg(tcg),HttpStatus.OK);
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
    public ResponseEntity<List<DuelMasterBtn>> getDMList(){
        return new ResponseEntity<List<DuelMasterBtn>>(boosterListRepositoryV2.getDuelMasterBooster(),HttpStatus.OK);
    }
}
