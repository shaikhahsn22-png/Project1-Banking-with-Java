package Model;

public abstract class Account implements ITransactable{
    //properties
    private int accountId;
    private double balance;
    private Customer owner;
    private boolean active;
    private Card card;

    //constructor for new account
    public Account(int accountId, Customer owner) {
        this.accountId = accountId;
        this.balance = 0.0;
        this.owner = owner;
        this.active = true;
        this.card = null;
    }

    //constructor for file loading - existing account
    protected Account(int accountId, double balance, Customer owner, boolean active, Card card) {
        this.accountId = accountId;
        this.balance = balance;
        this.owner = owner;
        this.active = true;
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

    public boolean isActive() {
        return active;
    }

    public Card getCard() {
        return card;
    }

    //account status
    public void deactivateAccount(){
        active = false;
    }

    public void activateAccount(){
        active = true;
    }


    @Override
    public void withdraw(double amount) {
        //check if account is active
        if(!active){
            System.out.println("Account is inactive");
            return;
        }

        //check if amount is 0 or less
        if(amount <= 0){
            System.out.println("Withdraw amount must be grater then 0");
            return;
        }

        //check if amount is grater than balance
        if(amount > balance){
            System.out.println("Insufficient funds");
            return;
        }
        balance -=amount;
        System.out.println("Withdraw successful");
    }

    @Override
    public void deposit(double amount) {
        //check if account is active
        if(!active){
            System.out.println("Account is inactive");
            return;
        }

        //check if amount is 0 or less
        if(amount <= 0){
            System.out.println("Deposit amount must be grater then 0");
            return;
        }
        balance += amount;
        System.out.println("Deposit successful");
    }

    @Override
    public void transfer(double amount, Account destinationAccount) {
        //check if account is active
        if(!active){
            System.out.println("Account is inactive");
            return;
        }

        //check if destinationAccount is active
        if(!destinationAccount.isActive()){
            System.out.println("Destination Account is inactive");
            return;
        }

        //check if amount is 0 or less
        if(amount <= 0){
            System.out.println("Transfer amount must be grater then 0");
            return;
        }

        //check if amount is grater than balance
        if(amount > balance){
            System.out.println("Insufficient funds");
            return;
        }
        balance -=amount;
        destinationAccount.deposit(amount);

        System.out.println("Transfer successful");

    }
}
