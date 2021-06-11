package com.devops3.dto;

import com.devops3.exception.ExceptionResponse;

public class GenericDTO {

    private Status status;
    private Integer responseCode;

    public GenericDTO(){

    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }
}
