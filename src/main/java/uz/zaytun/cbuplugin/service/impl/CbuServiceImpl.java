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
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.repository.CurrencyRepository;
import uz.zaytun.cbuplugin.service.CbuService;
import uz.zaytun.cbuplugin.service.criteria.CurrencyFilter;

import java.net.SocketTimeoutException;
import java.util.List;

import static uz.zaytun.cbuplugin.service.CbuService.CBU_SERVICE;

@Slf4j
@Service(value = CBU_SERVICE)
@ConditionalOnProperty(name = "cbu.setting.simulate", havingValue = "impl")
public class CbuServiceImpl implements CbuService {

    private final RestTemplate restTemplate;

    private final CurrencyRepository currencyRepository;

    public CbuServiceImpl(@Qualifier(value = "cbuRestTemplate") RestTemplate restTemplate, CurrencyRepository currencyRepository) {
        log.debug("##### CbuService: simulate service is off #####");
        this.restTemplate = restTemplate;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public BaseResponse<List<CurrencyDTO>> getCurrencies(CurrencyDTO request) {
        try {
            var currencies = currencyRepository.findAll(CurrencyFilter.filter(request));
            log.info("Repository currencies: {}", currencies);
            if (!currencies.isEmpty()) {
                var currencyDTOs = currencies.stream().map(CurrencyDTO::toDTO).toList();
                return new BaseResponse<>(currencyDTOs);
            }

            ResponseEntity<List<CurrencyDTO>> response = restTemplate.exchange(
                    CBU_CURRENCY_ENDPOINT,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            var responseBody = response.getBody();
            var filteredDTOs = responseBody.stream()
                    .filter(currencyDTO ->
                            (request.getCurrency() == null || currencyDTO.getCurrency().equals(request.getCurrency()))
                                    && (request.getCode() == null || currencyDTO.getCode().equals(request.getCode()))
                    ).toList();

            log.info("Cbu filtered response: {}", filteredDTOs);
            if (!filteredDTOs.isEmpty()) {
                currencyRepository.saveAll(filteredDTOs.stream().map(CurrencyDTO::fromDTO).toList());
            }

            return new BaseResponse<>(filteredDTOs);
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
