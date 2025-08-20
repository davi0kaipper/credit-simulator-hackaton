package org.acme.domain.payloads.errors;

import java.time.LocalDateTime;
import java.util.ArrayList;

public record ErrorResponse(
    String error,
    String code,
    ArrayList<String> errorMessages,
    String path,
    String timestamp
) { 
    public static ErrorResponse from(
        String error,
        String code,
        ArrayList<String> errorMessages,
        String path
    ){
        var timestamp = LocalDateTime.now().toString();
        return new ErrorResponse(error, code, errorMessages, path, timestamp);
    }
}
