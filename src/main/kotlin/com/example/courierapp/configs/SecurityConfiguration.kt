package com.example.courierapp.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val authenticationProvider: AuthenticationProvider
) {
    @Bean
    fun securityFilterChain (
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): DefaultSecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth", "/api/auth/refresh", "/error.html", "/api/login", "/login", "/logout", "/index.html", "/customers.html", "/couriers.html", "/auth.html", "dashboard/index.html", "couriersList/index.html")
                    .permitAll()
                    .requestMatchers (HttpMethod.POST,"/api/customers")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/couriers")
                    .hasAnyRole("ADMIN", "CUSTOMER", "COURIER")
                    .requestMatchers(HttpMethod.POST, "api/reviews/couriers/**")
                    .hasAnyRole("CUSTOMER", "COURIER")
                    .requestMatchers(HttpMethod.DELETE, "api/reviews/couriers/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "api/reviews/couriers/**")
                    .hasAnyRole("ADMIN", "COURIER")
                    .requestMatchers("/api/customers**", "/api/couriers**", "/api/packages**", "/api/reviews**", "/api/reviews/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/packages/couriers/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/packages/**")
                    .hasAnyRole("ADMIN", "CUSTOMER", "COURIER")
                    .requestMatchers(HttpMethod.GET, "/api/packages/**")
                    .hasAnyRole("ADMIN", "CUSTOMER", "COURIER")
                    .requestMatchers(HttpMethod.PUT, "/api/packages/**")
                    .hasAnyRole("ADMIN", "COURIER")
                    .requestMatchers(HttpMethod.POST, "/api/packages/customers/**")
                    .hasAnyRole("ADMIN", "CUSTOMER", "COURIER")
                    .requestMatchers("/api/packages/couriers/**", "/api/packages/**")
                    .hasAnyRole("ADMIN", "COURIER")
                    .anyRequest()
                    .fullyAuthenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}