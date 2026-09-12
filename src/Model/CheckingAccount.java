package Model;

public class CheckingAccount extends Account{
    //overdraft behavior to checkingAccount
    private int overdraftCounter;

    //constructor to create a new checking account
    public CheckingAccount(int accountId, Customer owner) {
        super(accountId, owner);
        this.overdraftCounter = 0;
    }

    //constructor to load existing checking about
    public CheckingAccount(int accountId, double balance, Customer owner,int overdraftCounter, boolean active, Card card) {
        super(accountId, balance, owner, active, card);
        this.overdraftCounter = overdraftCounter;
    }

    public int getOverdraftCounter() {
        return overdraftCounter;
    }
}
