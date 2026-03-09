package com.cht.directory.connector.filter.config;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "logEntityManagerFactory",
        transactionManagerRef = "logTransactionManager",
        basePackages = { "com.cht.directory.connector.filter.log.repository" }
)
public class LogDbConfig {

    @Bean(name = "logDataSource")
    @Primary
    @ConfigurationProperties(prefix = "log.datasource")
    public DataSource logDataSource() {

        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "logEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean logEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("logDataSource") DataSource logDataSource) {
        return builder
                .dataSource(logDataSource)
                .packages("com.cht.directory.connector.filter.log.entity")
                .persistenceUnit("logPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "logTransactionManager")
    public PlatformTransactionManager logTransactionManager(
            @Qualifier("logEntityManagerFactory") LocalContainerEntityManagerFactoryBean logEntityManagerFactory) {
        return new JpaTransactionManager(logEntityManagerFactory.getObject());
    }

    @Primary
    @Bean(name = "logEntityManager")
    public EntityManager logEntityManager(
            @Qualifier("logEntityManagerFactory") LocalContainerEntityManagerFactoryBean logEntityManagerFactory) {
        return logEntityManagerFactory.getObject().createEntityManager();
    }
}
