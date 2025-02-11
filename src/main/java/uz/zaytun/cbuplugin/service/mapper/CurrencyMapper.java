package uz.zaytun.cbuplugin.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.zaytun.cbuplugin.domain.data.Currency;
import uz.zaytun.cbuplugin.domain.dto.CbuCurrencyResponseDTO;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.service.mapper.base.BaseMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CurrencyMapper extends BaseMapper<Currency, CurrencyDTO> {

    @Mapping(target = "id", ignore = true)
    Currency toEntity(CbuCurrencyResponseDTO dto);

    CurrencyDTO toDto(CbuCurrencyResponseDTO dto);
}
