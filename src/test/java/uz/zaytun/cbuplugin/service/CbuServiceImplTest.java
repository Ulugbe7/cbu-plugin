package uz.zaytun.cbuplugin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import uz.zaytun.cbuplugin.config.CbuProperties;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CbuCurrencyResponseDTO;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.exception.CustomException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@Slf4j
@SpringBootTest(properties = {"cbu.cbu.simulate=impl"})
class CbuServiceImplTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CbuProperties cbuProperties;

    @Autowired
    private CbuService cbuService;

    @Autowired
    @Qualifier(value = "cbuObjectMapper")
    private ObjectMapper objectMapper;

    private MockRestServiceServer mockServer;
    private static final String CBU_CURRENCY_ENDPOINT = "/uz/arkhiv-kursov-valyut/json";

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @AfterEach
    public void tearDown() {
        mockServer.verify();
    }

    @Test
    void testGetCurrencies_Success() throws Exception {

        List<CbuCurrencyResponseDTO> mockCurrencies = loadMockCurrencies();

        String responseJson = objectMapper.writeValueAsString(mockCurrencies);

        mockServer.expect(ExpectedCount.once(), requestTo(cbuProperties.getSetting().getBaseUrl() + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies(new CurrencyDTO());
        log.info("Success test: {}", response);

        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals("USD", response.getData().get(0).getCurrency());
    }

    private List<CbuCurrencyResponseDTO> loadMockCurrencies() {
        var currencyUSD = CbuCurrencyResponseDTO.builder()
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

        return List.of(currencyUSD);
    }

    @Test
    void testGetCurrencies_ClientError() {
        mockServer.expect(ExpectedCount.once(), requestTo(cbuProperties.getSetting().getBaseUrl() + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        CustomException exception = assertThrows(CustomException.class, () -> cbuService.getCurrencies(new CurrencyDTO()));
        assertEquals(CbuErrors.CLIENT_ERROR, exception.getError());
    }

    @Test
    void testGetCurrencies_ServerError() {

        mockServer.expect(ExpectedCount.once(), requestTo(cbuProperties.getSetting().getBaseUrl() + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        CustomException exception = assertThrows(CustomException.class, () -> cbuService.getCurrencies(new CurrencyDTO()));
        assertEquals(CbuErrors.SERVER_ERROR, exception.getError());
    }

    @Test
    void testGetCurrencies_ConnectionTimeout() {
        mockServer.expect(ExpectedCount.once(), requestTo(cbuProperties.getSetting().getBaseUrl() + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(request -> {
                    throw new ResourceAccessException("Connection timed out");
                });

        CustomException exception = assertThrows(CustomException.class, () -> cbuService.getCurrencies(new CurrencyDTO()));
        assertEquals(CbuErrors.CONNECTION_TIMEOUT_ERROR, exception.getError());
    }

    @Test
    @Disabled
    void testGetCurrencies_ReadTimeout() {
        mockServer.expect(ExpectedCount.once(), requestTo(cbuProperties.getSetting().getBaseUrl() + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(request -> {
                            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                            scheduler.schedule(() -> {
                            }, 6, TimeUnit.SECONDS);
                            scheduler.shutdown();
                            try {
                                scheduler.awaitTermination(6, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return withSuccess("[]", MediaType.APPLICATION_JSON).createResponse(request);
                        }
                );

        CustomException exception = assertThrows(CustomException.class, () -> cbuService.getCurrencies(new CurrencyDTO()));
        assertEquals(CbuErrors.READ_TIMEOUT_ERROR, exception.getError());
    }
}
