package uz.zaytun.cbuplugin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static uz.zaytun.cbuplugin.utils.CbuConstants.CBU_OBJECT_MAPPER;

@Configuration
public class CbuMapperConfiguration {

    @Bean(name = CBU_OBJECT_MAPPER)
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
