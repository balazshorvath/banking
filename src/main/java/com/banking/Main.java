package com.banking;

import com.banking.account.AccountManager;

import java.io.IOException;

public class Main {
    private static Database database;
    private static AccountManager accountManager;

    public static void main(String[] args) {
        try {
            init();
            process(args);
            deinit();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException occurred while trying to save the new state of the database.");
            printHelp();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Error: %s\n", e.getMessage());
            printHelp();
        }
    }

    private static void deinit() throws IOException {
        database.deinit();
    }

    private static void process(String[] args) throws IOException {
        if (args.length < 1) {
            printHelp();
            return;
        }
        Features features = Features.valueOf(args[0].toUpperCase());
        switch (features) {
            case WITHDRAW:
                if (args.length != 3) {
                    printHelp();
                    return;
                }
                accountManager.withdraw(args[1], Double.parseDouble(args[2]));
                break;
            case DEPOSIT:
                if (args.length != 3) {
                    printHelp();
                    return;
                }
                accountManager.deposit(args[1], Double.parseDouble(args[2]));
                break;
            case CREATE:
                if (args.length != 2) {
                    printHelp();
                    return;
                }
                accountManager.createAccount(args[1]);
                break;
            case HISTORY:
                if (args.length != 2) {
                    printHelp();
                    return;
                }
                accountManager.history(args[1]);
                break;
            case BALANCE:
                if (args.length != 2) {
                    printHelp();
                    return;
                }
                accountManager.balance(args[1]);
                break;
            case TRANSFER:
                if (args.length != 4) {
                    printHelp();
                    return;
                }
                accountManager.transfer(args[1], args[2], Double.valueOf(args[3]));
                break;
        }

    }

    private static void printHelp() {
        System.out.println(
                "Usage: \n" +
                        "WITHDRAW\taccountId\t\tamount\n" +
                        "DEPOSIT\t\taccountId\t\tamount\n" +
                        "CREATE\t\towner\n" +
                        "HISTORY\t\taccountId\n" +
                        "BALANCE\t\taccountId\n" +
                        "TRANSFER\taccountId\t\treceiverAccountId\t\tamount"
        );
    }

    private static void init() throws IOException {
        database = new Database("db.json");
        database.init();
        accountManager = new AccountManager(database);
    }

    private enum Features {
        WITHDRAW,
        DEPOSIT,
        CREATE,
        HISTORY,
        BALANCE,
        TRANSFER
    }
}
