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
        List<CurrencyDTO> mockCurrencies = getListResponseEntity();

        BaseResponse<List<CurrencyDTO>> mockResponse = new BaseResponse<>(mockCurrencies);

        Mockito.when(cbuService.getCurrencies()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].Ccy").value("USD"))
                .andExpect(jsonPath("$.data[0].Code").value("840"))
                .andExpect(jsonPath("$.data[1].Ccy").value("EUR"))
                .andExpect(jsonPath("$.data[1].Code").value("841"));
    }

    private List<CurrencyDTO> getListResponseEntity() {
        return List.of(
                new CurrencyDTO(69, "840", "USD", "Доллар США", "AQSH dollari", "АҚШ доллари",
                        "US Dollar", "1", "12985.32", "-13.59", "06.02.2025"),
                new CurrencyDTO(69, "841", "EUR", "Доллар США", "AQSH dollari", "АҚШ доллари",
                        "US Dollar", "1", "12985.32", "-13.59", "06.02.2025")
        );
    }

    @Test
    void testGetCurrencies_ClientError() throws Exception {
        BaseResponse<List<CurrencyDTO>> mockResponse = new BaseResponse<>(false, CbuErrors.CLIENT_ERROR);

        Mockito.when(cbuService.getCurrencies()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("CLIENT_ERROR"));
    }

    @Test
    void testGetCurrencies_ServerError() throws Exception {
        BaseResponse<List<CurrencyDTO>> mockResponse = new BaseResponse<>(false, CbuErrors.SERVER_ERROR);

        Mockito.when(cbuService.getCurrencies()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("SERVER_ERROR"));
    }

    @Test
    void testGetCurrencies_TimeoutError() throws Exception {
        BaseResponse<List<CurrencyDTO>> mockResponse = new BaseResponse<>(false, CbuErrors.READ_TIMEOUT_ERROR);

        Mockito.when(cbuService.getCurrencies()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("READ_TIMEOUT_ERROR"));
    }

    @Test
    void testGetCurrencies_ConnectionTimeoutError() throws Exception {
        BaseResponse<List<CurrencyDTO>> mockResponse = new BaseResponse<>(false, CbuErrors.CONNECTION_TIMEOUT_ERROR);

        Mockito.when(cbuService.getCurrencies()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isGatewayTimeout())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("CONNECTION_TIMEOUT_ERROR"));
    }
}
