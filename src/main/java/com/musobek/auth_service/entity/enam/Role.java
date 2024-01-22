package com.musobek.auth_service.entity.enam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.musobek.auth_service.entity.enam.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                   ADMIN_CREATE,
                   ADMIN_READ,
                   ADMIN_DELETE,
                   ADMIN_UPDATE,
                   MANGER_CREATE,
                   MANGER_READ
            )
    ),
    USER(Set.of(
            USER_READ,
            USER_CREATE,
            USER_DELETE,
            USER_UPDATE
    )),
    MENGER(
            Set.of(
                    MANGER_READ,
                    MANGER_CREATE,
                    MANGER_DELETE,
                    MANGER_UPDATE
            )
    );
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> grantedAuthorities(){
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name())).collect(Collectors.toCollection(ArrayList::new));

        authorities.add(new SimpleGrantedAuthority("ROLE_"+ this.name()));
        return authorities;
    }

}
