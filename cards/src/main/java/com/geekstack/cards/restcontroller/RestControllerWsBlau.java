package com.geekstack.cards.restcontroller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.WeibSchwarzBlauCard;
import com.geekstack.cards.service.CardListService;

@RestController
@RequestMapping("/api/data/wsblau")
public class RestControllerWsBlau {

    @Autowired
    private CardListService cardListService;

    // http://localhost:8080/api/data/wsblau
    @GetMapping()
    public ResponseEntity<List<WeibSchwarzBlauCard>> allUnionArenaPage() {
        return new ResponseEntity<List<WeibSchwarzBlauCard>>(cardListService.listofwsblau().all(), HttpStatus.OK);
    }

    // http://localhost:8080/api/data/wsblau/{booster}
    @GetMapping("/{booster}")
    public ResponseEntity<List<WeibSchwarzBlauCard>> allUnionArenaPageByAnimeCode(@PathVariable String booster) {
        String processedBooster = booster.replace("_SLASH_", "/");
        return new ResponseEntity<List<WeibSchwarzBlauCard>>(cardListService.listofwsblau().byBooster(processedBooster),
                HttpStatus.OK);
    }
}