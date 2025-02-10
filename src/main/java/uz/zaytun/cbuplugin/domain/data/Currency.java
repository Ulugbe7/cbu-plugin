package uz.zaytun.cbuplugin.domain.data;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "currency")
    private String currency;

    @Column(name = "currency_name_ru")
    private String currencyNameRu;

    @Column(name = "currency_name_uz")
    private String currencyNameUz;

    @Column(name = "currency_name_uz_cyrillic")
    private String currencyNameUzCyrillic;

    @Column(name = "currency_name_en")
    private String currencyNameEn;

    @Column(name = "nominal")
    private String nominal;

    @Column(name = "rate")
    private String rate;

    @Column(name = "difference")
    private String difference;

    @Column(name = "date")
    private String date;
}
