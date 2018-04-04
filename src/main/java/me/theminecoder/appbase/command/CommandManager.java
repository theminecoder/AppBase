package me.theminecoder.appbase.command;

import me.theminecoder.appbase.util.ConsoleColor;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * @author theminecoder
 */
public final class CommandManager {

    private static final Pattern commandNamePattern = Pattern.compile("[a-zA-Z0-9_-]*");

    private static final Map<String, Command> commandMap = new TreeMap<>();

    static {
        commandMap.put("help", new HelpCommand());
    }

    private CommandManager() {
    }

    public static void registerCommand(String name, Command command) {
        if (!commandNamePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid command name! (No Regex match: " + commandNamePattern.pattern() + ")");
        }

        name = name.toLowerCase();
        if (commandMap.containsKey(name)) {
            throw new IllegalStateException("Command already registered with that name!");
        }

        commandMap.put(name, command);
    }

    public static Command unregisterCommand(String name) {
        if (!commandNamePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid command name! (No Regex match: " + commandNamePattern.pattern() + ")");
        }

        name = name.toLowerCase();
        return commandMap.remove(name);
    }

    public static Command getCommand(String name) {
        name = name.toLowerCase();
        return commandMap.get(name);
    }

    private static class HelpCommand extends Command {

        private HelpCommand() {
            super("Displays all registered commands");
        }

        @Override
        public void invoke(String[] args) {
            System.out.println(ConsoleColor.AQUA + "Commands:");
            commandMap.entrySet().stream().map(entry ->
                    " - " + ConsoleColor.YELLOW + entry.getKey() +
                    (entry.getValue().getDescription() != null ? ConsoleColor.GOLD + " - " + entry.getValue().getDescription() : "")
            ).forEach(System.out::println);
        }
    }
}
