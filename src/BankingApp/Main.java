package BankingApp;

import FileIO.UserFileManager;
import Model.Banker;
import Model.Customer;
import Model.Person;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username: "); //ask for username
        String username= scanner.nextLine(); //store username

        //search customer first
        Person user = UserFileManager.findCustomer(username);

        if(user == null){
            user = UserFileManager.findBanker(username);
        }

        //if user is found, then fine the rule of the user
        if(user instanceof Customer){
            System.out.println("Role: Customer");
        } else if (user instanceof Banker){
            System.out.println("Role: Banker");
        }

        //check if user account is locked
        if(user.idLocked()){
            System.out.println("Account is locked, try again later!");
            return;
        }

        while (!user.idLocked()){
            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            if(user.checkPassword(password)){
                user.resetFailedLoginCounter();

                UserFileManager.saveUser(user);

                System.out.println("You are successfully logged in");
                break;
            }

            user.incrementFailedLoginCounter();
            System.out.println("Incorrect password");

            if(user.getFailedLoginCounter() >=3){
                user.lockAccount();

                UserFileManager.saveUser(user);

                System.out.println("Account is locked for 1 minute due too many failed attempts.");
                break;
            }

            // save failed attempt count
            UserFileManager.saveUser(user);
        }




    }
}
