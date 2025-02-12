package uz.zaytun.cbuplugin.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uz.zaytun.cbuplugin.domain.data.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    private Currency currencyUSD;
    private Currency currencyEUR;

    @BeforeEach
    public void setUp() {
        currencyUSD = Currency.builder()
                .code("840")
                .currency("USD")
                .currencyNameRu("Доллар США")
                .currencyNameUz("AQSH dollari")
                .currencyNameUzCyrillic("АҚШ доллари")
                .currencyNameEn("US Dollar")
                .nominal("1")
                .rate("12985.32")
                .difference("-13.59")
                .date("06.02.2025")
                .build();
        currencyUSD = currencyRepository.save(currencyUSD);

        currencyEUR = Currency.builder()
                .code("841")
                .currency("EUR")
                .currencyNameRu("Доллар США")
                .currencyNameUz("AQSH dollari")
                .currencyNameUzCyrillic("АҚШ доллари")
                .currencyNameEn("US Dollar")
                .nominal("1")
                .rate("12985.32")
                .difference("-13.59")
                .date("06.02.2025")
                .build();
        currencyEUR = currencyRepository.save(currencyEUR);
    }

    @AfterEach
    public void tearDown() {
        currencyRepository.delete(currencyUSD);
        currencyRepository.delete(currencyEUR);
    }

    @Test
    void check_currency_is_saved_database() {
        var foundCurrencyUSD = currencyRepository.findById(currencyUSD.getId()).orElse(null);
        var foundCurrencyEUR = currencyRepository.findById(currencyEUR.getId()).orElse(null);

        assertNotNull(foundCurrencyUSD);
        assertNotNull(foundCurrencyEUR);

        assertEquals(currencyUSD, foundCurrencyUSD);
        assertEquals(currencyEUR, foundCurrencyEUR);

        assertEquals(1L, foundCurrencyEUR.getId() - foundCurrencyUSD.getId());
    }
}
