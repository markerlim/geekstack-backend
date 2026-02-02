package com.geekstack.cards.model;

import java.util.List;

public class ImageSearchResponse {
    private String status;
    private String query_image;
    private String card_name;
    private int results_count;
    private List<SearchResult> results;
    private String search_type; // embedding_match, card_detection, or ocr_fallback
    private String search_type_message; // User-friendly message explaining the search type

    public static class SearchResult {
        private int rank;
        private String image_id;
        private double similarity_score;
        private Object metadata;

        public SearchResult() {}

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getImage_id() {
            return image_id;
        }

        public void setImage_id(String image_id) {
            this.image_id = image_id;
        }

        public double getSimilarity_score() {
            return similarity_score;
        }

        public void setSimilarity_score(double similarity_score) {
            this.similarity_score = similarity_score;
        }

        public Object getMetadata() {
            return metadata;
        }

        public void setMetadata(Object metadata) {
            this.metadata = metadata;
        }
    }

    public ImageSearchResponse() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuery_image() {
        return query_image;
    }

    public void setQuery_image(String query_image) {
        this.query_image = query_image;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public int getResults_count() {
        return results_count;
    }

    public void setResults_count(int results_count) {
        this.results_count = results_count;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
    }

    public String getSearch_type() {
        return search_type;
    }

    public void setSearch_type(String search_type) {
        this.search_type = search_type;
    }

    public String getSearch_type_message() {
        return search_type_message;
    }

    public void setSearch_type_message(String search_type_message) {
        this.search_type_message = search_type_message;
    }
}
