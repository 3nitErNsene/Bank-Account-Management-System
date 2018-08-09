package com.bams.dao;

import com.bams.main.BankAccountManagementSystem;
import com.bams.model.Customer;
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

public class CustomerDAOImpl implements CustomerDAO {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public CustomerDAOImpl() {
    }

    @Override
    public void add(Customer cust) throws DAOException {
        String filename = "./flat_database/Customer.txt";

        String delimiter = "";
        for (int delim = 1; delim < 7; delim++) {
            delimiter += Character.toString((char) 32);
        }

        String addRecord = cust.getHkid() + delimiter + String.valueOf(cust.getAccountNo()) + delimiter + cust.getLoginName() + delimiter + cust.getPassword()
                + delimiter + cust.getGender() + delimiter + cust.getAddress() + delimiter + cust.getLastname() + delimiter + cust.getFirstname()
                + delimiter + sdf.format(cust.getDob()) + delimiter + cust.getPhone() + delimiter + cust.getEmail();
        //System.out.println(addRecord);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(addRecord);
            writer.write("\n");
            writer.flush();
            writer.close();
        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

//        } catch (ParseException ex) {
//            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void update(Customer cust) throws DAOException {
        ArrayList<Customer> newCust = new ArrayList<>();
        Customer[] allcust = findAll();

        for (Customer saveCust : allcust) {
            if (saveCust.getHkid().equals(cust.getHkid())) {
                newCust.add(cust);
            } else {
                newCust.add(saveCust);
            }
        }

        try {
            String line;
            String filename = "./flat_database/Customer.txt";

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(line);
            writer.newLine();

            String delimiter = "";
            for (int delim = 1; delim < 7; delim++) {
                delimiter += Character.toString((char) 32);
            }

            for (Customer theCust : newCust) {
                String addRecord = theCust.getHkid() + delimiter + String.valueOf(theCust.getAccountNo()) + delimiter + theCust.getLoginName() + delimiter + theCust.getPassword()
                        + delimiter + theCust.getGender() + delimiter + theCust.getAddress() + delimiter + theCust.getLastname() + delimiter + theCust.getFirstname()
                        + delimiter + sdf.format(theCust.getDob()) + delimiter + theCust.getPhone() + delimiter + theCust.getEmail();
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
    public void deleteCust(Customer customer) throws DAOException {
        ArrayList<Customer> newCust = new ArrayList<>();
        Customer[] allcust = findAll();

        for (Customer saveCust : allcust) {
            if (!saveCust.getHkid().equals(customer.getHkid())) {
                newCust.add(saveCust);
            }
        }

        try {
            String line;
            String filename = "./flat_database/Customer.txt";

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(line);
            writer.newLine();

            String delimiter = "";
            for (int delim = 1; delim < 7; delim++) {
                delimiter += Character.toString((char) 32);
            }

            for (Customer theCust : newCust) {
                String addRecord = theCust.getHkid() + delimiter + String.valueOf(theCust.getAccountNo()) + delimiter + theCust.getLoginName() + delimiter + theCust.getPassword()
                        + delimiter + theCust.getGender() + delimiter + theCust.getAddress() + delimiter + theCust.getLastname() + delimiter + theCust.getFirstname()
                        + delimiter + sdf.format(theCust.getDob()) + delimiter + theCust.getPhone() + delimiter + theCust.getEmail();
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
    public Customer login(String loginName, String loginPwd) throws DAOException {
        Customer[] getallcust = findAll();
        for (Customer theCust : getallcust) {
            if (theCust.getLoginName().equals(loginName) && theCust.getPassword().equals(loginPwd)) {
                return theCust;
            }
        }
        return null;
    }

    @Override
    public Customer findById(String id) throws DAOException {
        Customer[] getallcust = findAll();
        for (Customer theCust : getallcust) {
            if (theCust.getHkid().equals(id)) {
                return theCust;
            }
        }
        return null;
    }

    @Override
    public Customer[] findAll() throws DAOException {
        String filename = "./flat_database/Customer.txt";
        String line;

        String customercol[] = new String[]{"hkid", "accountNo", "loginName", "password", "gender", "address", "lastname", "firstname", "dob", "phone", "email"};

        LinkedHashMap<String, String> mapCust = new LinkedHashMap<String, String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String delimiter = "\\s{6}";
            ArrayList<Customer> cust = new ArrayList<>();
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(delimiter);
                for (int i = 0; i < record.length; i++) {
                    mapCust.put(customercol[i], record[i]);
                }

                try {
                    Customer customer = new Customer(mapCust.get("hkid"), Long.parseLong(mapCust.get("accountNo")), mapCust.get("loginName"),
                            mapCust.get("password"), mapCust.get("gender"), mapCust.get("address"), mapCust.get("lastname"), mapCust.get("firstname"),
                            sdf.parse(mapCust.get("dob")), mapCust.get("phone"), mapCust.get("email"));

                    cust.add(customer);

                } catch (ParseException ex) {
                    Logger.getLogger(BankAccountManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            reader.close();
            return cust.toArray(new Customer[0]);

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return null;
    }

}
