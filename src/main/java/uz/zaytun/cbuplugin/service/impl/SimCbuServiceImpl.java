package uz.zaytun.cbuplugin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.service.CbuService;

import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(name = "cbu.setting.simulate", havingValue = "mock", matchIfMissing = true)
public class SimCbuServiceImpl implements CbuService {

    public SimCbuServiceImpl() {
        log.debug("##### CbuService: simulate service is on #####");
    }

    @Override
    public BaseResponse<List<CurrencyDTO>> getCurrencies(CurrencyDTO request) {
        var currencies = loadSimulateData();
        log.debug("Simulate currencies: {}", currencies);
        return new BaseResponse<>(currencies);
    }

    private List<CurrencyDTO> loadSimulateData() {
        var currencyUSD = CurrencyDTO.builder()
                .id(69L)
                .code("840")
                .currency("USD")
                .currencyNameRu("Доллар США")
                .currencyNameUz("AQSH dollari")
                .currencyNameUzCyrillic("АҚШ доллари")
                .currencyNameEn("US Dollar")
                .nominal("1")
                .rate("12985.32")
                .difference("-13.59")
                .date("06.02.2025")
                .build();

        var currencyEUR = CurrencyDTO.builder()
                .id(21L)
                .code("841")
                .currency("EUR")
                .currencyNameRu("Доллар США")
                .currencyNameUz("AQSH dollari")
                .currencyNameUzCyrillic("АҚШ доллари")
                .currencyNameEn("US Dollar")
                .nominal("1")
                .rate("12985.32")
                .difference("-13.59")
                .date("06.02.2025")
                .build();

        return List.of(currencyUSD, currencyEUR);
    }
}

