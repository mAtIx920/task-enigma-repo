package org.rest.restapp.controllers.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String message;
    private int status;
    private String timestamp;
    private ErrorType errorType;

}

@Getter
enum ErrorType {
    RESOURCE_ALREADY_EXISTS(409, "Resource already exists"),
    RESOURCE_NOT_FOUND(404, "Resource not found"),
    BAD_REQUEST(400, "Bad request");

    private final int status;
    private final String message;

    ErrorType(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
