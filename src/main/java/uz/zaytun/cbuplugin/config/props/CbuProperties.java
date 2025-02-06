package uz.zaytun.cbuplugin.config.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cbu")
public class CbuProperties {
    private String appName;
    private final Cbu cbu = new Cbu();
    private final CbuClientProperties clientConfig = new CbuClientProperties();

    @Getter
    @Setter
    @ToString
    public static class Cbu {
        private String baseUrl;
        private String simulate;
    }

    @Getter
    @Setter
    @ToString
    public static class CbuClientProperties {
        private Integer readTimeout;
        private Integer connectionTimeout;
    }
}
