package BankingApp;

import FileIO.UserFileManager;
import Model.Banker;
import Model.Customer;

import java.io.IOException;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) throws IOException {
        //Banker banker1 = new Banker("Ali", 123,"banker123");
        //UserFileManager.saveBanker(banker1);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username: "); //ask for username
        String username= scanner.nextLine(); //store username

        Banker banker = UserFileManager.findBanker(username);

        if(banker == null){
            System.out.println("Banker does not exist.");
            return;
        }

        if(banker.idLocked()){
            System.out.println("Account is locked, try again later!");
            return;
        }

        while (!banker.idLocked()){
            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            if(banker.checkPassword(password)){
                banker.resetFailedLoginCounter();
                System.out.println("You are successfully logged in");
                break;
            }

            banker.incrementFailedLoginCounter();
            System.out.println("Incorrect password");

            if(banker.getFailedLoginCounter() >=3){
                banker.lockAccount();
                UserFileManager.saveBanker(banker);
                System.out.println("Account is locked for 1 minute due too many failed attempts.");
                break;
            }

            // save failed attempt count
            UserFileManager.saveBanker(banker);

        }

    }

}
