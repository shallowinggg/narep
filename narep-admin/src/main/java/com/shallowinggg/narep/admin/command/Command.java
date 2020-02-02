package com.shallowinggg.narep.admin.command;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 * Simple Interface for cmd execution.
 *
 * @author shallowinggg
 */
public interface Command {

    /**
     * Return the name of this command.
     *
     * @return command name
     */
    String commandName();

    /**
     * Return the description of this command, including
     * its use.
     *
     * @return command description
     */
    String commandDesc();

    /**
     * Build all possible options of the command and return it
     * for use.
     *
     * You can use it to print help messages or parse user given
     * arguments.
     *
     * @return all options
     * @see org.apache.commons.cli.HelpFormatter#printHelp(String, Options)
     * @see org.apache.commons.cli.CommandLineParser#parse(Options, String[])
     */
    Options buildCommandlineOptions();

    /**
     * Execute the command with given arguments.
     *
     * @param commandLine including given args
     * @throws CommandException when execute fail
     * @see CommandLine
     */
    void execute(final CommandLine commandLine) throws CommandException;
}
