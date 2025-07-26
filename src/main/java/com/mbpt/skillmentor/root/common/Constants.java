package com.mbpt.skillmentor.root.common;

public class Constants {
    public static final String APPLICATION_JSON = "application/json";

    public static final String ADMIN_ROLE_PERMISSION = "hasRole('ADMIN')";
    public static final String MODERATOR_ROLE_PERMISSION = "hasRole('MODERATOR')";

    public enum SessionStatus {
        PENDING,
        ACCEPTED,
        COMPLETED
    }
}
