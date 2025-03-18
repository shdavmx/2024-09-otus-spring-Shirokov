package ru.otus.hw.security.services;


import org.springframework.security.acls.model.Permission;

public interface AclWrapperService {
    void createPermission(Object object, Permission permission);

    void createAllPermission(Object object);
}
