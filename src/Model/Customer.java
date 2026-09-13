package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {
    private List<Account> accounts = new ArrayList<>(); //so when customer created will already have empty list of accounts


    //constructor for creating a new Customer
    public Customer(String username, int id, String password) {
        super(username, id, password);
    }

    //constructor for loading existing customer from file
    public Customer(String username, int id, String password, boolean alreadyHashed){
        super(username, id, password, alreadyHashed);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    //a method to create a new account
    public void addAccount(Account account){
        accounts.add(account);
    }

    //method to check if customer has checking account
    public boolean hasCheckingAccount() {
        for (Account account : accounts) {
            if (account instanceof CheckingAccount) {
                return true;
            }
        }
        return false;
    }

    //method to check if customer has saving account
    public boolean hasSavingAccount(){
        for (Account account : accounts){
            if(account instanceof SavingsAccount){
                return true;
            }
        }
        return false;
    }
}
