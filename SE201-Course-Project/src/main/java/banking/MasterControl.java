package banking;

import java.util.List;

public class MasterControl {
    private CommandValidator commandValidator;
    private CommandProcessor commandProcessor;
    private CommandStore commandStore;
    private Bank bank;

    public MasterControl(CommandValidator commandValidator, CommandProcessor commandProcessor, CommandStore commandStore, Bank bank) {
        this.commandValidator = commandValidator;
        this.commandProcessor = commandProcessor;
        this.commandStore = commandStore;
        this.bank = bank;
    }

    public List<String> start(List<String> input) {
        for (String command : input) {
            if (commandValidator.validate(command)) {
                commandProcessor.process(command);
            } else {
                commandStore.addInvalid(command);
            }
        }

        OutputGenerator outputGenerator = new OutputGenerator(bank, commandStore);
        return outputGenerator.generateOutput();
    }
}
