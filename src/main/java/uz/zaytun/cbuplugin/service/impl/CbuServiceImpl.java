package uz.zaytun.cbuplugin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.zaytun.cbuplugin.config.props.CbuProperties;
import uz.zaytun.cbuplugin.domain.dto.BaseResponse;
import uz.zaytun.cbuplugin.domain.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.domain.enumuration.CbuErrors;
import uz.zaytun.cbuplugin.service.CbuService;
import uz.zaytun.cbuplugin.utils.ResponseUtils;

import java.util.List;

@Slf4j
@Service
@EnableConfigurationProperties(CbuProperties.class)
@ConditionalOnProperty(name = "mock.simulated", havingValue = "impl")
public class CbuServiceImpl implements CbuService {

    private static final String CBU_CURRENCY_ENDPOINT = "/uz/arkhiv-kursov-valyut/json";

    private final RestTemplate restTemplate;
    private final CbuProperties cbuProperties;

    public CbuServiceImpl(RestTemplate restTemplate, CbuProperties cbuProperties) {
        log.debug("CbuServiceImpl started");
        this.restTemplate = restTemplate;
        this.cbuProperties = cbuProperties;
    }

    @Override
    public BaseResponse<List<CurrencyDTO>> getCurrencies() {
        try {
            var response = restTemplate.exchange(
                    cbuProperties.getCbu().getBaseUrl() + CBU_CURRENCY_ENDPOINT,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CurrencyDTO>>() {
                    }
            );
            return ResponseUtils.validate(response);
        } catch (Exception e) {
            log.error("getCurrencies error: {}", e.getMessage());
            return new BaseResponse<>(false, CbuErrors.UNKNOWN_ERROR);
        }
    }
}
