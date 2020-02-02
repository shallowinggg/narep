package com.shallowinggg.narep.admin;

import com.shallowinggg.narep.admin.command.Command;
import com.shallowinggg.narep.admin.command.NarepCommand;
import org.apache.commons.cli.*;

/**
 * @author shallowinggg
 */
public class NarepStarter {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
        } else {
            Command root = new NarepCommand();
            Options options = root.buildCommandlineOptions();
            CommandLine cl = parseCmdLine(args, options, new PosixParser());
            if (cl == null) {
                return;
            }

            root.execute(cl);
        }
    }

    private static void printHelp() {
        NarepCommand command = new NarepCommand();
        command.printHelp();
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
