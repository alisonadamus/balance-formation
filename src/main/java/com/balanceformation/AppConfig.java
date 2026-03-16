package com.balanceformation;

import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Конфігураційний клас для налаштування Spring Framework, бази даних H2, Hibernate та валідації.
 * Визначає джерело даних, фабрику сутностей, менеджер транзакцій та валідатор.
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "com.balanceformation")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.balanceformation.repository")
public class AppConfig {

    /**
     * Налаштовує джерело даних для бази даних H2.
     * @return об’єкт DataSource, що представляє підключення до бази даних H2.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:file:./data;DB_CLOSE_ON_EXIT=FALSE");
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }

    /**
     * Налаштовує фабрику сутностей для роботи з Hibernate.
     * Визначає пакети для сканування сутностей та конфігурацію Hibernate.
     * @param dataSource джерело даних для підключення до бази.
     * @return об’єкт LocalContainerEntityManagerFactoryBean для управління сутностями.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.balanceformation.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        emf.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        emf.setJpaProperties(jpaProperties);

        return emf;
    }

    /**
     * Налаштовує менеджер транзакцій для управління транзакціями JPA.
     * @param emf фабрика сутностей для управління транзакціями.
     * @return об’єкт PlatformTransactionManager для обробки транзакцій.
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    /**
     * Налаштовує обробник винятків для перекладу виключень JPA у винятки Spring.
     * @return об’єкт PersistenceExceptionTranslationPostProcessor для обробки винятків.
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * Налаштовує валідатор для перевірки даних сутностей.
     * @return об’єкт Validator для виконання валідації.
     */
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}