package uz.zaytun.cbuplugin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.zaytun.cbuplugin.domain.data.Currency;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CbuCurrencyResponseDTO;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.repository.CurrencyRepository;
import uz.zaytun.cbuplugin.service.CurrencyService;
import uz.zaytun.cbuplugin.service.criteria.CurrencyFilter;
import uz.zaytun.cbuplugin.service.mapper.CurrencyMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;

    @Override
    public List<Currency> saveAll(List<CbuCurrencyResponseDTO> currencies) {
        var newCurrencies = currencies.stream().map(currencyMapper::toEntity).toList();
        log.info("Save all currencies size: {}", newCurrencies.size());
        return currencyRepository.saveAll(newCurrencies);
    }

    @Override
    public BaseResponse<List<CurrencyDTO>> findAll(CurrencyDTO request) {
        var currencyDTOs = currencyRepository.findAll(CurrencyFilter.filter(request))
                .stream().map(currencyMapper::toDto).toList();
        log.info("Find all currencies size: {}", currencyDTOs.size());
        return new BaseResponse<>(currencyDTOs);
    }
}
