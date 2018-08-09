package com.bams.main.user;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login {

    /*
    1. hash password
    2. welcome user
     */
    private String hashpassword;
    private String loginname;

    public Login(String loginname, String password) {
        this.hashpassword = password;
        this.loginname = loginname;
    }

    public String userPassword() {
        return encode(hashpassword);
    }

    public String getLoginname() {
        return loginname;
    }

    private static String encode(String str) {
        MessageDigest md = null;
        String enpwd = null;
        str += "LoGiN";
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(str.getBytes());
            enpwd = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return enpwd;
    }

}
