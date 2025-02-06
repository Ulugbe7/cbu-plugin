package uz.zaytun.cbuplugin.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cbu")
public class CbuProps {
    private String baseUrl;
}
