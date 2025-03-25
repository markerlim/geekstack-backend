package com.geekstack.cards.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FixerApiResponse {

    private boolean success;
    private String base;
    private String date;
    private long timestamp;

    @JsonProperty("rates")
    private Rates rates;

    public static class Rates {
        @JsonProperty("SGD")
        private double SGD;

        @JsonProperty("JPY")
        private double JPY;
        
        public double getSGD() {
            return SGD;
        }
        public void setSGD(double SGD) {
            this.SGD = SGD;
        }
        public double getJPY() {
            return JPY;
        }
        public void setJPY(double JPY) {
            this.JPY = JPY;
        }

    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

