package com.banking.account;

import com.banking.Database;
import com.banking.entities.Account;
import com.banking.entities.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

public class AccountManager {
    private final Database database;

    public AccountManager(Database database) {
        this.database = database;
    }

    /* FEATURES */

    public void createAccount(String owner) {
        Account account = database.setAccount(
                Account.builder().owner(owner).balance(0.0D).history(new HashSet<>()).build()
        );
        System.out.printf("New account: %s\n", account);
    }

    public void withdraw(String accountId, Double amount) {
        Account account = getAccount(accountId);
        enoughMoney(amount, account);
        account.getHistory().add(
                Transaction.builder().amount(amount).fromAccountId(accountId).date(new Date().getTime()).build()
        );
        account = account.getCloneBuilder().balance(account.getBalance() - amount).build();

        database.setAccount(account);
        System.out.printf("New state: %s\n", account);
    }

    public void deposit(String accountId, Double amount) {
        checkAmount(amount);
        Account account = getAccount(accountId);
        account.getHistory().add(
                Transaction.builder().amount(amount).toAccountId(accountId).date(new Date().getTime()).build()
        );
        account = account.getCloneBuilder().balance(account.getBalance() + amount).build();

        database.setAccount(account);
        System.out.printf("New state: %s\n", account);
    }

    public void history(String accountId) throws IOException {
        Account account = getAccount(accountId);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // TODO: This should be ordered by date
        objectMapper.writeValue(System.out, account.getHistory());
    }

    public void transfer(String from, String to, Double amount) {
        checkAmount(amount);
        Account fromAccount = getAccount(from);
        enoughMoney(amount, fromAccount);
        Account toAccount = getAccount(to);

        fromAccount = fromAccount.getCloneBuilder().balance(fromAccount.getBalance() - amount).build();
        toAccount = toAccount.getCloneBuilder().balance(toAccount.getBalance() + amount).build();


        Transaction transaction = Transaction.builder().amount(amount).fromAccountId(from).toAccountId(to).date(new Date().getTime()).build();
        fromAccount.getHistory().add(transaction);
        toAccount.getHistory().add(transaction);

        database.setAccount(fromAccount);
        database.setAccount(toAccount);

        System.out.printf("New state: %s\n", fromAccount);
        System.out.printf("[DEBUG] New state 'toAccount': %s\n", toAccount);
    }

    public void balance(String accountId) {
        Account account = getAccount(accountId);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.printf("Current balance: %.2f", account.getBalance());
    }

    /* UTIL */

    private void checkAmount(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount has to be positive.");
        }
    }

    private void enoughMoney(Double amount, Account fromAccount) {
        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Not enough money to withdraw.");
        }
    }

    private Account getAccount(String accountId) {
        Account account = database.getAccount(accountId);
        if (account == null) {
            throw new IllegalArgumentException(String.format("Account with id %s not found.", accountId));
        }
        return account;
    }

}
