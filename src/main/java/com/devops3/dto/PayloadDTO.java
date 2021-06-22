package com.devops3.dto;

public class PayloadDTO extends GenericDTO{

    String data;

    public PayloadDTO(){

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
