package banking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckingAccountTest {

    CheckingAccount checkingAccount;

    @Test
    public void checking_account_created_with_balance_of_$0() {
        checkingAccount = new CheckingAccount(0, 0);
        double actual = checkingAccount.getBalance();

        assertEquals(0, actual);
    }
}
