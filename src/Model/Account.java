package Model;

public abstract class Account implements ITransactable{
    //properties
    private int accountId;
    private double balance;
    private Customer owner;
    private boolean active;
    private  int overdraftCounter;
    private Card card;

    //constructor for new account
    public Account(int accountId, Customer owner) {
        this.accountId = accountId;
        this.balance = 0.0;
        this.owner = owner;
        this.active = true;
        this.overdraftCounter = 0;
        this.card = null;
    }

    //constructor for file loading - existing account
    protected Account(int accountId, double balance, Customer owner, boolean active, int overdraftCounter, Card card) {
        this.accountId = accountId;
        this.balance = 0.0;
        this.owner = owner;
        this.active = true;
        this.overdraftCounter = 0;
        this.card = null;
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
