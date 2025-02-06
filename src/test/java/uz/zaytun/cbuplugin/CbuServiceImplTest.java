package uz.zaytun.cbuplugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import uz.zaytun.cbuplugin.config.props.CbuProperties;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.service.impl.CbuServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CbuServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CbuProperties cbuProperties;

    private CbuServiceImpl cbuService;

    private MockRestServiceServer mockServer;


    @BeforeEach
    void setUp() {
        CbuProperties.Cbu cbuMock = mock(CbuProperties.Cbu.class);
        when(cbuMock.getBaseUrl()).thenReturn("https://cbu.uz");
        when(cbuProperties.getCbu()).thenReturn(cbuMock);

        cbuService = new CbuServiceImpl(restTemplate, cbuProperties);

        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetCurrencies_Success() {
        // Mock ma'lumot yaratamiz
        ResponseEntity<List<CurrencyDTO>> mockResponse = getListResponseEntity();

        // RestTemplate ni mock qilish
        when(restTemplate.exchange(
                eq("https://cbu.uz/uz/arkhiv-kursov-valyut/json"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponse);

        // Servis metodini chaqiramiz
        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies();

        // Test validatsiyalari
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());
        assertEquals("USD", response.getData().get(0).getCurrency());
        assertEquals("EUR", response.getData().get(1).getCurrency());

        // RestTemplate exchange faqat 1 marta chaqirilganini tekshiramiz
        verify(restTemplate, times(1)).exchange(
                anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)
        );
    }

    private static ResponseEntity<List<CurrencyDTO>> getListResponseEntity() {
        List<CurrencyDTO> mockCurrencies = List.of(
                new CurrencyDTO(69, "840", "USD", "Доллар США", "AQSH dollari", "АҚШ доллари",
                        "US Dollar", "1", "12985.32", "-13.59", "06.02.2025"),
                new CurrencyDTO(70, "978", "EUR", "Евро", "Yevro", "Евро", "Euro",
                        "1", "14000.50", "10.00", "06.02.2025")
        );

        ResponseEntity<List<CurrencyDTO>> mockResponse =
                new ResponseEntity<>(mockCurrencies, HttpStatus.OK);
        return mockResponse;
    }

    @Test
    void testGetCurrencies_ClientError() {
        ResponseEntity<List<CurrencyDTO>> mockResponse =
                new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // 400 error

        when(restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponse);

        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies();

        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertEquals(CbuErrors.NULL_BODY_ERROR, response.getMessage());

        verify(restTemplate, times(1)).exchange(
                anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetCurrencies_ServerError() {
        ResponseEntity<List<CurrencyDTO>> mockResponse =
                new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 error

        when(restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponse);

        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies();

        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertEquals(CbuErrors.NULL_BODY_ERROR, response.getMessage());

        verify(restTemplate, times(1)).exchange(
                anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetCurrencies_Error() {
        // RestTemplate exception chiqarishi uchun mock qilamiz
        when(restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)
        )).thenThrow(new RuntimeException("API error"));

        // Servis metodini chaqiramiz
        BaseResponse<List<CurrencyDTO>> response = cbuService.getCurrencies();

        // Xato holatini tekshiramiz
        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertEquals(CbuErrors.UNKNOWN_ERROR, response.getMessage());

        // RestTemplate exchange chaqirilganini tekshiramiz
        verify(restTemplate, times(1)).exchange(
                anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)
        );
    }
}
