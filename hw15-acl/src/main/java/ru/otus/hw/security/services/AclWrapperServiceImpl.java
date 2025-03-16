package ru.otus.hw.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
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
}
