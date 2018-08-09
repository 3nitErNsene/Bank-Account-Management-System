package com.bams.dao;

import com.bams.model.Account;
import com.bams.model.AdminStaff;
import com.bams.model.Customer;
import com.bams.model.Transaction;

public interface AccountDAO {
    
    public void addAc(Account account) throws DAOException;
    
    public void deleteAc(Customer customer, AdminStaff adminstaff) throws DAOException; //Can only delete ac
    
    public boolean chkBalance(Customer customer, int amount) throws DAOException;
    
    public boolean chkStatus(Customer customer) throws DAOException;
    
    public void transactionProcess(Transaction trans) throws DAOException; //deposit or withdraw money
    
    public void transactionProcess(Transaction[] trans) throws DAOException; //deposit or withdraw money

    public Account findByAc(long accountNo) throws DAOException;
    
    public Account[] findAll() throws DAOException;

}
