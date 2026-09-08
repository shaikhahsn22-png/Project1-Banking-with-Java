package Model;

public class Person {
    private String username;
    private int id;
    private String encryptedPassword;

    public Person(String username, int id, String encryptedPassword) {
        this.username = username;
        this.id = id;
        this.encryptedPassword = encryptedPassword;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}
