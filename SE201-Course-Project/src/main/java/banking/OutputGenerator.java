package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OutputGenerator {
    private Bank bank;
    private CommandStore commandStore;
    private DecimalFormat decimalFormat;

    public OutputGenerator(Bank bank, CommandStore commandStore) {
        this.bank = bank;
        this.commandStore = commandStore;
        this.decimalFormat = new DecimalFormat("0.00");
        this.decimalFormat.setRoundingMode(RoundingMode.FLOOR);
    }

    public List<String> generateOutput() {
        List<String> output = new ArrayList<>();

        // Add account information in creation order
        for (Integer accountId : bank.getAccountCreationOrder()) {
            BankAccount account = bank.getAccount(accountId);
            if (account != null) { // Only include open accounts
                // Add account state
                output.add(formatAccountState(account));

                // Add transaction history
                List<String> history = bank.getTransactionHistory(accountId);
                output.addAll(history);
            }
        }

        // Add invalid commands
        output.addAll(commandStore.getAll());

        return output;
    }

    private String formatAccountState(BankAccount account) {
        String accountType = getAccountTypeName(account);
        String balance = decimalFormat.format(account.getBalance());
        String apr = decimalFormat.format(account.getAPR());

        return accountType + " " + account.getId() + " " + balance + " " + apr;
    }

    private String getAccountTypeName(BankAccount account) {
        if (account instanceof CheckingAccount) {
            return "Checking";
        } else if (account instanceof SavingsAccount) {
            return "Savings";
        } else if (account instanceof CdAccount) {
            return "Cd";
        }
        return "";
    }
}

