package com.devops3.payload.request;

import com.devops3.exception.TokenRefreshException;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;

    public TokenRefreshRequest(){

    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
