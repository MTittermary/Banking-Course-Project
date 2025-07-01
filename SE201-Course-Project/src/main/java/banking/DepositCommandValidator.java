package banking;

public class DepositCommandValidator extends AbstractCommandValidator {

    public DepositCommandValidator(Bank bank) {
        super(bank);
    }

    @Override
    protected boolean validateCommand(String[] parts) {
        String idStr = parts[1];

        if (!validateCommandLength(parts)) {
            return false;
        }

        if (!validateAccountId(idStr)) {
            return false;
        }

        if (!validateDepositAllowed(idStr)) {
            return false;
        }

        if (!validateAmount(parts)) {
            return false;
        }

        return true;
    }

    private boolean validateCommandLength(String[] parts) {
        return parts.length == 3;
    }

    private boolean validateAccountId(String idString) {
        return super.validateId(idString, true);
    }

    private boolean validateDepositAllowed(String idString) {
        int id = Integer.parseInt(idString);
        BankAccount account = bank.getAccount(id);
        return account.getMaxDepositAmount() > 0;
    }

    private boolean validateAmount(String[] parts) {
        double amount;
        int accountId;

        try {
            amount = Double.parseDouble(parts[2]);
            accountId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (amount < 0) {
            return false;
        }

        BankAccount account = bank.getAccount(accountId);
        //Account Id is checked before this function is called so no need to check if account exists

        double maxAllowed = account.getMaxDepositAmount();
        return amount <= maxAllowed;
    }
}

