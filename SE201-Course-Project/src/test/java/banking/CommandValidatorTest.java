package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandValidatorTest {
    CommandValidator commandValidator;
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
    }

    @Test
    void valid_create_command() {
        boolean actual = commandValidator.validate("Create checking 12345678 4.5");
        assertTrue(actual);
    }

    @Test
    void valid_cd_create_command() {
        boolean actual = commandValidator.validate("Create cd 12345678 4.5 1500");
        assertTrue(actual);
    }

    @Test
    void valid_deposit_command() {
        Bank bank = new Bank();
        CheckingAccount checkingAccount = new CheckingAccount(12345678, 0);
        bank.addAccount(checkingAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 200");
        assertTrue(actual);
    }

    @Test
    void invalid_command_type() {
        boolean actual = commandValidator.validate("Close 12345678 500");
        assertFalse(actual);
    }

    @Test
    void create_invalid_account_type() {
        boolean actual = commandValidator.validate("Create investment 12345678 4.5");
        assertFalse(actual);
    }

    @Test
    void valid_command_with_mixed_cases() {
        boolean actual = commandValidator.validate("CrEaTe ChEcKiNg 12345678 4.5");
        assertTrue(actual);
    }

    @Test
    void create_cd_account_with_extra_argument() {
        boolean actual = commandValidator.validate("Create cd 33456789 4.5 2000 48");
        assertFalse(actual);
    }

    @Test
    void create_cd_account_with_missing_argument() {
        boolean actual = commandValidator.validate("Create cd 33456789 4.5");
        assertFalse(actual);
    }

    @Test
    void create_checking_account_with_extra_argument() {
        boolean actual = commandValidator.validate("Create checking 23456789 4.5 999");
        assertFalse(actual);
    }

    @Test
    void create_checking_account_with_missing_argument() {
        boolean actual = commandValidator.validate("Create checking 23456789");
        assertFalse(actual);
    }

    @Test
    void create_savings_account_with_extra_argument() {
        boolean actual = commandValidator.validate("Create savings 23456789 4.5 999 ");
        assertFalse(actual);
    }

    @Test
    void create_savings_account_with_missing_argument() {
        boolean actual = commandValidator.validate("Create savings 23456789");
        assertFalse(actual);
    }

    @Test
    void create_account_with_minimum_APR() {
        boolean actual = commandValidator.validate("Create savings 12345678 0");
        assertTrue(actual);
    }

    @Test
    void create_account_with_maximum_APR() {
        boolean actual = commandValidator.validate("Create savings 12345678 10");
        assertTrue(actual);
    }

    @Test
    void create_account_with_APR_below_minimum() {
        boolean actual = commandValidator.validate("Create savings 12345678 -0.1");
        assertFalse(actual);
    }

    @Test
    void create_account_with_APR_above_maximum() {
        boolean actual = commandValidator.validate("Create savings 12345678 10.1");
        assertFalse(actual);
    }

    @Test
    void create_account_with_APR_in_scientific_notation() {
        boolean actual = commandValidator.validate("Create savings 12345678 1.2*10^-2");
        assertFalse(actual);
    }

    @Test
    void create_account_with_APR_that_is_not_a_number() {
        boolean actual = commandValidator.validate("Create savings 12345678 three");
        assertFalse(actual);
    }

    @Test
    void create_account_with_ID_that_is_less_than_8_digits() {
        boolean actual = commandValidator.validate("Create savings 1234567 4.5");
        assertFalse(actual);
    }

    @Test
    void create_account_with_ID_that_is_greater_than_8_digits() {
        boolean actual = commandValidator.validate("Create savings 123456789 4.5");
        assertFalse(actual);
    }

    @Test
    void create_account_with_ID_that_has_non_numeric_characters() {
        boolean actual = commandValidator.validate("Create savings o2E4567B 4.5");
        assertFalse(actual);
    }

    @Test
    void create_account_with_ID_that_is_not_a_whole_number() {
        boolean actual = commandValidator.validate("Create savings 123456.78 4.5");
        assertFalse(actual);
    }

    @Test
    void create_CD_account_with_amount_at_minimum() {
        boolean actual = commandValidator.validate("Create cd 12345678 4.5 1000");
        assertTrue(actual);
    }

    @Test
    void create_CD_account_with_amount_at_maximum() {
        boolean actual = commandValidator.validate("Create cd 12345678 4.5 10000");
        assertTrue(actual);
    }

    @Test
    void create_CD_account_with_amount_below_minimum() {
        boolean actual = commandValidator.validate("Create cd 12345678 4.5 999");
        assertFalse(actual);
    }

    @Test
    void create_CD_account_with_amount_above_maximum() {
        boolean actual = commandValidator.validate("Create cd 12345678 4.5 10001");
        assertFalse(actual);
    }

    @Test
    void create_CD_account_with_amount_of_non_numeric_value() {
        boolean actual = commandValidator.validate("Create cd 12345678 4.5 one-thousand");
        assertFalse(actual);
    }

    @Test
    void create_CD_account_with_amount_of_scientific_notation() {
        boolean actual = commandValidator.validate("Create cd 12345678 4.5 1.2*10^3");
        assertFalse(actual);
    }

    @Test
    void create_account_with_duplicate_ID_is_invalid() {
        Bank bank = new Bank();
        CheckingAccount checkingAccount = new CheckingAccount(12345678, 0);
        bank.addAccount(checkingAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Create savings 12345678 4.5");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_into_cd_account() {
        Bank bank = new Bank();
        CdAccount cdAccount = new CdAccount(12345678, 0, 1001);
        bank.addAccount(cdAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 200");
        assertFalse(actual);
    }

    @Test
    void valid_deposit_of_0() {
        Bank bank = new Bank();
        CheckingAccount checkingAccount = new CheckingAccount(12345678, 0);
        bank.addAccount(checkingAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 0");
        assertTrue(actual);
    }

    @Test
    void invalid_deposit_of_negative_amount() {
        Bank bank = new Bank();
        CheckingAccount checkingAccount = new CheckingAccount(12345678, 0);
        bank.addAccount(checkingAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 -2");
        assertFalse(actual);
    }

    @Test
    void valid_deposit_of_max_amount_into_checking_account() {
        Bank bank = new Bank();
        CheckingAccount checkingAccount = new CheckingAccount(12345678, 0);
        bank.addAccount(checkingAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 1000");
        assertTrue(actual);
    }

    @Test
    void invalid_deposit_of_above_max_amount_into_checking_account() {
        Bank bank = new Bank();
        CheckingAccount checkingAccount = new CheckingAccount(12345678, 0);
        bank.addAccount(checkingAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 1001");
        assertFalse(actual);
    }

    @Test
    void valid_deposit_of_max_amount_into_savings_account() {
        Bank bank = new Bank();
        SavingsAccount savingsAccount = new SavingsAccount(12345678, 0);
        bank.addAccount(savingsAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 2500");
        assertTrue(actual);
    }

    @Test
    void invalid_deposit_of_above_max_amount_into_savings_account() {
        Bank bank = new Bank();
        SavingsAccount savingsAccount = new SavingsAccount(12345678, 0);
        bank.addAccount(savingsAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 2501");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_of_non_int_value() {
        Bank bank = new Bank();
        SavingsAccount savingsAccount = new SavingsAccount(12345678, 0);
        bank.addAccount(savingsAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 two");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_command_with_misspelled_command_word() {
        Bank bank = new Bank();
        SavingsAccount savingsAccount = new SavingsAccount(12345678, 0);
        bank.addAccount(savingsAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Depsit 12345678 2000");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_command_with_extra_command_words() {
        Bank bank = new Bank();
        SavingsAccount savingsAccount = new SavingsAccount(12345678, 0);
        bank.addAccount(savingsAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678 2000 4.5");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_command_with_missing_command_word() {
        Bank bank = new Bank();
        SavingsAccount savingsAccount = new SavingsAccount(12345678, 0);
        bank.addAccount(savingsAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 12345678");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_command_to_invalid_id() {
        Bank bank = new Bank();
        SavingsAccount savingsAccount = new SavingsAccount(12345678, 0);
        bank.addAccount(savingsAccount);
        CommandValidator commandValidator = new CommandValidator(bank);
        boolean actual = commandValidator.validate("Deposit 123456789");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_invalid_account_id_format() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Deposit abc123 100");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_nonexistent_account_id() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Deposit 99999999 100");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_account_with_zero_max_deposit() {
        CdAccount cd = new CdAccount(12345678, 2.5, 1000);
        bank.addAccount(cd);
        boolean actual = commandValidator.validate("Deposit 12345678 100");
        assertFalse(actual);
    }

    @Test
    void valid_deposit_account_with_positive_max_deposit() {
        // Test the boundary condition where maxDepositAmount > 0
        CheckingAccount checking = new CheckingAccount(12345678, 0);
        bank.addAccount(checking);
        boolean actual = commandValidator.validate("Deposit 12345678 100");
        assertTrue(actual);
    }

    @Test
    void invalid_deposit_command_too_few_arguments() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Deposit 12345678");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_command_too_many_arguments() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Deposit 12345678 100 extra");
        assertFalse(actual);
    }

    @Test
    void invalid_deposit_command_no_arguments() {
        boolean actual = commandValidator.validate("Deposit");
        assertFalse(actual);
    }

    @Test
    void valid_withdraw_checking_within_limit() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Withdraw 12345678 400");
        assertTrue(actual);
    }

    @Test
    void invalid_withdraw_checking_above_limit() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Withdraw 12345678 401");
        assertFalse(actual);
    }

    @Test
    void valid_withdraw_savings_within_limit_first_withdrawal() {
        bank.addAccount(new SavingsAccount(12345678, 0));
        boolean actual = commandValidator.validate("Withdraw 12345678 1000");
        assertTrue(actual);
    }

    @Test
    void invalid_withdraw_savings_above_limit() {
        bank.addAccount(new SavingsAccount(12345678, 0));
        boolean actual = commandValidator.validate("Withdraw 12345678 1001");
        assertFalse(actual);
    }

    @Test
    void invalid_withdraw_cd_before_12_months() {
        CdAccount cd = new CdAccount(12345678, 0, 2000);
        bank.addAccount(cd);
        boolean actual = commandValidator.validate("Withdraw 12345678 2000");
        assertFalse(actual);
    }

    @Test
    void invalid_withdraw_cd_partial_withdraw_after_12_months() {
        CdAccount cd = new CdAccount(12345678, 0, 2000);
        cd.setMonthsSinceCreation(12);
        bank.addAccount(cd);
        boolean actual = commandValidator.validate("Withdraw 12345678 1500"); // less than full balance
        assertFalse(actual);
    }

    @Test
    void invalid_withdraw_negative_amount() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Withdraw 12345678 -5");
        assertFalse(actual);
    }

    @Test
    void valid_withdraw_zero_amount() {
        CheckingAccount checkingAccount = new CheckingAccount(12345678, 0);
        bank.addAccount(checkingAccount);
        boolean actual = commandValidator.validate("Withdraw 12345678 0");
        assertTrue(actual);
    }

    @Test
    void valid_withdraw_cd_over_balance_after_12_months_goes_to_zero() {
        CdAccount cd = new CdAccount(12345678, 4.5, 2000);
        cd.setMonthsSinceCreation(12);
        bank.addAccount(cd);
        boolean actual = commandValidator.validate("Withdraw 12345678 2500");
        assertTrue(actual);
        //Check
    }

    @Test
    void valid_withdraw_cd_exact_balance_after_12_months() {
        CdAccount cd = new CdAccount(12345678, 0, 2000);
        cd.setMonthsSinceCreation(12);
        bank.addAccount(cd);
        boolean actual = commandValidator.validate("Withdraw 12345678 2000");
        assertTrue(actual);
    }

    @Test
    void invalid_withdraw_savings_second_withdrawal_in_same_month() {
        SavingsAccount savings = new SavingsAccount(12345678, 0);
        savings.withdrawlMoney(500.0);
        bank.addAccount(savings);
        boolean actual = commandValidator.validate("Withdraw 12345678 500");
        assertFalse(actual);
    }

    @Test
    void invalid_withdraw_command_too_few_arguments() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Withdraw 12345678");
        assertFalse(actual);
    }

    @Test
    void invalid_withdraw_command_too_many_arguments() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Withdraw 12345678 100 extra");
        assertFalse(actual);
    }

    @Test
    void invalid_withdraw_command_no_arguments() {
        boolean actual = commandValidator.validate("Withdraw");
        assertFalse(actual);
    }

    @Test
    void invalid_withdraw_invalid_account_id_format() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Withdraw abc123 100");
        assertFalse(actual);
    }

    @Test
    void invalid_withdraw_nonexistent_account_id() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        boolean actual = commandValidator.validate("Withdraw 99999999 100");
        assertFalse(actual);
    }

    @Test
    void valid_transfer_between_checking_accounts() {
        BankAccount from = new CheckingAccount(12345678, 0);
        BankAccount to = new CheckingAccount(87654321, 0);
        from.depositMoney(200);

        bank.addAccount(from);
        bank.addAccount(to);
        assertTrue(commandValidator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    void valid_transfer_between_checking_and_savings() {
        BankAccount from = new CheckingAccount(12345678, 0);
        BankAccount to = new SavingsAccount(87654321, 0);
        from.depositMoney(200);

        bank.addAccount(from);
        bank.addAccount(to);
        assertTrue(commandValidator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    void invalid_transfer_from_cd_account() {
        bank.addAccount(new CdAccount(12345678, 0, 2000));
        bank.addAccount(new CheckingAccount(87654321, 0));
        assertFalse(commandValidator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    void invalid_transfer_to_cd_account() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        bank.addAccount(new CdAccount(87654321, 0, 2000));
        assertFalse(commandValidator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    void transfer_exceeds_from_account_limit() {
        CheckingAccount from = new CheckingAccount(12345678, 0);
        SavingsAccount to = new SavingsAccount(87654321, 0);
        bank.addAccount(from);
        bank.addAccount(to);
        assertFalse(commandValidator.validate("Transfer 12345678 87654321 600")); // checking limit is 400
    }

    @Test
    void transfer_exceeds_to_account_limit() {
        SavingsAccount from = new SavingsAccount(12345678, 0);
        CheckingAccount to = new CheckingAccount(87654321, 0);
        bank.addAccount(from);
        bank.addAccount(to);
        assertFalse(commandValidator.validate("Transfer 12345678 87654321 3000")); // savings deposit limit is 2500
    }

    //PassTime TestCases

    @Test
    void valid_pass_time_command_with_1_month() {
        assertTrue(commandValidator.validate("Pass 1"));
    }

    @Test
    void valid_pass_time_command_with_12_months() {
        assertTrue(commandValidator.validate("Pass 12"));
    }

    @Test
    void valid_pass_time_command_with_60_months() {
        assertTrue(commandValidator.validate("Pass 60"));
    }

    @Test
    void valid_pass_time_command_case_insensitive() {
        assertTrue(commandValidator.validate("pass 5"));
        assertTrue(commandValidator.validate("PASS 10"));
        assertTrue(commandValidator.validate("PaSs 15"));
    }

    @Test
    void invalid_pass_time_command_with_0_months() {
        assertFalse(commandValidator.validate("Pass 0"));
    }

    @Test
    void invalid_pass_time_command_with_61_months() {
        assertFalse(commandValidator.validate("Pass 61"));
    }

    @Test
    void invalid_pass_time_command_with_negative_months() {
        assertFalse(commandValidator.validate("Pass -5"));
    }

    @Test
    void invalid_pass_time_command_with_non_numeric_months() {
        assertFalse(commandValidator.validate("Pass five"));
    }

    @Test
    void invalid_pass_time_command_with_decimal_months() {
        assertFalse(commandValidator.validate("Pass 5.5"));
    }

    @Test
    void invalid_pass_time_command_with_missing_months() {
        assertFalse(commandValidator.validate("Pass"));
    }

    @Test
    void invalid_pass_time_command_with_extra_arguments() {
        assertFalse(commandValidator.validate("Pass 5 extra"));
    }

    @Test
    void invalid_empty_command() {
        assertFalse(commandValidator.validate(""));
    }

    @Test
    void invalid_transfer_command_with_wrong_number_of_arguments() {

        bank.addAccount(new CheckingAccount(12345678, 0));
        bank.addAccount(new CheckingAccount(87654321, 0));

        assertFalse(commandValidator.validate("Transfer 12345678 87654321"));

        assertFalse(commandValidator.validate("Transfer 12345678 87654321 100 extra"));
    }

    @Test
    void invalid_transfer_with_nonexistent_from_account_id() {

        bank.addAccount(new CheckingAccount(87654321, 0));
        assertFalse(commandValidator.validate("Transfer 99999999 87654321 100"));
    }

    @Test
    void invalid_transfer_with_nonexistent_to_account_id() {

        bank.addAccount(new CheckingAccount(12345678, 0));
        assertFalse(commandValidator.validate("Transfer 12345678 99999999 100"));
    }

    @Test
    void invalid_transfer_between_incompatible_account_types() {

        bank.addAccount(new CdAccount(12345678, 0, 2000));
        bank.addAccount(new CheckingAccount(87654321, 0));
        assertFalse(commandValidator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    void invalid_transfer_with_negative_amount() {

        bank.addAccount(new CheckingAccount(12345678, 0));
        bank.addAccount(new CheckingAccount(87654321, 0));
        assertFalse(commandValidator.validate("Transfer 12345678 87654321 -1"));
    }

    @Test
    void invalid_transfer_exceeds_from_account_withdrawal_limit_boundary() {
        CheckingAccount from = new CheckingAccount(12345678, 0);
        CheckingAccount to = new CheckingAccount(87654321, 0);
        bank.addAccount(from);
        bank.addAccount(to);

        assertTrue(commandValidator.validate("Transfer 12345678 87654321 400"));
    }

    @Test
    void invalid_transfer_exceeds_to_account_deposit_limit_boundary() {
        SavingsAccount from = new SavingsAccount(12345678, 0);
        CheckingAccount to = new CheckingAccount(87654321, 0);
        bank.addAccount(from);
        bank.addAccount(to);

        assertTrue(commandValidator.validate("Transfer 12345678 87654321 1000"));
    }

    @Test
    void invalid_transfer_with_malformed_amount_string() {
        bank.addAccount(new CheckingAccount(12345678, 0));
        bank.addAccount(new CheckingAccount(87654321, 0));
        assertFalse(commandValidator.validate("Transfer 12345678 87654321 abc"));
    }

    @Test
    void invalid_transfer_from_savings_account_exceeding_monthly_withdrawal_limit() {
        SavingsAccount from = new SavingsAccount(12345678, 0);
        CheckingAccount to = new CheckingAccount(87654321, 0);

        from.withdrawlMoney(100);

        bank.addAccount(from);
        bank.addAccount(to);

        assertFalse(commandValidator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    void valid_transfer_from_savings_account_within_monthly_withdrawal_limit() {
        SavingsAccount from = new SavingsAccount(12345678, 0);
        CheckingAccount to = new CheckingAccount(87654321, 0);
        from.depositMoney(200); // Add some balance

        bank.addAccount(from);
        bank.addAccount(to);

        assertTrue(commandValidator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    void invalid_transfer_amount_exceeds_to_account_max_deposit() {
        CheckingAccount from = new CheckingAccount(12345678, 0);
        SavingsAccount to = new SavingsAccount(87654321, 0);

        bank.addAccount(from);
        bank.addAccount(to);

        assertFalse(commandValidator.validate("Transfer 12345678 87654321 2501"));
    }

    @Test
    void test_deposit_allowed_boundary_condition() {
        CdAccount cd = new CdAccount(12345678, 2.5, 1000);
        bank.addAccount(cd);

        boolean actual = commandValidator.validate("Deposit 12345678 1");
        assertFalse(actual);

        CheckingAccount checking = new CheckingAccount(87654321, 0);
        bank.addAccount(checking);

        boolean actual2 = commandValidator.validate("Deposit 87654321 1");
        assertTrue(actual2);
    }

    @Test
    void test_validate_deposit_allowed_with_zero_max_deposit() {
        CdAccount cd = new CdAccount(12345678, 2.5, 1000);
        bank.addAccount(cd);

        boolean result1 = commandValidator.validate("Deposit 12345678 1");
        assertFalse(result1);

        boolean result2 = commandValidator.validate("Deposit 12345678 0.01");
        assertFalse(result2);
    }

    @Test
    void test_withdrawal_boundary_condition_balance_equals_zero() {
        CheckingAccount account = new CheckingAccount(12345678, 0);
        account.depositMoney(100.0);
        bank.addAccount(account);

        assertTrue(commandValidator.validate("Withdraw 12345678 100"));

        assertTrue(commandValidator.validate("Withdraw 12345678 0"));
    }

    @Test
    void test_checking_account_unlimited_withdrawals() {
        CheckingAccount account = new CheckingAccount(12345678, 0);
        account.depositMoney(1000.0);
        bank.addAccount(account);

        for (int i = 0; i < 5; i++) {
            assertTrue(commandValidator.validate("Withdraw 12345678 50"));
            account.withdrawlMoney(50);
        }
    }
}
