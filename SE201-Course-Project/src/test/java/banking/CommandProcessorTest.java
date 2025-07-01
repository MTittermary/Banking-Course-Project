package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandProcessorTest {

    private Bank bank;
    private CommandProcessor processor;

    @BeforeEach
    public void setup() {
        bank = new Bank();
        processor = new CommandProcessor(bank);
    }

    @Test
    public void createCheckingAccount_command_createsAccount() {
        String command = "create checking 12345678 1.0";

        processor.process(command);

        BankAccount account = bank.getAccount(12345678);
        assertNotNull(account);
        assertTrue(account instanceof CheckingAccount);
        assertEquals(12345678, account.getId());
        assertEquals(1.0, account.getAPR());
        assertEquals(0.0, account.getBalance());
    }

    @Test
    public void createSavingsAccount_command_createsAccount() {
        String command = "create savings 12345678 1.0";

        processor.process(command);

        BankAccount account = bank.getAccount(12345678);
        assertNotNull(account);
        assertTrue(account instanceof SavingsAccount);
        assertEquals(12345678, account.getId());
        assertEquals(1.0, account.getAPR());
        assertEquals(0.0, account.getBalance());
    }

    @Test
    public void createCdAccount_command_createsAccountWithInitialBalance() {
        String command = "create cd 87654321 2.0 5000";

        processor.process(command);

        BankAccount account = bank.getAccount(87654321);
        assertNotNull(account);
        assertTrue(account instanceof CdAccount);
        assertEquals(87654321, account.getId());
        assertEquals(2.0, account.getAPR());
        assertEquals(5000.0, account.getBalance());
    }

    @Test
    public void deposit_command_depositsMoneyIntoAccount() {
        bank.addAccount(new CheckingAccount(12345678, 1.5));

        String command = "deposit 12345678 100";

        processor.process(command);

        BankAccount account = bank.getAccount(12345678);
        assertEquals(100, account.getBalance());
    }

    @Test
    public void deposit_command_addsMoneyToExistingBalance() {
        CheckingAccount account = new CheckingAccount(12345678, 1.5);
        account.depositMoney(50);
        bank.addAccount(account);

        String command = "deposit 12345678 100";

        processor.process(command);

        assertEquals(150, account.getBalance());
    }

    //For Withdrawal Commands
    @Test
    public void withdraw_checking_within_balance_and_limit() {
        CheckingAccount account = new CheckingAccount(12345678, 4.5);
        account.depositMoney(500);
        bank.addAccount(account);

        processor.process("withdraw 12345678 300");

        assertEquals(200, account.getBalance());
    }

    @Test
    public void withdraw_checking_over_balance_results_in_zero_balance() {
        CheckingAccount account = new CheckingAccount(12345678, 4.5);
        account.depositMoney(300);
        bank.addAccount(account);

        processor.process("withdraw 12345678 400");

        assertEquals(0, account.getBalance());
    }

    @Test
    public void withdraw_checking_just_over_balance_results_in_zero_balance() {
        CheckingAccount account = new CheckingAccount(12345678, 4.5);
        account.depositMoney(300);
        bank.addAccount(account);

        processor.process("withdraw 12345678 301");

        assertEquals(0, account.getBalance());
    }

    @Test
    public void withdraw_checking_just_under_balance_results_in_zero_balance() {
        CheckingAccount account = new CheckingAccount(12345678, 4.5);
        account.depositMoney(300);
        bank.addAccount(account);

        processor.process("withdraw 12345678 299");

        assertEquals(1, account.getBalance());
    }

    @Test
    public void withdraw_savings_within_limit_first_withdrawal() {
        SavingsAccount account = new SavingsAccount(12345678, 4.5);
        account.depositMoney(1500);
        bank.addAccount(account);

        processor.process("withdraw 12345678 1000");

        assertEquals(500, account.getBalance());
    }

    @Test
    public void withdraw_cd_full_after_12_months_sets_balance_zero() {
        CdAccount cd = new CdAccount(12345678, 4.5, 2000);
        cd.setMonthsSinceCreation(12);
        bank.addAccount(cd);

        processor.process("withdraw 12345678 2000");

        assertEquals(0, cd.getBalance());
    }

    @Test
    public void withdraw_cd_partial_after_12_months_does_not_change_balance() {
        CdAccount cd = new CdAccount(12345678, 4.5, 2000);
        cd.setMonthsSinceCreation(12);
        bank.addAccount(cd);

        processor.process("withdraw 12345678 1500");

        assertEquals(2000, cd.getBalance());
    }

    @Test
    public void withdraw_cd_over_balance_after_12_months_sets_balance_zero() {
        CdAccount cd = new CdAccount(12345678, 4.5, 2000);
        cd.setMonthsSinceCreation(12);
        bank.addAccount(cd);

        processor.process("withdraw 12345678 2500");

        assertEquals(0, cd.getBalance());
    }

    @Test
    public void withdraw_zero_amount_does_nothing() {
        CheckingAccount account = new CheckingAccount(12345678, 4.5);
        account.depositMoney(500);
        bank.addAccount(account);

        processor.process("withdraw 12345678 0");

        assertEquals(500, account.getBalance());
    }

    @Test
    void valid_transfer_from_checking_to_checking() {
        CheckingAccount from = new CheckingAccount(12345678, 1.0);
        CheckingAccount to = new CheckingAccount(87654321, 1.0);
        from.depositMoney(500);
        bank.addAccount(from);
        bank.addAccount(to);

        processor.process("transfer 12345678 87654321 300");

        assertEquals(200, from.getBalance());
        assertEquals(300, to.getBalance());
    }

    @Test
    void valid_transfer_from_savings_to_checking() {
        SavingsAccount from = new SavingsAccount(12345678, 1.0);
        CheckingAccount to = new CheckingAccount(87654321, 1.0);
        from.depositMoney(1000);
        bank.addAccount(from);
        bank.addAccount(to);

        processor.process("transfer 12345678 87654321 500");

        assertEquals(500, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    @Test
    void valid_transfer_from_checking_to_savings() {
        CheckingAccount from = new CheckingAccount(12345678, 1.0);
        SavingsAccount to = new SavingsAccount(87654321, 1.0);
        from.depositMoney(2000);
        bank.addAccount(from);
        bank.addAccount(to);

        processor.process("transfer 12345678 87654321 800");

        assertEquals(1600, from.getBalance());
        assertEquals(400, to.getBalance());
    }

    @Test
    void transfer_from_savings_to_savings_successfully() {
        SavingsAccount from = new SavingsAccount(12345678, 1.0);
        SavingsAccount to = new SavingsAccount(87654321, 1.0);
        from.depositMoney(500);
        bank.addAccount(from);
        bank.addAccount(to);

        processor.process("transfer 12345678 87654321 300");

        assertEquals(200, from.getBalance());
        assertEquals(300, to.getBalance());
    }


    @Test
    void valid_transfer_only_available_amount_when_insufficient_funds() {
        CheckingAccount from = new CheckingAccount(12345678, 1.0);
        CheckingAccount to = new CheckingAccount(87654321, 1.0);
        from.depositMoney(200);
        bank.addAccount(from);
        bank.addAccount(to);

        processor.process("transfer 12345678 87654321 500");

        assertEquals(0, from.getBalance());
        assertEquals(200, to.getBalance());
    }

    @Test
    void valid_savings_transfer_increments_withdrawal_counter() {
        SavingsAccount from = new SavingsAccount(12345678, 1.0);
        CheckingAccount to = new CheckingAccount(87654321, 1.0);
        from.depositMoney(1000);
        bank.addAccount(from);
        bank.addAccount(to);

        processor.process("transfer 12345678 87654321 400");

        assertEquals(600, from.getBalance());
        assertEquals(400, to.getBalance());
        assertEquals(1, from.getWithdrawalsThisMonth());
    }

    //PassTime TestCases
    @Test
    void pass_time_closes_accounts_with_zero_balance() {
        CheckingAccount account = new CheckingAccount(12345678, 1.0);
        bank.addAccount(account);

        processor.process("Pass 1");

        assertNull(bank.getAccount(12345678));
        assertEquals(0, bank.getNumberOfAccounts());
    }

    @Test
    void pass_time_deducts_minimum_balance_fee_for_accounts_below_100() {
        CheckingAccount account = new CheckingAccount(12345678, 6.0);
        account.depositMoney(50);
        bank.addAccount(account);

        processor.process("Pass 1");

        BankAccount retrievedAccount = bank.getAccount(12345678);
        assertEquals(25.125, retrievedAccount.getBalance()); // Balance + APR calculation
    }

    @Test
    void pass_time_does_not_deduct_fee_for_accounts_100_or_above() {
        CheckingAccount account = new CheckingAccount(12345678, 1.0);
        account.depositMoney(150);
        bank.addAccount(account);

        processor.process("Pass 1");

        BankAccount retrievedAccount = bank.getAccount(12345678);
        assertTrue(retrievedAccount.getBalance() > 150); // Should have interest added
    }

    @Test
    void pass_time_accrues_apr_monthly() {
        CheckingAccount account = new CheckingAccount(12345678, 12.0); // 12% APR = 1% monthly
        account.depositMoney(1000);
        bank.addAccount(account);

        processor.process("Pass 1");

        BankAccount retrievedAccount = bank.getAccount(12345678);
        assertEquals(1010, retrievedAccount.getBalance(), 0.01);
    }

    @Test
    void pass_time_processes_multiple_months() {
        CheckingAccount account = new CheckingAccount(12345678, 12.0);
        account.depositMoney(1000);
        bank.addAccount(account);

        processor.process("Pass 2");

        BankAccount retrievedAccount = bank.getAccount(12345678);
        // After 1 month: 1000 + 10 = 1010
        // After 2 months: 1010 + 10.10 = 1020.10
        assertEquals(1020.10, retrievedAccount.getBalance(), 0.01);
    }

    @Test
    void pass_time_removes_account_that_goes_to_zero_after_fee() {
        CheckingAccount account = new CheckingAccount(12345678, 1.0);
        account.depositMoney(25);
        bank.addAccount(account);

        processor.process("Pass 1");

        assertNull(bank.getAccount(12345678));
        assertEquals(0, bank.getNumberOfAccounts());
    }

    @Test
    void pass_time_resets_savings_withdrawal_counter() {
        SavingsAccount account = new SavingsAccount(12345678, 1.0);
        account.depositMoney(1000);
        account.withdrawlMoney(500); // This increments withdrawal counter
        bank.addAccount(account);

        assertEquals(1, account.getWithdrawalsThisMonth());

        processor.process("Pass 1");

        BankAccount retrievedAccount = bank.getAccount(12345678);
        assertEquals(0, ((SavingsAccount) retrievedAccount).getWithdrawalsThisMonth());
    }

    @Test
    void pass_time_increments_months_since_creation() {
        CheckingAccount account = new CheckingAccount(12345678, 1.0);
        account.depositMoney(1000);
        bank.addAccount(account);

        assertEquals(0, account.getMonthsSinceCreation());

        processor.process("Pass 3");

        BankAccount retrievedAccount = bank.getAccount(12345678);
        assertEquals(3, retrievedAccount.getMonthsSinceCreation());
    }

    @Test
    void pass_time_processes_cd_account_correctly() {
        CdAccount account = new CdAccount(12345678, 2.1, 2000);
        bank.addAccount(account);

        processor.process("Pass 1");

        BankAccount retrievedAccount = bank.getAccount(12345678);
        assertEquals(2014.0367928937578, retrievedAccount.getBalance(), 0.01); // 2000 + interest
        assertEquals(1, retrievedAccount.getMonthsSinceCreation());
    }

    @Test
    void pass_time_processes_multiple_accounts() {
        CheckingAccount checking = new CheckingAccount(12345678, 6.0);
        SavingsAccount savings = new SavingsAccount(87654321, 12.0);
        checking.depositMoney(1000);
        savings.depositMoney(2000);
        bank.addAccount(checking);
        bank.addAccount(savings);

        processor.process("Pass 1");

        assertEquals(1005, bank.getAccount(12345678).getBalance(), 0.01); // 0.5% monthly
        assertEquals(2020, bank.getAccount(87654321).getBalance(), 0.01); // 1% monthly
    }

    @Test
    void pass_time_handles_edge_case_balance_exactly_100() {
        CheckingAccount account = new CheckingAccount(12345678, 1.0);
        account.depositMoney(100);
        bank.addAccount(account);

        processor.process("Pass 1");

        BankAccount retrievedAccount = bank.getAccount(12345678);
        // Should not deduct fee, only add interest
        assertTrue(retrievedAccount.getBalance() > 100);
    }
}
