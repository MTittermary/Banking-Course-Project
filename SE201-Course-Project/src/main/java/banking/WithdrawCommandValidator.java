package banking;

public class WithdrawCommandValidator extends AbstractCommandValidator {

    public WithdrawCommandValidator(Bank bank) {
        super(bank);
    }

    @Override
    protected boolean validateCommand(String[] parts) {
        if (!validateCommandLength(parts)) {
            return false;
        }

        if (!validateAccountId(parts[1])) {
            return false;
        }

        int accountId = Integer.parseInt(parts[1]);
        double amount = Double.parseDouble(parts[2]);
        BankAccount account = bank.getAccount(accountId);

        if (!validateAmount(amount, account)) {
            return false;
        }

        if (account instanceof SavingsAccount) {
            if (!validateSavingsRules(account)) {
                return false;
            }
        }

        if (account instanceof CdAccount) {
            return validateCdRules(amount, account);
        }

        return true;
    }

    private boolean validateCommandLength(String[] parts) {
        return parts.length == 3;
    }

    private boolean validateAccountId(String idString) {
        return super.validateId(idString, true);
    }

    private boolean validateAmount(double amount, BankAccount account) {
        return ((amount <= account.getMaxWithdrawalAmount()) && (amount >= 0));
    }

    private boolean validateSavingsRules(BankAccount account) {
        SavingsAccount sa = (SavingsAccount) account;
        return sa.getWithdrawalsThisMonth() < sa.getMaxWithdrawalsPerMonth();
    }

    private boolean validateCdRules(double amount, BankAccount account) {
        CdAccount cd = (CdAccount) account;
        if (amount < cd.getBalance()) {
            return false;
        }
        return cd.getMonthsSinceCreation() >= 12;
    }
}

