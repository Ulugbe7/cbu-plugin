package uz.zaytun.cbuplugin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyFetcherServiceImpl implements CurrencyFetcherService {

    private final CbuService cbuService;
    private final CurrencyService currencyService;

    @Override
    public BaseResponse<List<CurrencyDTO>> fetchCurrencies(CurrencyDTO request) {
        var localCurrenciesResponse = currencyService.findAll(request);

        if (!localCurrenciesResponse.getData().isEmpty()) {
            log.info("##### fetched currency from database #####");
            return localCurrenciesResponse;
        }

        log.info("##### fetched currency from server #####");
        return cbuService.getCurrencies(request);
    }
}
