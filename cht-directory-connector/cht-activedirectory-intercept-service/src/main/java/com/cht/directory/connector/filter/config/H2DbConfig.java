package com.cht.directory.connector.filter.config;

import jakarta.persistence.EntityManager;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "h2EntityManagerFactory",
        transactionManagerRef = "h2TransactionManager",
        basePackages = { "com.cht.directory.connector.filter.h2.repository" }
)
public class H2DbConfig {

    @Bean(name = "h2DataSource")
    @ConfigurationProperties(prefix = "h2.datasource")
    public DataSource h2DataSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean(name = "h2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean h2EntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("h2DataSource") DataSource h2DataSource) {
        return builder
                .dataSource(h2DataSource)
                .packages("com.cht.directory.connector.filter.h2.entity")
                .persistenceUnit("h2PersistenceUnit")
                .build();
    }

    @Bean(name = "h2TransactionManager")
    public PlatformTransactionManager h2TransactionManager(
            @Qualifier("h2EntityManagerFactory") LocalContainerEntityManagerFactoryBean h2EntityManagerFactory) {
        return new JpaTransactionManager(h2EntityManagerFactory.getObject());
    }

    @Bean(name = "h2EntityManager")
    public EntityManager h2EntityManager(
            @Qualifier("h2EntityManagerFactory") LocalContainerEntityManagerFactoryBean h2EntityManagerFactory) {
        return h2EntityManagerFactory.getObject().createEntityManager();
    }

    @Bean
    public SpringLiquibase h2Liquibase(@Qualifier("h2DataSource") DataSource h2DataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(h2DataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-h2.yaml");
        liquibase.setContexts("h2");
        liquibase.setShouldRun(true);
        return liquibase;
    }
}
