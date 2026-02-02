package com.geekstack.cards.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekstack.cards.model.DuelMastersCard;
import com.geekstack.cards.model.ImageSearchResponse;

@Service
public class ImageSearchService {

    @Autowired
    @Qualifier("imageSearchRestTemplate")
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${image.embedding.api.url}")
    private String imageSearchUrl;

    private static final int DEFAULT_TOP_K = 5;
    private static final double DEFAULT_MIN_SIMILARITY = 0.0;

    public ImageSearchResponse uploadImageAndSearch(MultipartFile image,String cardgame, String collection) {
        return uploadImageAndSearch(image, DEFAULT_TOP_K, DEFAULT_MIN_SIMILARITY, cardgame, collection);
    }

    public ImageSearchResponse uploadImageAndSearchMulti(MultipartFile image, String collection) {
        return uploadImageAndSearchMulti(image, DEFAULT_TOP_K, DEFAULT_MIN_SIMILARITY, collection);
    }

    public ImageSearchResponse uploadImageAndSearch(MultipartFile image, int topK, double minSimilarity, String cardgame,
            String collection) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        try {
            // Create multipart form data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            });
            body.add("top_k", topK);
            body.add("min_similarity", minSimilarity);
            body.add("collection", collection);
            body.add("card_game", cardgame);
            // Execute POST request
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    imageSearchUrl + "/search",
                    body,
                    String.class);

            // Check if response is successful
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                handleErrorResponse(responseEntity);
            }

            ImageSearchResponse searchResponse = objectMapper.readValue(responseEntity.getBody(),
                    ImageSearchResponse.class);

            return searchResponse;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle HTTP errors from FastAPI
            handleHttpException(e);
            throw new RuntimeException("Failed to search image", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search image: " + e.getMessage(), e);
        }
    }

    public ImageSearchResponse uploadImageAndSearchMulti(MultipartFile image, int topK, double minSimilarity,
            String collection) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        try {
            // Create multipart form data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            });
            body.add("top_k", topK);
            body.add("min_similarity", minSimilarity);
            body.add("collection", collection);
            // Execute POST request
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    imageSearchUrl + "/search-multi",
                    body,
                    String.class);

            // Check if response is successful
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                handleErrorResponse(responseEntity);
            }

            ImageSearchResponse searchResponse = objectMapper.readValue(responseEntity.getBody(),
                    ImageSearchResponse.class);

            return searchResponse;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle HTTP errors from FastAPI
            handleHttpException(e);
            throw new RuntimeException("Failed to search image", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search image: " + e.getMessage(), e);
        }
    }

    /**
     * Handle HTTP exceptions from FastAPI service
     */
    private void handleHttpException(Exception e) {
        String errorDetail = "Unknown error";
        try {
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException clientException = (HttpClientErrorException) e;
                String responseBody = clientException.getResponseBodyAsString();
                errorDetail = extractErrorDetail(responseBody);
            } else if (e instanceof HttpServerErrorException) {
                HttpServerErrorException serverException = (HttpServerErrorException) e;
                String responseBody = serverException.getResponseBodyAsString();
                errorDetail = extractErrorDetail(responseBody);
            }
        } catch (Exception parseException) {
            // If we can't parse the error, just use the original message
        }
        throw new RuntimeException(errorDetail, e);
    }

    /**
     * Extract the 'detail' field from FastAPI error response
     */
    private String extractErrorDetail(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.has("detail")) {
                JsonNode detailNode = jsonNode.get("detail");
                // detail can be a string or an object
                if (detailNode.isTextual()) {
                    return detailNode.asText();
                } else {
                    return detailNode.toString();
                }
            }
        } catch (Exception e) {
            // If parsing fails, return the raw response
            return responseBody;
        }
        return responseBody;
    }

    /**
     * Handle non-2xx response status codes
     */
    private void handleErrorResponse(ResponseEntity<String> responseEntity) {
        String errorDetail = "HTTP " + responseEntity.getStatusCode().value();
        try {
            String responseBody = responseEntity.getBody();
            if (responseBody != null && !responseBody.isEmpty()) {
                errorDetail = extractErrorDetail(responseBody);
            }
        } catch (Exception e) {
            // If we can't parse the error, just use the status code
        }
        throw new RuntimeException("Image search API error: " + errorDetail);
    }

        /**
     * Create a standardized error response
     */
    public Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", message);
        return errorResponse;
    }

    /**
     * Create a search response with cards and metadata about the search type
     */
    public Map<String, Object> createSearchResponse(List<DuelMastersCard> cards, String searchType, String searchTypeMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("search_type", searchType);
        response.put("search_type_message", searchTypeMessage);
        response.put("results", cards);
        response.put("results_count", cards.size());
        return response;
    }
}
