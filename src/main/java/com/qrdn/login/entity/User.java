package com.qrdn.login.entity;

import java.sql.Date;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
// @Table(name = "\"user\"")
@Table(name = "users")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User extends BaseResponse {

    @Id
    @Column(name = "user_name")
    private String user_name;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "organization_code")
    private String organization_code;

    @Column(name = "office_code")
    private String office_code;

    @Column(name = "password_validility_date")
    private String password_validility_date;

    @Column(name = "password_frequency")
    private String password_frequency;

    @Column(name = "group_code")
    private String group_code;

    @Column(name = "first_name_en")
    private String first_name_en;

    @Column(name = "last_name_en")
    private String last_name_en;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "role")
    private int role;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_date")
    private Date updated_date;

    @Column(name = "updated_by")
    private String updated_by;

    @Column(name = "created_date")
    private Date created_date;

    @Column(name = "created_by")
    private String created_by;

    @Transient
    private String password;

    @Transient
    private String type;

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getOrganizationCode() {
        return organization_code;
    }

    public void setOrganizationCode(String organization_code) {
        this.organization_code = organization_code;
    }

    public String getOfficeCode() {
        return office_code;
    }

    public void setOfficeCode(String office_code) {
        this.office_code = office_code;
    }

    public String getPasswordValidilityDate() {
        return password_validility_date;
    }

    public void setPasswordValidilityDate(String password_validility_date) {
        this.password_validility_date = password_validility_date;
    }

    public String getPasswordFrequency() {
        return password_frequency;
    }

    public void setPasswordFrequency(String password_frequency) {
        this.password_frequency = password_frequency;
    }

    public String getGroupCode() {
        return group_code;
    }

    public void setGroupCode(String group_code) {
        this.group_code = group_code;
    }

    public String getFirstNameEn() {
        return first_name_en;
    }

    public void setFirstNameEn(String first_name_en) {
        this.first_name_en = first_name_en;
    }

    public String getLastNameEn() {
        return last_name_en;
    }

    public void setLastNameEn(String last_name_en) {
        this.last_name_en = last_name_en;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdatedDate() {
        return updated_date;
    }

    public void setUpdatedDate(Date updated_date) {
        this.updated_date = updated_date;
    }

    public String getUpdatedBy() {
        return updated_by;
    }

    public void setUpdatedBy(String updated_by) {
        this.updated_by = updated_by;
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
