package com.qrdn.login.controller;

import java.util.UUID;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;

@RestControllerAdvice
public class RestAdvice implements ResponseBodyAdvice<Object> {

    /**
     * @param converterType
     * @return boolean
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        return true;
    }

    /**
     * beforeBodyWrite method is invoked before the response body is written. It
     * modifies the response by encapsulating it in a ResponseWrapper object and
     * adds additional fields to the response wrapper.
     * 
     * @param selectedConverterType
     * @param request
     * @param response
     * @return Object
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {

        ResponseWrapper<Object> modifiedResponse = new ResponseWrapper<>();
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        modifiedResponse.setResponse(body);
        modifiedResponse.setRequestTimeStamp(servletRequest.getAttribute("request_time_stamp").toString());
        modifiedResponse.setRequestId(servletRequest.getAttribute("request_id").toString());
        modifiedResponse.setResponseTimeStamp(String.valueOf(new java.util.Date()));
        modifiedResponse.setResponseId(UUID.randomUUID().toString());
        modifiedResponse.setClientIp(servletRequest.getAttribute("client_ip").toString());

        return modifiedResponse;
    }
}
