package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MasterControlTest {

    MasterControl masterControl;
    List<String> input;

    @BeforeEach
    void setUp() {
        input = new ArrayList<>();
        Bank bank = new Bank();
        masterControl = new MasterControl(new CommandValidator(bank), new CommandProcessor(bank), new CommandStore(), bank);
    }

    private void assertSingleCommand(String command, List<String> actual) {
        assertEquals(1, actual.size());
        assertEquals(command, actual.get(0));
    }

    @Test
    void typo_in_create_command_is_invalid() {
        input.add("creat checking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("creat checking 12345678 1.0", actual);
    }

    @Test
    void typo_in_deposit_command_is_invalid() {
        input.add("depositt 12345678 100");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("depositt 12345678 100", actual);
    }

    @Test
    void two_typo_commands_both_invalid() {
        input.add("depositt 12345678 100");
        input.add("creat checking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("depositt 12345678 100", actual.get(0));
        assertEquals("creat checking 12345678 1.0", actual.get(1));
    }

    @Test
    void invalid_to_create_accounts_with_same_ID() {
        input.add("create checking 12345678 1.0");
        input.add("create checking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertEquals("create checking 12345678 1.0", actual.get(1));
    }

    @Test
    void withdraw_savings_above_limit_is_invalid() {
        input.add("create savings 12345678 4.5");
        input.add("deposit 12345678 1500");
        input.add("withdraw 12345678 1500");

        List<String> actual = masterControl.start(input);

        assertEquals("withdraw 12345678 1500", actual.get(2));
    }

    @Test
    void withdraw_savings_second_withdrawal_same_month_is_invalid() {
        input.add("create savings 12345678 4.5");
        input.add("deposit 12345678 1500");
        input.add("withdraw 12345678 500");
        input.add("withdraw 12345678 300");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        assertEquals("withdraw 12345678 300", actual.get(3));
    }

    @Test
    void withdraw_cd_before_12_months_is_invalid() {
        input.add("create cd 12345678 4.5 2000");
        input.add("withdraw 12345678 2000");

        List<String> actual = masterControl.start(input);

        assertEquals("withdraw 12345678 2000", actual.get(1));
    }

    @Test
    void withdraw_negative_amount_is_invalid() {
        input.add("create checking 12345678 4.5");
        input.add("deposit 12345678 500");
        input.add("withdraw 12345678 -100");

        List<String> actual = masterControl.start(input);

        assertEquals("withdraw 12345678 -100", actual.get(2));
    }

    @Test
    void single_savings_account_with_deposit_shows_account_state_and_transaction() {
        input.add("Create savings 12345678 0.6");
        input.add("Deposit 12345678 700");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Savings 12345678 700.00 0.60", actual.get(0));
        assertEquals("Deposit 12345678 700", actual.get(1));
    }

    @Test
    void account_with_decimal_balance_truncates_correctly() {
        input.add("Create checking 12345678 1.234");
        input.add("Deposit 12345678 100.999");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 100.99 1.23", actual.get(0));
        assertEquals("Deposit 12345678 100.999", actual.get(1));
    }

    @Test
    void whole_number_apr_shows_decimal_places() {
        input.add("Create savings 12345678 3");
        input.add("Deposit 12345678 500");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Savings 12345678 500.00 3.00", actual.get(0));
        assertEquals("Deposit 12345678 500", actual.get(1));
    }

    @Test
    void multiple_accounts_shown_in_creation_order() {
        input.add("Create savings 12345678 0.6");
        input.add("Create checking 98765432 0.01");
        input.add("Deposit 12345678 700");
        input.add("Deposit 98765432 300");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        assertEquals("Savings 12345678 700.00 0.60", actual.get(0));
        assertEquals("Deposit 12345678 700", actual.get(1));
        assertEquals("Checking 98765432 300.00 0.01", actual.get(2));
        assertEquals("Deposit 98765432 300", actual.get(3));
    }

    @Test
    void transfer_shows_in_both_account_histories() {
        input.add("Create savings 12345678 0.6");
        input.add("Create checking 98765432 0.01");
        input.add("Deposit 12345678 700");
        input.add("Deposit 98765432 300");
        input.add("Transfer 98765432 12345678 100");

        List<String> actual = masterControl.start(input);

        assertEquals(6, actual.size());
        assertEquals("Savings 12345678 800.00 0.60", actual.get(0));
        assertEquals("Deposit 12345678 700", actual.get(1));
        assertEquals("Transfer 98765432 12345678 100", actual.get(2));
        assertEquals("Checking 98765432 200.00 0.01", actual.get(3));
        assertEquals("Deposit 98765432 300", actual.get(4));
        assertEquals("Transfer 98765432 12345678 100", actual.get(5));
    }

    @Test
    void cd_account_shows_correct_format() {
        input.add("Create cd 23456789 1.2 2000");

        List<String> actual = masterControl.start(input);

        assertEquals(1, actual.size());
        assertEquals("Cd 23456789 2000.00 1.20", actual.get(0));
    }

    @Test
    void closed_account_not_shown_in_output() {
        input.add("Create checking 12345678 1.0");
        input.add("Deposit 12345678 25");
        input.add("Withdraw 12345678 25");
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(0, actual.size());
    }

    @Test
    void invalid_commands_appear_at_end() {
        input.add("Create savings 12345678 0.6");
        input.add("Deposit 12345678 700");
        input.add("Deposit 12345678 5000");  // Invalid - too high
        input.add("Create checking 98765432 0.01");
        input.add("invalid command");  // Invalid command

        List<String> actual = masterControl.start(input);

        assertEquals(5, actual.size());
        assertEquals("Savings 12345678 700.00 0.60", actual.get(0));
        assertEquals("Deposit 12345678 700", actual.get(1));
        assertEquals("Checking 98765432 0.00 0.01", actual.get(2));
        assertEquals("Deposit 12345678 5000", actual.get(3));  // Invalid commands at end
        assertEquals("invalid command", actual.get(4));
    }

    @Test
    void account_with_multiple_transactions_shows_all_in_order() {
        input.add("Create checking 12345678 1.0");
        input.add("Deposit 12345678 100");
        input.add("Withdraw 12345678 50");
        input.add("Deposit 12345678 25");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        assertEquals("Checking 12345678 75.00 1.00", actual.get(0));
        assertEquals("Deposit 12345678 100", actual.get(1));
        assertEquals("Withdraw 12345678 50", actual.get(2));
        assertEquals("Deposit 12345678 25", actual.get(3));
    }

    @Test
    void pass_time_not_included_in_transaction_history() {
        input.add("Create savings 12345678 1.0");
        input.add("Deposit 12345678 1000");
        input.add("Pass 1");  // This should not appear in transaction history
        input.add("Deposit 12345678 500");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Savings 12345678 1500.83 1.00", actual.get(0));  // After interest calculation
        assertEquals("Deposit 12345678 1000", actual.get(1));
        assertEquals("Deposit 12345678 500", actual.get(2));
    }

    @Test
    void balance_truncation_floors_correctly() {
        input.add("Create savings 12345678 1.0");
        input.add("Deposit 12345678 1000");
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        // Balance should be floored: 1000.833... becomes 1000.83
        assertEquals("Savings 12345678 1000.83 1.00", actual.get(0));
        assertEquals("Deposit 12345678 1000", actual.get(1));
    }

    @Test
    void apr_truncation_floors_correctly() {
        input.add("Create checking 12345678 1.999");  // Should floor to 1.99
        input.add("Deposit 12345678 100");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 100.00 1.99", actual.get(0));
        assertEquals("Deposit 12345678 100", actual.get(1));
    }

    @Test
    void empty_input_produces_empty_output() {
        List<String> actual = masterControl.start(input);

        assertEquals(0, actual.size());
    }

    @Test
    void only_invalid_commands_produces_only_invalid_output() {
        input.add("invalid command 1");
        input.add("another invalid");
        input.add("create invalid 123 abc");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("invalid command 1", actual.get(0));
        assertEquals("another invalid", actual.get(1));
        assertEquals("create invalid 123 abc", actual.get(2));
    }

    @Test
    void pass_time_invalid_zero_months() {
        input.add("Create checking 12345678 1.0");
        input.add("Pass 0");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
        assertEquals("Pass 0", actual.get(1)); // Invalid command appears at end
    }

    @Test
    void pass_time_invalid_61_months() {
        input.add("Create checking 12345678 1.0");
        input.add("Pass 61");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
        assertEquals("Pass 61", actual.get(1)); // Invalid command appears at end
    }

    @Test
    void pass_time_valid_boundary_1_month() {
        input.add("Create savings 12345678 1.2");
        input.add("Deposit 12345678 1000");
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Savings 12345678 1001.00 1.20", actual.get(0));
        assertEquals("Deposit 12345678 1000", actual.get(1));
    }

    @Test
    void pass_time_valid_boundary_60_months() {
        input.add("Create savings 12345678 1.2");
        input.add("Deposit 12345678 1000");
        input.add("Pass 60");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        // After 60 months of compound interest at 1.2% APR
        // This would be a significant amount - exact calculation depends on implementation
        assertEquals("Deposit 12345678 1000", actual.get(1));
    }

    @Test
    void pass_time_closes_zero_balance_account() {
        input.add("Create checking 12345678 1.0");
        input.add("Deposit 12345678 100");
        input.add("Withdraw 12345678 100"); // Balance becomes 0
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        // Account should be closed and not appear in output
        assertEquals(0, actual.size());
    }

    @Test
    void pass_time_deducts_minimum_balance_fee() {
        input.add("Create checking 12345678 1.0");
        input.add("Deposit 12345678 50"); // Below $100
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        // 50 - 25 (fee) + interest = 25 + (25 * 0.01/12) = 25.02 (approximately)
        assertEquals("Checking 12345678 25.02 1.00", actual.get(0));
        assertEquals("Deposit 12345678 50", actual.get(1));
    }

    @Test
    void pass_time_fee_creates_zero_balance_then_closes_account() {
        input.add("Create checking 12345678 1.0");
        input.add("Deposit 12345678 25"); // Exactly $25
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        // 25 - 25 (fee) = 0, then account gets closed
        assertEquals(0, actual.size());
    }

    @Test
    void pass_time_no_fee_for_balance_exactly_100() {
        input.add("Create checking 12345678 1.0");
        input.add("Deposit 12345678 100"); // Exactly $100
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 100.08 1.00", actual.get(0));
        assertEquals("Deposit 12345678 100", actual.get(1));
    }

    @Test
    void pass_time_cd_calculates_interest_four_times_per_month() {
        input.add("Create cd 12345678 2.1 2000");
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(1, actual.size());
        // Following the example: 2000 -> 2014.036792893758, truncated to 2014.03
        assertEquals("Cd 12345678 2014.03 2.10", actual.get(0));
    }

    @Test
    void pass_time_multiple_months_accumulates_interest() {
        input.add("Create savings 12345678 3.0");
        input.add("Deposit 12345678 1000");
        input.add("Pass 2");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        // Two months of compound interest at 3% APR
        // Month 1: 1000 * (3/100/12) = 2.5, balance = 1002.5
        // Month 2: 1002.5 * (3/100/12) = 2.50625, balance = 1005.00625, truncated to 1005.00
        assertEquals("Savings 12345678 1005.00 3.00", actual.get(0));
        assertEquals("Deposit 12345678 1000", actual.get(1));
    }

    @Test
    void pass_time_affects_multiple_accounts() {
        input.add("Create savings 12345678 1.0");
        input.add("Create checking 98765432 2.0");
        input.add("Deposit 12345678 1000");
        input.add("Deposit 98765432 500");
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        // Savings: 1000 + (1000 * 0.01/12) = 1000.83
        assertEquals("Savings 12345678 1000.83 1.00", actual.get(0));
        assertEquals("Deposit 12345678 1000", actual.get(1));
        // Checking: 500 + (500 * 0.02/12) = 500.83
        assertEquals("Checking 98765432 500.83 2.00", actual.get(2));
        assertEquals("Deposit 98765432 500", actual.get(3));
    }

    @Test
    void pass_time_closes_account_after_fee_makes_balance_negative() {
        input.add("Create checking 12345678 1.0");
        input.add("Deposit 12345678 20"); // Less than $25 fee
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        // 20 - 25 = -5, which should close the account
        assertEquals(0, actual.size());
    }

    @Test
    void pass_time_processes_fee_before_interest() {
        input.add("Create savings 12345678 10.0"); // High APR for visible effect
        input.add("Deposit 12345678 90"); // Below $100
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Savings 12345678 65.54 10.00", actual.get(0));
        assertEquals("Deposit 12345678 90", actual.get(1));
    }

    @Test
    void pass_time_invalid_negative_months() {
        input.add("Create checking 12345678 1.0");
        input.add("Pass -1");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
        assertEquals("Pass -1", actual.get(1)); // Invalid command appears at end
    }

    @Test
    void pass_time_invalid_non_numeric() {
        input.add("Create checking 12345678 1.0");
        input.add("Pass abc");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
        assertEquals("Pass abc", actual.get(1)); // Invalid command appears at end
    }

    @Test
    void pass_time_truncation_floors_balance_correctly() {
        input.add("Create savings 12345678 1.0");
        input.add("Deposit 12345678 1000");
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        // 1000 * (1/100/12) = 0.833..., balance = 1000.833..., should floor to 1000.83
        assertEquals("Savings 12345678 1000.83 1.00", actual.get(0));
        assertEquals("Deposit 12345678 1000", actual.get(1));
    }

    @Test
    void sample_make_sure_this_passes_unchanged_or_you_will_fail() {
        input.add("Create savings 12345678 0.6");
        input.add("Deposit 12345678 700");
        input.add("Deposit 12345678 5000");
        input.add("creAte cHecKing 98765432 0.01");
        input.add("Deposit 98765432 300");
        input.add("Transfer 98765432 12345678 300");
        input.add("Pass 1");
        input.add("Create cd 23456789 1.2 2000");
        List<String> actual = masterControl.start(input);

        assertEquals(5, actual.size());
        assertEquals("Savings 12345678 1000.50 0.60", actual.get(0));
        assertEquals("Deposit 12345678 700", actual.get(1));
        assertEquals("Transfer 98765432 12345678 300", actual.get(2));
        assertEquals("Cd 23456789 2000.00 1.20", actual.get(3));
        assertEquals("Deposit 12345678 5000", actual.get(4));
    }
}
