package com.bams.main;

import com.bams.dao.DAOException;
import com.bams.dao.DAOFactory;
import com.bams.main.services.CustomerService;
import com.bams.main.services.StaffService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BankAccountManagementSystem {

    public static void main(String[] args) throws IOException, DAOException {
        DAOFactory factory = new DAOFactory();
        boolean timeToQuit = false;

        do {
            clear();
            mainface();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            timeToQuit = menuAction(in, factory);
        } while (!timeToQuit);
    }

    public static void mainface() {
        System.out.println(" ____    __    _  _  _  _      __     ___   ___  _____  __  __  _  _  ____    __  __    __    _  _    __     ___  ____  __  __  ____  _  _  ____    ___  _  _  ___  ____  ____  __  __ ");
        System.out.println("(  _ \\  /__\\  ( \\( )( )/ )    /__\\   / __) / __)(  _  )(  )(  )( \\( )(_  _)  (  \\/  )  /__\\  ( \\( )  /__\\   / __)( ___)(  \\/  )( ___)( \\( )(_  _)  / __)( \\/ )/ __)(_  _)( ___)(  \\/  )");
        System.out.println(" ) _ < /(__)\\  )  (  )  (    /(__)\\ ( (__ ( (__  )(_)(  )(__)(  )  (   )(     )    (  /(__)\\  )  (  /(__)\\ ( (_-. )__)  )    (  )__)  )  (   )(    \\__ \\ \\  / \\__ \\  )(   )__)  )    ( ");
        System.out.println("(____/(__)(__)(_)\\_)(_)\\_)  (__)(__) \\___) \\___)(_____)(______)(_)\\_) (__)   (_/\\/\\_)(__)(__)(_)\\_)(__)(__) \\___/(____)(_/\\/\\_)(____)(_)\\_) (__)   (___/ (__) (___/ (__) (____)(_/\\/\\_)");
        System.out.println("\n");
        System.out.println("|                                            [L]ogin                                            |                                 [Q]uit                                           |");
    }
    
    public static void clear() {
        for(int i = 0;i<20;i++) System.out.print("\n");
     }

    public static boolean menuAction(BufferedReader in, DAOFactory factory) throws IOException, DAOException {
        String action;
        action = in.readLine();

        if ((action.length() == 0) || action.toUpperCase().charAt(0) == 'Q') {
            return true;
        }

        boolean timeToQuit = false;
        switch (action.toUpperCase().charAt(0)) {
            case 'L':
                do {
                    try {
                        timeToQuit = login(in, factory);
                    } catch (IOException e) {
                        System.out.println("Message: " + e.getMessage());
                    }
                } while (!timeToQuit);
                break;
        }
        return false;
    }

    public static boolean login(BufferedReader in, DAOFactory factory) throws IOException, DAOException {
        String action;
        clear();
        System.out.println("|          [C]ustomer         |         [S]taff          |         [B]ack         |");
        action = in.readLine();

        if ((action.length() == 0) || action.toUpperCase().charAt(0) == 'B') {
            return true;
        }
        
        boolean timeToQuit = false;
        
        switch (action.toUpperCase().charAt(0)) {
            case 'C':
                do {
                    //set timeToQuit and display customer choice
                    CustomerService cs = new CustomerService();
                    timeToQuit = cs.Customer(in, factory);
                    
                } while (!timeToQuit);
                break;

            case 'S':
                do {
                    StaffService ss = new StaffService();
                    timeToQuit = ss.Staff(in, factory);
                } while (!timeToQuit);

                break;

        }
        return false;
    }

}
