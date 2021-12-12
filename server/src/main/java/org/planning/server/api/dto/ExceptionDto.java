package org.planning.server.api.dto;

public class ExceptionDto {

    public String message;

    public static ExceptionDto of(String message) {
        ExceptionDto dto = new ExceptionDto();
        dto.message = message;
        return dto;
    }
}
