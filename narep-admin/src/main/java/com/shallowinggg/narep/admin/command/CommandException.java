package com.shallowinggg.narep.admin.command;

import org.apache.commons.cli.CommandLine;

/**
 * Exception thrown when command exec failed.
 *
 * @author shallowinggg
 * @see Command#execute(CommandLine)
 */
public class CommandException extends RuntimeException {

    public CommandException(String msg) {
        super(msg);
    }

    public CommandException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
