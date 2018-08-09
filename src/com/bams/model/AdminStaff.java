package com.bams.model;

import java.util.Date;

public class AdminStaff {
    
    private String staffId;
    private String loginName;
    private String name;
    private String password;
    private Date lastLogin;
    private String role;

    public AdminStaff(String staffId, String loginName, String name, String password, Date lastLogin, String role) {
        this.staffId = staffId;
        this.loginName = loginName;
        this.name = name;
        this.password = password;
        this.lastLogin = lastLogin;
        this.role = role;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public String getRole() {
        return role;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    @Override
    public String toString() {
        return "AdminStaff{" + "staffId=" + staffId + ", loginName=" + loginName + ", name=" + name + ", password=" + password + ", lastLogin=" + lastLogin + ", role=" + role + '}';
    }
    
}
