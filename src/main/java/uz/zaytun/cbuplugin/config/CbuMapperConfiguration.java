package uz.zaytun.cbuplugin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CbuMapperConfiguration {
    @Bean(name = "cbuObjectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
