package banking;

public abstract class AbstractCommandValidator {
    protected Bank bank;

    protected AbstractCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        if (command == null) {
            return false;
        }

        String[] parts = command.trim().split("\\s");
        return validateCommand(parts);
    }

    protected abstract boolean validateCommand(String[] parts);

    protected boolean validateId(String accountId, boolean shouldExist) {
        try {
            int id = Integer.parseInt(accountId);

            if (accountId.length() != 8) {
                return false;
            }

            boolean accountExists = bank.getAccount(id) != null;
            return shouldExist == accountExists;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}


