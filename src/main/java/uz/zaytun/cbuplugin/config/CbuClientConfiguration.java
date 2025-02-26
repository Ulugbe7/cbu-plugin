package uz.zaytun.cbuplugin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CbuProperties.class)
public class CbuClientConfiguration {

    private final CbuProperties cbuProperties;

    @Bean(value = "cbuRestTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(cbuProperties.getClientConfig().getConnectionTimeout());
        factory.setReadTimeout(cbuProperties.getClientConfig().getReadTimeout());
        return restTemplateBuilder
                .messageConverters(createMessageConverters())
                .rootUri(cbuProperties.getSetting().getBaseUrl())
                .requestFactory(() -> factory)
                .build();
    }

    private List<HttpMessageConverter<?>> createMessageConverters() {
        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        return List.of(jsonMessageConverter);
    }
}