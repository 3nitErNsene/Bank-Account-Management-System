package com.bams.dao;

import com.bams.main.BankAccountManagementSystem;
import com.bams.model.AdminStaff;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminStaffDAOImpl implements AdminStaffDAO{
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public AdminStaff login(String loginName, String loginPwd) throws DAOException {
        AdminStaff[] getallstaff = findAll();
        
        for (AdminStaff theStaff : getallstaff) {
            if (theStaff.getLoginName().equals(loginName) && theStaff.getPassword().equals(loginPwd)) {
                
                return theStaff;
            }
        }
        return null;
    }

    @Override
    public void add(AdminStaff staff) throws DAOException {
        String filename = "./flat_database/AdminStaff.txt";

        String delimiter = "";
        for (int delim = 1; delim < 7; delim++) {
            delimiter += Character.toString((char) 32);
        }

        String addRecord = staff.getStaffId() + delimiter + staff.getLoginName() + delimiter + staff.getName() + delimiter + staff.getPassword()
                + delimiter + sdf.format(staff.getLastLogin()) + delimiter + staff.getRole();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(addRecord);
            writer.write("\n");
            writer.flush();
            writer.close();
        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }
    }

    @Override
    public void update(AdminStaff staff) throws DAOException {
        ArrayList<AdminStaff> newStaff = new ArrayList<>();
        AdminStaff[] allstaff = findAll();

        for (AdminStaff saveStaff : allstaff) {
            if ( saveStaff.getStaffId().equals(staff.getStaffId()) ){
                newStaff.add(staff);
            } else {
                newStaff.add(saveStaff);
            }
        }
        
        try {
            String line;
            String filename = "./flat_database/AdminStaff.txt";

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(line);
            writer.newLine();

            String delimiter = "";
            for (int delim = 1; delim < 7; delim++) {
                delimiter += Character.toString((char) 32);
            }

            for (AdminStaff theStaff : newStaff) {
                String addRecord = theStaff.getStaffId() + delimiter + theStaff.getLoginName() + delimiter + theStaff.getName() + delimiter + theStaff.getPassword()
                + delimiter + sdf.format(theStaff.getLastLogin()) + delimiter + theStaff.getRole();
                writer.write(addRecord);
                writer.write("\n");
            }

            writer.flush();
            writer.close();
        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }
    }

    @Override
    public void delete(AdminStaff staff) throws DAOException {
        ArrayList<AdminStaff> newStaff = new ArrayList<>();
        AdminStaff[] allstaff = findAll();

        for (AdminStaff saveStaff : allstaff) {
            if ( !saveStaff.getStaffId().equals(staff.getStaffId()) ){
                newStaff.add(saveStaff);
            }
        }
        
        try {
            String line;
            String filename = "./flat_database/AdminStaff.txt";

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(line);
            writer.newLine();

            String delimiter = "";
            for (int delim = 1; delim < 7; delim++) {
                delimiter += Character.toString((char) 32);
            }

            for (AdminStaff theStaff : newStaff) {
                String addRecord = theStaff.getStaffId() + delimiter + theStaff.getLoginName() + delimiter + theStaff.getName() + delimiter + theStaff.getPassword()
                + delimiter + sdf.format(theStaff.getLastLogin()) + delimiter + theStaff.getRole();
                writer.write(addRecord);
                writer.write("\n");
            }

            writer.flush();
            writer.close();
        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }
    }
    
    @Override
    public AdminStaff findById(String staffId) throws DAOException {
        AdminStaff[] getallstaff = findAll();
        for (AdminStaff theStaff : getallstaff) {
            if (theStaff.getStaffId().equals(staffId)) {
                return theStaff;
            }
        }
        return null;
    }

    @Override
    public AdminStaff[] findAll() throws DAOException {
        String filename = "./flat_database/AdminStaff.txt";
        String line;

        String adminstaffcol[] = new String[]{"staffId", "loginName", "name", "password", "lastLogin", "role"};

        LinkedHashMap<String, String> mapAdminStaff = new LinkedHashMap<String, String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String delimiter = "\\s{6}";
            ArrayList<AdminStaff> as = new ArrayList<>();
            
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(delimiter);
                for (int i = 0; i < record.length; i++) {
                    mapAdminStaff.put(adminstaffcol[i], record[i]);
                }

                try {
                    AdminStaff adminstaff = new AdminStaff(mapAdminStaff.get("staffId"), mapAdminStaff.get("loginName"),
                             mapAdminStaff.get("name"), mapAdminStaff.get("password"), sdf.parse(mapAdminStaff.get("lastLogin")), mapAdminStaff.get("role"));

                    as.add(adminstaff);

                } catch (ParseException ex) {
                    Logger.getLogger(BankAccountManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            reader.close();
            return as.toArray(new AdminStaff[0]);

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return null;
    }

}
