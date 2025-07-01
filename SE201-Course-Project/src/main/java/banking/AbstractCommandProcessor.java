package banking;

public abstract class AbstractCommandProcessor {
    protected Bank bank;

    protected AbstractCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public abstract boolean handles(String command);
    public abstract void process(String[] commandParts);
}
