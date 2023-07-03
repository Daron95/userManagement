package com.qrdn.login.controller;

import java.util.UUID;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class BaseController {

    /**
     * The fields that will be added to each request : request_time_stamp,
     * request_id, client_ip
     * 
     * @param request
     */
    @ModelAttribute
    public void addCommonAttributes(HttpServletRequest request) {

        request.setAttribute("request_time_stamp", new java.util.Date());
        request.setAttribute("request_id", UUID.randomUUID().toString());
        request.setAttribute("client_ip", request.getRemoteAddr());
    }
}