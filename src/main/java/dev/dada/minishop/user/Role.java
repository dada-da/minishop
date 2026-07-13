package dev.dada.minishop.user;

/**
 * TASK MS-10: Vai tro nguoi dung.
 */
public enum Role {
    CUSTOMER,
    ADMIN;

    public String getAuthorityName() {
        return "ROLE_" + name();
    }
}

