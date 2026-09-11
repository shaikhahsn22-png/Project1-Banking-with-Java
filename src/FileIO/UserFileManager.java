package FileIO;

import Model.Account;
import Model.Banker;
import Model.Customer;
import Model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class UserFileManager {

    //creating data folders and files to save data
    private static final String CUSTOMER_DIRECTORY = "data/customers";
    private static final String BANKER_DIRECTORY = "data/bankers";
    private static final String ACCOUNT_DIRECTORY = "data/accounts";

    // method to create a customer and update existing customer details
    public static void saveCustomer(Customer customer) throws IOException {

        //check if folder exist, if not create it
        Path directoryPath = Paths.get(CUSTOMER_DIRECTORY);
        Files.createDirectories(directoryPath);

        //create fileName
        //Customer-<CustomerName>-<CustomerID>
        String fileName = "Customer-" + customer.getUsername() + "-" + customer.getId() + ".txt";

        //combine folder and filename
        Path filePath = directoryPath.resolve(fileName);

        //store info inside the customer file
        String info = "username: " + customer.getUsername() +"\nid: " +customer.getId() +
                        "\nhashedPassword: "+ customer.getEncryptedPassword() +
                        "\nfailedLoginCounter: " + customer.getFailedLoginCounter() +
                        "\nlockedUntil: " + customer.getLockedUntil();
        Files.writeString(filePath, info);

    }

    // method to find customers
    public static Customer findCustomer(String username) throws IOException {

        //to access customers folder
        Path directoryPath = Paths.get(CUSTOMER_DIRECTORY);

        //return null if there are no customer files
       if (!Files.exists(directoryPath)){
           return null;
       }

       try(Stream<Path> files = Files.list(directoryPath)){

           //loop through the files
            for (Path file : files.toList()){
                //convert the filename to a string
                String fileName = file.getFileName().toString();

                if(fileName.startsWith("Customer-" + username + "-")){
                    //read all lines from the file
                    List<String> lines = Files.readAllLines(file);

                    //extract values from each line in the file
                    String storedUsername= lines.get(0).split(": ",2)[1];
                    int storedId = Integer.parseInt(lines.get(1).split(": ",2)[1]);
                    String storedHashedPassword= lines.get(2).split(": ",2)[1];

                    int storedFailedLoginCounter = 0;
                    String storedTimer = "null";

                    if(lines.size() > 3){
                        storedFailedLoginCounter = Integer.parseInt(lines.get(3).split(": ",2)[1]);
                    }

                    if(lines.size() > 4){
                        storedTimer = lines.get(4).split(": ",2)[1];
                    }

                    //recreate customer using the already hashed password
                    Customer customer = new Customer(storedUsername,storedId,storedHashedPassword, true);

                    customer.setFailedLoginCounter(storedFailedLoginCounter);

                    if(!storedTimer.equals("null")){
                        customer.setTimer(LocalDateTime.parse(storedTimer));
                    }

                    return customer;
                }

            }
       }

        return null;
    }

    //method to save a banker details
    public static void saveBanker(Banker banker) throws IOException{
        //check if folder exist, if not create it
        Path directoryPath = Paths.get(BANKER_DIRECTORY);
        Files.createDirectories(directoryPath);

        //create fileName
        //Banker-<BankerName>-<BankerID>
        String fileName = "Banker-" + banker.getUsername() + "-" + banker.getId() + ".txt";

        //combine folder and filename
        Path filePath = directoryPath.resolve(fileName);

        //store info inside the customer file
        String info = "username: " + banker.getUsername() +"\nid: " +banker.getId() +
                "\nhashedPassword: "+ banker.getEncryptedPassword() +
                "\nfailedLoginCounter: " + banker.getFailedLoginCounter() +
                "\nlockedUntil: " + banker.getLockedUntil();
        Files.writeString(filePath, info);
    }

    //method to find banker
    public static Banker findBanker(String username) throws IOException{
        //to access banker folder
        Path directoryPath = Paths.get(BANKER_DIRECTORY);

        //return null if there are no banker files
        if (!Files.exists(directoryPath)){
            return null;
        }
        try(Stream<Path> files = Files.list(directoryPath)){

            //loop through the files
            for (Path file : files.toList()){
                //convert the filename to a string
                String fileName = file.getFileName().toString();

                if(fileName.startsWith("Banker-" + username + "-")){
                    //read all lines from the file
                    List<String> lines = Files.readAllLines(file);

                    //extract values from each line in the file
                    String storedUsername= lines.get(0).split(": ",2)[1];
                    int storedId = Integer.parseInt(lines.get(1).split(": ",2)[1]);
                    String storedHashedPassword= lines.get(2).split(": ",2)[1];

                    int storedFailedLoginCounter = 0;
                    String storedTimer = "null";

                    if(lines.size() > 3){
                        storedFailedLoginCounter = Integer.parseInt(lines.get(3).split(": ",2)[1]);
                    }

                    if(lines.size() > 4){
                        storedTimer = lines.get(4).split(": ",2)[1];
                    }

                    //recreate banker using the already hashed password
                    Banker banker = new Banker(storedUsername,storedId,storedHashedPassword, true);

                    banker.setFailedLoginCounter(storedFailedLoginCounter);

                    if(!storedTimer.equals("null")){
                        banker.setTimer(LocalDateTime.parse(storedTimer));
                    }

                    return banker;
                }

            }
        }

        return null;
    }

    //method to check if user is customer or banker, then save user depending on his role
    public static void saveUser(Person user) throws IOException{
        if(user instanceof Customer){
            saveCustomer((Customer)user);
        } else if(user instanceof Banker){
            saveBanker((Banker) user);
        }
    }

    //method to auto generate customer id
    public static int generateCustomerId() throws IOException{
        Path directoryPath = Paths.get(CUSTOMER_DIRECTORY);

        //if customer folder exists, start id's with 1001
        if(!Files.exists((directoryPath))){
            return 1001;
        }

        int highestId = 1000;

        try(Stream<Path> files = Files.list(directoryPath)){
            for(Path file : files.toList()){
                String fileName = file.getFileName().toString();

                if(fileName.startsWith("Customer-") && file.endsWith(".txt")){
                    String withoutExtension = fileName.replace(".txt", "");
                    String[] parts = withoutExtension.split("-");
                    int id= Integer.parseInt(parts[parts.length - 1]);
                    if(id > highestId){
                        highestId = id;
                    }

                }
            }
        }
        return highestId + 1;
    }

    //method to auto generate account id
    public static int generateAccountId() throws IOException{
        Path directoryPath = Paths.get(ACCOUNT_DIRECTORY);

        if(!Files.exists(directoryPath)){
            return 1001;
        }

        int highestId = 1000;

        try(Stream<Path> files = Files.list(directoryPath)){
            for(Path file : files.toList()){
                String fileName = file.getFileName().toString();

                if(fileName.startsWith("Account-") && file.endsWith(".txt")){
                    String withoutExtension = fileName.replace(".txt", "");
                    String[] parts = withoutExtension.split("-");
                    int id= Integer.parseInt(parts[parts.length - 1]);
                    if(id > highestId){
                        highestId = id;
                    }

                }
            }
        }
        return highestId + 1;
    }

    //method to save account
    public static void saveAccount(Account account) throws IOException{

        //check if folder exist, if not create it
        Path directoryPath = Paths.get(ACCOUNT_DIRECTORY);
        Files.createDirectories(directoryPath);

        // Account-<AccountID>.txt
        String fileName = "Account-" + account.getAccountId() + ".txt";

        Path filePath = directoryPath.resolve(fileName);

        String info = "accountId: " + account.getAccountId() +
                        "\nbalance: " + account.getBalance() +
                        "\nownerUsername: " + account.getOwner().getUsername() +
                        "\nownerId: " + account.getOwner().getId() +
                        "\nactive: " + account.isActive() ;
        Files.writeString(filePath, info);


    }

}
