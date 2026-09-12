package Model;

public class SavingsAccount extends Account{

    //constructor to create a new saving account
    public SavingsAccount(int accountId, Customer owner) {
        super(accountId, owner);
    }

    //constructor to load existing saving account
    public SavingsAccount(int accountId, double balance, Customer owner, boolean active, Card card) {
        super(accountId, balance, owner, active, card);
    }

}
