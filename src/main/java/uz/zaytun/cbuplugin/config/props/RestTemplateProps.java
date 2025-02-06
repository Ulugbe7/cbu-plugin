package uz.zaytun.cbuplugin.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rest-template")
public class RestTemplateProps {
    private int readTimeout;
    private int connectionTimeout;
}
