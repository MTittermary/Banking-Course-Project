package banking;

import banking.BankAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankAccountTest {

    BankAccount bankAccount;

    @Test
    public void account_created_with_specified_APR() {
        bankAccount = new BankAccount(0,9.8);
        double actual = bankAccount.getAPR();

        assertEquals(9.8, actual);
    }

    @Test
    public void account_created_with_specified_ID() {
        bankAccount = new BankAccount(12345678, 1);
        double actual = bankAccount.getId();

        assertEquals(12345678, actual);
    }

    @Test
    public void account_balance_increases_when_deposit_is_made() {
        bankAccount = new BankAccount(12345678, 1);
        bankAccount.depositMoney(101);
        double actual = bankAccount.getBalance();

        assertEquals(101, actual);
        }

    @Test
    public void account_balance_decreases_when_withdrawal_is_made() {
        bankAccount = new BankAccount(12345678, 1);
        bankAccount.depositMoney(100);
        bankAccount.withdrawlMoney(20);
        double actual = bankAccount.getBalance();

        assertEquals(80, actual);
    }

    @Test
    public void if_withdrawal_is_greater_than_balance_then_balance_is_$0() {
        bankAccount = new BankAccount(12345678, 1);
        bankAccount.depositMoney(20);
        bankAccount.withdrawlMoney(100);
        double actual = bankAccount.getBalance();

        assertEquals(0, actual);
    }

    @Test
    public void depositing_twice_into_an_account_works_as_expected() {
        bankAccount = new BankAccount(12345678, 1);
        bankAccount.depositMoney(100);
        bankAccount.depositMoney(50);
        double actual = bankAccount.getBalance();

        assertEquals(150, actual);
    }

    @Test
    public void withdrawing_twice_from_account_works_as_expected() {
        bankAccount = new BankAccount(12345678, 1);
        bankAccount.depositMoney(100);
        bankAccount.withdrawlMoney(20);
        bankAccount.withdrawlMoney(25);
        double actual = bankAccount.getBalance();

        assertEquals(55, actual);
    }
}
