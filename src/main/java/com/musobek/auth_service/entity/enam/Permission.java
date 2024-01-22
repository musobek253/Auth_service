package com.musobek.auth_service.entity.enam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:deleted"),

    MANGER_READ("manger:read"),
    MANGER_CREATE("manger:create"),
    MANGER_UPDATE("manger:update"),
    MANGER_DELETE("manger:delete"),
    USER_READ("user:read"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete");

    private final String permission;
}
