package io.codepool.springchallenge.common.enums;

/**
 * The Authority enum
 * Helps define the authorities in the system.
 * Ideally, the authorities, and the associated permissions would be kept somewhere in the db/cache
 * But for the sake of time and requirements we will be implementing them this way.
 */
public enum AuthorityEnum {

    BUYER("BUYER"),
    SELLER("SELLER");

    private final String value;

    AuthorityEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
