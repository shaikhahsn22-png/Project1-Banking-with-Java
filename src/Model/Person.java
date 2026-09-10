package Model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public abstract class Person {
    private String username;
    private int id;
    private String encryptedPassword;

    private int failedLoginCounter;
    private LocalDateTime lockedUntil;

    //constructor for a new user creation: hash raw password then storing
    public Person(String username, int id, String password) {
        this(username, id, password, false);
    }

    //constructor finding an existing user from file
    //receive already hashed password, store it directly without hashing it again
    protected   Person(String username, int id, String password, boolean alreadyHashed){
        this.username = username;
        this.id = id;
        if(alreadyHashed){
            this.encryptedPassword = password;
        }else {
            this.encryptedPassword = hashPassword(password);
        }
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

    public void setFailedLoginCounter(int failedLoginCounter) {
        this.failedLoginCounter = failedLoginCounter;
    }

    public int getFailedLoginCounter() {
        return failedLoginCounter;
    }

    public void setTimer(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    //checks the given password by user if it matches the stored one
    public boolean checkPassword(String attemptPassword){
        String hashAttempt = hashPassword(attemptPassword); //convert given password by user to has
        return hashAttempt.equals(this.encryptedPassword); // compare hashed password to the stored one
    }

    //takes normal password, hash it, return the hashed string
    private static String hashPassword(String password) {
        //hashing implementation
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); //store algorithm
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8)); //convert string(password) into bytes

            //create StringBuilder to construct the hexadecimal string
            StringBuilder hexadecimalString = new StringBuilder();

            //loop through byte[] hashedBytes to convert into hexadecimal
            for(byte b : hashedBytes){
                hexadecimalString.append(String.format("%02x", b)); //format byte
            }

            //return the final hash as a string
            return hexadecimalString.toString();

        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException("SHA-256 algorithm is not available." + exception);
        }

    }

    //a method to increment failedLoginCounter
    public void incrementFailedLoginCounter(){
        failedLoginCounter++;
    }

    //a method to reset failedLoginCounter
    public void resetFailedLoginCounter(){
        failedLoginCounter = 0;
    }

    //a method to lock account after 3 failed login for 1m
    public void lockAccount(){
        lockedUntil = LocalDateTime.now().plusMinutes(1);
    }

    //a method to check account if locked
    public boolean idLocked(){
        //return true if account is locked AND current time has not reached lock time
        return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil);
    }
}
