package banking;

public class TransferProcessor extends AbstractCommandProcessor {

    public TransferProcessor(Bank bank) {
        super(bank);
    }

    @Override
    public boolean handles(String command) {
        return command.toLowerCase().startsWith("transfer");
    }

    @Override
    public void process(String[] parts) {
        int fromId = Integer.parseInt(parts[1]);
        int toId = Integer.parseInt(parts[2]);
        double amount = Double.parseDouble(parts[3]);

        BankAccount fromAccount = bank.getAccount(fromId);
        BankAccount toAccount = bank.getAccount(toId);

        TransferFromAccount(fromAccount, toAccount, amount);

        String transaction = "Transfer " + parts[1] + " " + parts[2] + " " + parts[3];
        bank.addTransaction(fromId, transaction);
        bank.addTransaction(toId, transaction);
    }

    private void TransferFromAccount(BankAccount fromAccount, BankAccount toAccount, double amount) {
        //Takes the smaller amount; either the balance or withdrawal amount
        double amountWithdrawn = Math.min(amount, fromAccount.getBalance());

        amountWithdrawn = Math.min(amountWithdrawn, fromAccount.getMaxWithdrawalAmount());
        amountWithdrawn = Math.min(amountWithdrawn, toAccount.getMaxDepositAmount());

        fromAccount.withdrawlMoney(amountWithdrawn);
        toAccount.depositMoney(amountWithdrawn);
    }
}
