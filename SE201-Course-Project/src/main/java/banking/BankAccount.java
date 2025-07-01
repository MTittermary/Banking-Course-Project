package banking;

public class BankAccount {
    private int id;
    protected double balance;
    private double APR;
    private int monthsSinceCreation;

    public BankAccount(int id, double APR) {
        this.id = id;
        this.APR = APR;
        this.balance = 0;
        this.monthsSinceCreation = 0;
    }

    public double getBalance() {
        return balance;
    }

    public double getAPR() {
        return APR;
    }

    public int getId() {
        return id;
    }

    public int getMonthsSinceCreation() { return monthsSinceCreation; }

    public void setMonthsSinceCreation(int months) {
        this.monthsSinceCreation = months;
    }

    public void depositMoney(double deposit) {
        balance += deposit;
    }

    public void withdrawlMoney(double withdrawal) {
        balance -= withdrawal;
        if (balance < 0) {
            balance = 0;
        }
    }

    public double getMaxDepositAmount() {
        return 0.0;
    }

    public double getMaxWithdrawalAmount() { return 0.0; }

    public int getMaxWithdrawalsPerMonth() { return Integer.MAX_VALUE; }
}
