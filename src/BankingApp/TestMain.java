package BankingApp;

import FileIO.UserFileManager;
import Model.Banker;
import Model.CheckingAccount;
import Model.Customer;
import Model.SavingsAccount;

import java.io.IOException;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) throws IOException {
        Customer customer = new Customer("TestUser", 1001, "1234");

        CheckingAccount account = new CheckingAccount(2001, customer);

        System.out.println("Starting balance: " + account.getBalance());

        account.deposit(100);

        System.out.println("Balance after deposit: " + account.getBalance());

        account.deposit(-50);

        System.out.println("Balance after invalid deposit: " + account.getBalance());

        System.out.println("Starting balance: " + account.getBalance());

        account.deposit(100);

        account.withdraw(40);
        System.out.println("Balance after withdrawal: " + account.getBalance());

        account.withdraw(100);
        System.out.println("Balance after invalid withdrawal: " + account.getBalance());


        Customer customer1 = new Customer("Test1", 1001, "1234");
        Customer customer2 = new Customer("Test2", 1002, "1234");

        CheckingAccount account1 = new CheckingAccount(2001, customer1);
        SavingsAccount account2 = new SavingsAccount(2002, customer2);

        account1.deposit(200);

        System.out.println("Before transfer:");
        System.out.println(account1.getBalance());
        System.out.println(account2.getBalance());

        account1.transfer(50, account2);

        System.out.println("After transfer:");
        System.out.println(account1.getBalance());
        System.out.println(account2.getBalance());

        account.deactivateAccount();
        account.deposit(100);
        account.withdraw(50);
    }

}
