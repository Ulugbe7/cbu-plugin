package uz.zaytun.cbuplugin.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Ccy")
    private String currency;

    @JsonProperty("CcyNm_RU")
    private String currencyNameRu;

    @JsonProperty("CcyNm_UZ")
    private String currencyNameUZ;

    @JsonProperty("CcyNm_UZC")
    private String currencyNameUZC;

    @JsonProperty("CcyNm_EN")
    private String currencyNameEN;

    @JsonProperty("Nominal")
    private String nominal;

    @JsonProperty("Rate")
    private String rate;

    @JsonProperty("Diff")
    private String difference;

    @JsonProperty("Date")
    private String date;
}
