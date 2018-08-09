package com.bams.model;

import java.util.Date;

public class Customer {

    private String hkid;
    private long accountNo;
    private String loginName;
    private String password;
    private String gender;
    private String address;
    private String lastname;
    private String firstname;
    private Date dob;
    private String phone;
    private String email;

    public Customer(String hkid, long accountNo, String loginName, String password, String gender, String address, String lastname, String firstname, Date dob, String phone, String email) {
        this.hkid = hkid;
        this.accountNo = accountNo;
        this.loginName = loginName;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.lastname = lastname;
        this.firstname = firstname;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }

    public String getHkid() {
        return hkid;
    }

    public long getAccountNo() {
        return accountNo;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public Date getDob() {
        return dob;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Customer{" + "hkid=" + hkid + ", accountNo=" + accountNo + ", loginName=" + loginName + ", password=" + password + ", gender=" + gender + ", address=" + address + ", lastname=" + lastname + ", firstname=" + firstname + ", dob=" + dob + ", phone=" + phone + ", email=" + email + '}';
    }

}
