package Model;

public abstract class Account implements ITransactable{
    //properties
    private int accountId;
    private double balance;
    private Customer owner;
    private  int overdraftCounter;
    private boolean active;
    private Card card;

    public Account(int accountId, double balance, Customer owner, int overdraftCounter, boolean active, Card card) {
        this.accountId = accountId;
        this.balance = balance;
        this.owner = owner;
        this.overdraftCounter = overdraftCounter;
        this.active = active;
        this.card = card;
    }

    public int getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public Customer getOwner() {
        return owner;
    }

    public int getOverdraftCounter() {
        return overdraftCounter;
    }

    public boolean isActive() {
        return active;
    }

    public Card getCard() {
        return card;
    }

    @Override
    public void withdraw(double amount) {

    }

    @Override
    public void deposit(double amount) {

    }

    @Override
    public void transfer(double amount, Account destinationAccount) {

    }
}
