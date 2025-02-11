package uz.zaytun.cbuplugin.service;

import uz.zaytun.cbuplugin.domain.data.Currency;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CbuCurrencyResponseDTO;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {

    List<Currency> saveAll(List<CbuCurrencyResponseDTO> currencies);

    BaseResponse<List<CurrencyDTO>> findAll(CurrencyDTO request);
}
