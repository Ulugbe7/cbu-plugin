package uz.zaytun.cbuplugin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Ccy")
    private String ccy;

    @JsonProperty("CcyNm_RU")
    private String ccyNmRU;

    @JsonProperty("CcyNm_UZ")
    private String ccyNmUZ;

    @JsonProperty("CcyNm_UZC")
    private String ccyNmUZC;

    @JsonProperty("CcyNm_EN")
    private String ccyNmEN;

    @JsonProperty("Nominal")
    private String nominal;

    @JsonProperty("Rate")
    private double rate;

    @JsonProperty("Diff")
    private double diff;

    @JsonProperty("Date")
    private String date;
}
