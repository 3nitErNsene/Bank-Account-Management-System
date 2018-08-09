package com.bams.dao;

public class DAOFactory {

    public CustomerDAO createCustomerDAO() {
        return new CustomerDAOImpl();
    }
    
    public AccountDAO createAccountDAO() {
        return new AccountDAOImpl();
    }
    
    public AdminStaffDAO createAdminStaffDAO() {
        return new AdminStaffDAOImpl();
    }
    
    public TransactionDAO createTransactionDAO() {
        return new TransactionDAOImpl();
    }
}
