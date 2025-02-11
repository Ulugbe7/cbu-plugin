package uz.zaytun.cbuplugin.service;

import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CbuCurrencyResponseDTO;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {

    void saveAll(List<CbuCurrencyResponseDTO> cbuCurrencyResponseDTOs);

    BaseResponse<List<CurrencyDTO>> findAll(CurrencyDTO request);
}
