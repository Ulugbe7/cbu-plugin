package uz.zaytun.cbuplugin.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.service.CbuService;
import uz.zaytun.cbuplugin.utils.ResponseUtils;

import java.net.SocketTimeoutException;
import java.util.List;

import static uz.zaytun.cbuplugin.service.CbuService.CBU_SERVICE;

@Slf4j
@Service(value = CBU_SERVICE)
@ConditionalOnProperty(name = "cbu.setting.simulate", havingValue = "impl")
public class CbuServiceImpl implements CbuService {

    private final RestTemplate restTemplate;

    public CbuServiceImpl(@Qualifier(value = "cbuRestTemplate") RestTemplate restTemplate) {
        log.debug("##### CbuService: simulate service is off #####");
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    @Override
    public BaseResponse<List<CurrencyDTO>> getCurrencies() {
        try {
            ResponseEntity<List<CurrencyDTO>> response = restTemplate.exchange(
                    CBU_CURRENCY_ENDPOINT,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            log.debug("Currencies response: {}", response);
            return ResponseUtils.validate(response);
        } catch (HttpClientErrorException e) {
            log.warn("Client error: {}", e.getMessage());
            return new BaseResponse<>(false, e.getMessage(), CbuErrors.CLIENT_ERROR);
        } catch (HttpServerErrorException e) {
            log.warn("Server error: {}", e.getMessage());
            return new BaseResponse<>(false, e.getMessage(), CbuErrors.SERVER_ERROR);
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                log.error("Read timeout error occurred", e);
                return new BaseResponse<>(false, e.getMessage(), CbuErrors.READ_TIMEOUT_ERROR);
            }
            log.warn("Connection timeout error: {}", e.getMessage());
            return new BaseResponse<>(false, e.getMessage(), CbuErrors.CONNECTION_TIMEOUT_ERROR);
        } catch (Exception e) {
            log.warn("Unexpected error in getCurrencies: {}", e.getMessage());
            return new BaseResponse<>(false, e.getMessage(), CbuErrors.UNKNOWN_ERROR);
        }
    }
}
