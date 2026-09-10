package Model;

public abstract class Card {
    private String cardNumber;
    private String cardType;

    private double withdrawLimitPerDay;
    private double transferLimitPerDay;
    private double ownTransferLimitPerDay;
    private double depositLimitPerDay;
    private double ownDepositLimitPerDay;

    public Card(String cardNumber, String cardType, double withdrawLimitPerDay, double transferLimitPerDay, double ownTransferLimitPerDay, double depositLimitPerDay, double ownDepositLimitPerDay) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.withdrawLimitPerDay = withdrawLimitPerDay;
        this.transferLimitPerDay = transferLimitPerDay;
        this.ownTransferLimitPerDay = ownTransferLimitPerDay;
        this.depositLimitPerDay = depositLimitPerDay;
        this.ownDepositLimitPerDay = ownDepositLimitPerDay;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public double getWithdrawLimitPerDay() {
        return withdrawLimitPerDay;
    }

    public double getTransferLimitPerDay() {
        return transferLimitPerDay;
    }

    public double getOwnTransferLimitPerDay() {
        return ownTransferLimitPerDay;
    }

    public double getDepositLimitPerDay() {
        return depositLimitPerDay;
    }

    public double getOwnDepositLimitPerDay() {
        return ownDepositLimitPerDay;
    }
}
