package com.emsi.eventplatform.paymentservice.client;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FeignInternalAuthConfig {

    @Bean
    RequestInterceptor internalTokenInterceptor(
            @Value("${app.internal.token}") String token,
            @Value("${app.internal.header}") String header
    ) {
        return template -> template.header(header, token);
    }
}
