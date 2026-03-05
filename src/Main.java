import com.bank.Account;

import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static void main(String[] args) {
        boolean exit = false;

        System.out.println("Welcome to CLI Wallet!");
        while (!exit) {
            System.out.println("1. Create Account");
            System.out.println("2. Exit");
            int option = Main.getOption();

            switch (option) {
                case 1:
                    System.out.println("Enter account holder's name");
                    String name = SCANNER.nextLine();
                    double amount = Main.getAmount();
                    Account account = new Account(name, amount);
                    Main.getWalletMenu(account);
                    break;
                default:
                    exit = true;
                    break;
            }
        }
        System.out.println("Exiting wallet");

        SCANNER.close();
    }

    /**
     * Menu options for the account holder
     *
     * @param account The account on which operations will be performed
     */
    private static void getWalletMenu(Account account) {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Check your balance");
            System.out.println("2. Add money");
            System.out.println("3. Withdraw money");
            System.out.println("4. Display Transaction History");
            System.out.println("5. Main menu");

            int option = Main.getOption();

            switch (option) {
                case 1:
                    account.getBalance();
                    break;
                case 2:
                    System.out.println("Enter amount you want to add");
                    double amt1 = Double.parseDouble(SCANNER.nextLine());
                    account.addMoney(amt1);
                    break;
                case 3:
                    System.out.println("Enter amount you want to withdraw");
                    double amt2 = Double.parseDouble(SCANNER.nextLine());
                    account.withdrawMoney(amt2);
                    break;
                case 4:
                    account.getTransactionHistory();
                    break;
                default :
                    exit = true;
                    break;
            }
        }
    }

    private static int getOption() {
        System.out.println("Choose an option");
        int option;

        while (!SCANNER.hasNextInt()) {
            System.out.println("Invalid input");
            SCANNER.next(); // Discarding invalid input
            System.out.println("Choose an option");
        }

        option = SCANNER.nextInt();
        SCANNER.nextLine();

        return option;
    }

    private static double getAmount() {
        boolean runLoop = true;
        double amount = 0;
        while (runLoop) {
            System.out.println("Enter a amount to deposit in your account");
            try {
                amount = Double.parseDouble(SCANNER.nextLine());
                if (amount < 0) {
                    System.out.println("Enter a positive amount");
                    runLoop = true;
                } else {
                    runLoop = false;
                }
            } catch (Exception ex) {
                System.out.println("Invalid amount entered");
            }
        }
        return amount;
    }
}