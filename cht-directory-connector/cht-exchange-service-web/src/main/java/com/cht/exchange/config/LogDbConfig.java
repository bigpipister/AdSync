package com.cht.exchange.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "logEntityManagerFactory",
        transactionManagerRef = "logTransactionManager",
        basePackages = { "com.cht.exchange.log.repository" }
)
public class LogDbConfig {

    @Primary
    @Bean(name = "logDataSource")
    @ConfigurationProperties(prefix = "log.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "logEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("logDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.cht.exchange.log.entity")
                .persistenceUnit("log")
                .build();
    }

    @Primary
    @Bean(name = "logTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("logEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}