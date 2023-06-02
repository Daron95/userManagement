package com.qrdn.login.controller;

public class ResponseWrapper<T> {

    private T response;
    private String request_time_stamp;
    private String request_id;
    private String response_time_stamp;
    private String response_id;
    private String clientIp;

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public String getRequestTimeStamp() {
        return request_time_stamp;
    }

    public void setRequestTimeStamp(String request_time_stamp) {
        this.request_time_stamp = request_time_stamp;
    }

    public String getRequestId() {
        return request_id;
    }

    public void setRequestId(String request_id) {
        this.request_id = request_id;
    }

    public String getResponseTimeStamp() {
        return response_time_stamp;
    }

    public void setResponseTimeStamp(String response_time_stamp) {
        this.response_time_stamp = response_time_stamp;
    }

    public String getResponseId() {
        return response_id;
    }

    public void setResponseId(String response_id) {
        this.response_id = response_id;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}