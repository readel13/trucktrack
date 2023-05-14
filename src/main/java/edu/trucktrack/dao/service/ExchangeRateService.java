package edu.trucktrack.dao.service;

import edu.trucktrack.dao.entity.enums.Currency;
import edu.trucktrack.openfeign.client.ExchangeRateClient;
import edu.trucktrack.openfeign.model.ExchangeRateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private List<ExchangeRateDTO> exchangeRates;

    private final ExchangeRateClient exchangeRateClient;

    //Every hour
//    @Scheduled(fixedRate = 30000)
//    public void updateRates() {
//        log.info("Update rates.....");
//        exchangeRates = exchangeRateClient.fetchExchangeRates();
//        log.info("Update rates finished!");
//    }

    public Long convertTo(Long value, String currency, String convertCurrency) {
        var rate = exchangeRateClient.fetchExchangeRates(currency, convertCurrency).info().get(ExchangeRateDTO.RATE_KEY);

        return Math.round(rate * value);
    }

//    public ExchangeRateDTO findRate(Currency fromCurrency, Currency toCurrency) {
//        return exchangeRates.stream()
//                .filter(rate -> rate.currencyCodeA() == fromCurrency.code())
//                .filter(rate -> rate.currencyCodeB() == toCurrency.code())
//                .findFirst()
//                .orElse(null);
//    }
}
