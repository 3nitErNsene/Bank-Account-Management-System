package com.bams.main.user;

import com.bams.dao.AdminStaffDAO;
import com.bams.dao.DAOException;
import com.bams.model.AdminStaff;

public class StaffLogin extends Login{
    
    AdminStaffDAO adminstaffdao;
    
    public StaffLogin(String loginname, String password) {
        super(loginname, password);
    }
    
    public StaffLogin(String loginname, String password, AdminStaffDAO adminstaffdao) {
        super(loginname, password);
        this.adminstaffdao = adminstaffdao;
    }
    
    public AdminStaff chkLogin() throws DAOException {
        
        AdminStaff loginstaff = adminstaffdao.login(super.getLoginname(), super.userPassword());
        
        return loginstaff;
    }
    
    public String regPwd(){
        return super.userPassword();
    }
    
}
