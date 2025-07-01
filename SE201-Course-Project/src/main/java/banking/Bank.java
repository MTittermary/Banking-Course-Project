package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bank {
    private HashMap<Number, BankAccount> accounts;
    private List<Integer> accountCreationOrder;
    private HashMap<Integer, List<String>> transactionHistory;

    public Bank() {

        this.accounts = new HashMap<>();
        this.accountCreationOrder = new ArrayList<>();
        this.transactionHistory = new HashMap<>();
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public void addAccount(BankAccount newAccount) {
        accounts.put(newAccount.getId(), newAccount);
        accountCreationOrder.add(newAccount.getId());
        transactionHistory.put(newAccount.getId(), new ArrayList<>());
    }

    public BankAccount getAccount(int id) {
        return accounts.get(id);
    }

    public void deposit(int id, double amount) {
        BankAccount account = accounts.get(id);
        if (account != null) {
            account.depositMoney(amount);
        }
    }

    public void withdraw(int id, double amount) {
        BankAccount account = accounts.get(id);
        if (account != null) {
            account.withdrawlMoney(amount);
        }
    }

    public List<Integer> getAllAccountIds() {
        List<Integer> accountIds = new ArrayList<>();
        for (Number key : accounts.keySet()) {
            accountIds.add((Integer) key);
        }
        return accountIds;
    }

    public void removeAccount(int id) {
        accounts.remove(id);
    }

    public void addTransaction(int accountId, String transaction) {
        List<String> history = transactionHistory.get(accountId);
        if (history != null) {
            history.add(transaction);
        }
    }

    public List<String> getTransactionHistory(int accountId) {
        return transactionHistory.getOrDefault(accountId, new ArrayList<>());
    }

    public List<Integer> getAccountCreationOrder() {
        return new ArrayList<>(accountCreationOrder);
    }
}
