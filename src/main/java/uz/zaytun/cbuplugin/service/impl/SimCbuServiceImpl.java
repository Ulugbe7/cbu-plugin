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
@ConditionalOnProperty(name = "mock.simulate", havingValue = "mock")
public class SimCbuServiceImpl implements CbuService {

    public SimCbuServiceImpl() {
        log.debug("SimCbuServiceImpl started");
    }

    @Override
    public BaseResponse<List<CurrencyDTO>> getCurrencies() {
        var currencies = loadMockData();
        log.debug("Mock currencies: {}", currencies);
        return new BaseResponse<>(currencies);
    }

    private List<CurrencyDTO> loadMockData() {
        var currency1 = new CurrencyDTO(
                69,
                "840",
                "USD",
                "Доллар США",
                "AQSH dollari",
                "АҚШ доллари",
                "US Dollar",
                "1",
                "12985.32",
                "-13.59",
                "06.02.2025"
        );
        var currency2 = new CurrencyDTO(
                69,
                "840",
                "USD",
                "Доллар США",
                "AQSH dollari",
                "АҚШ доллари",
                "US Dollar",
                "1",
                "12985.32",
                "-13.59",
                "06.02.2025"
        );
        return List.of(currency1, currency2);
    }
}

