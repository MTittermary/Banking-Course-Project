package banking;

public class TransferCommandValidator extends AbstractCommandValidator {

    public TransferCommandValidator(Bank bank) {
        super(bank);
    }

    @Override
    protected boolean validateCommand(String[] parts) {
        if (!validateCommandLength(parts)) {
            return false;
        }

        String fromIdStr = parts[1];
        String toIdStr = parts[2];
        String amountStr = parts[3];

        if (!validateIdsExist(fromIdStr, toIdStr)) {
            return false;
        }

        int fromId = Integer.parseInt(fromIdStr);
        int toId = Integer.parseInt(toIdStr);
        BankAccount fromAccount = bank.getAccount(fromId);
        BankAccount toAccount = bank.getAccount(toId);

        if (!validateAccountTypes(fromAccount, toAccount)) {
            return false;
        }

        if (!validateTransferLimits(fromAccount, toAccount, amountStr)) {
            return false;
        }

        return validateSavingsWithdrawalLimit(fromAccount);
    }

    private boolean validateCommandLength(String[] parts) {
        return parts.length == 4;
    }

    private boolean validateIdsExist(String fromIdStr, String toIdStr) {
        return super.validateId(fromIdStr, true) && super.validateId(toIdStr, true);
    }

    private boolean validateAccountTypes(BankAccount from, BankAccount to) {
        return (from instanceof CheckingAccount || from instanceof SavingsAccount) && (to instanceof CheckingAccount || to instanceof SavingsAccount);
    }

    private boolean validateTransferLimits(BankAccount from, BankAccount to, String amountStr) {
        double amount;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            return false;
        }

        if (amount < 0) return false;
        if (amount > from.getMaxWithdrawalAmount()) return false;
        return !(amount > to.getMaxDepositAmount());
    }

    private boolean validateSavingsWithdrawalLimit(BankAccount fromAccount) {
        if (fromAccount instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) fromAccount;
            return savings.getWithdrawalsThisMonth() < savings.getMaxWithdrawalsPerMonth();
        }
        return true;
    }
}
