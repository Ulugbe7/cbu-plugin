package uz.zaytun.cbuplugin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uz.zaytun.cbuplugin.dto.BaseResponse;
import uz.zaytun.cbuplugin.dto.CurrencyDTO;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "mock.simulated", havingValue = "true")
public class SimCBUServiceImpl implements CBUService {

    @Override
    public BaseResponse<List<CurrencyDTO>> getCurrencies() {
        var currencies = List.of(new CurrencyDTO(), new CurrencyDTO());
        return new BaseResponse<>(currencies);
    }
}

