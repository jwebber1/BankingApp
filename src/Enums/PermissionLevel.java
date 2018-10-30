package Enums;

public enum PermissionLevel {
    CUSTOMER("Customer"),
    TELLER("Teller"),
    MANAGER("Manager");

    private final String name;

    PermissionLevel(String s) {
        name = s;
    }
}
