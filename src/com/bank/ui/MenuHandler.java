package com.bank.ui;

import com.bank.exceptions.AccountAlreadyExistsException;
import com.bank.models.Account;
import com.bank.services.WalletService;
import com.bank.util.IConstant;
import com.bank.util.PrintData;

import java.util.Scanner;

public class MenuHandler {
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Makes a call to the wallet service based on the user input
     * @param service Wallet service containing the wallet account logic
     */
    public void run(WalletService service) {
        boolean exit = false;
        String name;

        PrintData.print("Welcome to CLI Wallet!");
        while (!exit) {
            PrintData.displayMainMenu();
            int option = getOption();

            try {
                switch (option) {
                    case 1:
                        PrintData.print(IConstant.ENTER_NAME);
                        name = SCANNER.nextLine();
                        if (service.checkAccountExists(name)) {
                            throw new AccountAlreadyExistsException(String.format(IConstant.ACCOUNT_ALREADY_EXISTS, name));
                        }
                        double amount = getAmount(IConstant.ENTER_DEPOSIT);
                        getWalletMenu(service.createAccount(name, amount), service);
                        break;
                    case 2:
                        PrintData.print(IConstant.ENTER_NAME);
                        name = SCANNER.nextLine();
                        Account account = service.fetchAccount(name);
                        getWalletMenu(account, service);
                        break;
                    case 3:
                        PrintData.printAccountHolders(service.fetchAccountHolders());
                        break;
                    default:
                        exit = true;
                        break;
                }
            } catch (Exception ex) {
                PrintData.printError(ex.getMessage());
            }


        }
        PrintData.print("Exiting wallet");
        SCANNER.close();
    }

    /**
     * Wallet Menu options for the account holder
     *
     * @param account The account on which operations will be performed
     * @param service Wallet service to perform operation on account
     */
    private static void getWalletMenu(Account account, WalletService service) {
        boolean exit = false;
        while (!exit) {
            PrintData.displayWalletMenu();
            int option = getOption();
            double amount;

            try {
                switch (option) {
                    case 1:
                        double balance = service.getAccountBalance(account.getId());
                        PrintData.print(IConstant.ACCOUNT_BALANCE, balance);
                        break;
                    case 2:
                        amount = getAmount(IConstant.ENTER_ADD_AMOUNT);
                        service.addMoney(amount, account.getId(), false);
                        PrintData.print(IConstant.ADD_SUCCESS, amount);
                        break;
                    case 3:
                        amount = getAmount(IConstant.ENTER_WITHDRAW_AMOUNT);
                        service.withdrawMoney(amount, account.getId(), false);
                        PrintData.print(IConstant.WITHDRAW_SUCCESS, amount);
                        break;
                    case 4:
                        PrintData.print(IConstant.TRANSFER_FUNDS_TO);
                        String transferTo = SCANNER.nextLine();
                        Account targetAccount = service.fetchAccount(transferTo);
                        amount = getAmount(IConstant.ENTER_TRANSFER_AMOUNT);
                        service.transferFunds(account, targetAccount, amount);
                        PrintData.print(IConstant.TRANSFER_SUCCESS);
                        break;
                    case 5:
                        PrintData.printTransactions(service.getTransactionHistory(account.getId()));
                        break;
                    default :
                        exit = true;
                        break;
                }
            } catch (Exception ex) {
                PrintData.printError(ex.getMessage());
            }
        }
    }

    /**
     * Method to select an option
     * @return A valid number for menu
     */
    private static int getOption() {
        PrintData.print(IConstant.CHOOSE_AN_OPTION);
        int option;
        while (!SCANNER.hasNextInt()) {
            PrintData.print(IConstant.INVALID_INPUT);
            SCANNER.next(); // Discarding invalid input
            PrintData.print(IConstant.CHOOSE_AN_OPTION);
        }
        option = SCANNER.nextInt();
        SCANNER.nextLine();
        return option;
    }

    /**
     * Method returns a positive amount and handles negative scenarios
     * @param message Message to be displayed in console
     * @return Returns amount
     */
    private static double getAmount(String message) {
        boolean runLoop = true;
        PrintData.print(message);
        double amount = 0;

        while (runLoop) {
            while (!SCANNER.hasNextDouble()) {
                PrintData.print(IConstant.INVALID_AMOUNT);
                SCANNER.next(); // Discarding invalid input
                PrintData.print(message);
            }

            amount = SCANNER.nextDouble();
            SCANNER.nextLine();

            if (amount > 0) {
                runLoop = false;
            } else {
                PrintData.print(IConstant.ENTER_POSITIVE_AMOUNT);
            }
        }
        return amount;
    }
}
