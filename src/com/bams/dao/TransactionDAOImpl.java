package com.bams.dao;

import com.bams.main.BankAccountManagementSystem;
import com.bams.model.Transaction;
import com.bams.model.Transfer;
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

public class TransactionDAOImpl implements TransactionDAO {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public Transaction[] addTransfer(Transfer transfer) throws DAOException {// add transfer record add transaction record
        String filename = "./flat_database/Transfer.txt";

        String delimiter = "";
        for (int delim = 1; delim < 7; delim++) {
            delimiter += Character.toString((char) 32);
        }

        String addRecord = transfer.getTransferId() + delimiter + String.valueOf(transfer.getFromAc()) + delimiter + String.valueOf(transfer.getToAc()) + delimiter
                + String.valueOf(transfer.getAmount()) + delimiter + sdf.format(transfer.getDateTime());

        //System.out.println(addRecord);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(addRecord);
            writer.write("\n");
            writer.flush();
            writer.close();

            String addTransactionCredit = String.valueOf(countAllTransaction() + 1) + delimiter + String.valueOf(transfer.getFromAc()) + delimiter + "Credit"
                    + delimiter + String.valueOf(transfer.getAmount()) + delimiter + sdf.format(transfer.getDateTime()) + delimiter + transfer.getTransferId();
            add(addTransactionCredit);

            String addTransactionDebit = String.valueOf(countAllTransaction() + 1) + delimiter + String.valueOf(transfer.getToAc()) + delimiter + "Debit"
                    + delimiter + String.valueOf(transfer.getAmount()) + delimiter + sdf.format(transfer.getDateTime()) + delimiter + transfer.getTransferId();
            add(addTransactionDebit);

            return findTransfer(transfer.getTransferId());

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return null;
    }

    @Override
    public void updateTransfer(Transfer transfer) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Transfer findByIdTransfer(String transferid) throws DAOException {
        Transfer[] getalltransfer = findAllTransfer();
        for (Transfer transfer : getalltransfer) {
            if (transfer.getTransferId().equals(transferid)) {
                return transfer;
            }
        }
        return null;
    }

    @Override
    public Transfer[] findAllTransfer() throws DAOException {
        String filename = "./flat_database/Transfer.txt";
        String line;

        String transfercol[] = new String[]{"transferId", "fromAc", "toAc", "amount", "dateTime"};

        LinkedHashMap<String, String> mapTransfer = new LinkedHashMap<String, String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String delimiter = "\\s{6}";
            ArrayList<Transfer> transfermoney = new ArrayList<>();

            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(delimiter);
                for (int i = 0; i < record.length; i++) {
                    mapTransfer.put(transfercol[i], record[i]);
                }

                try {
                    Transfer transfer = new Transfer(mapTransfer.get("tranId"), Long.parseLong(mapTransfer.get("fromAc")), Long.parseLong(mapTransfer.get("toAc")),
                            Integer.parseInt(mapTransfer.get("amount")), sdf.parse(mapTransfer.get("dateTime")));

                    transfermoney.add(transfer);

                } catch (ParseException ex) {
                    Logger.getLogger(BankAccountManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            reader.close();
            return transfermoney.toArray(new Transfer[0]);

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return null;
    }

    @Override
    public Transaction add(Transaction trans) throws DAOException {
        String filename = "./flat_database/Transaction.txt";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String delimiter = "";
        for (int delim = 1; delim < 7; delim++) {
            delimiter += Character.toString((char) 32);
        }

        String addRecord = trans.getTranId() + delimiter + String.valueOf(trans.getAccount()) + delimiter + trans.getType() + delimiter + String.valueOf(trans.getAmount())
                + delimiter + sdf.format(trans.getDateTime()) + delimiter + trans.getRemark();
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

        return trans;
    }

    @Override
    public void add(String trans) throws DAOException {
        String filename = "./flat_database/Transaction.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(trans);
            writer.write("\n");
            writer.flush();
            writer.close();
        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }
    }

    @Override
    public void delete(Transaction trans) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Transaction findById(int tranID) throws DAOException {
        Transaction[] getalltransaction = findAll();
        for (Transaction theTrans : getalltransaction) {
            if (theTrans.getTranId() == tranID) {
                return theTrans;
            }
        }
        return null;
    }

    @Override
    public Transaction[] findAll() throws DAOException {
        String filename = "./flat_database/Transaction.txt";
        String line;

        String transactioncol[] = new String[]{"tranId", "account", "type", "amount", "dateTime", "remark"};

        LinkedHashMap<String, String> mapTransaction = new LinkedHashMap<String, String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String delimiter = "\\s{6}";
            ArrayList<Transaction> trans = new ArrayList<>();

            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(delimiter);
                for (int i = 0; i < record.length; i++) {
                    mapTransaction.put(transactioncol[i], record[i]);
                }

                try {
                    Transaction transaction = new Transaction(Integer.parseInt(mapTransaction.get("tranId")), Long.parseLong(mapTransaction.get("account")), mapTransaction.get("type"),
                            Integer.parseInt(mapTransaction.get("amount")), sdf.parse(mapTransaction.get("dateTime")), mapTransaction.get("remark"));

                    trans.add(transaction);

                } catch (ParseException ex) {
                    Logger.getLogger(BankAccountManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            reader.close();
            return trans.toArray(new Transaction[0]);

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return null;
    }

    @Override
    public Transaction[] findByAc(long account) throws DAOException {
        String filename = "./flat_database/Transaction.txt";
        String line;

        String transactioncol[] = new String[]{"tranId", "account", "type", "amount", "dateTime", "remark"};

        LinkedHashMap<String, String> mapTransaction = new LinkedHashMap<String, String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String delimiter = "\\s{6}";
            ArrayList<Transaction> trans = new ArrayList<>();

            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(delimiter);
                for (int i = 0; i < record.length; i++) {
                    mapTransaction.put(transactioncol[i], record[i]);
                }

                try {
                    if (Long.parseLong(mapTransaction.get("account")) == account) {
                        Transaction transaction = new Transaction(Integer.parseInt(mapTransaction.get("tranId")), Long.parseLong(mapTransaction.get("account")), mapTransaction.get("type"),
                                Integer.parseInt(mapTransaction.get("amount")), sdf.parse(mapTransaction.get("dateTime")), mapTransaction.get("remark"));

                        trans.add(transaction);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(BankAccountManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            reader.close();
            return trans.toArray(new Transaction[0]);

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return null;
    }

    @Override
    public Transaction[] findTransfer(String transferid) throws DAOException {
        String filename = "./flat_database/Transaction.txt";
        String line;

        String transactioncol[] = new String[]{"tranId", "account", "type", "amount", "dateTime", "remark"};

        LinkedHashMap<String, String> mapTransaction = new LinkedHashMap<String, String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String delimiter = "\\s{6}";
            ArrayList<Transaction> trans = new ArrayList<>();

            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(delimiter);
                for (int i = 0; i < record.length; i++) {
                    mapTransaction.put(transactioncol[i], record[i]);
                }

                try {
                    if (mapTransaction.get("remark").equals(transferid)) {
                        Transaction transaction = new Transaction(Integer.parseInt(mapTransaction.get("tranId")), Long.parseLong(mapTransaction.get("account")), mapTransaction.get("type"),
                                Integer.parseInt(mapTransaction.get("amount")), sdf.parse(mapTransaction.get("dateTime")), mapTransaction.get("remark"));

                        trans.add(transaction);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(BankAccountManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            reader.close();
            return trans.toArray(new Transaction[0]);

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return null;
    }

    @Override
    public int countAllTransfer() throws DAOException {
        String filename = "./flat_database/Transfer.txt";
        String line;
        int count = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                count++;
            }

            reader.close();

            return count;

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return count;
    }

    @Override
    public int countAllTransaction() throws DAOException {
        String filename = "./flat_database/Transaction.txt";
        String line;
        int count = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                count++;
            }

            reader.close();

            return count;

        } catch (IOException iox) {
            System.out.println("Message" + iox);
        }

        return count;
    }

}
