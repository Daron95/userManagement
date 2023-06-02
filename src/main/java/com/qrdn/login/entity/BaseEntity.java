package com.qrdn.login.entity;

public class BaseEntity {

    private String response_time_stamp;

    private String request_id;

    private String client_ip;

    public String getResponseTimeStamp() {
        return response_time_stamp;
    }

    public void setResponseTimeStamp(String response_time_stamp) {
        this.response_time_stamp = response_time_stamp;
    }

    public String getRequestId() {
        return request_id;
    }

    public void setRequestId(String request_id) {
        this.request_id = request_id;
    }

    public String getClientIp() {
        return client_ip;
    }

    public void setClientIp(String client_ip) {
        this.client_ip = client_ip;
    }

}
