package BankingApp;

import FileIO.UserFileManager;
import Model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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
            System.out.println("2. Create Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Transaction History");
            System.out.println("7. Logout");

            System.out.println("Choose a service");
            String choice = scanner.nextLine();

            switch (choice){
                case "1":
                    viewAccountDetails(customer);
                    break;
                case "2":
                    createAccount(customer, scanner);
                    break;
                case "3":
                    deposit(customer,scanner);
                    break;
                case "4":
                    withdraw(customer,scanner);
                    break;
                case "5":
                    transfer(customer,scanner);
                    break;
                case "6":
                    viewTransactionHistory(customer, scanner);
                    break;
                case "7":
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
        System.out.println("Deposit to: ");
        System.out.println("1. My Account");
        System.out.println("2. Another Account");

        String choice = scanner.nextLine();
        Account account;
        boolean ownAccount = false;

        if(choice.equals("1")){
            ownAccount = true;
            //check account
            if (customer.getAccounts().isEmpty()) {
                System.out.println("You don't have any accounts");
                return;
            }

            //if customer has only one account
            if(customer.getAccounts().size() == 1){
                account = customer.getAccounts().get(0);
            } else {//if has more than one account

                System.out.println("Choose an account: ");
                for (int i =0; i < customer.getAccounts().size(); i++){
                    Account currentAccount = customer.getAccounts().get(i);

                    System.out.println((i + 1) + ". " + currentAccount.getClass().getSimpleName() + " - " + currentAccount.getAccountId());
                }

                int accountChoice = Integer.parseInt(scanner.nextLine());

                if(accountChoice < 1 || accountChoice > customer.getAccounts().size()){
                    System.out.println("Invalid account choice");
                    return;
                }
                account = customer.getAccounts().get(accountChoice -1);
            }

        } else if(choice.equals("2")){
            System.out.println("Enter account ID to deposit into: ");
            int destinationAccountId = Integer.parseInt(scanner.nextLine());
            account = UserFileManager.findAccountById(destinationAccountId);
            if(account == null){
                System.out.println("Account does not exist");
                return;
            }
        }else {
            System.out.println("Invalid choice");
            return;
        }

        System.out.println("Enter deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        account.deposit(amount);
        UserFileManager.saveAccount(account);

        //transaction
        //generate transaction ID
        int transactionId = UserFileManager.generateTransactionId();
        //create DEPOSIT transaction
        Transaction transaction = new Transaction(transactionId, LocalDateTime.now(), TransactionType.DEPOSIT,amount,account.getBalance(),null,account);
        //save transaction file
        UserFileManager.saveTransaction(transaction);

        if (ownAccount) {
            System.out.println("Current balance: " + account.getBalance());
        } else {
            System.out.println("Deposit successful to account " + account.getAccountId());
        }

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

        double balanceBefore = account.getBalance();
        account.withdraw(amount);

        //if balance did not change, withdrawal failed
        if (account.getBalance() == balanceBefore) {
            return;
        }

        UserFileManager.saveAccount(account);
        System.out.println("Current balance: " + account.getBalance());

        //transaction
        //generate transaction ID
        int transactionId = UserFileManager.generateTransactionId();
        //create WITHDRAW transaction
        Transaction transaction = new Transaction(transactionId, LocalDateTime.now(), TransactionType.WITHDRAW,amount,account.getBalance(),account,null);
        //save transaction file
        UserFileManager.saveTransaction(transaction);

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

        double balanceBefore = sourceAccount.getBalance();

        System.out.println("Enter transfer amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        sourceAccount.transfer(amount, destinationAccount);

        // if source balance did not change, transfer failed
        if (sourceAccount.getBalance() == balanceBefore) {
            return;
        }

        //save both accounts
        UserFileManager.saveAccount(sourceAccount);
        UserFileManager.saveAccount(destinationAccount);

        //transaction
        //generate transaction ID
        int transactionId = UserFileManager.generateTransactionId();
        //create DEPOSIT transaction
        Transaction transaction = new Transaction(transactionId, LocalDateTime.now(), TransactionType.TRANSFER,amount,sourceAccount.getBalance(),sourceAccount,destinationAccount);
        //save transaction file
        UserFileManager.saveTransaction(transaction);

        System.out.println("Current balance: " + sourceAccount.getBalance());
    }

    public static void createAccount(Customer customer, Scanner scanner) throws IOException{

        System.out.println("Choose Account Type: ");
        System.out.println("1. Checking Account");
        System.out.println("2. Saving Account");

        String choice = scanner.nextLine();

        if(choice.equals("1")){
            // check if customer already has a checking account
            if (customer.hasCheckingAccount()){
                System.out.println("You already have a checking account");
                return;
            }

            int accountId = UserFileManager.generateAccountId();
            CheckingAccount checkingAccount = new CheckingAccount(accountId, customer);
            customer.addAccount(checkingAccount);
            UserFileManager.saveAccount(checkingAccount);

            System.out.println("Checking account created successfully.");
            System.out.println("Account ID: " +accountId);

        } else if (choice.equals("2")){
            // check if customer already has a saving account
            if (customer.hasSavingAccount()){
                System.out.println("You already have a checking account");
                return;
            }

            int accountId = UserFileManager.generateAccountId();
            SavingsAccount savingsAccount = new SavingsAccount(accountId, customer);
            customer.addAccount(savingsAccount);
            UserFileManager.saveAccount(savingsAccount);

            System.out.println("Saving account created successfully.");
            System.out.println("Account ID: " +accountId);

        } else {
            System.out.println("Invalid account type.");
        }

    }

    public static void viewTransactionHistory(Customer customer, Scanner scanner) throws IOException{
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        Account account;
        // if customer has only one account
        if (customer.getAccounts().size() == 1) {
            account = customer.getAccounts().get(0);
        } else {

            System.out.println("Choose account:");

            for (int i = 0; i < customer.getAccounts().size(); i++) {

                Account currentAccount = customer.getAccounts().get(i);

                System.out.println(
                        (i + 1) + ". "
                                + currentAccount.getClass().getSimpleName()
                                + " - "
                                + currentAccount.getAccountId()
                );
            }

            int choice = Integer.parseInt(scanner.nextLine());

            account = customer.getAccounts().get(choice - 1);
        }

        List<Transaction> transactions = UserFileManager.findTransactionsByAccount(account.getAccountId());

        if (transactions.isEmpty()) {
            System.out.println("No transactions found");
            return;
        }

        System.out.println("\n---- Transaction History ----");

        for (Transaction transaction : transactions) {

            System.out.println("ID: " + transaction.getTransactionId()
                            + " \nType: " + transaction.getType()
                            + " \nAmount: " + transaction.getAmount()
                            + " \nDate: " + transaction.getDateTime()
                            + " \nBalance After: " + transaction.getBalanceAfter());
            System.out.println("-------------");
        }
    }

}

