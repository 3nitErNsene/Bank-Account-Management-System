package com.bams.main.user;

import com.bams.dao.CustomerDAO;
import com.bams.dao.DAOException;
import com.bams.model.Customer;

public class CustomerLogin extends Login{
    
    CustomerDAO customerdao;
    
    public CustomerLogin(String loginname, String password) {
        super(loginname, password);
    }
    
    public CustomerLogin(String loginname, String password, CustomerDAO customerdao) {
        super(loginname, password);
        this.customerdao = customerdao;
    }
    
    public Customer chkLogin() throws DAOException {
        
        Customer logincust = customerdao.login(super.getLoginname(), super.userPassword());
        
        return logincust;
        
    }
    
    public String regPwd(){
        return super.userPassword();
    }
}
