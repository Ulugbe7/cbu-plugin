package uz.zaytun.cbuplugin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.zaytun.cbuplugin.config.props.CbuProps;
import uz.zaytun.cbuplugin.dto.BaseResponse;
import uz.zaytun.cbuplugin.dto.CurrencyDTO;
import uz.zaytun.cbuplugin.utils.Message;
import uz.zaytun.cbuplugin.utils.ResponseValidator;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(CbuProps.class)
@ConditionalOnProperty(name = "mock.simulated", havingValue = "false")
public class CBUServiceImpl implements CBUService {

    private final RestTemplate restTemplate;

    private final CbuProps cbuProps;

    @Override
    public BaseResponse<?> getCurrencies() {
        try {
            String cbuEndpoint = "/uz/arkhiv-kursov-valyut/json";
            var response = restTemplate.exchange(
                    cbuProps.getBaseUrl() + cbuEndpoint,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CurrencyDTO>>() {
                    }
            );
            return ResponseValidator.validate(response);
        } catch (Exception e) {
            log.error("getCurrencies error: {}", e.getMessage());
            return new BaseResponse<>(false, Message.UNKNOWN_ERROR);
        }
    }
}
