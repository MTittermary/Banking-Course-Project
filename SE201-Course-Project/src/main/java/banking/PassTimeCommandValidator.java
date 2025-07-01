package banking;

public class PassTimeCommandValidator extends AbstractCommandValidator {
    public PassTimeCommandValidator(Bank bank) {
        super(bank);
    }

    @Override
    protected boolean validateCommand(String[] parts) {
        if (!validateCommandLength(parts)) {
            return false;
        }

        if (!validateMonthsValue(parts)) {
            return false;
        }

        return true;
    }

    private boolean validateCommandLength(String[] parts) {
        return parts.length == 2;
    }

    private boolean validateMonthsValue(String[] parts) {
        try {
            int months = Integer.parseInt(parts[1]);
            return months >= 1 && months <= 60;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
