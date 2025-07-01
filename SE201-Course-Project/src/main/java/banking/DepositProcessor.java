package banking;

public class DepositProcessor extends AbstractCommandProcessor {

    public DepositProcessor(Bank bank) {
        super(bank);
    }

    @Override
    public boolean handles(String command) {
        return command.trim().toLowerCase().startsWith("deposit");
    }

    @Override
    public void process(String[] parts) {
        int id = Integer.parseInt(parts[1]);
        double amount = Double.parseDouble(parts[2]);
        processDeposit(id, amount);

        String transaction = "Deposit " + parts[1] + " " + parts[2];
        bank.addTransaction(id, transaction);
    }

    private void processDeposit(int accountId, double amount) {
        bank.deposit(accountId, amount);
    }
}
