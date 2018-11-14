package Enums;

public enum AccountType {
    SAVING("Savings"),
    LOAN("Loan"),
    CHECKING("Checking"),
    CD("CD");

    public final String name;

    AccountType(String s) {
        name = s;
    }
}
