package com.bams.dao;

import com.bams.main.BankAccountManagementSystem;
import com.bams.model.Account;
import com.bams.model.AdminStaff;
import com.bams.model.Customer;
import com.bams.model.Transaction;
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

public class AccountDAOImpl implements AccountDAO {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public AccountDAOImpl() {
    }

    @Override
    public void addAc(Account account) throws DAOException {
        String filename = "./flat_database/Account.txt";

        String delimiter = "";
        for (int delim = 1; delim < 7; delim++) {
            delimiter += Character.toString((char) 32);
        }

        String addRecord = String.valueOf(account.getAccountNo()) + delimiter + String.valueOf(account.getBalance())
                + delimiter + account.getStatus() + delimiter + sdf.format(account.getActiveDate()) + delimiter + account.getBlockBy();
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

    }

    @Override
    public void deleteAc(Customer customer, AdminStaff adminstaff) throws DAOException {
        ArrayList<Account> newAc = new ArrayList<>();
        Account[] allac = findAll();

        for (Account saveAc : allac) {
            if (saveAc.getAccountNo()==(customer.getAccountNo())) {
                saveAc.setStatus("block");
                saveAc.setBlockBy(adminstaff.getName());
                newAc.add(saveAc);
            } else {
                newAc.add(saveAc);
            }
        }
        
        try {
            String line;
            String filename = "./flat_database/Account.txt";

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(line);
            writer.newLine();

            String delimiter = "";
            for (int delim = 1; delim < 7; delim++) {
                delimiter += Character.toString((char) 32);
            }

            for (Account theAc : newAc) {
                String addRecord = String.valueOf(theAc.getAccountNo()) + delimiter + String.valueOf(theAc.getBalance())
                + delimiter + theAc.getStatus() + delimiter + sdf.format(theAc.getActiveDate()) + delimiter + theAc.getBlockBy();
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
    public boolean chkBalance(Customer customer, int amount) throws DAOException {
        Account account = findByAc(customer.getAccountNo());
        if (account.getBalance()<amount){
            if(account.getBalance()==amount){
                return true;
            }else{
                return false;
            }           
        }else{
            return true;
        }
    }

    @Override
    public boolean chkStatus(Customer customer) throws DAOException{
        String status = findByAc(customer.getAccountNo()).getStatus();
        if (status.equals("active")){
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public void transactionProcess(Transaction trans) throws DAOException {
        int newBalance = 0;
        ArrayList<Account> newAccount = new ArrayList<>();
        Account[] allac = findAll();

        for (Account saveAc : allac) {
            if (saveAc.getAccountNo() == trans.getAccount()) {
                if (trans.getType().equals("Credit")) {
                    newBalance = saveAc.getBalance() - trans.getAmount();
                } else if (trans.getType().equals("Debit")) {
                    newBalance = saveAc.getBalance() + trans.getAmount();
                }

                saveAc.setBalance(newBalance);
                newAccount.add(saveAc);

            } else {
                newAccount.add(saveAc);
            }
        }

        try {
            String line;
            String filename = "./flat_database/Account.txt";

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(line);
            writer.newLine();

            String delimiter = "";
            for (int delim = 1; delim < 7; delim++) {
                delimiter += Character.toString((char) 32);
            }

            for (Account theAc : newAccount) {
                String addRecord = String.valueOf(theAc.getAccountNo()) + delimiter + String.valueOf(theAc.getBalance())
                        + delimiter + theAc.getStatus() + delimiter + sdf.format(theAc.getActiveDate()) + delimiter + theAc.getBlockBy();

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
    public void transactionProcess(Transaction[] trans) throws DAOException { //transfer
        int newBalance = 0;
        ArrayList<Account> newAccount = new ArrayList<>();
        Account[] allac = findAll();

        for (Account saveAc : allac) {
            boolean match = false;
            for (Transaction transTransfer : trans) {
                if (saveAc.getAccountNo() == transTransfer.getAccount()) {
                    match = true;
                    if (transTransfer.getType().equals("Credit")) {
                        newBalance = saveAc.getBalance() - transTransfer.getAmount();
                    } else if (transTransfer.getType().equals("Debit")) {
                        newBalance = saveAc.getBalance() + transTransfer.getAmount();
                    }
                    saveAc.setBalance(newBalance);
                    newAccount.add(saveAc);
                }
            }
            if (!match) {
                newAccount.add(saveAc);
            }
        }

        try {
            String line;
            String filename = "./flat_database/Account.txt";

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(line);
            writer.newLine();

            String delimiter = "";
            for (int delim = 1; delim < 7; delim++) {
                delimiter += Character.toString((char) 32);
            }

            for (Account theAc : newAccount) {
                String addRecord = String.valueOf(theAc.getAccountNo()) + delimiter + String.valueOf(theAc.getBalance())
                        + delimiter + theAc.getStatus() + delimiter + sdf.format(theAc.getActiveDate()) + delimiter + theAc.getBlockBy();

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
    public Account findByAc(long accountNo) throws DAOException {
        Account[] getallac = findAll();
        for (Account theAc : getallac) {
            if (theAc.getAccountNo() == accountNo) {
                return theAc;
            }
        }
        return null;
    }

    @Override
    public Account[] findAll() throws DAOException {
        String filename = "./flat_database/Account.txt";
        String line;

        String accountcol[] = new String[]{"accountNo", "balance", "status", "activeDate", "blockBy"};

        LinkedHashMap<String, String> mapAccount = new LinkedHashMap<String, String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String delimiter = "\\s{6}";
            ArrayList<Account> ac = new ArrayList<>();

            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(delimiter);
                for (int i = 0; i < record.length; i++) {
                    mapAccount.put(accountcol[i], record[i]);
                }

                try {
                    Account account = new Account(Long.parseLong(mapAccount.get("accountNo")), Integer.parseInt(mapAccount.get("balance")),
                            mapAccount.get("status"), sdf.parse(mapAccount.get("activeDate")), mapAccount.get("blockBy"));

                    ac.add(account);

                } catch (ParseException ex) {
                    Logger.getLogger(BankAccountManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            reader.close();
            
            return ac.toArray(new Account[0]);

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return null;
    }

}
