package uz.zaytun.cbuplugin.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import uz.zaytun.cbuplugin.config.props.CbuProperties;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.service.CbuService;
import uz.zaytun.cbuplugin.utils.ResponseUtils;

import java.net.SocketTimeoutException;
import java.util.List;

@Slf4j
@Service
@Qualifier("impl")
@EnableConfigurationProperties(CbuProperties.class)
@ConditionalOnProperty(name = "cbu.cbu.simulate", havingValue = "impl")
public class CbuServiceImpl implements CbuService {

    private static final String CBU_CURRENCY_ENDPOINT = "/uz/arkhiv-kursov-valyut/json";

    private final RestTemplate restTemplate;
    private final CbuProperties cbuProperties;

    public CbuServiceImpl(RestTemplate restTemplate, CbuProperties cbuProperties) {
        log.debug("CbuServiceImpl started");
        this.restTemplate = restTemplate;
        this.cbuProperties = cbuProperties;
    }

    @PostConstruct
    @Override
    public BaseResponse<List<CurrencyDTO>> getCurrencies() {
        try {
            ResponseEntity<List<CurrencyDTO>> response = restTemplate.exchange(
                    cbuProperties.getCbu().getBaseUrl() + CBU_CURRENCY_ENDPOINT,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CurrencyDTO>>() {
                    }
            );
            log.debug("Currencies response: {}", response);
            return ResponseUtils.validate(response);
        } catch (HttpClientErrorException e) {
            log.error("Client error: {}", e.getMessage());
            return new BaseResponse<>(false, CbuErrors.CLIENT_ERROR);
        } catch (HttpServerErrorException e) {
            log.error("Server error: {}", e.getMessage());
            return new BaseResponse<>(false, CbuErrors.SERVER_ERROR);
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                log.error("Read timeout error: {}", e.getMessage());
                return new BaseResponse<>(false, CbuErrors.READ_TIMEOUT_ERROR);
            }
            log.error("Connection timeout error: {}", e.getMessage());
            return new BaseResponse<>(false, CbuErrors.CONNECTION_TIMEOUT_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error in getCurrencies: {}", e.getMessage());
            return new BaseResponse<>(false, CbuErrors.UNKNOWN_ERROR);
        }
    }
}
