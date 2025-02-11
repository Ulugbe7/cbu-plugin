package uz.zaytun.cbuplugin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
