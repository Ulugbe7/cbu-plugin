package uz.zaytun.cbuplugin.service.impl;

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
import uz.zaytun.cbuplugin.domain.dto.CbuCurrencyResponseDTO;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.exception.CustomException;
import uz.zaytun.cbuplugin.service.CbuService;
import uz.zaytun.cbuplugin.service.CurrencyService;
import uz.zaytun.cbuplugin.service.mapper.CurrencyMapper;

import java.net.SocketTimeoutException;
import java.util.List;

import static uz.zaytun.cbuplugin.service.CbuService.CBU_SERVICE;


@Slf4j
@Service(value = CBU_SERVICE)
@ConditionalOnProperty(name = "cbu.setting.simulate", havingValue = "impl")
public class CbuServiceImpl implements CbuService {

    private final RestTemplate restTemplate;

    private final CurrencyMapper currencyMapper;

    private final CurrencyService currencyService;

    public CbuServiceImpl(@Qualifier(value = "cbuRestTemplate") RestTemplate restTemplate, CurrencyMapper currencyMapper, CurrencyService currencyService) {
        log.debug("##### CbuService: simulate service is off #####");
        this.restTemplate = restTemplate;
        this.currencyMapper = currencyMapper;
        this.currencyService = currencyService;
    }

    @Override
    public BaseResponse<List<CurrencyDTO>> getCurrencies(CurrencyDTO request) {
        try {
            ResponseEntity<List<CbuCurrencyResponseDTO>> response = restTemplate.exchange(
                    CBU_CURRENCY_ENDPOINT,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (response.getBody() == null) {
                throw new CustomException(CbuErrors.NULL_RESPONSE_ERROR, "cbu currency response body is null");
            }

            var responseBody = response.getBody();
            log.info("Cbu currencies response size: {}", responseBody.size());
            var filteredDTOs = responseBody.stream()
                    .filter(currencyDTO ->
                            (request.getCurrency() == null || currencyDTO.getCurrency().equals(request.getCurrency()))
                                    && (request.getCode() == null || currencyDTO.getCode().equals(request.getCode()))
                    ).toList();

            log.info("Cbu filtered response size: {}", filteredDTOs.size());
            if (!filteredDTOs.isEmpty()) {
                currencyService.saveAll(filteredDTOs);
            }
            return new BaseResponse<>(filteredDTOs.stream().map(currencyMapper::toDto).toList());

        } catch (HttpClientErrorException e) {
            log.warn("Client error: {}", e.getMessage());
            throw new CustomException(CbuErrors.CLIENT_ERROR, e.getMessage());
        } catch (HttpServerErrorException e) {
            log.warn("Server error: {}", e.getMessage());
            throw new CustomException(CbuErrors.SERVER_ERROR, e.getMessage());
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                log.error("Read timeout error occurred", e);
                throw new CustomException(CbuErrors.READ_TIMEOUT_ERROR, e.getMessage());
            }
            log.warn("Connection timeout error: {}", e.getMessage());
            throw new CustomException(CbuErrors.CONNECTION_TIMEOUT_ERROR, e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error in getCurrencies: {}", e.getMessage());
            throw new CustomException(CbuErrors.UNKNOWN_ERROR, e.getMessage());
        }
    }
}
