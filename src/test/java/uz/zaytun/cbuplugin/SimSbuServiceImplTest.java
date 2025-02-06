package uz.zaytun.cbuplugin;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.service.impl.SimCbuServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SimSbuServiceImplTest {

    private static SimCbuServiceImpl simCbuService;

    @BeforeAll
    static void setUp() {
        simCbuService = new SimCbuServiceImpl();
    }

    @Test
    void testGetCurrencies() {
        BaseResponse<List<CurrencyDTO>> response = simCbuService.getCurrencies();

        assertNotNull(response);
        assertNotNull(response.getData());
        assertFalse(response.getData().isEmpty());
        assertEquals(2, response.getData().size());

        CurrencyDTO currency = response.getData().get(0);
        assertEquals("840", currency.getCode());
        assertEquals("USD", currency.getCurrency());
        assertEquals("US Dollar", currency.getCurrencyNameEN());
        assertEquals("06.02.2025", currency.getDate());
    }
}
