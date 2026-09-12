package BankingApp;

import FileIO.UserFileManager;
import Model.*;

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
                } else if (user instanceof Customer) {
                    customerMenu((Customer) user, scanner);
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

    //banker's menu method
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

    //customer's menu
    public static void customerMenu(Customer customer, Scanner scanner) throws IOException{
        boolean running = true;

        while (running){
            System.out.println("\n------ Customer Menu ------");
            System.out.println("1. View Account Details");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Logout");

            System.out.println("Choose a service");
            String choice = scanner.nextLine();

            switch (choice){
                case "1":
                    viewAccountDetails(customer);
                    break;
                case "2":
                    deposit(customer,scanner);
                    break;
                case "3":
                    withdraw(customer,scanner);
                    break;
                case "4":
                    transfer(customer,scanner);
                    break;
                case "5":
                    System.out.println("Logged out successfully");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");

            }
        }
    }

    public static void viewAccountDetails(Customer customer) throws IOException{
        //check account
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.println("\n---- ACCOUNT DETAILS ----");

        for (Account account : customer.getAccounts()){
            System.out.println("Account ID: " + account.getAccountId());
            System.out.println("Account Type: " + account.getClass().getSimpleName());
            System.out.println("Balance: " + account.getBalance());
            System.out.println("Status: " + (account.isActive()? "Active" : "Inactive"));
            System.out.println("--------------------");
        }

    }

    public static void deposit(Customer customer, Scanner scanner) throws IOException{
        //check account
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        //use customer's first account
        Account account = customer.getAccounts().get(0);

        System.out.println("Enter deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        account.deposit(amount);

        UserFileManager.saveAccount(account);
        System.out.println("Current balance: " + account.getBalance());
    }

    public static void withdraw(Customer customer, Scanner scanner) throws IOException{
        //check account
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        //use customer's first account
        Account account = customer.getAccounts().get(0);

        System.out.println("Enter withdraw amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        account.withdraw(amount);

        UserFileManager.saveAccount(account);
        System.out.println("Current balance: " + account.getBalance());
    }

    public static void transfer(Customer customer, Scanner scanner) throws IOException{
        //check account
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        //use customer's first account
        Account sourceAccount = customer.getAccounts().get(0);

        System.out.println("Enter destination account ID: ");
        int destinationAccountId = Integer.parseInt(scanner.nextLine());

        Account destinationAccount = UserFileManager.findAccountById(destinationAccountId);
         if(destinationAccount == null){
             System.out.println("Destination account does not exist");
             return;
         }

        System.out.println("Enter transfer amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        sourceAccount.transfer(amount, destinationAccount);

        UserFileManager.saveAccount(sourceAccount);
        UserFileManager.saveAccount(destinationAccount);

        System.out.println("Current balance: " + sourceAccount.getBalance());
    }
}

