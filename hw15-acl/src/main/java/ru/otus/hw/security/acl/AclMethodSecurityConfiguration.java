package ru.otus.hw.security.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class AclMethodSecurityConfiguration {
    private final AclService aclService;

    @Autowired
    MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler;

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return defaultMethodSecurityExpressionHandler;
    }

}
