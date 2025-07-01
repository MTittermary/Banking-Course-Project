package banking;

public class CreateCommandValidator extends AbstractCommandValidator {

    public CreateCommandValidator(Bank bank) {
        super(bank);
    }

    @Override
    protected boolean validateCommand(String[] parts) {
        if (!validateCommandLength(parts)) {
            return false;
        }

        String accountStr = parts[1].toLowerCase();
        String idStr = parts[2];
        String aprStr = parts[3];

        if (!validateAccountType(accountStr)) {
            return false;
        }

        if (!validateAccountId(idStr)) {
            return false;
        }

        if (!validateAccountApr(aprStr)) {
            return false;
        }

        if (accountStr.equals("cd")) {
            return validateCdAmount(parts[4]);
        }

        return true;
    }

    protected boolean validateAccountType(String accountType) {
        accountType = accountType.toLowerCase();
        return accountType.equals("checking") ||
                accountType.equals("savings") ||
                accountType.equals("cd");
    }

    private boolean validateCommandLength(String[] parts) {
        String accountType = parts[1].toLowerCase();
        if ((accountType.equals("checking") || accountType.equals("savings")) && parts.length == 4) {
            return true;
        } else if (accountType.equals("cd") && parts.length == 5) {
            return true;
        }
        return false;
    }

    private boolean validateAccountId(String accountId) {
        return super.validateId(accountId, false);
    }

    private boolean validateAccountApr(String accountApr) {
        try {
            double apr = Double.parseDouble(accountApr);
            return apr >= 0 && apr <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateCdAmount(String amountString) {
        try {
            double amount = Double.parseDouble(amountString);
            return amount >= 1000 && amount <= 10000;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

