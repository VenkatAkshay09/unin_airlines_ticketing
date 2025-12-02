package com.unin.airlines.user_service.entity;

import lombok.Builder;

public enum CountryCodes {

    IND,
    AUS,
    UK,
    SG,
    USA;

    public String getDialCode() {
        return switch (this) {
            case IND -> "+91";
            case USA -> "+1";
            case UK -> "+44";
            case SG -> "+65";
            case AUS -> "+61";
        };
    }
}
