package banking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavingsAccountTest {

    SavingsAccount savingsAccount;

    @Test
    public void account_created_with_balance_of_$0() {
        savingsAccount = new SavingsAccount(0, 0);
        double actual = savingsAccount.getBalance();

        assertEquals(0, actual);
    }
}
