package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandStoreTest {

    private CommandStore store;

    @BeforeEach
    public void setup() {
        store = new CommandStore();
    }

    @Test
    public void store_is_empty_initially() {
        List<String> commands = store.getAll();
        assertTrue(commands.isEmpty());
    }

    @Test
    public void can_add_one_invalid_command() {
        store.addInvalid("invalid command 1");

        List<String> commands = store.getAll();
        assertEquals(1, commands.size());
        assertEquals("invalid command 1", commands.get(0));
    }

    @Test
    public void can_add_multiple_invalid_commands() {
        store.addInvalid("bad 1");
        store.addInvalid("bad 2");
        store.addInvalid("bad 3");

        List<String> commands = store.getAll();
        assertEquals(3, commands.size());
        assertEquals("bad 1", commands.get(0));
        assertEquals("bad 2", commands.get(1));
        assertEquals("bad 3", commands.get(2));
    }

    @Test
    public void retrieve_all_stored_invalid_commands() {
        store.addInvalid("invalid 1");
        store.addInvalid("invalid 2");
        store.addInvalid("invalid 3");

        List<String> allCommands = store.getAll();

        assertEquals(3, allCommands.size());
        assertTrue(allCommands.contains("invalid 1"));
        assertTrue(allCommands.contains("invalid 2"));
        assertTrue(allCommands.contains("invalid 3"));
    }

}

