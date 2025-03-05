package com.epam.labaratory.springboottask.config;

import com.epam.labaratory.springboottask.filter.TransactionIdFilter;
import com.epam.labaratory.springboottask.interceptor.LoggingInterceptor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebConfig implements WebMvcConfigurer {

    LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }

    @Bean
    public FilterRegistrationBean<TransactionIdFilter> transactionIdFilterFilterRegistration(TransactionIdFilter transactionIdFilter) {
        FilterRegistrationBean<TransactionIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(transactionIdFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}