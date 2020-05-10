package com.shallowinggg.narep.admin;

import com.shallowinggg.narep.admin.command.Command;
import com.shallowinggg.narep.admin.command.NarepCommand;
import org.apache.commons.cli.*;

/**
 * @author shallowinggg
 */
public class NarepStarter {

    public static void main(String[] args) {
        Command root = new NarepCommand();
        Options options = root.buildCommandlineOptions();
        CommandLine cl = parseCmdLine(args, options, new PosixParser());
        if (cl == null) {
            return;
        }

        root.execute(cl);
    }

    private static CommandLine parseCmdLine(String[] args, Options options,
                                            CommandLineParser parser) {
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.print(e.getMessage() + "\n" +
                    "usage: narep <options> [source files]\n" +
                    "'-h' list all options description\n");
        }

        return commandLine;
    }
}
