package Model;

public class CheckingAccount extends Account{
    public CheckingAccount(int accountId, double balance, Customer owner, int overdraftCounter, boolean active, Card card) {
        super(accountId, balance, owner, overdraftCounter, active, card);
    }
}
