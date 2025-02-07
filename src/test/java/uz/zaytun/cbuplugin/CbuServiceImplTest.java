package uz.zaytun.cbuplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import uz.zaytun.cbuplugin.config.props.CbuProperties;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.service.CbuService;

import java.util.List;

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

        List<CurrencyDTO> mockCurrencies = getListResponseEntity();

        String responseJson = objectMapper.writeValueAsString(mockCurrencies);

        mockServer.expect(ExpectedCount.once(), requestTo(cbuProperties.getCbu().getBaseUrl() + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies();
        log.info("Success test: {}", response);

        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals("USD", response.getData().get(0).getCurrency());
    }

    private List<CurrencyDTO> getListResponseEntity() {
        return List.of(
                new CurrencyDTO(69, "840", "USD", "Доллар США", "AQSH dollari", "АҚШ доллари",
                        "US Dollar", "1", "12985.32", "-13.59", "06.02.2025")
        );
    }

    @Test
    void testGetCurrencies_ClientError() {
        String baseUrl = cbuProperties.getCbu().getBaseUrl();

        mockServer.expect(ExpectedCount.once(), requestTo(baseUrl + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies();
        log.info("Client error test: {}", response);

        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertEquals(CbuErrors.CLIENT_ERROR, response.getMessage());
    }

    @Test
    void testGetCurrencies_ServerError() {

        mockServer.expect(ExpectedCount.once(), requestTo(cbuProperties.getCbu().getBaseUrl() + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies();
        log.info("Server error test: {}", response);

        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertEquals(CbuErrors.SERVER_ERROR, response.getMessage());
    }

    @Test
    void testGetCurrencies_ConnectionTimeout() {
        mockServer.expect(ExpectedCount.once(), requestTo(cbuProperties.getCbu().getBaseUrl() + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(request -> {
                    throw new ResourceAccessException("Connection timed out");
                });

        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies();

        log.info("Connection timeout test: {}", response);

        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertEquals(CbuErrors.CONNECTION_TIMEOUT_ERROR, response.getMessage());
    }

/*
    @Test
    void testGetCurrencies_ReadTimeout() {
        mockServer.expect(ExpectedCount.once(), requestTo(cbuProperties.getCbu().getBaseUrl() + CBU_CURRENCY_ENDPOINT))
                .andExpect(method(HttpMethod.GET))
                .andRespond(request -> {
                            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                            scheduler.schedule(() -> {
                            }, 6, TimeUnit.SECONDS); // 6s kechikish
                            scheduler.shutdown();
                            try {
                                scheduler.awaitTermination(6, TimeUnit.SECONDS); // Asosiy oqimni bloklaydi
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return withSuccess("[]", MediaType.APPLICATION_JSON).createResponse(request);
                        }
                );

        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies();
        log.info("Read timeout test: {}", response);

        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertEquals(CbuErrors.READ_TIMEOUT_ERROR, response.getMessage());
    }
*/
}
