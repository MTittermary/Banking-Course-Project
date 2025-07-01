package banking;

import java.util.ArrayList;
import java.util.List;

public class PassTimeProcessor extends AbstractCommandProcessor {

    public PassTimeProcessor(Bank bank) {
        super(bank);
    }

    @Override
    public boolean handles(String command) {
        return command.trim().toLowerCase().startsWith("pass");
    }

    @Override
    public void process(String[] parts) {
        int months = Integer.parseInt(parts[1]);

        for (int i = 0; i < months; i++) {
            processOneMonth();
        }
    }

    private void processOneMonth() {
        List<Integer> accountIds = bank.getAllAccountIds();

        // Process each account
        for (Integer accountId : accountIds) {
            BankAccount account = bank.getAccount(accountId);
            if (account != null) {
                account.setMonthsSinceCreation(account.getMonthsSinceCreation() + 1);
                checkForMinBalance(account);
                calculateAPR(account);
            }
        }

        removeZeroBalanceAccounts();
        resetMonthlyWithdrawals();
    }

    private void calculateAPR(BankAccount account) {
        int loop;

        if (account instanceof CdAccount) {
            loop = 4;
        } else loop = 1;

        for (int i = 0; i < loop; i++) {
                double monthlyInterest = (account.getAPR() / 100) / 12 * account.getBalance();
                account.depositMoney(monthlyInterest);
        }
    }

    private void checkForMinBalance(BankAccount account) {
        if ((account.getBalance() > 0) && (account.getBalance() < 100)) {
            account.withdrawlMoney(25);
        }
    }

    private void removeZeroBalanceAccounts() {
        List<Integer> accountIds = bank.getAllAccountIds();
        List<Integer> accountsToRemove = new ArrayList<>();

        for (Integer accountId : accountIds) {
            BankAccount account = bank.getAccount(accountId);
            if (account != null && account.getBalance() == 0) {
                accountsToRemove.add(accountId);
            }
        }

        for (Integer accountId : accountsToRemove) {
            bank.removeAccount(accountId);
        }
    }

    private void resetMonthlyWithdrawals() {
        List<Integer> accountIds = bank.getAllAccountIds();

        for (Integer accountId : accountIds) {
            BankAccount account = bank.getAccount(accountId);
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).resetWithdrawals();
            }
        }
    }
}
