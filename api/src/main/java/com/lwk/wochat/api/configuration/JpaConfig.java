package com.lwk.wochat.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.lwk.wochat.api.dao.repository")
public class JpaConfig {
    @Resource(name = "dataSource")
    private DataSource dataSource;

    @Value("${spring.jpa.entityClassPackage}")
    private String entityClassPackage;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan(entityClassPackage);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
