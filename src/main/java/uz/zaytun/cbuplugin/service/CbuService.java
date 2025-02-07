package uz.zaytun.cbuplugin.service;

import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;

import java.util.List;

public interface CbuService {

    String CBU_CURRENCY_ENDPOINT = "/uz/arkhiv-kursov-valyut/json";
    String CBU_SERVICE = "cbuService";

    BaseResponse<List<CurrencyDTO>> getCurrencies();
}
