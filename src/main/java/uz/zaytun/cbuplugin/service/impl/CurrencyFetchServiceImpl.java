package uz.zaytun.cbuplugin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.service.CbuService;
import uz.zaytun.cbuplugin.service.CurrencyFetchService;
import uz.zaytun.cbuplugin.service.CurrencyService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyFetchServiceImpl implements CurrencyFetchService {

    private final CbuService cbuService;
    private final CurrencyService currencyService;

    @Override
    public BaseResponse<List<CurrencyDTO>> fetchCurrencies(CurrencyDTO request) {
        var localCurrenciesResponse = currencyService.findAll(request);

        if (!localCurrenciesResponse.getData().isEmpty()) {
            log.info("fetch currency from database");
            return localCurrenciesResponse;
        }

        log.info("fetch currency from server");
        return cbuService.getCurrencies(request);
    }
}
