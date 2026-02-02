package com.geekstack.cards.restcontroller;

import static com.geekstack.cards.utils.Constants.C_DUELMASTER;
import static com.geekstack.cards.utils.Constants.T_DM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geekstack.cards.model.DuelMastersCard;
import com.geekstack.cards.model.ImageSearchResponse;
import com.geekstack.cards.service.CardListService;
import com.geekstack.cards.service.ImageSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/search")
public class ImageSearchController {
    
    private static final Logger logger = LoggerFactory.getLogger(ImageSearchController.class);
    
    @Autowired
    private ImageSearchService imageSearchService;

    @Autowired
    private CardListService cardListService;

    @PostMapping(path="/image/v1",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "top_k", defaultValue = "5") int topK,
            @RequestParam(value = "min_similarity", defaultValue = "0") double minSimilarity) {
        
        if (image.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(imageSearchService.createErrorResponse("Image file is empty"));
        }
        
        try {
            //Using duelmaster first
            ImageSearchResponse result = imageSearchService.uploadImageAndSearch(image, topK, minSimilarity,T_DM,C_DUELMASTER);
            
            List<DuelMastersCard> sortedCards;
            String searchType;
            String searchTypeMessage;
            
            // Check if card_name is provided in the response (OCR fallback)
            if (result.getCard_name() != null && !result.getCard_name().isEmpty()) {
                searchType = "ocr_fallback";
                searchTypeMessage = "Photo was too zoomed in - card could not be detected. OCR was used to search by card name instead.";
                logger.info("OCR Fallback: Card name detected: {}. Using regex search instead of vector search.", result.getCard_name());
                // Use regex search with the card name
                sortedCards = cardListService.listofduelmaster().searchForCardsMultiFieldRegex(result.getCard_name());
            } else if (result.getCard_name() != null && result.getCard_name().isEmpty()) {
                // OCR fallback but failed to extract text
                searchType = "ocr_fallback";
                searchTypeMessage = "Photo was too zoomed in and OCR failed to extract any text. Please provide a clearer picture.";
                logger.warn("OCR Fallback: Failed to extract card name from image.");
                sortedCards = List.of(); // Return empty list
            } else {
                // Card detection or embedding match (vector search)
                searchType = result.getResults() != null && !result.getResults().isEmpty() ? "card_detection" : "embedding_match";
                searchTypeMessage = searchType.equals("card_detection") 
                    ? "Card was detected and matched using visual search."
                    : "Card was matched using embedding search.";
                logger.info("Search Type: {}. Using vector search results.", searchType);
                
                // Get image IDs or card names in order from search results
                List<String> identifiers = result.getResults().stream()
                    .map(r -> {
                        // Check if metadata contains cardName, use it; otherwise use image_id
                        if (r.getMetadata() instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> metadataMap = (Map<String, Object>) r.getMetadata();
                            String cardName = (String) metadataMap.get("cardName");
                            if (cardName != null && !cardName.isEmpty()) {
                                return cardName;
                            }
                        }
                        return r.getImage_id();
                    })
                    .toList();
                
                // Get all cards by identifier (either cardName or image_id)
                List<DuelMastersCard> allCards = cardListService.listofduelmaster().getCardsByMongoId(identifiers);
                
                // Create a map for quick lookup
                Map<String, DuelMastersCard> cardMap = allCards.stream()
                    .collect(Collectors.toMap(DuelMastersCard::get_id, card -> card));
                
                // Sort cards to match the order from search results
                sortedCards = identifiers.stream()
                    .map(cardMap::get)
                    .filter(card -> card != null) // Filter out any null cards
                    .collect(Collectors.toList());
            }
            
            // Wrap results with metadata
            return ResponseEntity.ok(imageSearchService.createSearchResponse(sortedCards, searchType, searchTypeMessage));
        } catch (RuntimeException e) {
            logger.error("Image search failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(imageSearchService.createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during image search", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(imageSearchService.createErrorResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }
    

    @PostMapping(path="/multicardimage/v1",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchMultiImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "top_k", defaultValue = "5") int topK,
            @RequestParam(value = "min_similarity", defaultValue = "0") double minSimilarity) {
        
        if (image.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(imageSearchService.createErrorResponse("Image file is empty"));
        }
        
        try {
            //Using duelmaster first
            ImageSearchResponse result = imageSearchService.uploadImageAndSearch(image, topK, minSimilarity,T_DM, C_DUELMASTER);
            
            List<DuelMastersCard> sortedCards;
            String searchType;
            String searchTypeMessage;
            
            // Check if card_name is provided in the response (OCR fallback)
            if (result.getCard_name() != null && !result.getCard_name().isEmpty()) {
                searchType = "ocr_fallback";
                searchTypeMessage = "Photo was too zoomed in - card could not be detected. OCR was used to search by card name instead.";
                logger.info("OCR Fallback: Card name detected: {}. Using regex search instead of vector search.", result.getCard_name());
                // Use regex search with the card name
                sortedCards = cardListService.listofduelmaster().searchForCardsMultiFieldRegex(result.getCard_name());
            } else if (result.getCard_name() != null && result.getCard_name().isEmpty()) {
                // OCR fallback but failed to extract text
                searchType = "ocr_fallback";
                searchTypeMessage = "Photo was too zoomed in and OCR failed to extract any text. Please provide a clearer picture.";
                logger.warn("OCR Fallback: Failed to extract card name from image.");
                sortedCards = List.of(); // Return empty list
            } else {
                // Card detection or embedding match (vector search)
                searchType = result.getResults() != null && !result.getResults().isEmpty() ? "card_detection" : "embedding_match";
                searchTypeMessage = searchType.equals("card_detection") 
                    ? "Card was detected and matched using visual search."
                    : "Card was matched using embedding search.";
                logger.info("Search Type: {}. Using vector search results.", searchType);
                
                // Get image IDs or card names in order from search results
                List<String> identifiers = result.getResults().stream()
                    .map(r -> {
                        // Check if metadata contains cardName, use it; otherwise use image_id
                        if (r.getMetadata() instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> metadataMap = (Map<String, Object>) r.getMetadata();
                            String cardName = (String) metadataMap.get("cardName");
                            if (cardName != null && !cardName.isEmpty()) {
                                return cardName;
                            }
                        }
                        return r.getImage_id();
                    })
                    .toList();
                
                // Get all cards by identifier (either cardName or image_id)
                List<DuelMastersCard> allCards = cardListService.listofduelmaster().getCardsByMongoId(identifiers);
                
                // Create a map for quick lookup
                Map<String, DuelMastersCard> cardMap = allCards.stream()
                    .collect(Collectors.toMap(DuelMastersCard::get_id, card -> card));
                
                // Sort cards to match the order from search results
                sortedCards = identifiers.stream()
                    .map(cardMap::get)
                    .filter(card -> card != null) // Filter out any null cards
                    .collect(Collectors.toList());
            }
            
            // Wrap results with metadata
            return ResponseEntity.ok(imageSearchService.createSearchResponse(sortedCards, searchType, searchTypeMessage));
        } catch (RuntimeException e) {
            logger.error("Image search failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(imageSearchService.createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during image search", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(imageSearchService.createErrorResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }
    


}


