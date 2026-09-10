package Model;

public class SavingsAccount extends Account{
    public SavingsAccount(int accountId, double balance, Customer owner, int overdraftCounter, boolean active, Card card) {
        super(accountId, balance, owner, overdraftCounter, active, card);
    }
}
