package uz.zaytun.cbuplugin.service;

import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyFetchService {

    BaseResponse<List<CurrencyDTO>> fetchCurrencies(CurrencyDTO request);
}
