package com.mobile.makefive.config;

import com.mobile.makefive.model.AppData;
import com.mobile.makefive.model.GameData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class WebConfig {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION)
    public AppData pref() {
        return new AppData();
    }

}