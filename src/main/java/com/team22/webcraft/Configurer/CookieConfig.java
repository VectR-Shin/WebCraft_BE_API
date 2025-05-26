package com.team22.webcraft.Configurer;

import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieConfig {
    @Bean
    public TomcatContextCustomizer sameSiteCookieConfig() {
        return context -> {
            Rfc6265CookieProcessor processor = new Rfc6265CookieProcessor();
            processor.setSameSiteCookies("None");
            context.setCookieProcessor(processor);
        };
    }
}
