package uz.zaytun.cbuplugin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.zaytun.cbuplugin.domain.data.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {

    private Long id;

    private String code;

    private String currency;

    private String currencyNameRu;

    private String currencyNameUz;

    private String currencyNameUzCyrillic;

    private String currencyNameEn;

    private String nominal;

    private String rate;

    private String difference;

    private String date;

    public static CurrencyDTO toDTO(Currency entity) {
        return CurrencyDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .currency(entity.getCurrency())
                .currencyNameRu(entity.getCurrencyNameRu())
                .currencyNameUz(entity.getCurrencyNameUz())
                .currencyNameUzCyrillic(entity.getCurrencyNameUzCyrillic())
                .currencyNameEn(entity.getCurrencyNameEn())
                .nominal(entity.getNominal())
                .rate(entity.getRate())
                .difference(entity.getDifference())
                .date(entity.getDate())
                .build();
    }

    public static CurrencyDTO toDTO(CbuCurrencyResponseDTO entity) {
        return CurrencyDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .currency(entity.getCurrency())
                .currencyNameRu(entity.getCurrencyNameRu())
                .currencyNameUz(entity.getCurrencyNameUz())
                .currencyNameUzCyrillic(entity.getCurrencyNameUzCyrillic())
                .currencyNameEn(entity.getCurrencyNameEn())
                .nominal(entity.getNominal())
                .rate(entity.getRate())
                .difference(entity.getDifference())
                .date(entity.getDate())
                .build();
    }

    public static Currency fromDTO(CurrencyDTO dto) {
        return Currency.builder()
                .code(dto.getCode())
                .currency(dto.getCurrency())
                .currencyNameRu(dto.getCurrencyNameRu())
                .currencyNameUz(dto.getCurrencyNameUz())
                .currencyNameUzCyrillic(dto.getCurrencyNameUzCyrillic())
                .currencyNameEn(dto.getCurrencyNameEn())
                .nominal(dto.getNominal())
                .rate(dto.getRate())
                .difference(dto.getDifference())
                .date(dto.getDate())
                .build(
                );
    }
}
