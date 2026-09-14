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

    @Override
    public void withdraw(double amount) {
        //check if account is active
        if(!isActive()){
            System.out.println("Account is inactive");
            return;
        }

        //check if amount is 0 or less
        if(amount <= 0){
            System.out.println("Withdraw amount must be grater then 0");
            return;
        }

        //normal withdraw
        if(amount <= getBalance()){
            super.withdraw(amount);
            return;
        }

        //overdraft withdraw logic
        double overdraftFee = 35.0;
        //calculate balance after withdrawal and fee
        double finalBalance = getBalance() - amount - overdraftFee;

        //check overdraft limit
        if(finalBalance < -100){
            System.out.println("Overdraft limit exceeded");
            return;
        }

        //update the balance
        setBalance(finalBalance);

        // increment overdraftCounter
        overdraftCounter++;

        System.out.println("Overdraft used, overdraft fee charge: 35.0");

        //deactivate account after 2 overdraft
        if (overdraftCounter >= 2){
            deactivateAccount();
            System.out.println("Account is deactivated after 2 overdrafts");
        }

        System.out.println("Balance: " + getBalance());
    }

    @Override
    public void deposit(double amount) {
        boolean wasInactive = !isActive(); //to stores the original account state before deposite
        super.deposit(amount);

        if(getBalance() >= 0 && wasInactive){
            activateAccount(); //activate account
            overdraftCounter = 0; //reset overdraft
            System.out.println("Account has been reactivated");
        }
    }

}
