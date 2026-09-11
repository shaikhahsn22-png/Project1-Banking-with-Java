package Model;

public class CheckingAccount extends Account{
    //overdraft behavior to checkingAccount
    private int overdraftCounter;

    public CheckingAccount(int accountId, Customer owner) {
        super(accountId, owner);
    }

    public int getOverdraftCounter() {
        return overdraftCounter;
    }
}
