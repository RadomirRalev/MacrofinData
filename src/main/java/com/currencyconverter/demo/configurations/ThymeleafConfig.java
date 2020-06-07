package com.currencyconverter.demo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import static com.currencyconverter.demo.constants.ConfigurationConstants.*;


@Configuration
public class ThymeleafConfig {
    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix(THYMELEAF_TEMPLATE_RESOLVER_SET_PREFIX);
        templateResolver.setSuffix(THYMELEAF_TEMPLATE_RESOLVER_SET_SUFFIX);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setTemplateEngineMessageSource(messageSource());
        return templateEngine;
    }

    @Bean public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(THYMELEAF_VIEW_RESOLVER_SET_ORDER);
        return viewResolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        return new ResourceBundleMessageSource();
    }

}
