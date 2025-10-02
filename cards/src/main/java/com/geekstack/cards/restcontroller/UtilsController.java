package com.geekstack.cards.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.ChangeLog;
import com.geekstack.cards.repository.ChangeLogRepository;

@RestController
@RequestMapping("/api/utils")
public class UtilsController {

    @Autowired
    private ChangeLogRepository changeLogRepository;

    @GetMapping("/changes")
    public ResponseEntity<ChangeLog> getChanges(@RequestParam(required = false) String target,
            @RequestParam(required = false) String type, @RequestParam(required = false) String tcg) {
        ChangeLog changeLog = changeLogRepository.getLatestChangeByParams(target, type, tcg);
        return new ResponseEntity<>(changeLog, HttpStatus.OK);
    }
}
