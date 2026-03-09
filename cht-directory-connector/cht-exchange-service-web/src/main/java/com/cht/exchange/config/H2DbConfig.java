package com.cht.exchange.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "h2EntityManagerFactory",
//        transactionManagerRef = "h2TransactionManager",
//        basePackages = { "com.cht.exchange.h2.repository" }
//)
public class H2DbConfig {

    @Bean(name = "h2DataSource")
    @ConfigurationProperties(prefix = "h2.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "h2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("h2DataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.cht.exchange.h2.entity")
                .persistenceUnit("h2")
                .build();
    }

    @Bean(name = "h2TransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("h2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // initial schema 只會作用在 primary datasource，如果是其它 datasource 則要設定 datasource initializer
    @Bean(name = "h2DataSourceInitializer")
    public DataSourceInitializer dataSourceInitializer(@Qualifier("h2DataSource") DataSource datasource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("db/h2-sql/schema.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("db/h2-sql/data.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(datasource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}