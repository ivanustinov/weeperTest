package ru.ustinov.sapertest.to;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 14.02.2024
 */

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets field
 */
public enum FieldEnum {
    SPACE(" "),

    _0("0"),

    _1("1"),

    _2("2"),

    _3("3"),

    _4("4"),

    _5("5"),

    _6("6"),

    _7("7"),

    _8("8"),

    M("M"),

    X("X");

    private final String value;

    FieldEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}