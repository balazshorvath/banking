package com.banking.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;


@JsonDeserialize(builder = Transaction.TransactionBuilder.class)
@Builder
@Value
public class Transaction {
    private Double amount;
    private Long date;

    /**
     * Optional, if null, this transaction was a deposit.
     */
    private String fromAccountId;

    /**
     * Optional, if null, this transaction was a withdrawal.
     */
    private String toAccountId;


//    public static TransactionBuilder builder() {
//        return new TransactionBuilder();
//    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TransactionBuilder {
    }
}
