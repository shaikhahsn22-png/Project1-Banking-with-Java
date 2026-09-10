package Model;

public class Mastercard extends Card{
    public Mastercard(String cardNumber) {
        super(cardNumber,"Mastercard", 5000, 10000, 20000, 100000, 200000);
    }
}
