package ru.otus.hw.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.hw.security.models.AuthorityNames;

@RequiredArgsConstructor
@Service
public class AclWrapperServiceImpl implements AclWrapperService {
    private final MutableAclService mutableAclService;

    @Override
    public void createPermission(Object object, Permission permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid owner = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        final Sid admin = new GrantedAuthoritySid(AuthorityNames.ROLE_ADMIN.name());

        MutableAcl acl = mutableAclService.createAcl(oid);
        acl.insertAce(acl.getEntries().size(), permission, owner, true);
        acl.insertAce(acl.getEntries().size(), permission, admin, true);
        mutableAclService.updateAcl(acl);
    }

    @Override
    public void createAllPermission(Object object) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid owner = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        final Sid admin = new GrantedAuthoritySid(AuthorityNames.ROLE_ADMIN.name());

        MutableAcl acl = mutableAclService.createAcl(oid);
        acl.insertAce(acl.getEntries().size(), BasePermission.CREATE, owner, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, owner, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, owner, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, owner, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, admin, true);
        mutableAclService.updateAcl(acl);
    }
}
