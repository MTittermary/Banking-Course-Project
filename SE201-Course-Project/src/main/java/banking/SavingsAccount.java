package banking;

public class SavingsAccount extends BankAccount{
    private int withdrawalsThisMonth = 0;

    public SavingsAccount(int id, double APR) {
        super(id, APR);
    }

    @Override
    public double getMaxDepositAmount() {
        return 2500.0;
    }

    @Override
    public double getMaxWithdrawalAmount() { return 1000.0;}

    @Override
    public int getMaxWithdrawalsPerMonth() { return 1;}

    public int getWithdrawalsThisMonth() { return withdrawalsThisMonth;}

    @Override
    public void withdrawlMoney(double withdrawal) {
        super.withdrawlMoney(withdrawal);
        incrementWithdrawals();
    }

    public void incrementWithdrawals() { withdrawalsThisMonth++; }

    public void resetWithdrawals() { withdrawalsThisMonth = 0;}
}
