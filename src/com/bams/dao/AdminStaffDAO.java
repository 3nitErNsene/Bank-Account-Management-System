package com.bams.dao;

import com.bams.model.AdminStaff;

public interface AdminStaffDAO{
    
    public AdminStaff login(String loginName, String loginPwd) throws DAOException;
    
    public void add(AdminStaff staff) throws DAOException;

    public void update(AdminStaff staff) throws DAOException;
    
    public void delete(AdminStaff staff) throws DAOException;
    
    public AdminStaff findById(String staffId) throws DAOException;
    
    public AdminStaff[] findAll() throws DAOException;
}
