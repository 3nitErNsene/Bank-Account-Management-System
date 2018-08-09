package com.bams.main.services;

import com.bams.dao.AccountDAO;
import com.bams.dao.CustomerDAO;
import com.bams.dao.DAOException;
import com.bams.dao.DAOFactory;
import com.bams.dao.TransactionDAO;
import static com.bams.main.BankAccountManagementSystem.clear;
import com.bams.main.user.CustomerLogin;
import com.bams.model.Account;
import com.bams.model.Customer;
import com.bams.model.Transaction;
import com.bams.model.Transfer;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CustomerService {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public CustomerService() {
    }

    public boolean Customer(BufferedReader in, DAOFactory factory) throws IOException, DAOException {
        CustomerDAO customerdao = factory.createCustomerDAO();
        AccountDAO accountdao = factory.createAccountDAO();
        TransactionDAO transactiondao = factory.createTransactionDAO();
        String username;
        String pwd;
        String action;

//        clear();
        System.out.println("|  [L]ogin  |  [A]ctivate account  |  [B]ack  |");

        action = in.readLine().trim();
        if ((action.length() == 0) || action.toUpperCase().charAt(0) == 'B') {
            return true;
        }

        switch (action.toUpperCase().charAt(0)) {
            case 'A':
                ActivateAccount(in, customerdao);
                return false;
        }

        if (action.toUpperCase().charAt(0) != 'L') {
            return false;
        }

        String prompt = "Username ";
        do {
            System.out.println(prompt + " : ");
            username = in.readLine().trim();

            if (username.length() < 1 || username.equals("NULL")) {
                System.out.println("Please retry with a valid Username");
            }

        } while (username.length() < 1 || username.equals("NULL"));

        prompt = "Password ";
        do {
            System.out.println(prompt + " : ");
            pwd = in.readLine().trim();

            if (pwd.length() < 1 || pwd.equals("NULL")) {
                System.out.println("Please retry with a valid Password");
            }
        } while (pwd.length() < 1 || pwd.equals("NULL"));

        CustomerLogin custlogin = new CustomerLogin(username, pwd, customerdao);

        boolean timeToQuit = false;
        Customer nowcust;
        if ((nowcust = custlogin.chkLogin()) != null) {

            clear();
            System.out.println("Login successfully");
            System.out.println("\n");
            System.out.println("\n");
            System.out.println("Welcome " + username + " !!");
            do {
                timeToQuit = CustomerMain(nowcust, in, accountdao, transactiondao, customerdao);
            } while (!timeToQuit);

            return true;
        } else {
            clear(); 
            System.out.println("|       Authentication fail!!!!        |");
            System.out.println("|      [Q]uit      |      [R]etype     |");
            
            action = in.readLine();
            if ((action.length() == 0) || action.toUpperCase().charAt(0) == 'Q') {
                return true;
            }
            switch (action.toUpperCase().charAt(0)) {
                case 'R':
                    return false;

                default:
                    return true;
            }
        }
    }

    public boolean CustomerMain(Customer nowcust, BufferedReader in, AccountDAO accountdao, TransactionDAO transactiondao, CustomerDAO customerdao) throws IOException, DAOException {
        String action;
        System.out.println("|   [T]ransfer money  |  [V]iew account  |  [B]ack   |");
        action = in.readLine();
        if ((action.length() == 0) || action.toUpperCase().charAt(0) == 'B') {
            return true;
        }
        boolean timeToQuit = false;

        switch (action.toUpperCase().charAt(0)) {
            case 'T':
                do {
                    System.out.println("-Press 'Q' anytime to quit Transfer Function-\n");
                    timeToQuit = TransferMoney(nowcust, in, accountdao, transactiondao);

                } while (!timeToQuit);
                break;

            case 'V':
                do {
                    timeToQuit = ViewAccount(nowcust, in, accountdao, transactiondao);
                } while (!timeToQuit);

                break;

        }
        return false;
    }

    public boolean TransferMoney(Customer nowcust, BufferedReader in, AccountDAO accountdao, TransactionDAO transactiondao) throws IOException, DAOException {

        String temp = null;
        String max = "555001200000";
        String min = "555001099999";
        long account = 0;
        int amount = 0;

        System.out.println("Transfer From Account : " + String.valueOf(nowcust.getAccountNo()));

        String prompt = "Transfer To Account ";
        Scanner scanner = new Scanner(System.in);
        boolean chkAccount = false;
        do {
            try {
                System.out.print(prompt + " : ");
                temp = scanner.nextLine();
                if (temp.equals("")) {
                    System.out.println("Please enter Account Number.");
                    chkAccount = false;
                } else if (!temp.matches("^[0-9]+$") && temp.toUpperCase().charAt(0) != 'Q') {
                    System.out.println("Please enter Account Number.");
                    chkAccount = false;
                } else if (temp.toUpperCase().charAt(0) == 'Q' && temp.length() == 1) {
                    System.out.println("Action cancel.");
                    return true;
                } else {
                    account = Long.parseLong(temp);
                    if (account < Long.parseLong(min) || account > Long.parseLong(max)) {
                        System.out.println("Please retry with a valid Account Number (format: 5550011XXXXX).");
                        chkAccount = false;
                    } else {
                        if (accountdao.findByAc(account) != null) {
                            if (account == nowcust.getAccountNo()) {
                                System.out.println("Enter a valid Transferee Account Number.");
                                chkAccount = false;
                            } else {
                                chkAccount = true;
                            }
                        } else {
                            System.out.println("Account Number not exist in our Bank.");
                            chkAccount = false;
                        }
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid Account Number: " + e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please retry with a valid Account Number: " + e.getMessage());
            }

        } while (!chkAccount);

        prompt = "Transfer Amount (HKD) ";
        boolean accountchk = false;
        do {
            try {
                System.out.print(prompt + " : $ ");
                temp = in.readLine().trim();

                if (temp.equals("")) {
                    System.out.println("Please enter Amount.");
                    accountchk = false;
                } else if (!temp.matches("^[0-9]+$") && temp.toUpperCase().charAt(0) != 'Q') {
                    System.out.println("Please enter Amount.");
                    accountchk = false;
                } else if (temp.toUpperCase().charAt(0) == 'Q' && temp.length() == 1) {
                    System.out.println("Action cancel.");
                    return true;
                } else if (Integer.parseInt(temp) < 1) {
                    System.out.println("Please enter enough Amount.");
                    accountchk = false;
                } else {
                    amount = Integer.parseInt(temp);
                    if (!accountdao.chkBalance(nowcust, amount)) {
                        System.out.println("Insufficient account balance!");
                        accountchk = false;
                    } else {
                        accountchk = true;
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid amount: " + e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please retry with a valid amount: " + e.getMessage());
            }

        } while (!accountchk);

        System.out.println("Transfer $" + String.valueOf(amount) + " from " + String.valueOf(nowcust.getAccountNo()) + " to " + String.valueOf(account) + " successfully. \n");

        String transferId = "tr" + String.valueOf(transactiondao.countAllTransfer() + 1);
        Date date = new Date();
        Transfer transfer = new Transfer(transferId, nowcust.getAccountNo(), account, amount, date);

        accountdao.transactionProcess(transactiondao.addTransfer(transfer));

        return true;
    }

    public boolean ViewAccount(Customer nowcust, BufferedReader in, AccountDAO accountdao, TransactionDAO transactiondao) throws IOException, DAOException {
        String action;
        Account viewac = accountdao.findByAc(nowcust.getAccountNo());
        Transaction[] viewacDetail = transactiondao.findByAc(nowcust.getAccountNo());
        
        System.out.println("\n");
        
        System.out.println(
                "Account : " + viewac.getAccountNo()
                + "\nBalance : " + viewac.getBalance() + "\n"
        );

        System.out.println("|      [V]iew Transfer Detail      |     [B]ack      | ");
        
        action = in.readLine();
        if ((action.length() == 0) || action.toUpperCase().charAt(0) == 'B') {
            return true;
        }

        switch (action.toUpperCase().charAt(0)) {
            case 'V':
                int i = 1;
                System.out.println("\n");
                for (Transaction detail : viewacDetail) {
                    System.out.println(
                            i + ". "
                            + "DateTime : " + sdf.format(detail.getDateTime())
                            + "    Action : " + detail.getType()
                            + "    Amount : " + detail.getAmount()
                    );
                    i++;
                }
                System.out.println();
                break;
        }

        return true;
    }

    public Customer ActivateAccount(BufferedReader in, CustomerDAO customerdao) throws IOException, DAOException {
        String action;
        Customer cust = null;
        do {
            System.out.print("HKID : ");
            action = in.readLine().trim();

            if (action.equals("")) {
                System.out.println("Input your HKID");
            } else if (action.toUpperCase().charAt(0) == 'Q' && action.length() == 1) {
                System.out.println("Action cancel.");
                return cust;
            } else if ((cust = customerdao.findById(action)) != null) {
                String username = null;
                String password = null;
                if (cust.getLoginName().equals("NULL") && cust.getPassword().equals("NULL")) {
                    do {
                        System.out.println("Your Username : ");
                        username = in.readLine().trim();
                        if (username.equals("")) {
                            System.out.println("Enter your username.");
                        } else if (username.toUpperCase().charAt(0) == 'Q' && username.length() == 1) {
                            return cust;
                        }
                    } while (username.equals(""));

                    do {
                        System.out.println("Your Password : ");
                        password = in.readLine().trim();
                        if (password.equals("")) {
                            System.out.println("Enter your password.");
                        } else if (password.toUpperCase().charAt(0) == 'Q' && password.length() == 1) {
                            return cust;
                        }
                    } while (password.equals(""));

                    CustomerLogin custlogin = new CustomerLogin(username, password);

                    cust.setLoginName(custlogin.getLoginname());
                    cust.setPassword(custlogin.regPwd());
                    customerdao.update(cust);

                    System.out.println("User login name and password are activated.\n");

                } else {
                    System.out.println("User login name and password have activated before.");
                }
                return cust;
            } else {
                System.out.println("No such customer exist.");
                return cust;
            }

        } while (action.equals(""));
        return cust;
    }
}
