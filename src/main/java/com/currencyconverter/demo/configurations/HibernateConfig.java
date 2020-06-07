package com.currencyconverter.demo.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

import static com.currencyconverter.demo.constants.ConfigurationConstants.*;

@Configuration
@EnableTransactionManagement
@PropertySource(value = HIBERNATE_PROPERTY_SOURCE)
public class HibernateConfig {
    private String dbUrl;
    private String dbUserName;
    private String dbPassword;

    @Autowired
    public HibernateConfig(Environment env) {
        dbUrl = env.getProperty(HIBERNATE_DATABASE_URL);
        dbUserName = env.getProperty(HIBERNATE_DATABASE_USERNAME);
        dbPassword = env.getProperty(HIBERNATE_DATABASE_PASSWORD);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(HIBERNATE_SET_PACKAGES_TO_SCAN);
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(HIBERNATE_SET_DRIVER_CLASS_NAME);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUserName);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(HIBERNATE_SET_PROPERTY_DIALECT, HIBERNATE_SET_PROPERTY_MYSQL_DIALECT);
        return hibernateProperties;
    }
}
