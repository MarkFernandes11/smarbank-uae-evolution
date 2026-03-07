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

            switch (option) {
                case 1:
                    PrintData.print(IConstant.ENTER_NAME);
                    name = SCANNER.nextLine();
                    if (service.checkAccountExists(name)) {
                        try {
                            throw new AccountAlreadyExistsException(IConstant.ACCOUNT_ALREADY_EXISTS);
                        } catch (Exception ex) {
                            PrintData.printError(ex.getMessage());
                            break;
                        }
                    }
                    double amount = getAmount(IConstant.ENTER_DEPOSIT);
                    getWalletMenu(service.createAccount(name, amount));
                    break;
                case 2:
                    PrintData.print(IConstant.ENTER_NAME);
                    name = SCANNER.nextLine();
                    Account account = null;
                    try {
                        account = service.fetchAccount(name);
                    } catch (Exception ex) {
                        PrintData.printError(ex.getMessage());
                        break;
                    }
                    getWalletMenu(account);
                    break;
                case 3:
                    PrintData.printAccountHolders(service.fetchAccountHolders());
                    break;
                default:
                    exit = true;
                    break;
            }
        }
        PrintData.print("Exiting wallet");
        SCANNER.close();
    }

    /**
     * Wallet Menu options for the account holder
     *
     * @param account The account on which operations will be performed
     */
    private static void getWalletMenu(Account account) {
        boolean exit = false;
        while (!exit) {
            PrintData.displayWalletMenu();
            int option = getOption();
            double amount;

            switch (option) {
                case 1:
                    double balance = account.getBalance();
                    PrintData.print(IConstant.ACCOUNT_BALANCE, balance);
                    break;
                case 2:
                    amount = getAmount(IConstant.ENTER_ADD_AMOUNT);
                    account.addMoney(amount, false);
                    PrintData.print(IConstant.ADD_SUCCESS, amount);
                    break;
                case 3:
                    amount = getAmount(IConstant.ENTER_WITHDRAW_AMOUNT);
                    try {
                        account.withdrawMoney(amount, false);
                        PrintData.print(IConstant.WITHDRAW_SUCCESS, amount);
                    } catch (Exception ex) {
                        PrintData.printError(ex.getMessage());
                    }
                    break;
                case 4:
                    PrintData.print(IConstant.TRANSFER_FUNDS_TO);
                    String transferTo = SCANNER.nextLine();
                    amount = getAmount(IConstant.ENTER_TRANSFER_AMOUNT);
                    try {
                        account.transferFunds(account, transferTo, amount);
                    } catch (Exception ex) {
                        PrintData.printError(ex.getMessage());
                    }
                    break;
                case 5:
                    PrintData.printTransactions(account.getTransactionHistory());
                    break;
                default :
                    exit = true;
                    break;
            }
        }
    }

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

    private static double getAmount(String message) {
        boolean runLoop = true;
        PrintData.print(message);
        double amount = 0;

        while (runLoop) {
            while (!SCANNER.hasNextDouble()) {
                PrintData.print(IConstant.INVALID_AMOUNT);
                SCANNER.next();
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
