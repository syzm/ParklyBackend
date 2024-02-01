package pw.react.backend.services;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pw.react.backend.openapi.OpenApiConfig;
import pw.react.backend.security.jwt.services.JwtConfig;

import java.util.*;

import static java.util.stream.Collectors.toSet;

@Configuration
@EnableWebSecurity
@Import({
        NonBatchConfig.class, JwtConfig.class, OpenApiConfig.class
})
public class MainConfig {

    private static final Logger log = LoggerFactory.getLogger(MainConfig.class);
    private static final Map<String, String> envPropertiesMap = System.getenv();

    private final String corsUrls;
    private final String corsMappings;

    public MainConfig(@Value(value = "${cors.urls}") String corsUrls,
                      @Value(value = "${cors.mappings}") String corsMappings) {
        this.corsUrls = corsUrls;
        this.corsMappings = corsMappings;
    }

    @PostConstruct
    protected void init() {
        log.debug("************** Environment variables **************");
        for (Map.Entry<String, String> entry : envPropertiesMap.entrySet()) {
            log.debug("[{}] : [{}]", entry.getKey(), entry.getValue());
        }
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public HttpService httpService(RestTemplate restTemplate) {
        return new HttpBaseService(restTemplate);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                final Set<String> mappings = getCorsMappings();
                if (mappings.isEmpty()) {
                    registry.addMapping("/**");
                } else {
                    for (String mapping : mappings) {
                        registry.addMapping(mapping).allowedOrigins(getCorsUrls());

                    }
                }
            }
        };
    }

    private String[] getCorsUrls() {
        return Optional.ofNullable(corsUrls)
                .map(value -> value.split(","))
                .orElseGet(() -> new String[0]);
    }

    private Set<String> getCorsMappings() {
        return Optional.ofNullable(corsMappings)
                .map(value -> Arrays.stream(value.split(",")))
                .map(stream -> stream.collect(toSet()))
                .orElseGet(HashSet::new);
    }
}
