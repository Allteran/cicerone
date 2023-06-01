package io.allteran.cicerone.backbone.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    @Value("${origin}")
    private String ALLOWED_ORIGIN;

//    private String ROLE_ADMIN_ID;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public WebSecurityConfig(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .cors().configurationSource(createCorsConfigSource())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((exchange, ex) ->
                        Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
                )
                .accessDeniedHandler((exchange, denied) ->
                        Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                )
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers("/auth/**", "/favicon.ico").permitAll()
                //implement next line in case when you want to use certain path only for certain role
                //and don't forget that GrantedAuthority.getName == Role.getId
//                .pathMatchers("/admin").hasRole(ROLE_ADMIN_ID)
                .anyExchange().authenticated()
                .and()
                .build();
    }

    public CorsConfigurationSource createCorsConfigSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(ALLOWED_ORIGIN);
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedHeader("*");

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
