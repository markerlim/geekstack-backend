package com.geekstack.cards.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geekstack.cards.model.CookieRunDecklist;
import com.geekstack.cards.model.DragonballzFWDecklist;
import com.geekstack.cards.model.Notification;
import com.geekstack.cards.model.OnePieceDecklist;
import com.geekstack.cards.model.UnionArenaDecklist;
import com.geekstack.cards.repository.UserDetailsMongoRepository;
import com.geekstack.cards.service.CurrencyConversionService;
import com.geekstack.cards.service.EmailService;
import com.geekstack.cards.service.FirebaseService;
import com.geekstack.cards.service.GoogleCloudStorageService;
import com.geekstack.cards.service.UserDetailService;
import com.google.firebase.auth.FirebaseToken;

@RestController
@RequestMapping("/api/user")
public class UserDetailsController {

    private final static Logger logger = LoggerFactory.getLogger(UserDetailsController.class);
    @Autowired
    private UserDetailsMongoRepository userDetailsMongoRepository;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createNewUser(@RequestBody String idToken) {
        Map<String, Object> response = new HashMap<>();
        try {
            FirebaseToken decoded = firebaseService.verifyIdToken(idToken);
            String userId = decoded.getUid();
            String name = decoded.getName();
            String displaypic = decoded.getPicture();
            String email = decoded.getEmail();
            if (userDetailService.createUser(userId, name, displaypic, email) == 1) {
                response.put("message", "User created successfully");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            response.put("message", "User exist in database");
            response.put("userObject", userDetailService.getOneUser(userId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/get")
    public ResponseEntity<Map<String, Object>> getOneUser(@RequestBody String idToken) {
        Map<String, Object> response = new HashMap<>();
        try {
            FirebaseToken decoded = firebaseService.verifyIdToken(idToken);
            String userId = decoded.getUid();
            response.put("userObject", userDetailService.getOneUser(userId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Union Arena save user deck endpoint
    @PostMapping("/save/unionarena/{userId}/deck")
    public ResponseEntity<Map<String, Object>> saveUADeck(@PathVariable String userId,
            @RequestParam(required = false) String deckuid,
            @RequestBody UnionArenaDecklist decklist) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (deckuid == null || deckuid.isEmpty()) {
                userDetailsMongoRepository.createUnionArenaDecklist(decklist, userId);
            } else {
                userDetailsMongoRepository.updateUnionArenaDecklist(decklist, userId, deckuid);
            }

            response.put("message", "Deck created successfully");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckId", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Union Arena load user deck endpoint
    @GetMapping("/load/unionarena/{userId}/deck")
    public ResponseEntity<List<UnionArenaDecklist>> loadUADeck(@PathVariable String userId) {
        return new ResponseEntity<List<UnionArenaDecklist>>(userDetailsMongoRepository.loadUnionArenaDecklist(userId),
                HttpStatus.OK);
    }

    // One Piece save user deck endpoint
    @PostMapping("/save/onepiece/{userId}/deck")
    public ResponseEntity<Map<String, Object>> saveOPDeck(@PathVariable String userId,
            @RequestBody OnePieceDecklist decklist) {
        Map<String, Object> response = new HashMap<>();

        try {
            userDetailsMongoRepository.createOnePieceDecklist(decklist, userId);
            response.put("message", "Deck created successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckId", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // One Piece load user deck endpoint
    @GetMapping("/load/onepiece/{userId}/deck")
    public ResponseEntity<List<OnePieceDecklist>> loadOPDeck(@PathVariable String userId) {
        return new ResponseEntity<List<OnePieceDecklist>>(userDetailsMongoRepository.loadOnePieceDecklist(userId),
                HttpStatus.OK);
    }

    // DragonballzFW save user deck endpoint
    @PostMapping("/save/dragonballzfw/{userId}/deck")
    public ResponseEntity<Map<String, Object>> saveDBZFWDeck(@PathVariable String userId,
            @RequestBody DragonballzFWDecklist decklist) {
        Map<String, Object> response = new HashMap<>();
        try {
            userDetailsMongoRepository.createDragonballzFWDecklist(decklist, userId);
            response.put("message", "Deck created successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckId", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DragonballzFW load user deck endpoint
    @GetMapping("/load/dragonballzfw/{userId}/deck")
    public ResponseEntity<List<DragonballzFWDecklist>> loadDBZFWDeck(@PathVariable String userId) {
        return new ResponseEntity<List<DragonballzFWDecklist>>(
                userDetailsMongoRepository.loadDragonballzFWDecklist(userId), HttpStatus.OK);
    }

    // DragonballzFW save user deck endpoint
    @PostMapping("/save/cookierunbraverse/{userId}/deck")
    public ResponseEntity<Map<String, Object>> saveCRBDeck(@PathVariable String userId,
            @RequestBody CookieRunDecklist decklist) {
        Map<String, Object> response = new HashMap<>();
        try {
            userDetailsMongoRepository.createCookieRunDecklist(decklist, userId);
            response.put("message", "Deck created successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckId", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Cookierunbraverse load user deck endpoint
    @GetMapping("/load/cookierunbraverse/{userId}/deck")
    public ResponseEntity<List<CookieRunDecklist>> loadCRBDeck(@PathVariable String userId) {
        return new ResponseEntity<List<CookieRunDecklist>>(userDetailsMongoRepository.loadCookieRunDecklist(userId),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{tcg}/{userId}/deck/{deckId}")
    public ResponseEntity<Map<String, Object>> deleteDeck(@PathVariable String tcg, @PathVariable String userId,
            @PathVariable String deckId) {
        Map<String, Object> response = new HashMap<>();
        try {
            userDetailsMongoRepository.deleteDecklist(tcg, userId, deckId);
            response.put("message", "Deck deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckId", deckId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Stopped at
    // Resolved [org.springframework.web.bind.MissingRequestHeaderException:
    // Required request header 'payload' for method parameter type String is not
    // present]
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> listOfNotifications(@RequestHeader("Authorization") String authorization,
            @RequestParam(defaultValue = "10") String limit) throws Exception {
        return new ResponseEntity<List<Notification>>(userDetailService.listNotifications(authorization, limit),
                HttpStatus.OK);
    }

    @PostMapping("/upd/{name}/of/{userId}")
    public ResponseEntity<Map<String, Object>> updateName(@PathVariable String name, @PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            userDetailService.updateUserName(name, userId);
            response.put("message", "Name change success!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/update/image/{userId}")
    public ResponseEntity<Map<String, Object>> updateImage(@PathVariable String userId,@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            String fileName = "profile-pic-" + System.currentTimeMillis() + ".png"; 
            String fileUrl = googleCloudStorageService.uploadImage(file.getBytes(), userId, fileName);
            userDetailService.updateDisplaypic(fileUrl, userId);
            response.put("message", "Successfully Uploaded");
            response.put("fileUrl", fileUrl);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("Error uploading image", e);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    

    @PostMapping("/report-error")
    public ResponseEntity<Map<String, Object>> reportError(@RequestBody String payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            emailService.sendReportEmail(payload);
            response.put("message", "Error reported successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getExcRate")
    public ResponseEntity<String> exchangeRate(@RequestParam(required = false, defaultValue = "SGD") String base,
            @RequestParam(required = false, defaultValue = "JPY") String symbol) {
        return new ResponseEntity<String>(currencyConversionService.getExchangeRate(base, symbol),
                HttpStatus.OK);
    }
}
