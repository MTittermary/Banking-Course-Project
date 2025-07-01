package banking;

import java.util.ArrayList;
import java.util.List;

public class CommandProcessor {
    private final Bank bank;
    private List<AbstractCommandProcessor> processors;

    public CommandProcessor(Bank bank) {
        this.bank = bank;
        this.processors = new ArrayList<>();

        processors.add(new CreateAccountProcessor(bank));
        processors.add(new DepositProcessor(bank));
        processors.add(new WithdrawProcessor(bank));
        processors.add(new TransferProcessor(bank));
        processors.add(new PassTimeProcessor(bank));
    }

    public void process(String command) {
        if (command == null || command.isEmpty()) {
            return;
        }

        String[] parts = command.trim().split("\\s");

        for (AbstractCommandProcessor processor : processors) {
            if (processor.handles(command)) {
                processor.process(parts);
                return;
            }
        }
    }
}

