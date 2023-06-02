package com.qrdn.login.entity;

import java.sql.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "credentials")
public class Credentials{

    @Id
    @Column(name = "user_name")
    private String user_name;

    @Column(name = "password")
    private String password;

    @Column(name = "type")
    private String type;

    @Column(name = "created_date")
    private Date created_date;

    @Column(name = "created_by")
    private String created_by;

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(Date created_date) {
        this.created_date = created_date;
    }

    public String getCreatedBy() {
        return created_by;
    }

    public void setCreatedBy(String created_by) {
        this.created_by = created_by;
    }
}
