package banking;

public class CreateAccountProcessor extends AbstractCommandProcessor {

    public CreateAccountProcessor(Bank bank) {
        super(bank);
    }

    @Override
    public boolean handles(String command) {
        return command.trim().toLowerCase().startsWith("create");
    }

    @Override
    public void process(String[] parts) {
        String accountType = parts[1].toLowerCase();
        int id = Integer.parseInt(parts[2]);
        double apr = Double.parseDouble(parts[3]);

        switch (accountType) {
            case "checking":
                createCheckingAccount(id, apr);
                break;
            case "savings":
                createSavingsAccount(id, apr);
                break;
            case "cd":
                double initialBalance = Double.parseDouble(parts[4]);
                createCdAccount(id, apr, initialBalance);
                break;
            default:
        }
    }

    private void createCheckingAccount(int id, double apr) {
        BankAccount account = new CheckingAccount(id, apr);
        bank.addAccount(account);
    }

    private void createSavingsAccount(int id, double apr) {
        BankAccount account = new SavingsAccount(id, apr);
        bank.addAccount(account);
    }

    private void createCdAccount(int id, double apr, double initialBalance) {
        BankAccount account = new CdAccount(id, apr, initialBalance);
        bank.addAccount(account);
    }
}
