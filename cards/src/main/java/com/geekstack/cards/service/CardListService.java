package com.geekstack.cards.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geekstack.cards.model.CardPriceFULLA;
import com.geekstack.cards.model.CardPriceYYT;
import com.geekstack.cards.model.CookieRunCard;
import com.geekstack.cards.model.DragonBallzFWCard;
import com.geekstack.cards.model.DuelMastersCard;
import com.geekstack.cards.model.GundamCard;
import com.geekstack.cards.model.HololiveCard;
import com.geekstack.cards.model.OnePieceCard;
import com.geekstack.cards.model.UnionArenaCard;
import com.geekstack.cards.model.UnionArenaCardDTO;
import com.geekstack.cards.repository.CL_CookieRunRepository;
import com.geekstack.cards.repository.CL_DragonBallzFWRepository;
import com.geekstack.cards.repository.CL_DuelMasterRepository;
import com.geekstack.cards.repository.CL_GundamCGRepository;
import com.geekstack.cards.repository.CL_HololiveRepository;
import com.geekstack.cards.repository.CL_OnePieceRepository;
import com.geekstack.cards.repository.CL_UnionArenaRepository;
import com.geekstack.cards.repository.PR_FullaheadRepository;
import com.geekstack.cards.repository.PR_YuyuteiRepository;

import jakarta.annotation.Nullable;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class CardListService {

    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CardListService.class);

    @Autowired
    private CL_DuelMasterRepository duelMasterRepository;

    @Autowired
    private CL_UnionArenaRepository unionArenaRepository;

    @Autowired
    private CL_CookieRunRepository cookieRunRepository;

    @Autowired
    private CL_OnePieceRepository onePieceRepository;

    @Autowired
    private CL_DragonBallzFWRepository dragonBallzFWRepository;

    @Autowired
    private CL_HololiveRepository hololiveRepository;

    @Autowired
    private CL_GundamCGRepository gundamCGRepository;

    @Autowired
    private PR_FullaheadRepository fullaheadRepository;

    @Autowired
    private PR_YuyuteiRepository yuyuteiRepository;

    // Nested class for DuelMaster actions
    public class DuelMasterActions {
        public List<DuelMastersCard> all() {
            return duelMasterRepository.getCards();
        }

        public List<DuelMastersCard> byBooster(String booster) {
            System.out.println(booster);
            return duelMasterRepository.getCardsByBooster(booster);
        }

        public List<DuelMastersCard> searchDatabase(String term) {
            return duelMasterRepository.searchForCards(term);
        }

        public List<String> boosterFilters() {
            return duelMasterRepository.getDistinctBooster();
        }

        public List<String> civilizationFilters(String booster) {
            return duelMasterRepository.getDistinctCivilization(booster);
        }

        public List<String> cardTypeFilters(String booster) {
            return duelMasterRepository.getDistinctCardType(booster);
        }

        public List<DuelMastersCard> deckExtract(String value) {
            Map<String, Integer> cardUidCountMap = new HashMap<>();

            if (value == null || value.trim().isEmpty()) {
                return new ArrayList<>();
            }

            JsonObject jobject = Json.createReader(new StringReader(value)).readObject();
            String data = jobject.getString("value");
            String[] cardEntries = data.split(",");

            for (String entry : cardEntries) {
                String trimmedEntry = entry.trim();

                if (trimmedEntry.contains("x")) {
                    String[] parts = trimmedEntry.split("x", 2);
                    if (parts.length == 2) {
                        int count = 1;
                        try {
                            count = Integer.parseInt(parts[0].trim());
                        } catch (NumberFormatException e) {
                        }
                        String cardUid = parts[1].trim();
                        cardUidCountMap.merge(cardUid, count, Integer::sum);
                    }
                } else {
                    cardUidCountMap.merge(trimmedEntry, 1, Integer::sum);
                }
            }

            List<String> cardUids = new ArrayList<>(cardUidCountMap.keySet());
            List<DuelMastersCard> cards = duelMasterRepository.getCardsByBatchUids(cardUids);

            for (DuelMastersCard card : cards) {
                String uid = card.getCardUid();
                Integer count = cardUidCountMap.getOrDefault(uid, 0);
                card.setCount(count);
            }

            return cards;
        }

    }

    // Nested class for UnionArena actions
    public class UnionArenaActions {
        public List<UnionArenaCard> all() {
            return unionArenaRepository.getCards();
        }

        public List<UnionArenaCard> byAnimeCode(String animeCode) {
            return unionArenaRepository.getCardsByAnimeCode(animeCode);
        }

        public List<UnionArenaCard> searchDatabaseFull(String term) {
            return unionArenaRepository.searchForCardsFull(term);
        }

        public List<UnionArenaCardDTO> searchDatabase(String term) {
            return unionArenaRepository.searchForCards(term);
        }

        public List<UnionArenaCard> deckExtract(String value) {
            Map<String, Integer> cardUidCountMap = new HashMap<>();

            if (value == null || value.trim().isEmpty()) {
                return new ArrayList<>();
            }

            JsonObject jobject = Json.createReader(new StringReader(value)).readObject();
            String data = jobject.getString("value");
            String[] cardEntries = data.split(",");

            for (String entry : cardEntries) {
                String trimmedEntry = entry.trim();

                if (trimmedEntry.contains("x")) {
                    String[] parts = trimmedEntry.split("x", 2);
                    if (parts.length == 2) {
                        int count = 1;
                        try {
                            count = Integer.parseInt(parts[0].trim());
                        } catch (NumberFormatException e) {
                        }
                        String cardUid = parts[1].trim();
                        cardUidCountMap.merge(cardUid, count, Integer::sum);
                    }
                } else {
                    cardUidCountMap.merge(trimmedEntry, 1, Integer::sum);
                }
            }

            // Convert to list for Mongo query
            List<String> cardUids = new ArrayList<>(cardUidCountMap.keySet());
            List<UnionArenaCard> cards = unionArenaRepository.getCardsByBatchUids(cardUids);

            for (UnionArenaCard card : cards) {
                String uid = card.getCardUid();
                Integer count = cardUidCountMap.getOrDefault(uid, 0);
                card.setCount(count);
            }
            return cards;
        }

        public List<String> boosterFilters(String animeCode) {
            return unionArenaRepository.getDistinctBooster(animeCode);
        }

        public List<String> colorFilters(String animeCode) {
            return unionArenaRepository.getDistinctColor(animeCode);
        }

        public List<String> rarityFilters(String animeCode) {
            return unionArenaRepository.getDistinctRarity(animeCode);
        }
    }

    // Nested class for OnePiece actions
    public class OnePieceActions {
        public List<OnePieceCard> all() {
            return onePieceRepository.getCards();
        }

        public List<OnePieceCard> byLeader(int page, int size, String search) {
            if (search.isEmpty()) {
                return onePieceRepository.getCardsIsLeader(page, size);
            } else {
                return onePieceRepository.getCardsIsLeader(page, size, search);
            }
        }

        public List<OnePieceCard> byBooster(String booster) {
            return onePieceRepository.getCardsByBooster(booster);
        }

        public List<OnePieceCard> searchDatabase(@Nullable String term, @Nullable String color,
                @Nullable String excludeCategory) {
            List<String> colorArray = null;

            if (color != null && !color.trim().isEmpty()) {
                colorArray = Arrays.asList(color.split(","));
            }

            if (term == null) {
                return onePieceRepository.getCardsByColor(colorArray, excludeCategory);

            } else {
                return onePieceRepository.searchForCards(term, colorArray, excludeCategory);

            }
        }

        public List<String> categoryFilters(String booster) {
            return onePieceRepository.getDistinctCategory(booster);
        }

        public List<String> colorFilters(String booster) {
            return onePieceRepository.getDistinctColor(booster);
        }

        public List<String> rarityFilters(String booster) {
            return onePieceRepository.getDistinctRarity(booster);
        }

        public List<OnePieceCard> deckExtract(String value) {
            Map<String, Integer> cardUidCountMap = new HashMap<>();

            if (value == null || value.trim().isEmpty()) {
                return new ArrayList<>();
            }

            JsonObject jobject = Json.createReader(new StringReader(value)).readObject();
            String data = jobject.getString("value");
            String[] cardEntries = data.split(",");

            for (String entry : cardEntries) {
                String trimmedEntry = entry.trim();

                if (trimmedEntry.contains("x")) {
                    String[] parts = trimmedEntry.split("x", 2);
                    if (parts.length == 2) {
                        int count = 1;
                        try {
                            count = Integer.parseInt(parts[0].trim());
                        } catch (NumberFormatException e) {
                        }
                        String cardUid = parts[1].trim();
                        cardUidCountMap.merge(cardUid, count, Integer::sum);
                    }
                } else {
                    cardUidCountMap.merge(trimmedEntry, 1, Integer::sum);
                }
            }

            List<String> cardUids = new ArrayList<>(cardUidCountMap.keySet());
            List<OnePieceCard> cards = onePieceRepository.getCardsByBatchUids(cardUids);

            for (OnePieceCard card : cards) {
                String uid = card.getCardUid();
                Integer count = cardUidCountMap.getOrDefault(uid, 0);
                card.setCount(count);
            }

            return cards;
        }

    }

    // Nested class for CookieRun actions
    public class CookieRunActions {
        public List<CookieRunCard> all() {
            return cookieRunRepository.getCards();
        }

        public List<CookieRunCard> byBooster(String booster) {
            return cookieRunRepository.getCardsByBooster(booster);
        }

        public List<CookieRunCard> searchDatabase(String term) {
            return cookieRunRepository.searchForCards(term);
        }

        public List<CookieRunCard> searchDatabase(@Nullable String term, @Nullable String color) {
            List<String> colorArray = null;

            if (color != null && !color.trim().isEmpty()) {
                colorArray = Arrays.asList(color.split(","));
            }

            if (term == null) {
                return cookieRunRepository.getCardsByColor(colorArray);
            } else {
                return cookieRunRepository.searchForCards(term, colorArray);

            }
        }

        public List<CookieRunCard> deckExtract(String value) {
            Map<String, Integer> cardUidCountMap = new HashMap<>();

            if (value == null || value.trim().isEmpty()) {
                return new ArrayList<>();
            }

            JsonObject jobject = Json.createReader(new StringReader(value)).readObject();
            String data = jobject.getString("value");
            String[] cardEntries = data.split(",");

            for (String entry : cardEntries) {
                String trimmedEntry = entry.trim();

                if (trimmedEntry.contains("x")) {
                    String[] parts = trimmedEntry.split("x", 2);
                    if (parts.length == 2) {
                        int count = 1;
                        try {
                            count = Integer.parseInt(parts[0].trim());
                        } catch (NumberFormatException e) {
                        }
                        String cardUid = parts[1].trim();
                        cardUidCountMap.merge(cardUid, count, Integer::sum);
                    }
                } else {
                    cardUidCountMap.merge(trimmedEntry, 1, Integer::sum);
                }
            }

            List<String> cardUids = new ArrayList<>(cardUidCountMap.keySet());
            List<CookieRunCard> cards = cookieRunRepository.getCardsByBatchUids(cardUids);

            for (CookieRunCard card : cards) {
                String uid = card.getCardUid();
                Integer count = cardUidCountMap.getOrDefault(uid, 0);
                card.setCount(count);
            }

            return cards;
        }

    }

    // Nested class for DragonballzFW actions
    public class DragonBallzFWActions {
        public List<DragonBallzFWCard> all() {
            return dragonBallzFWRepository.getCards();
        }

        public List<DragonBallzFWCard> byBooster(String booster) {
            return dragonBallzFWRepository.getCardsByBooster(booster);
        }

        public List<String> cardtypeFilters(String booster) {
            return dragonBallzFWRepository.getDistinctCardtype(booster);
        }

        public List<String> colorFilters(String booster) {
            return dragonBallzFWRepository.getDistinctColor(booster);
        }

        public List<String> rarityFilters(String booster) {
            return dragonBallzFWRepository.getDistinctRarity(booster);
        }

        public List<DragonBallzFWCard> deckExtract(String value) {
            Map<String, Integer> cardUidCountMap = new HashMap<>();

            if (value == null || value.trim().isEmpty()) {
                return new ArrayList<>();
            }

            JsonObject jobject = Json.createReader(new StringReader(value)).readObject();
            String data = jobject.getString("value");
            String[] cardEntries = data.split(",");

            for (String entry : cardEntries) {
                String trimmedEntry = entry.trim();

                if (trimmedEntry.contains("x")) {
                    String[] parts = trimmedEntry.split("x", 2);
                    if (parts.length == 2) {
                        int count = 1;
                        try {
                            count = Integer.parseInt(parts[0].trim());
                        } catch (NumberFormatException e) {
                        }
                        String cardUid = parts[1].trim();
                        cardUidCountMap.merge(cardUid, count, Integer::sum);
                    }
                } else {
                    cardUidCountMap.merge(trimmedEntry, 1, Integer::sum);
                }
            }

            List<String> cardUids = new ArrayList<>(cardUidCountMap.keySet());
            List<DragonBallzFWCard> cards = dragonBallzFWRepository.getCardsByBatchUids(cardUids);

            for (DragonBallzFWCard card : cards) {
                String uid = card.getCardUid();
                Integer count = cardUidCountMap.getOrDefault(uid, 0);
                card.setCount(count);
            }

            return cards;
        }

    }

    // Nested class for Hololive actions
    public class HololiveActions {
        public List<HololiveCard> all() {
            return hololiveRepository.getCards();
        }

        public List<HololiveCard> byBooster(String booster) {
            return hololiveRepository.getCardsByBooster(booster);
        }

        public List<HololiveCard> searchDatabase(String term) {
            return hololiveRepository.searchForCards(term);
        }
    }

    // Nested class for Gundam actions
    public class GundamActions {
        public List<GundamCard> all() {
            return gundamCGRepository.getCards();
        }

        public List<GundamCard> byBooster(String booster) {
            return gundamCGRepository.getCardsByBooster(booster);
        }

        public List<GundamCard> searchDatabase(@Nullable String term, @Nullable String color) {
            List<String> colorArray = null;

            if (color != null && !color.trim().isEmpty()) {
                colorArray = Arrays.asList(color.split(","));
            }

            if (term == null) {
                return gundamCGRepository.getCardsByColor(colorArray);
            } else {
                return gundamCGRepository.searchForCards(term, colorArray);

            }
        }

        public List<GundamCard> deckExtract(String value) {
            Map<String, Integer> cardUidCountMap = new HashMap<>();

            if (value == null || value.trim().isEmpty()) {
                return new ArrayList<>();
            }

            JsonObject jobject = Json.createReader(new StringReader(value)).readObject();
            String data = jobject.getString("value");
            String[] cardEntries = data.split(",");

            for (String entry : cardEntries) {
                String trimmedEntry = entry.trim();

                if (trimmedEntry.contains("x")) {
                    String[] parts = trimmedEntry.split("x", 2);
                    if (parts.length == 2) {
                        int count = 1;
                        try {
                            count = Integer.parseInt(parts[0].trim());
                        } catch (NumberFormatException e) {
                        }
                        String cardUid = parts[1].trim();
                        cardUidCountMap.merge(cardUid, count, Integer::sum);
                    }
                } else {
                    cardUidCountMap.merge(trimmedEntry, 1, Integer::sum);
                }
            }

            List<String> cardUids = new ArrayList<>(cardUidCountMap.keySet());
            List<GundamCard> cards = gundamCGRepository.getCardsByBatchUids(cardUids);

            for (GundamCard card : cards) {
                String uid = card.getCardUid();
                Integer count = cardUidCountMap.getOrDefault(uid, 0);
                card.setCount(count);
            }

            return cards;
        }

    }

    public class CardPriceActions {
        public CardPriceFULLA byFulla(String id) {
            return fullaheadRepository.findCardById(id);
        }

        public CardPriceYYT byYuyutei(String id) {
            return yuyuteiRepository.findCardById(id);
        }
    }

    // Methods to access the nested action classes
    public DuelMasterActions listofduelmaster() {
        return new DuelMasterActions();
    }

    public UnionArenaActions listofunionarena() {
        return new UnionArenaActions();
    }

    public OnePieceActions listofonepiece() {
        return new OnePieceActions();
    }

    public CookieRunActions listofcookierun() {
        return new CookieRunActions();
    }

    public DragonBallzFWActions listofdragonballzfw() {
        return new DragonBallzFWActions();
    }

    public HololiveActions listofhololive() {
        return new HololiveActions();
    }

    public GundamActions listofgundam() {
        return new GundamActions();
    }

    public CardPriceActions cardprices() {
        return new CardPriceActions();
    }
}
