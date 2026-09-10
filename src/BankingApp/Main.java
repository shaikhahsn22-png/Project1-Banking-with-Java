package BankingApp;

import FileIO.UserFileManager;
import Model.Banker;
import Model.Customer;
import Model.Person;

import java.io.IOException;
import java.util.Scanner;

import static FileIO.UserFileManager.findCustomer;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n------ ACME Bank Login ------");
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
        boolean loggedIn = false;

        while (!user.idLocked() && !loggedIn){
            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            if(user.checkPassword(password)){
                user.resetFailedLoginCounter();

                UserFileManager.saveUser(user);

                System.out.println("You are successfully logged in");
                if (user instanceof Banker) {
                    bankerMenu((Banker) user, scanner);
                }
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

    //method to print menu
    public static void bankerMenu(Banker banker, Scanner scanner) throws IOException{
        boolean running = true;

        while (running){
            System.out.println("\n------ Banker Menu ------");
            System.out.println("1. Create Customer");
            System.out.println("2. Find Customer");
            System.out.println("3. View My Profile");
            System.out.println("4. Logout");

            System.out.println("Choose a service");
            String choice = scanner.nextLine();

            switch (choice){
                case "1":
                    createCustomer(scanner);
                    break;
                case "2":
                    findCustomer(scanner);
                    break;
                case "3":
                    viewBankerAccount(banker);
                    break;
                case "4":
                    System.out.println("Logged out successfully");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");

            }
        }
    }

    public static void createCustomer(Scanner scanner) throws IOException{
        System.out.println("Enter customer username: ");
        String username =scanner.nextLine();

        if(UserFileManager.findCustomer(username) != null){
            System.out.println("Username already exists");
            return;
        }


        int id = UserFileManager.generateCustomerId();

        System.out.print("Enter customer password: ");
        String password = scanner.nextLine();

        Customer customer = new Customer(username, id, password);

        UserFileManager.saveCustomer(customer);

        System.out.println("Customer created successfully");
        System.out.println("Customer Id: " + id);

    }

    public static void findCustomer(Scanner scanner) throws IOException {

        System.out.print("Enter customer username: ");
        String username = scanner.nextLine();

        Customer customer = UserFileManager.findCustomer(username);

        if (customer == null) {
            System.out.println("Customer does not exist.");
            return;
        }

        System.out.println("Customer found.");
        System.out.println("Username: " + customer.getUsername());
        System.out.println("ID: " + customer.getId());
    }

    public static void viewBankerAccount(Banker banker) {

        System.out.println("\n------ MY ACCOUNT ------");
        System.out.println("Username: " + banker.getUsername());
        System.out.println("Banker ID: " + banker.getId());
    }
}

