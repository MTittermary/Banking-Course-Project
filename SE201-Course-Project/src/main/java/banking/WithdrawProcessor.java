package banking;

public class WithdrawProcessor extends AbstractCommandProcessor {

    public WithdrawProcessor(Bank bank) {
        super(bank);
    }

    @Override
    public boolean handles(String command) {
        return command.trim().toLowerCase().startsWith("withdraw");
    }

    @Override
    public void process(String[] parts) {
        int id = Integer.parseInt(parts[1]);
        double amount = Double.parseDouble(parts[2]);
        BankAccount account = bank.getAccount(id);

        if (account instanceof CheckingAccount) {
            WithdrawCheckingAccount(account, amount);
        } else if (account instanceof SavingsAccount) {
            WithdrawSavingsAccount((SavingsAccount) account, amount);
        } else if (account instanceof CdAccount) {
            WithdrawCdAccount(account, amount);
        }

        String transaction = "Withdraw " + parts[1] + " " + parts[2];
        bank.addTransaction(id, transaction);
    }

    private void WithdrawSavingsAccount(SavingsAccount account, double amount) {
        account.withdrawlMoney(amount);
        account.incrementWithdrawals();
    }

    private void WithdrawCdAccount(BankAccount account, double amount) {
        if (amount >= account.getBalance()) {
            account.withdrawlMoney(account.getBalance()); // zero out
        }
    }

    private void WithdrawCheckingAccount(BankAccount account, double amount) {
        account.withdrawlMoney(amount);
    }
}
