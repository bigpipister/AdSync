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
        entityManagerFactoryRef = "webEntityManagerFactory",
        transactionManagerRef = "webTransactionManager",
        basePackages = { "com.cht.directory.connector.filter.web.repository" }
)
public class WebDbConfig {

    @Bean(name = "webDataSource")
    @ConfigurationProperties(prefix = "web.datasource")
    public DataSource webDataSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean(name = "webEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean webEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("webDataSource") DataSource webDataSource) {
        return builder
                .dataSource(webDataSource)
                .packages("com.cht.directory.connector.filter.web.entity")
                .persistenceUnit("webPersistenceUnit")
                .build();
    }

    @Bean(name = "webTransactionManager")
    public PlatformTransactionManager webTransactionManager(
            @Qualifier("webEntityManagerFactory") LocalContainerEntityManagerFactoryBean webEntityManagerFactory) {
        return new JpaTransactionManager(webEntityManagerFactory.getObject());
    }

    @Bean(name = "webEntityManager")
    public EntityManager webEntityManager(
            @Qualifier("webEntityManagerFactory") LocalContainerEntityManagerFactoryBean webEntityManagerFactory) {
        return webEntityManagerFactory.getObject().createEntityManager();
    }
}
