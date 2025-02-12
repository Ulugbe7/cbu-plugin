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

    Currency currencyUSD;
    Currency currencyEUR;

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
        var mCurrencyUSD = currencyRepository.findById(currencyUSD.getId()).orElse(null);
        var mCurrencyEUR = currencyRepository.findById(currencyEUR.getId()).orElse(null);

        assertNotNull(mCurrencyUSD);
        assertNotNull(mCurrencyEUR);

        assertEquals(currencyUSD, mCurrencyUSD);
        assertEquals(currencyEUR, mCurrencyEUR);

        assertEquals(1L, mCurrencyEUR.getId() - mCurrencyUSD.getId());
    }
}
