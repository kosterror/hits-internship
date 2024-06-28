package ru.tsu.hits.hitsinternship.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Mark {
    ZERO("0"),
    THREE("3"),
    THREE_M("3-"),
    THREE_P("3+"),
    FOUR("4"),
    FOUR_M("4-"),
    FOUR_P("4+"),
    FIVE("5"),
    FIVE_M("5-"),
    FIVE_P("5+");

    private final String value;

    Mark(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Mark fromValue(String value) {
        for (Mark mark : Mark.values()) {
            if (mark.value.equals(value)) {
                return mark;
            }
        }

        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
