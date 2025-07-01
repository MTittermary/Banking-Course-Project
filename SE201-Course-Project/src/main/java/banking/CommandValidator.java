package banking;

public class CommandValidator {
    private Bank bank;
    private CreateCommandValidator createValidator;
    private DepositCommandValidator depositValidator;
    private WithdrawCommandValidator withdrawValidator;
    private TransferCommandValidator transferValidator;
    private PassTimeCommandValidator passTimeValidator;

    public CommandValidator(Bank bank) {
        this.bank = bank;
        this.createValidator = new CreateCommandValidator(bank);
        this.depositValidator = new DepositCommandValidator(bank);
        this.withdrawValidator = new WithdrawCommandValidator(bank);
        this.transferValidator = new TransferCommandValidator(bank);
        this.passTimeValidator = new PassTimeCommandValidator(bank);
    }

    public boolean validate(String command) {
        String[] parts = command.trim().split("\\s");
        if (parts.length <= 1) {
            return false;
        }

        String commandType = parts[0].toLowerCase();

        if (commandType.equals("create")) {
            return createValidator.validate(command);
        } else if (commandType.equals("deposit")) {
            return depositValidator.validate(command);
        } else if (commandType.equals("withdraw")) {
            return withdrawValidator.validate(command);
        } else if (commandType.equals("transfer")) {
            return transferValidator.validate(command);
        } else if (commandType.equals("pass")) {
            return passTimeValidator.validate(command);
        }
        else return false;
    }
}

