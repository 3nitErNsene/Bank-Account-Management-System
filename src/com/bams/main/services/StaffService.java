package com.bams.main.services;

import com.bams.dao.AccountDAO;
import com.bams.dao.AdminStaffDAO;
import com.bams.dao.CustomerDAO;
import com.bams.dao.DAOException;
import com.bams.dao.DAOFactory;
import com.bams.dao.TransactionDAO;
import static com.bams.main.BankAccountManagementSystem.clear;
import com.bams.main.user.StaffLogin;
import com.bams.model.Account;
import com.bams.model.AdminStaff;
import com.bams.model.Customer;
import com.bams.model.Transaction;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaffService {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    public StaffService() {

    }

    public String AccountNumber() {
        StringBuilder a = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            a.append(random.nextInt(10));
        }
        return a.toString();
    }

    public boolean Staff(BufferedReader in, DAOFactory factory) throws IOException, DAOException {
        CustomerDAO customerdao = factory.createCustomerDAO();
        AccountDAO accountdao = factory.createAccountDAO();
        TransactionDAO transactiondao = factory.createTransactionDAO();
        AdminStaffDAO adminstaffdao = factory.createAdminStaffDAO();
        String username;
        String pwd;
        String action;

        String prompt = "Staff username ";
        do {
            System.out.println(prompt + " : ");
            username = in.readLine().trim();

            if (username.length() < 1) {
                System.out.println("Please retry with a valid Username");
            }

        } while (username.length() < 1);

        prompt = "Password ";
        do {
            System.out.println(prompt + " : ");
            pwd = in.readLine().trim();

            if (pwd.length() < 1) {
                System.out.println("Please retry with a valid Password");
            }
        } while (pwd.length() < 1);

        StaffLogin stafflogin = new StaffLogin(username, pwd, adminstaffdao);

        boolean timeToQuit = false;
        AdminStaff nowstaff;
        if ((nowstaff = stafflogin.chkLogin()) != null) {

            clear();
            System.out.println("Login successfully");
            System.out.println("\n");
            System.out.println("\n");
            if (nowstaff.getRole().equals("staff")) {
                System.out.println("                                   Welcome " + nowstaff.getName() + " !!                                       ");
            } else if (nowstaff.getRole().equals("admin")) {
                System.out.println("                                                            Welcome " + nowstaff.getName() + " !!                                                                     ");
            } else {
                System.out.println("                                   Welcome " + nowstaff.getName() + " !!                                       ");

            }
            Date date = new Date();
            nowstaff.setLastLogin(date);
            adminstaffdao.update(nowstaff);
            do {
                timeToQuit = AdminStaffMain(nowstaff, in, accountdao, customerdao, transactiondao, adminstaffdao);
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

    public boolean AdminStaffMain(AdminStaff nowstaff, BufferedReader in, AccountDAO accountdao, CustomerDAO customerdao, TransactionDAO transactiondao, AdminStaffDAO adminstaffdao) throws IOException, DAOException {
        String action;

        if (nowstaff.getRole().equals("staff")) {
             System.out.println("|  [R]egister Customer  |  [U]date Customer  |  [D]eposit  |  [W]ithdrawal  |  [V]iew |  [B]ack |");
        } else if (nowstaff.getRole().equals("admin")) {
            System.out.println("|  [R]egister Customer  |  [U]date Customer  |  D[E]lete Customer Info  |  B[L]ock Account  |  [D]eposit  |  [W]ithdrawal  |  [V]iew |  [S]taff Management  |  [B]ack   |");
        } else {
            System.out.println("|  [R]egister Customer  |  [U]date Customer  |  [D]eposit  |  [W]ithdrawal  |  [V]iew |  [B]ack |");
        }

        action = in.readLine();

        if ((action.length() == 0) || action.toUpperCase().charAt(0) == 'B') {
            return true;
        }

        boolean timeToQuit = false;
        Customer customer = null;

        switch (action.toUpperCase().charAt(0)) {
            case 'R':
                System.out.println("-Press 'Q' anytime to quit Register-\n");
                RegisterCTM(in, customerdao, accountdao);
                break;

            case 'U':
                System.out.println("-Press 'Q' anytime to quit Update-\n");
                UpdateCTM(in, customerdao, accountdao);
                break;

            case 'D':
                do {
                    customer = getCustomer(in, customerdao);
                    if (customer != null) {
                        if (accountdao.chkStatus(customer)) {
                            System.out.println("-Press 'Q' anytime to quit Deposit-\n");
                            timeToQuit = Deposit(in, customer, customerdao, accountdao, transactiondao);
                        } else {
                            System.out.println("Account blocked.\n");
                            timeToQuit = true;
                        }
                    } else {
                        timeToQuit = false;
                    }
                } while (!timeToQuit);

                break;

            case 'W':
                do {
                    customer = getCustomer(in, customerdao);
                    if (customer != null) {
                        if (accountdao.chkStatus(customer)) {
                            System.out.println("-Press 'Q' anytime to quit Withdrawal-\n");
                            timeToQuit = Withdrawal(in, customer, customerdao, accountdao, transactiondao);
                        } else {
                            System.out.println("Account blocked.\n");
                            timeToQuit = true;
                        }
                    } else {
                        timeToQuit = false;
                    }

                } while (!timeToQuit);

                break;

            case 'V':
                System.out.println("View all customer ? [Y/N]");
                action = in.readLine().trim();
                if (action.toUpperCase().charAt(0) == 'Y') {
                    showCustomer(customerdao.findAll());
                } else {
                    System.out.print("Customer ID : ");
                    action = in.readLine().trim();
                    if (action.length() == 7) {
                        Customer found = customerdao.findById(action);
                        if (found != null) {
                            showCustomer(found);
                        } else {
                            System.out.println("HKID not found.");
                        }
                    } else {
                        System.out.println("Wrong HKID.");
                    }
                }
                break;

            case 'L':
                if (nowstaff.getRole().equals("admin")) {
                    System.out.println("-Press 'Q' anytime to quit Update-\n");
                    BlockAccount(in, customerdao, accountdao, nowstaff);
                }
                break;

            case 'S':
                if (nowstaff.getRole().equals("admin")) {
                    System.out.println("-Press 'Q' anytime to quit Update-\n");
                    do{
                    timeToQuit = StaffManagement(in, adminstaffdao);
                    }while(!timeToQuit);
                }
                break;
                
            case 'E':
                System.out.println("-Press 'Q' anytime to quit Update-\n");
                DeleteCTM(in, customerdao, accountdao, nowstaff);
                break;

        }
        return false;
    }

    public boolean StaffManagement(BufferedReader in, AdminStaffDAO adminstaffdao) throws IOException, DAOException {
        String action;
        System.out.println("|  [A]dd Staff  |   [U]pdate Staff  |  [D]elete Staff  |  [V]iew all Staffs  |  V[I]ew Staff  |  [B]ack  |");
        action = in.readLine();

        if ((action.length() == 0) || action.toUpperCase().charAt(0) == 'B') {
            return true;
        }

        switch (action.toUpperCase().charAt(0)) {
            case 'A':
                System.out.println("-Press 'Q' anytime to quit Register-\n");
                AddStaff(in, adminstaffdao);
                break;

            case 'U':
                System.out.println("-Press 'Q' anytime to quit Register-\n");
                UpdateStaff(in, adminstaffdao);
                break;

            case 'D':
                System.out.println("-Press 'Q' anytime to quit Register-\n");
                DeleteStaff(in, adminstaffdao);
                break;

            case 'V':
                System.out.println("-Press 'Q' anytime to quit Register-\n");
                showStaff(adminstaffdao.findAll());
                break;

            case 'I':
                System.out.println("-Press 'Q' anytime to quit Register-\n");
                AdminStaff getStaff = getStaff(in, adminstaffdao);
                if (getStaff != null) {
                    showStaff(getStaff);
                }
                break;
        }

        return false;
    }

    public Customer RegisterCTM(BufferedReader in, CustomerDAO customerdao, AccountDAO accountdao) throws DAOException, IOException {
        Customer[] custall = customerdao.findAll();
        Customer customer = inputCustomer(in, custall, accountdao);
        if (customer != null) {
            customerdao.add(customer);
            System.out.println("Customer added.");
            RegisterAccount(in, customer, accountdao);
        }

        return customer;
    }

    public Customer UpdateCTM(BufferedReader in, CustomerDAO customerdao, AccountDAO accountdao) throws DAOException, IOException {
        Customer[] custall = customerdao.findAll();
        Customer customer = null;

        Customer getCustomer = getCustomer(in, customerdao);
        if (getCustomer != null) {
            customer = inputCustomer(in, getCustomer, custall, accountdao);
            if (customer != null) {
                customerdao.update(customer);
                System.out.println("Customer edited.\n");
//                RegisterAccount(in, customer, accountdao);
            } else {
                System.out.println("Fail to edit customer");
            }
        }

        return customer;
    }
    
    public Customer DeleteCTM(BufferedReader in, CustomerDAO customerdao, AccountDAO accountdao, AdminStaff adminstaff) throws DAOException, IOException {
        Customer[] custall = customerdao.findAll();
        Customer customer = null;

        Customer getCustomer = getCustomer(in, customerdao);
        if (getCustomer != null) {
            customerdao.deleteCust(getCustomer);
            accountdao.deleteAc(getCustomer, adminstaff);
            System.out.println("Customer deleted.\n");
        }

        return customer;
    }

    public boolean BlockAccount(BufferedReader in, CustomerDAO customerdao, AccountDAO accountdao, AdminStaff adminstaff) throws IOException, DAOException {
        Customer getCustomer = getCustomer(in, customerdao);
        if (getCustomer != null) {
            accountdao.deleteAc(getCustomer, adminstaff);
            return true;
        } else {
//            System.out.println("Fail to block this account.");
            return false;
        }
    }

    public Account RegisterAccount(BufferedReader in, Customer customer, AccountDAO accountdao) throws DAOException, IOException {
        Account foundAc = chkAccountExist(customer.getAccountNo(), accountdao);
        String temp;
        int amount = 0;
        boolean accountchk = false;
        if (foundAc == null) {
            System.out.println("Register a saving account.\n");
            System.out.println("At lease $ 5,000 would be needed to save and sign up a saving account.");
            do {
                System.out.print("Saving amount : ");
                temp = in.readLine().trim();
                if (temp.equals("") || Integer.parseInt(temp) < 5000) {
                    System.out.println("Please enter Amount.");
                    accountchk = false;
                } else if (temp.toUpperCase().charAt(0) == 'Q' && temp.length() == 1) {
                    System.out.println("Action cancel.");
                    return null;
                } else {
                    amount = Integer.parseInt(temp);
                    accountchk = true;
                }
            } while (!accountchk);

            System.out.println("\nAccount number ---- " + customer.getAccountNo());
            System.out.println("Saving amount  ---- $ " + amount);

            Date date = new Date();
            Account account = new Account(customer.getAccountNo(), amount, "active", date, "NULL");
            accountdao.addAc(account);
            System.out.println("Account added\n");

            return account;

        } else {
            System.out.println("Account " + customer.getAccountNo() + " existed.");
            return foundAc;
        }
    }

    public AdminStaff AddStaff(BufferedReader in, AdminStaffDAO adminstaffdao) throws DAOException, IOException {
        AdminStaff[] staffall = adminstaffdao.findAll();
        AdminStaff adminstaff = inputStaff(in, staffall);
        if (adminstaff != null) {
            adminstaffdao.add(adminstaff);
            System.out.println("Staff added.\n");
        } else {
            System.out.println("Add staff fail.\n");
        }

        return adminstaff;
    }

    public AdminStaff UpdateStaff(BufferedReader in, AdminStaffDAO adminstaffdao) throws DAOException, IOException {
        AdminStaff[] staffall = adminstaffdao.findAll();
        AdminStaff adminstaff = null;

        AdminStaff getStaff = getStaff(in, adminstaffdao);
        if (getStaff != null) {
            adminstaff = inputStaff(in, getStaff, staffall);
            if (adminstaff != null) {
                adminstaffdao.update(adminstaff);
                System.out.println("Staff updated.");
            } else {
                System.out.println("Update Staff failed.");
            }
        }

        return adminstaff;
    }

    public AdminStaff DeleteStaff(BufferedReader in, AdminStaffDAO adminstaffdao) throws IOException, DAOException {
        AdminStaff adminstaff = null;
        AdminStaff getStaff = getStaff(in, adminstaffdao);
        if (getStaff != null) {
            adminstaffdao.delete(getStaff);
            System.out.println("Staff deleted.\n");
        }
        return adminstaff;
    }

    public boolean Deposit(BufferedReader in, Customer customer, CustomerDAO customerdao, AccountDAO accountdao, TransactionDAO transactiondao) throws IOException, DAOException {
        String prompt;
        String temp;
        int amount = 0;

        prompt = "Deposite Amount (HKD) ";
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
                    accountchk = true;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid amount: " + e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please retry with a valid amount: " + e.getMessage());
            }
        } while (!accountchk);

        System.out.println("Deposit $ " + String.valueOf(amount) + " to account --- " + String.valueOf(customer.getAccountNo()) + "\n");

        Date date = new Date();
        Transaction trans = new Transaction((transactiondao.countAllTransaction() + 1), customer.getAccountNo(), "Debit", amount, date, "NULL");

        accountdao.transactionProcess(transactiondao.add(trans));

        return true;
    }

    public boolean Withdrawal(BufferedReader in, Customer customer, CustomerDAO customerdao, AccountDAO accountdao, TransactionDAO transactiondao) throws IOException, DAOException {
        String prompt;
        String temp;
        int amount = 0;

        prompt = "Withdrawal Amount (HKD) ";
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
                    return true;
                } else if (Integer.parseInt(temp) < 1) {
                    System.out.println("Please enter enough Amount.");
                    accountchk = false;
                } else {
                    amount = Integer.parseInt(temp);
                    if (!accountdao.chkBalance(customer, amount)) {
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

        System.out.println("Withdraw $ " + String.valueOf(amount) + " to account --- " + String.valueOf(customer.getAccountNo()) + "\n");

        Date date = new Date();
        Transaction trans = new Transaction((transactiondao.countAllTransaction() + 1), customer.getAccountNo(), "Credit", amount, date, "NULL");

        accountdao.transactionProcess(transactiondao.add(trans));

        return true;
    }

    public Customer inputCustomer(BufferedReader in, Customer[] custall, AccountDAO accountdao) throws IOException, DAOException {
        return inputCustomer(in, null, true, custall, accountdao);
    }

    public Customer inputCustomer(BufferedReader in, Customer custDefaults, Customer[] custall, AccountDAO accountdao) throws IOException, DAOException {
        return inputCustomer(in, custDefaults, false, custall, accountdao);
    }

    public Customer inputCustomer(BufferedReader in, Customer custDefaults, boolean newCustomer, Customer[] custall, AccountDAO accountdao) throws IOException, DAOException {

        String temp = null;
        String hkid = null;
        long accountNo = 0;
        String loginName = null;
        String password = null;
        String gender = null;
        String address = null;
        String lastname = null;
        String firstname = null;
        Date dob = null;
        String phone = null;
        String email = null;
        Customer cust = null;
        String prompt;
        boolean chkvalue = false;

        if (newCustomer) {
            System.out.println("Register A New Customer Account.\n");
            do {
                chkvalue = true;
                System.out.println("HKID (e.g. S123456)  : ");
                try {
                    hkid = in.readLine().trim();

                    for (Customer chkCust : custall) {
                        if (chkCust.getHkid().equals(hkid)) {
                            System.out.println("Registered this customer. ");
                            return cust;
                        }
                    }

                    if (hkid.equals("")) {
                        System.out.println("Enter Customer HKID.");
                        chkvalue = false;
                    } else if (hkid.toUpperCase().charAt(0) == 'Q' && hkid.length() == 1) {
                        return cust;
                    } else if (!hkid.matches("^[a-zA-Z]{1}[0-9]{6}$")) {
                        System.out.println("Please retry with a valid HKID");
                        chkvalue = false;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Enter valid HKID " + e.getMessage());
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Enter valid HKID " + e.getMessage());
                }

            } while (!chkvalue);

            do {
                chkvalue = true;
                try {
                    do {
                        accountNo = Long.parseLong("5550011" + AccountNumber());
                    } while (chkAccountExist(accountNo, accountdao) != null);
                    System.out.println("New Account Number" + " : " + accountNo);

                } catch (NumberFormatException e) {
                    System.out.println("Please retry with a valid Account Number: " + e.getMessage());
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Please retry with a valid Account Number: " + e.getMessage());
                }
            } while (!chkvalue);

            do {
                chkvalue = true;
                loginName = "NULL";
                password = "NULL";
                System.out.println("Login Name : wait to register.");
                System.out.println("Password : wait to register.");
            } while (!chkvalue);

        } else {
            hkid = custDefaults.getHkid();
            accountNo = custDefaults.getAccountNo();
            loginName = custDefaults.getLoginName();
            password = custDefaults.getPassword();
            System.out.println("Modify Customer HKID : " + hkid + " record.");
            System.out.println("\nAccount Number : " + accountNo + ".");
            System.out.println("Login name : " + loginName);
        }

        do {
            chkvalue = true;
            prompt = "Gender "
                    + ((custDefaults == null) ? "[Press 'F': Female / 'M' : Male]" : " [" + custDefaults.getGender() + "]");

            try {
                do {
                    System.out.println(prompt + " : ");
                    gender = in.readLine().trim();

                    if (gender.equals("") && custDefaults != null) {
                        gender = custDefaults.getGender();
                        chkvalue = true;
                    } else if (gender.equals("")) {
                        System.out.println("Please retry with a valid Gender Choice");
                        chkvalue = false;
                    } else if (gender.toUpperCase().charAt(0) == 'Q' && gender.length() == 1) {
                        return cust;
                    }

                    switch (gender.toUpperCase().charAt(0)) {
                        case 'F':
                            gender = "Female";
                            break;
                        case 'M':
                            gender = "Male";
                            break;
                    }
                } while (gender.equals(""));

            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid Gender Choice");
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please retry with a valid Gender Choice");
            }

        } while (!chkvalue);

        do {
            prompt = "Customer Address "
                    + ((custDefaults == null) ? "" : " [" + custDefaults.getAddress() + "]");
            System.out.println(prompt + " : ");

            try {
                chkvalue = true;
                address = in.readLine().trim();

                if (address.equals("") && custDefaults != null) {
                    address = custDefaults.getAddress();
                    chkvalue = true;
                } else if (address.length() < 5) {
                    System.out.println("Please retry with a valid Address");
                    chkvalue = false;
                } else if (address.toUpperCase().charAt(0) == 'Q' && address.length() == 1) {
                    return cust;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid Address" + e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please enter Address" + e.getMessage());
            }

        } while (!chkvalue);

        prompt = "FirstName " + ((custDefaults == null) ? "" : " [" + custDefaults.getFirstname() + "]");

        do {
            try {
                chkvalue = true;
                System.out.println(prompt + " : ");
                firstname = in.readLine().trim();

                if (firstname.equals("") && custDefaults != null) {
                    firstname = custDefaults.getFirstname();
                    chkvalue = true;
                } else if (firstname.length() < 2) {
                    System.out.println("Please retry with a valid FirstName");
                    chkvalue = false;
                } else if (firstname.toUpperCase().charAt(0) == 'Q' && firstname.length() == 1) {
                    return cust;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid FirstName: " + e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please retry with a valid FirstName: " + e.getMessage());
            }

        } while (!chkvalue);

        prompt = "LastName " + ((custDefaults == null) ? "" : " [" + custDefaults.getLastname() + "]");
        do {
            try {
                chkvalue = true;
                System.out.println(prompt + " : ");
                lastname = in.readLine().trim();

                if (lastname.equals("") && custDefaults != null) {
                    lastname = custDefaults.getLastname();
                    chkvalue = true;
                } else if (lastname.length() < 2) {
                    System.out.println("Please retry with a valid LastName");
                    chkvalue = false;
                } else if (lastname.toUpperCase().charAt(0) == 'Q' && lastname.length() == 1) {
                    return cust;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid LastName: " + e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please retry with a valid LastName: " + e.getMessage());
            }

        } while (!chkvalue);

        do {
            chkvalue = true;
            prompt = "Enter your birth date " + "(" + sdf.toLocalizedPattern() + ")" + ((custDefaults == null) ? "" : " [" + sdf.format(custDefaults.getDob()) + "]");
            System.out.println(prompt + " : ");
            temp = in.readLine().trim();

            if (temp.equals("") && custDefaults != null) {
                dob = custDefaults.getDob();
            } else if (temp.equals("")) {
                System.out.println("Please enter birth date");
                chkvalue = false;
            } else if (temp.toUpperCase().charAt(0) == 'Q' && temp.length() == 1) {
                return cust;
            } else {
                try {
                    //dob = new Date(sdf.parse(temp).getTime());
                    dob = sdf.parse(temp);
                } catch (ParseException e) {
                    System.out.println("Please retry with a valid birth date: " + e.getMessage());
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Please retry with a valid birth date: " + e.getMessage());
                }

            }
        } while (dob == null);

        prompt = "Phone number " + ((custDefaults == null) ? "" : " [" + custDefaults.getPhone() + "]");
        do {
            try {
                chkvalue = true;
                System.out.println(prompt + " : ");
                phone = in.readLine().trim();

                if (phone.equals("") && custDefaults != null) {
                    phone = custDefaults.getPhone();
                    chkvalue = true;
                } else if (phone.length() != 8) {
                    System.out.println("Please retry with a valid Phone Number");
                    chkvalue = false;
                } else if (phone.toUpperCase().charAt(0) == 'Q' && phone.length() == 1) {
                    return cust;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid Phone Number: " + e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please retry with a valid Phone Number: " + e.getMessage());
            }

        } while (!chkvalue);

        prompt = "Email address " + ((custDefaults == null) ? "" : " [" + custDefaults.getEmail() + "]");
        do {
            try {
                System.out.println(prompt + " : ");
                email = in.readLine().trim();
                chkvalue = true;

                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(email);

                if (email.equals("") && custDefaults != null) {
                    email = custDefaults.getEmail();
                    chkvalue = true;
                } else if (!matcher.matches()) {
                    System.out.println("Please retry with a valid Email Address.");
                    chkvalue = false;
                } else if (email.toUpperCase().charAt(0) == 'Q' && email.length() == 1) {
                    return cust;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid Email Address: " + e.getMessage());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please retry with a valid Email Address: " + e.getMessage());
            }

        } while (!chkvalue);
        cust = new Customer(hkid, accountNo, loginName, password, gender, address, lastname, firstname, dob, phone, email);
        return cust;

    }

    public AdminStaff inputStaff(BufferedReader in, AdminStaff[] staffall) throws IOException {
        return inputStaff(in, null, true, staffall);
    }

    public AdminStaff inputStaff(BufferedReader in, AdminStaff staffDefaults, AdminStaff[] staffall) throws IOException {
        return inputStaff(in, staffDefaults, false, staffall);
    }

    public AdminStaff inputStaff(BufferedReader in, AdminStaff staffDefaults, boolean newStaff, AdminStaff[] staffall) throws IOException {
        String staffId = null;
        String loginName = null;
        String name = null;
        String password = null;
        Date lastLogin = null;
        String role = null;

        AdminStaff adminstaff = null;
        String prompt;
        boolean chkvalue = false;

        if (newStaff) {
            System.out.println("Register New Staff.\n");
            staffId = "as" + (staffall.length + 1);
            System.out.println("Staff ID  : " + staffId);
        } else {
            staffId = staffDefaults.getStaffId();
            System.out.println("Update Staff " + staffId + ".\n");
            System.out.println("Staff ID  : " + staffId);
        }

        do {
            chkvalue = true;
            prompt = "Login Name "
                    + ((staffDefaults == null) ? ":" : " : " + staffDefaults.getLoginName() + " .");
            System.out.println(prompt);
            if (newStaff) {
                try {
                    loginName = in.readLine().trim();

                    if (loginName.equals("") && staffDefaults != null) {
                        loginName = staffDefaults.getLoginName();
                        chkvalue = true;
                    } else if (loginName.equals("")) {
                        System.out.println("Enter Staff login name.");
                        chkvalue = false;
                    } else if (loginName.toUpperCase().charAt(0) == 'Q' && loginName.length() == 1) {
                        return adminstaff;
                    }

                    for (AdminStaff chkstaff : staffall) {
                        if (chkstaff.getLoginName().equals(loginName)) {
                            System.out.println("Existed login name. ");
                            return adminstaff;
                        }
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Enter valid login name " + e.getMessage());
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Enter valid login name " + e.getMessage());
                }
            }

        } while (!chkvalue);

        do {
            chkvalue = true;
            prompt = "Name "
                    + ((staffDefaults == null) ? "" : " [" + staffDefaults.getName() + "]");
            try {
                do {
                    System.out.println(prompt + " : ");
                    name = in.readLine().trim();

                    if (name.equals("") && staffDefaults != null) {
                        name = staffDefaults.getLoginName();
                        chkvalue = true;
                    } else if (name.equals("")) {
                        System.out.println("Please enter name");
                        chkvalue = false;
                    } else if (name.toUpperCase().charAt(0) == 'Q' && name.length() == 1) {
                        return adminstaff;
                    }
                } while (name.equals(""));

            } catch (NumberFormatException e) {
                System.out.println("Please valid enter name");
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please valid enter name");
            }

        } while (!chkvalue);

        do {
            chkvalue = true;
            prompt = ((staffDefaults == null) ? "Password " : " Change New Password ? (Press [Enter] if remain unchanged.) ");
            try {
                do {
                    System.out.println(prompt + " : ");
                    password = in.readLine().trim();

                    if (password.equals("") && staffDefaults != null) {
                        password = staffDefaults.getPassword();
                        chkvalue = true;
                    } else if (password.equals("")) {
                        System.out.println("Please enter password");
                        chkvalue = false;
                    } else if (password.toUpperCase().charAt(0) == 'Q' && password.length() == 1) {
                        return adminstaff;
                    } else if (password.length() <= 8) {
                        System.out.println("Password length should > 7");
                        chkvalue = false;
                    } else {
                        StaffLogin stafflogin = new StaffLogin(loginName, password);
                        password = stafflogin.regPwd();
                        chkvalue = true;
                    }
                } while (!chkvalue);

            } catch (NumberFormatException e) {
                System.out.println("Please enter valid password");
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please enter valid password");
            }

        } while (!chkvalue);

        if (newStaff) {
            Date date = new Date();
            lastLogin = date;
        } else {
            lastLogin = staffDefaults.getLastLogin();
        }

        do {
            chkvalue = true;
            prompt = "Role "
                    + ((staffDefaults == null) ? "[Press 'A': Admin / 'S' : Staff]" : " [" + staffDefaults.getRole() + "]");

            try {
                do {
                    System.out.println(prompt + " : ");
                    role = in.readLine().trim();

                    if (role.equals("") && staffDefaults != null) {
                        role = staffDefaults.getRole();
                        chkvalue = true;
                    } else if (role.equals("")) {
                        System.out.println("Please retry with a valid Role Choice");
                        chkvalue = false;
                    } else if (role.toUpperCase().charAt(0) == 'Q' && role.length() == 1) {
                        return adminstaff;
                    }

                    switch (role.toUpperCase().charAt(0)) {
                        case 'A':
                            role = "admin";
                            break;
                        case 'S':
                            role = "staff";
                            break;
                    }
                } while (role.equals(""));

            } catch (NumberFormatException e) {
                System.out.println("Please retry with a valid role Choice");
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Please retry with a validrole Choice");
            }

        } while (!chkvalue);

        adminstaff = new AdminStaff(staffId, loginName, name, password, lastLogin, role);

        return adminstaff;
    }

    public Customer getCustomer(BufferedReader in, CustomerDAO customerdao) throws IOException, DAOException {
        String action;
        Customer cust = null;
        do {
            System.out.print("Customer HKID : ");
            action = in.readLine().trim();

            if (action.equals("")) {
                System.out.println("Input Customer HKID");
            } else if (action.toUpperCase().charAt(0) == 'Q' && action.length() == 1) {
                System.out.println("Action cancel.");
                return cust;
            } else if ((cust = customerdao.findById(action)) != null) {
                return cust;
            } else {
                System.out.println("No such customer exist.");
                return cust;
            }

        } while (action.equals(""));
        return cust;
    }

    public AdminStaff getStaff(BufferedReader in, AdminStaffDAO adminstaffdao) throws IOException, DAOException {
        String action;
        AdminStaff staff = null;
        do {
            System.out.print("Staff ID : ");
            action = in.readLine().trim();

            if (action.equals("")) {
                System.out.println("Input Staff ID");
            } else if (action.toUpperCase().charAt(0) == 'Q' && action.length() == 1) {
                System.out.println("Action cancel.");
                return staff;
            } else if ((staff = adminstaffdao.findById(action)) != null) {
                return staff;
            } else {
                System.out.println("No such Staff ID exist.");
                return staff;
            }

        } while (action.equals(""));
        return staff;
    }

    public Account chkAccountExist(long accountno, AccountDAO accountdao) throws DAOException {
        for (Account account : accountdao.findAll()) {
            if (account.getAccountNo() == accountno) {
                return account;
            }
        }
        return null;
    }

    public void showCustomer(Customer customer) {
        System.out.println("\n");
        System.out.println("HKID : " + customer.getHkid());
        System.out.println("Geder : " + customer.getGender());
        System.out.println("Account No : " + customer.getAccountNo());
        System.out.println("Password length : " + customer.getPassword().length());
        System.out.println("Login Name : " + customer.getLoginName());
        System.out.println("First Name : " + customer.getFirstname());
        System.out.println("Last Name : " + customer.getLastname());
        System.out.println("Address : " + customer.getAddress());
        System.out.println("Phone number : " + customer.getPhone());
        System.out.println("Email address : " + customer.getEmail());
        System.out.println("Date of birth : " + sdf.format(customer.getDob()));
        System.out.println("\n");
    }

    public void showCustomer(Customer[] customer) {
        for (Customer cust : customer) {
            showCustomer(cust);
        }
    }

    public void showStaff(AdminStaff adminstaff) {
        System.out.println("\n");
        System.out.println("StaffID : " + adminstaff.getStaffId());
        System.out.println("Role : " + adminstaff.getRole());
        System.out.println("Name : " + adminstaff.getName());
        System.out.println("Last Login : " + adminstaff.getLastLogin());
        System.out.println("\n");
    }

    public void showStaff(AdminStaff[] adminstaff) {
        for (AdminStaff staff : adminstaff) {
            showStaff(staff);
        }
    }

}
