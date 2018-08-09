package com.bams.dao;

import com.bams.model.Customer;

public interface CustomerDAO {
    
    public void add(Customer cust) throws DAOException;
    
    public void update(Customer cust) throws DAOException;
    
    public void deleteCust(Customer cust) throws DAOException; //Whem delete Cust must deleteAc at the same time

    public Customer findById(String id) throws DAOException;
    
    public Customer[] findAll() throws DAOException;
    
    public Customer login(String loginName, String loginPwd) throws DAOException;
    
}
