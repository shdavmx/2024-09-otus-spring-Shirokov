package ru.otus.hw.security.acl;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;

public interface AclMethodSecurityExpressionOperations extends MethodSecurityExpressionOperations {
    boolean isAdministrator(Object targetId, Class<?> targetClass);

    boolean isAdministrator(Object target);

    boolean canRead(Object targetId, Class<?> targetClass);

    boolean canCreate(Object targetId, Class<?> targetClass);
}
