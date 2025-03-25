package com.geekstack.cards.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekstack.cards.model.FixerApiResponse;
import com.geekstack.cards.repository.ExchangeRateRepository;

@Service
public class CurrencyConversionService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${fixer.api.url}")
    private String apiUrl;

    @Value("${fixer.api.key}")
    private String apiKey;

    private static double sgdToJpyRate;

    // This method will be called daily
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void fetchExchangeRatesAndCalculateConversion() {

        String url = apiUrl + "/latest?access_key=" + apiKey + "&base=EUR&symbols=SGD,JPY";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        String rawResponse = responseEntity.getBody();

        try {
            FixerApiResponse response = objectMapper.readValue(rawResponse, FixerApiResponse.class);

            if (response != null && response.getRates() != null) {
                double eurToSgdRate = response.getRates().getSGD();
                double eurToJpyRate = response.getRates().getJPY();
                sgdToJpyRate = eurToJpyRate / eurToSgdRate; // SGD to JPY rate

                exchangeRateRepository.updateExchangeRate("SGD", "JPY", sgdToJpyRate, LocalDateTime.now());
            }
        } catch (Exception e) {
        }

    }

}
