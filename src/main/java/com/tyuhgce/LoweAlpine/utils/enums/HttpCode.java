package com.tyuhgce.LoweAlpine.utils.enums;

public enum HttpCode {
    SUCCESS(200, "The place booked successfully"),
    LOCKED(423, "The place is already booked"),
    SERVER_ERROR(500, "The server was crashed. Please try again later");

    private final int code;
    private final String description;

    HttpCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
