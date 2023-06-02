package com.qrdn.login.entity;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BaseResponse extends BaseEntity {

    @JsonDeserialize(as = ArrayList.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> errorCode = new ArrayList<>();

    @JsonDeserialize(as = ArrayList.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> errorDescription = new ArrayList<>();

    public List<String> getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String error) {
        this.errorCode.add(error);
    }

    public List<String> getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String description) {
        this.errorDescription.add(description);
    }
}
