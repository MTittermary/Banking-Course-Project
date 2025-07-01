package banking;

public class CheckingAccount extends BankAccount {
    public CheckingAccount(int id, double APR) {
        super(id, APR);
    }

    @Override
    public double getMaxDepositAmount() {
        return 1000.0;
    }

    @Override
    public double getMaxWithdrawalAmount() { return 400.0;}
}
