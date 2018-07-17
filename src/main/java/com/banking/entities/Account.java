package com.banking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@JsonDeserialize(builder = Account.AccountBuilder.class)
@Builder
@Value
public class Account {
    private String owner;
    private String accountId;
    private Double balance;
    private List<Transaction> history;

    @JsonIgnore
    public AccountBuilder getCloneBuilder() {
        return Account.builder().balance(balance).owner(owner)
                      .accountId(accountId).history(history);
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class AccountBuilder {
    }
}
