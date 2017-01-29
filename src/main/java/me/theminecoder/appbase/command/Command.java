package me.theminecoder.appbase.command;

/**
 * @author theminecoder
 */
public abstract class Command {

    private String description;

    public Command() {
        this(null);
    }

    public Command(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract void invoke(String[] args);

}
