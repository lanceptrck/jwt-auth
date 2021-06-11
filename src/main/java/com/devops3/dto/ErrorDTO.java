package com.devops3.dto;

import com.devops3.exception.ExceptionResponse;

public class ErrorDTO extends GenericDTO {

    private ExceptionResponse error;

    public ErrorDTO(){

    }

    public ExceptionResponse getError() {
        return error;
    }

    public void setError(ExceptionResponse error) {
        this.error = error;
    }
}
