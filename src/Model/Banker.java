package Model;

public class Banker extends Person{

    //constructor for creating a new banker
    public Banker(String username, int id, String password) {
        super(username, id, password);
    }

    //constructor for loading an existing banker from file
    public Banker(String username, int id, String password, boolean alreadyHashed) {
        super(username, id, password, alreadyHashed);
    }

}
