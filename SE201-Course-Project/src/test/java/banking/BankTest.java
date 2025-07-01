package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankTest {

    Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
    }

    @Test
    public void when_bank_is_created_it_has_no_accounts() {
        int actual = bank.getNumberOfAccounts();

        assertEquals(0, actual)      ;
    }

    @Test
    public void when_an_account_is_added_the_bank_has_one_account() {
        BankAccount newAccount = new SavingsAccount(12345678, 1.5);
        bank.addAccount(newAccount);
        int actual = bank.getNumberOfAccounts();

        assertEquals(1, actual);
    }

    @Test
    public void when_two_accounts_are_added_to_the_bank_it_has_two_accounts() {
        BankAccount oneAccount = new SavingsAccount(12345678, 1.5);
        bank.addAccount(oneAccount);
        BankAccount twoAccount = new CheckingAccount(87654321, 1.5);
        bank.addAccount(twoAccount);
        int actual = bank.getNumberOfAccounts();

        assertEquals(2, actual);
    }

    @Test
    public void when_retrieving_one_account_from_the_bank_the_correct_account_is_retrieved() {
        BankAccount oneAccount = new SavingsAccount(12345678, 1.5);
        bank.addAccount(oneAccount);
        BankAccount twoAccount = new CheckingAccount(87654321, 1.5);
        bank.addAccount(twoAccount);
        BankAccount actual = bank.getAccount(12345678);

        assertEquals(oneAccount, actual);
    }

    @Test
    public void when_depositing_through_bank_correct_account_gets_money() {
        BankAccount oneAccount = new SavingsAccount(12345678, 1.5);
        bank.addAccount(oneAccount);
        BankAccount twoAccount = new CheckingAccount(87654321, 1.5);
        bank.addAccount(twoAccount);
        bank.deposit(12345678, 223);
        double actual = bank.getAccount(12345678).getBalance();

        assertEquals(223, actual);
    }

    @Test
    public void when_withdrawing_through_bank_correct_account_loses_money() {
        BankAccount oneAccount = new SavingsAccount(12345678, 1.5);
        bank.addAccount(oneAccount);
        BankAccount twoAccount = new CheckingAccount(87654321, 1.5);
        bank.addAccount(twoAccount);
        bank.deposit(12345678, 223);
        bank.withdraw(12345678, 23);
        double actual = bank.getAccount(12345678).getBalance();

        assertEquals(200, actual);
    }

    @Test
    public void when_depositing_twice_through_banks_correct_account_gets_money() {
        BankAccount oneAccount = new SavingsAccount(12345678, 1.5);
        bank.addAccount(oneAccount);
        bank.deposit(12345678, 223);
        bank.deposit(12345678, 220);
        double actual = bank.getAccount(12345678).getBalance();

        assertEquals(443, actual);
    }

    @Test
    public void when_withdrawing_twice_through_bank_correct_account_loses_money() {
        BankAccount oneAccount = new CheckingAccount(12345678, 1.5);
        bank.addAccount(oneAccount);
        bank.deposit(12345678, 400);
        bank.withdraw(12345678, 20);
        bank.withdraw(12345678, 19.5);
        double actual = bank.getAccount(12345678).getBalance();

        assertEquals(360.5, actual);
    }
}
