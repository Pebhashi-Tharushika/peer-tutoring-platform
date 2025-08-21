package com.mbpt.peertutoringplatform.common;

public class Constants {

    public static final String ADMIN_ROLE_PERMISSION = "hasRole('ADMIN')";
    public static final String STUDENT_ROLE_PERMISSION = "hasRole('STUDENT')";
    public static final String ADMIN_OR_STUDENT_PERMISSION = "hasAnyRole('ADMIN', 'STUDENT')";

    public enum SessionStatus {
        PENDING,
        ACCEPTED,
        COMPLETED
    }

    public enum Title {
        MR("Mr."),
        MRS("Mrs."),
        MISS("Miss."),
        MS("Ms."),
        DR("Dr."),
        PROF("Prof.");

        private final String displayName;

        Title(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
