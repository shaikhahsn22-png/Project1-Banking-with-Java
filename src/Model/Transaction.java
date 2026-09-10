package Model;

import java.time.LocalDateTime;

public class Transaction {
    //properties
    private int transactionId;
    private LocalDateTime dateTime;
    private TransactionType type;
    private double amount;
    private double balanceAfter;
    private Account sender;
    private Account recipient;

    public Transaction(int transactionId, LocalDateTime dateTime, TransactionType type, double amount, double balanceAfter, Account sender, Account recipient) {
        this.transactionId = transactionId;
        this.dateTime = dateTime;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.sender = sender;
        this.recipient = recipient;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public Account getSender() {
        return sender;
    }

    public Account getRecipient() {
        return recipient;
    }
}
