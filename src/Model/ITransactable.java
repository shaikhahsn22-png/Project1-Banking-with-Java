package Model;

public interface ITransactable {
    void withdraw(double amount);
    void deposit(double amount);
    void transfer(double amount, Account destinationAccount);
}
