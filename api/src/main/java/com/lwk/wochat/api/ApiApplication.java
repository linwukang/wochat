package com.lwk.wochat.api;

import com.lwk.wochat.api.configuration.DataSourceConfiguration;
import com.lwk.wochat.api.configuration.JpaConfig;
import com.lwk.wochat.api.configuration.SpringCloudConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
@Import({
        DataSourceConfiguration.class,
        JpaConfig.class,
        SpringCloudConfiguration.class
})
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
