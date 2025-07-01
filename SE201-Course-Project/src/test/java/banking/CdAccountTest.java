package banking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CdAccountTest {

    CdAccount cdAccount;

    @Test
    public void cd_account_created_with_specified_balance() {
        cdAccount = new CdAccount(0, 0, 108);
        double actual = cdAccount.getBalance();

        assertEquals(108, actual);
    }
}
