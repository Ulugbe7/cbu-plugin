package uz.zaytun.cbuplugin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CbuCurrencyResponseDTO;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.repository.CurrencyRepository;
import uz.zaytun.cbuplugin.service.CurrencyService;
import uz.zaytun.cbuplugin.service.criteria.CurrencyFilter;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public void saveAll(List<CbuCurrencyResponseDTO> cbuCurrencyResponseDTOs) {
        currencyRepository.saveAll(cbuCurrencyResponseDTOs.stream().map(CbuCurrencyResponseDTO::fromDTO).toList());
    }

    @Override
    public BaseResponse<List<CurrencyDTO>> findAll(CurrencyDTO request) {
        var currencyDTOs = currencyRepository.findAll(CurrencyFilter.filter(request))
                .stream().map(CurrencyDTO::toDTO).toList();
        return new BaseResponse<>(currencyDTOs);
    }
}
