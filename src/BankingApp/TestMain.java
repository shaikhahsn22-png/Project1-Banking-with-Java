package BankingApp;

import FileIO.UserFileManager;
import Model.*;

import java.io.IOException;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) throws IOException {
        System.out.println(
                "Next account ID: " + UserFileManager.generateAccountId()
        );

        Customer shaikha = UserFileManager.findCustomerBasic("Shaikha");

        int shaikhaAccountId = UserFileManager.generateAccountId();

        CheckingAccount shaikhaAccount =
                new CheckingAccount(shaikhaAccountId, shaikha);

        shaikha.addAccount(shaikhaAccount);

        UserFileManager.saveAccount(shaikhaAccount);

        Customer ahmed = UserFileManager.findCustomerBasic("Ahmed");

        int ahmedAccountId = UserFileManager.generateAccountId();

        CheckingAccount ahmedAccount =
                new CheckingAccount(ahmedAccountId, ahmed);

        ahmed.addAccount(ahmedAccount);

        UserFileManager.saveAccount(ahmedAccount);


    }
}
