/*
 * 
 */
package com.ideas2it.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ideas2it.chatbot.intersept.GoogleSheetsInterceptor;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public HandlerInterceptor getControllerInterceptor() {
        final HandlerInterceptor c = new GoogleSheetsInterceptor();
        return c;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(getControllerInterceptor()).addPathPatterns("/api/*");
    }
}
