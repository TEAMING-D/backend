package com.allin.teaming.Dto.Response;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ErrorResponse extends BasicResponse {
    private int errorCode;

    public ErrorResponse() {
        setSuccess(false);
    }
    public ErrorResponse(String errorMessage) {
        this();
        setMessage(errorMessage);
        this.errorCode = 404;
    }
    public ErrorResponse(String errorMessage, int errorCode) {
        this(errorMessage);
        this.errorCode = errorCode;
    }
}
