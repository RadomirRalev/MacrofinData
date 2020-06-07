package com.currencyconverter.demo.constants;

public class ConfigurationConstants {
    //Hibernate
    public static final String HIBERNATE_PROPERTY_SOURCE = "classpath:application.properties";
    public static final String HIBERNATE_DATABASE_URL = "database.url";
    public static final String HIBERNATE_DATABASE_USERNAME = "database.username";
    public static final String HIBERNATE_DATABASE_PASSWORD = "database.password";
    public static final String HIBERNATE_SET_PACKAGES_TO_SCAN = "com.currencyconverter.demo.models";
    public static final String HIBERNATE_SET_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String HIBERNATE_SET_PROPERTY_DIALECT = "hibernate.dialect";
    public static final String HIBERNATE_SET_PROPERTY_MYSQL_DIALECT = "org.hibernate.dialect.MySQLDialect";

    //Thymeleaf
    public static final String THYMELEAF_TEMPLATE_RESOLVER_SET_PREFIX = "classpath:templates/";
    public static final String THYMELEAF_TEMPLATE_RESOLVER_SET_SUFFIX = ".html";
    public static final int THYMELEAF_VIEW_RESOLVER_SET_ORDER = 1;

}
