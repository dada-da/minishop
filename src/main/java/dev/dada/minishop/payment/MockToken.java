package dev.dada.minishop.payment;

public enum MockToken {
    DECLINED("tok_declined"),
    TIMEOUT("tok_timeout"),
    SUCCESS("tok_success");

    private final String value;

    MockToken(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static MockToken fromValue(final String value) {
        for (final MockToken mockToken : MockToken.values()) {
            if (mockToken.getValue().equals(value)) {
                return mockToken;
            }
        }

        return null;
    }
}
