package com.geekstack.cards.restcontroller;

import static com.geekstack.cards.utils.Constants.F_CRBDECKS;
import static com.geekstack.cards.utils.Constants.F_DBZFWDECKS;
import static com.geekstack.cards.utils.Constants.F_DMDECKS;
import static com.geekstack.cards.utils.Constants.F_GCGDECKS;
import static com.geekstack.cards.utils.Constants.F_OPDECKS;
import static com.geekstack.cards.utils.Constants.F_UADECKS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geekstack.cards.model.CookieRunDecklist;
import com.geekstack.cards.model.DragonballzFWDecklist;
import com.geekstack.cards.model.DuelMasterDecklist;
import com.geekstack.cards.model.FirebaseUser;
import com.geekstack.cards.model.GenericDecklist;
import com.geekstack.cards.model.GundamDecklist;
import com.geekstack.cards.model.Notification;
import com.geekstack.cards.model.NotificationMerged;
import com.geekstack.cards.model.OnePieceDecklist;
import com.geekstack.cards.model.UnionArenaDecklist;
import com.geekstack.cards.model.NotificationMerged.SenderInfo;
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

    // Same function as /init, gonna remove this soon
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createNewUser(@RequestBody String idToken) {
        Map<String, Object> response = new HashMap<>();
        idToken = idToken.trim().replaceAll("\"", "");
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

    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initUser(@AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userId = user.getUid();
            String name = user.getName(); // From Firebase claims
            String displaypic = user.getPicture(); // From Firebase claims
            String email = user.getEmail();
            if (userDetailService.createUser(userId, name, displaypic, email) == 1) {
                response.put("message", "User created successfully");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            response.put("message", "User exist in database");
            response.put("userObject", userDetailService.getOneUser(userId));
            response.put("unreadNotification",userDetailService.checkNumOfUnread(userId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/get")
    public ResponseEntity<Map<String, Object>> getOneUser(@AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userId = user.getUid();
            response.put("userObject", userDetailService.getOneUser(userId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Union Arena save user deck endpoint
    @PostMapping("/save/unionarena")
    public ResponseEntity<Map<String, Object>> saveUADeck(
            @RequestParam(required = false) String deckuid,
            @RequestBody GenericDecklist decklist, @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String uuid = "";
        String userId = user.getUid();
        try {
            if (deckuid == null || deckuid.isEmpty()) {
                uuid = userDetailsMongoRepository.createUnionArenaDecklist(decklist, userId);
                response.put("message", "Deck created successfully");
            } else {
                uuid = userDetailsMongoRepository.updateUnionArenaDecklist(decklist, userId, deckuid);
                response.put("message", "Deck updated successfully");

            }

            response.put("deckuid", uuid);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckuid", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Union Arena load user lightweight of all deck endpoint
    @GetMapping("/load/unionarena")
    public ResponseEntity<List<GenericDecklist>> loadUADeck(@AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<List<GenericDecklist>>(userDetailsMongoRepository.loadDecklist(userId, F_UADECKS),
                HttpStatus.OK);
    }

    // Union Arena load deck data by deckuid
    @GetMapping("/load/unionarena/{deckuid}")
    public ResponseEntity<UnionArenaDecklist> loadOneUADeck(@PathVariable String deckuid,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<UnionArenaDecklist>(
                userDetailsMongoRepository.loadUnionArenaDecklist(userId, deckuid),
                HttpStatus.OK);
    }

    // One Piece save user deck endpoint
    @PostMapping("/save/onepiece")
    public ResponseEntity<Map<String, Object>> saveOPDeck(
            @RequestParam(required = false) String deckuid,
            @RequestBody GenericDecklist decklist,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String uuid = "";
        String userId = user.getUid();
        try {
            if (deckuid == null || deckuid.isEmpty()) {
                uuid = userDetailsMongoRepository.createOnePieceDecklist(decklist, userId);
                response.put("message", "Deck created successfully");
            } else {
                uuid = userDetailsMongoRepository.updateOnePieceDecklist(decklist, userId, deckuid);
                response.put("message", "Deck updated successfully");

            }
            response.put("deckuid", uuid);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckuid", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // One Piece load user lightweight of all deck endpoint
    @GetMapping("/load/onepiece")
    public ResponseEntity<List<GenericDecklist>> loadOPDeck(@AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<List<GenericDecklist>>(userDetailsMongoRepository.loadDecklist(userId, F_OPDECKS),
                HttpStatus.OK);
    }

    // One Piece load deck data by deckuid
    @GetMapping("/load/onepiece/{deckuid}")
    public ResponseEntity<OnePieceDecklist> loadOneOPDeck(@PathVariable String deckuid,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<OnePieceDecklist>(
                userDetailsMongoRepository.loadOnePieceDecklist(userId, deckuid),
                HttpStatus.OK);
    }

    // DragonballzFW save user deck endpoint
    @PostMapping("/save/dragonballzfw")
    public ResponseEntity<Map<String, Object>> saveDBZFWDeck(
            @RequestParam(required = false) String deckuid,
            @RequestBody GenericDecklist decklist,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String uuid = "";
        String userId = user.getUid();
        try {
            if (deckuid == null || deckuid.isEmpty()) {
                uuid = userDetailsMongoRepository.createDragonballzFWDecklist(decklist, userId);
                response.put("message", "Deck created successfully");
            } else {
                uuid = userDetailsMongoRepository.updateDragonballzFWDecklist(decklist, userId, deckuid);
                response.put("message", "Deck updated successfully");
            }
            response.put("deckuid", uuid);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckuid", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DragonballzFW load user lightweight of all deck endpoint
    @GetMapping("/load/dragonballzfw")
    public ResponseEntity<List<GenericDecklist>> loadDBZFWDeck(
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<List<GenericDecklist>>(
                userDetailsMongoRepository.loadDecklist(userId, F_DBZFWDECKS), HttpStatus.OK);
    }

    // DragonballzFW load deck data by deckuid
    @GetMapping("/load/dragonballzfw/{deckuid}")
    public ResponseEntity<DragonballzFWDecklist> loadOneDBZFWDeck(@PathVariable String deckuid,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<DragonballzFWDecklist>(
                userDetailsMongoRepository.loadDragonballzFWDecklist(userId, deckuid),
                HttpStatus.OK);
    }

    // Cookierunbraverse save user deck endpoint
    @PostMapping("/save/cookierunbraverse")
    public ResponseEntity<Map<String, Object>> saveCRBDeck(
            @RequestParam(required = false) String deckuid,
            @RequestBody GenericDecklist decklist,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String uuid = "";
        String userId = user.getUid();
        try {
            if (deckuid == null || deckuid.isEmpty()) {
                uuid = userDetailsMongoRepository.createCookieRunDecklist(decklist, userId);
                response.put("message", "Deck created successfully");
            } else {
                uuid = userDetailsMongoRepository.updateCookieRunDecklist(decklist, userId, deckuid);
                response.put("message", "Deck updated successfully");
            }
            response.put("deckuid", uuid);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckuid", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Cookierunbraverse load user lightweight of all deck endpoint
    @GetMapping("/load/cookierunbraverse")
    public ResponseEntity<List<GenericDecklist>> loadCRBDeck(@AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<List<GenericDecklist>>(userDetailsMongoRepository.loadDecklist(userId, F_CRBDECKS),
                HttpStatus.OK);
    }

    // Cookierunbraverse load deck data by deckuid
    @GetMapping("/load/cookierunbraverse/{deckuid}")
    public ResponseEntity<CookieRunDecklist> loadOneCRBDeck(@PathVariable String deckuid,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<CookieRunDecklist>(
                userDetailsMongoRepository.loadCookieRunDecklist(userId, deckuid),
                HttpStatus.OK);
    }

    // Duelmasters save user deck endpoint
    @PostMapping("/save/duelmasters")
    public ResponseEntity<Map<String, Object>> saveDMDeck(
            @RequestParam(required = false) String deckuid,
            @RequestBody GenericDecklist decklist,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String uuid = "";
        String userId = user.getUid();
        try {
            if (deckuid == null || deckuid.isEmpty()) {
                uuid = userDetailsMongoRepository.createDuelMasterDecklist(decklist, userId);
                response.put("message", "Deck created successfully");
            } else {
                uuid = userDetailsMongoRepository.updateDuelMasterDecklist(decklist, userId, deckuid);
                response.put("message", "Deck updated successfully");
            }
            response.put("deckuid", uuid);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckuid", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Duelmasters load user lightweight of all deck endpoint
    @GetMapping("/load/duelmasters")
    public ResponseEntity<List<GenericDecklist>> loadDMDeck(@AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<List<GenericDecklist>>(userDetailsMongoRepository.loadDecklist(userId, F_DMDECKS),
                HttpStatus.OK);
    }

    // Duelmasters load deck data by deckuid
    @GetMapping("/load/duelmasters/{deckuid}")
    public ResponseEntity<DuelMasterDecklist> loadOneDMDeck(@PathVariable String deckuid,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<DuelMasterDecklist>(
                userDetailsMongoRepository.loadDuelMasterDecklist(userId, deckuid),
                HttpStatus.OK);
    }

    // Gundam save user deck endpoint
    @PostMapping("/save/gundam")
    public ResponseEntity<Map<String, Object>> saveGCGDeck(
            @RequestParam(required = false) String deckuid,
            @RequestBody GenericDecklist decklist,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String uuid = "";
        String userId = user.getUid();
        try {
            if (deckuid == null || deckuid.isEmpty()) {
                uuid = userDetailsMongoRepository.createGundamDecklist(decklist, userId);
                response.put("message", "Deck created successfully");
            } else {
                uuid = userDetailsMongoRepository.updateGundamDecklist(decklist, userId, deckuid);
                response.put("message", "Deck updated successfully");
            }
            response.put("deckuid", uuid);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "Error adding deck: " + e.getMessage());
            response.put("deckuid", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Gundam load user lightweight of all deck endpoint
    @GetMapping("/load/gundam")
    public ResponseEntity<List<GenericDecklist>> loadGCGDeck(@AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<List<GenericDecklist>>(userDetailsMongoRepository.loadDecklist(userId, F_GCGDECKS),
                HttpStatus.OK);
    }

    // Gundam load deck data by deckuid
    @GetMapping("/load/gundam/{deckuid}")
    public ResponseEntity<GundamDecklist> loadOneGCGDeck(@PathVariable String deckuid,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
        return new ResponseEntity<GundamDecklist>(
                userDetailsMongoRepository.loadGundamDecklist(userId, deckuid),
                HttpStatus.OK);
    }

    // Delete deck
    @DeleteMapping("/delete/{tcg}/deck/{deckId}")
    public ResponseEntity<Map<String, Object>> deleteDeck(@PathVariable String tcg,
            @PathVariable String deckId,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        String userId = user.getUid();
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


    // Notification endpoint    
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> listOfNotifications(
            @RequestParam(defaultValue = "10") String limit,
            @AuthenticationPrincipal FirebaseUser.Principal user) throws Exception {
        String userId = user.getUid();
        return new ResponseEntity<List<Notification>>(userDetailService.listNotifications(userId, limit),
                HttpStatus.OK);
    }

    @GetMapping("/notifications/merged")
    public ResponseEntity<List<NotificationMerged>> listOfNotificationsMerged(
            @RequestParam(defaultValue = "10") String limit,
            @AuthenticationPrincipal FirebaseUser.Principal user) throws Exception {
        String userId = user.getUid();
        List<NotificationMerged> list = userDetailService.listNotificationsMerge(userId, limit);
        for (NotificationMerged n : list) {
            for (SenderInfo s : n.getLatestSender()) {
                System.out.println(s.getName());
            }
        }
        return new ResponseEntity<List<NotificationMerged>>(list, HttpStatus.OK);
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

    @PostMapping("/upd/image/{userId}")
    public ResponseEntity<Map<String, Object>> updateImage(@PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
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

    @PostMapping("/upd/preference")
    public ResponseEntity<Map<String, Object>> updatePreference(@RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userId = user.getUid();
            userDetailService.updatePreference(payload, userId);
            response.put("message", "Preference change success!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
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
