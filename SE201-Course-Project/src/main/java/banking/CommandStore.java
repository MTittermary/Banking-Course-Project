package banking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandStore {

    private List<String> invalidCommands;

    public CommandStore() {
        this.invalidCommands = new ArrayList<>();
    }

    public void addInvalid(String command) {
        invalidCommands.add(command);
    }

    public List<String> getAll() {
        return Collections.unmodifiableList(invalidCommands);
    }
}

