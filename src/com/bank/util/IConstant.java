package com.bank.util;

public class IConstant {

    // Enter messages
    public static final String ENTER_NAME = "Enter account holder's name";
    public static final String ENTER_ADD_AMOUNT = "Enter amount you want to add";
    public static final String ENTER_WITHDRAW_AMOUNT = "Enter amount you want to withdraw";
    public static final String ENTER_POSITIVE_AMOUNT = "Amount should be greater than 0";
    public static final String CHOOSE_AN_OPTION = "Choose an option";
    public static final String ENTER_DEPOSIT = "Enter a amount to deposit in your account";
    public static final String TRANSFER_FUNDS_TO = "Enter account holder's name to transfer funds";
    public static final String ENTER_TRANSFER_AMOUNT = "Enter an amount you want to transfer";


    // Error messages
    public static final String PRINT_ERROR = "Error: %s";
    public static final String INVALID_AMOUNT = "Invalid amount entered";
    public static final String INVALID_INPUT = "Invalid input";

    // Exception messages
    public static final String ACCOUNT_ALREADY_EXISTS = "Account already exists with name %s";
    public static final String ACCOUNT_NOT_FOUND = "Account not found with name %s";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance in your account. Available balance is %.2f";
    public static final String SELF_TRANSFER_ERROR = "Self transfer not supported";

    // General messages
    public static final String ACCOUNTS_NOT_CREATED = "No accounts created";
    public static final String ACCOUNT_BALANCE = "You have %.2f in your account";
    public static final String ADD_SUCCESS = "%.2f added to your account.";
    public static final String WITHDRAW_SUCCESS = "%.2f withdrawn from your account";
    public static final String NO_TRANSACTIONS = "No transactions to be displayed";
    public static final String TRANSFER_SUCCESS = "Funds transferred successfully";

}
