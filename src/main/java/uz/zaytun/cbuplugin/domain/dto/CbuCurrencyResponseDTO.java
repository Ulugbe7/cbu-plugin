package uz.zaytun.cbuplugin.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CbuCurrencyResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Ccy")
    private String currency;

    @JsonProperty("CcyNm_RU")
    private String currencyNameRu;

    @JsonProperty("CcyNm_UZ")
    private String currencyNameUz;

    @JsonProperty("CcyNm_UZC")
    private String currencyNameUzCyrillic;

    @JsonProperty("CcyNm_EN")
    private String currencyNameEn;

    @JsonProperty("Nominal")
    private String nominal;

    @JsonProperty("Rate")
    private String rate;

    @JsonProperty("Diff")
    private String difference;

    @JsonProperty("Date")
    private String date;
}
