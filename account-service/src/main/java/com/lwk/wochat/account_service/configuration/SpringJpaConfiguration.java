package com.lwk.wochat.account_service.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(com.lwk.wochat.api.configuration.SpringJpaConfiguration.class)
public class SpringJpaConfiguration {
}
