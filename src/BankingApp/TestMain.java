package BankingApp;

import FileIO.UserFileManager;
import Model.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) throws IOException {
        List<Transaction> transactions =
                UserFileManager.findTransactionsByAccount(2001);

        System.out.println("Transactions found: " + transactions.size());

        for (Transaction transaction : transactions) {
            System.out.println(
                    transaction.getTransactionId()
                            + " - "
                            + transaction.getType()
                            + " - "
                            + transaction.getAmount()
            );
        }

    }
}
