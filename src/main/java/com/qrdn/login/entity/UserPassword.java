package com.qrdn.login.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPassword {

    private String user_name;

    private String old_password;

    private String new_password;

    private String confirm_new_password;

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getOldPassword() {
        return old_password;
    }

    public void setOldPassword(String old_password) {
        this.old_password = old_password;
    }

    public String getNewPassword() {
        return new_password;
    }

    public void setNewPassword(String new_password) {
        this.new_password = new_password;
    }

    public String getConfirmNewPassword() {
        return confirm_new_password;
    }

    public void setConfirmNewPassword(String confirm_new_password) {
        this.confirm_new_password = confirm_new_password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user_name == null) ? 0 : user_name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserPassword other = (UserPassword) obj;
        if (user_name == null) {
            if (other.user_name != null)
                return false;
        } else if (!user_name.equals(other.user_name))
            return false;
        return true;
    }

}
