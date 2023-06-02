package com.qrdn.login.service;

import org.springframework.stereotype.Service;

@Service
public class Endpoints {

    String serviceName;
    String methodName;
    String url;
    String address = "localhost";
    // String address = "172.22.32.1";
    // String address = "172.20.32.20";
    String port = "8090";

    public Endpoints() {
    }

    public Endpoints(String serviceName, String methodName) {
        this.serviceName = serviceName;
        this.methodName = methodName;
    }

    public String getUrl() {
        if (serviceName.equals("Credentials") && methodName.equals("createCredentials")) {
            url = "http://" + address + ":" + port + "/createCredentials";
        } else if (serviceName.equals("Credentials") && methodName.equals("changePassword")) {
            url = "http://" + address + ":" + port + "/changePassword";
        }
        return url;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

}
