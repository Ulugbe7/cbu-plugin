package uz.zaytun.cbuplugin.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.exception.CustomException;
import uz.zaytun.cbuplugin.exception.ExceptionManager;
import uz.zaytun.cbuplugin.service.CurrencyFetchService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ExceptionManager.class)
@WebMvcTest(controllers = CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyFetchService currencyFetchService;

    @Test
    void testGetCurrencies_Success() throws Exception {
        var simulateCurrencies = loadSimulateData();

        var mockResponse = new BaseResponse<>(simulateCurrencies);

        initCbuMockResponse(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].currency").value("USD"))
                .andExpect(jsonPath("$.data[0].code").value("840"))
                .andExpect(jsonPath("$.data[1].currency").value("EUR"))
                .andExpect(jsonPath("$.data[1].code").value("841"));
    }

    private List<CurrencyDTO> loadSimulateData() {
        var currencyUSD = CurrencyDTO.builder()
                .id(69L)
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

        var currencyEUR = CurrencyDTO.builder()
                .id(69L)
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

        return List.of(currencyUSD, currencyEUR);
    }

    @Test
    void testGetCurrencies_ClientError() throws Exception {

        Mockito.when(currencyFetchService.fetchCurrencies(new CurrencyDTO()))
                .thenThrow(new CustomException(CbuErrors.CLIENT_ERROR, "CLIENT_ERROR"));

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("CLIENT_ERROR"));
    }

    @Test
    void testGetCurrencies_ServerError() throws Exception {
        Mockito.when(currencyFetchService.fetchCurrencies(new CurrencyDTO()))
                .thenThrow(new CustomException(CbuErrors.SERVER_ERROR, "SERVER_ERROR"));

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("SERVER_ERROR"));
    }

    @Test
    void testGetCurrencies_TimeoutError() throws Exception {
        Mockito.when(currencyFetchService.fetchCurrencies(new CurrencyDTO()))
                .thenThrow(new CustomException(CbuErrors.READ_TIMEOUT_ERROR, "READ_TIMEOUT_ERROR"));

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("READ_TIMEOUT_ERROR"));
    }

    @Test
    void testGetCurrencies_ConnectionTimeoutError() throws Exception {
        Mockito.when(currencyFetchService.fetchCurrencies(new CurrencyDTO()))
                .thenThrow(new CustomException(CbuErrors.CONNECTION_TIMEOUT_ERROR, "CONNECTION_TIMEOUT_ERROR"));

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("CONNECTION_TIMEOUT_ERROR"));
    }

    private void initCbuMockResponse(BaseResponse<List<CurrencyDTO>> mockResponse) {
        Mockito.when(currencyFetchService.fetchCurrencies(new CurrencyDTO())).thenReturn(mockResponse);
    }
}
