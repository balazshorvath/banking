package com.banking;

import com.banking.entities.Account;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Database {
    private Map<String, Account> accounts = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String dbFile;

    public Database(String dbFile) {
        this.dbFile = dbFile;
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public Account setAccount(Account account) {
        Account res = null;
        if (account.getAccountId() == null) {
            res = account.getCloneBuilder().accountId(generateAccountId()).build();
            accounts.put(res.getAccountId().toString(), res);
        } else {
            res = accounts.put(account.getAccountId().toString(), account);
        }
        return res;
    }

    private String generateAccountId() {
        Long value = null;
        do {
            value = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
        } while (accounts.containsKey(value.toString()));
        return value.toString();
    }

    public void deinit() throws IOException {
        File old = new File(dbFile);
        old.renameTo(new File(dbFile + ".old"));
        File resultFile = new File(dbFile);
        mapper.writeValue(resultFile, accounts.values());

    }

    public void init() throws IOException {
        File db = new File(dbFile);
        if (!db.exists()) {
            return;
        }
        List<Account> accountList = mapper.readValue(
                db,
                new TypeReference<List<Account>>() {
                }
        );
        accounts = accountList.stream().collect(Collectors.toMap(Account::getAccountId, o -> o));
    }
}
