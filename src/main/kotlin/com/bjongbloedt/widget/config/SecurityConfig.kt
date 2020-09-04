package com.bjongbloedt.widget.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http {
            cors { }
            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
        }
    }

    @Profile("local")
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = UrlBasedCorsConfigurationSource().also { urlBasedCorsConfigurationSource ->
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", CorsConfiguration().also { corsConfiguration ->
            corsConfiguration.allowedOrigins = listOf("*")
            corsConfiguration.allowedMethods = listOf("*")
            corsConfiguration.allowedHeaders = listOf("*")
            corsConfiguration.allowCredentials = true
        })
    }


}