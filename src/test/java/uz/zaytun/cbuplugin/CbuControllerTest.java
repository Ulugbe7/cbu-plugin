package uz.zaytun.cbuplugin;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uz.zaytun.cbuplugin.controller.CbuController;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.service.CbuService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = CbuController.class)
class CbuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CbuService cbuService;

    @Test
    void testGetCurrencies_Success() throws Exception {
        var simulateCurrencies = loadSimulateData();

        var mockResponse = new BaseResponse<>(simulateCurrencies);

        initCbuMockResponse(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].Ccy").value("USD"))
                .andExpect(jsonPath("$.data[0].Code").value("840"))
                .andExpect(jsonPath("$.data[1].Ccy").value("EUR"))
                .andExpect(jsonPath("$.data[1].Code").value("841"));
    }

    private List<CurrencyDTO> loadSimulateData() {
        var currency1 = CurrencyDTO.builder()
                .id(69)
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

        var currency2 = CurrencyDTO.builder()
                .id(69)
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

        return List.of(currency1, currency2);
    }

    @Test
    void testGetCurrencies_ClientError() throws Exception {
        BaseResponse<List<CurrencyDTO>> mockResponse = new BaseResponse<>(false, "Client error", CbuErrors.CLIENT_ERROR);

        initCbuMockResponse(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("CLIENT_ERROR"));
    }

    @Test
    void testGetCurrencies_ServerError() throws Exception {
        BaseResponse<List<CurrencyDTO>> mockResponse = new BaseResponse<>(false, "Server error", CbuErrors.SERVER_ERROR);

        initCbuMockResponse(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("SERVER_ERROR"));
    }

    @Test
    void testGetCurrencies_TimeoutError() throws Exception {
        BaseResponse<List<CurrencyDTO>> mockResponse = new BaseResponse<>(false, "Timeout error", CbuErrors.READ_TIMEOUT_ERROR);

        initCbuMockResponse(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("READ_TIMEOUT_ERROR"));
    }

    @Test
    void testGetCurrencies_ConnectionTimeoutError() throws Exception {
        BaseResponse<List<CurrencyDTO>> mockResponse = new BaseResponse<>(false, "Connection timeout error", CbuErrors.CONNECTION_TIMEOUT_ERROR);

        initCbuMockResponse(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("CONNECTION_TIMEOUT_ERROR"));
    }

    private void initCbuMockResponse(BaseResponse<List<CurrencyDTO>> mockResponse) {
        Mockito.when(cbuService.getCurrencies()).thenReturn(mockResponse);
    }
}
