package org.planning.server.api.v1.dto;

public class ExceptionDto {

    public String message;

    public static ExceptionDto of(String message) {
        ExceptionDto dto = new ExceptionDto();
        dto.message = message;
        return dto;
    }
}
