package com.bams.dao;

import com.bams.model.Transaction;
import com.bams.model.Transfer;

public interface TransactionDAO {

    public Transaction[] addTransfer(Transfer transfer) throws DAOException;

    public void updateTransfer(Transfer transfer) throws DAOException;
    
    public Transfer findByIdTransfer(String transferid) throws DAOException;
    
    public Transfer[] findAllTransfer() throws DAOException;
    
    public int countAllTransfer() throws DAOException;
    
    public Transaction add(Transaction trans) throws DAOException;
    
    public void add(String trans) throws DAOException;

    public void delete(Transaction trans) throws DAOException;

    public Transaction findById(int tranID) throws DAOException;
    
    public Transaction[] findByAc(long account) throws DAOException;
    
    public Transaction[] findAll() throws DAOException;
    
    public Transaction[] findTransfer(String transferid) throws DAOException;
    
    public int countAllTransaction() throws DAOException;
    
}
