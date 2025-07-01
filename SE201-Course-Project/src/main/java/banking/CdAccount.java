package banking;

public class CdAccount extends BankAccount {
    public CdAccount(int id, double APR, double initialBalance) {
        super(id, APR);
        this.balance = initialBalance;
    }

    @Override
    public double getMaxDepositAmount() {
        return 0.0;
    }

    @Override
    public double getMaxWithdrawalAmount() { return Double.POSITIVE_INFINITY;}

}
