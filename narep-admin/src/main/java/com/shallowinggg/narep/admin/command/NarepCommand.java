package com.shallowinggg.narep.admin.command;

import com.shallowinggg.narep.admin.xml.NarepDefinition;
import com.shallowinggg.narep.admin.xml.NarepDefinitionStoreException;
import com.shallowinggg.narep.admin.xml.XmlNarepDefinitionReader;
import com.shallowinggg.narep.core.GeneratorController;
import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.common.LogConfig;
import com.shallowinggg.narep.core.common.ProtocolConfig;
import com.shallowinggg.narep.core.lang.ProtocolField;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.List;

/**
 * Starter Command.
 *
 * There are 2 options for use:
 * 1. -c: specify the location of xml config
 * 2. -h: print all options description
 *
 * @author shallowinggg
 */
public class NarepCommand implements Command {
    private static final String CONFIG_LOCATION_OPTION = "c";
    private static final String DEFAULT_CONFIG_LOCATION = "classpath:narep-config.xml";
    private static final String HELP_OPTION = "h";

    @Override
    public String commandName() {
        return "narep";
    }

    @Override
    public String commandDesc() {
        return "Starter Command";
    }

    @Override
    public Options buildCommandlineOptions() {
        Options options = new Options();
        Option opt = new Option("c", true, "specify the location of xml config");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("h", false, "print all options description");
        options.addOption(opt);

        return options;
    }

    @Override
    public void execute(CommandLine commandLine) throws CommandException {
        if(commandLine.hasOption(HELP_OPTION)) {
            printHelp();
        } else {
            try {
                String location;
                if(commandLine.hasOption(CONFIG_LOCATION_OPTION)) {
                    location = commandLine.getOptionValue(CONFIG_LOCATION_OPTION).trim();
                } else {
                    location = DEFAULT_CONFIG_LOCATION;
                }
                XmlNarepDefinitionReader reader = new XmlNarepDefinitionReader();
                NarepDefinition definition = reader.loadNarepDefinition(location);
                GeneratorController controller = new GeneratorController();

                convertDefinition2Config(definition, controller);
                controller.init();
                controller.start();
            } catch (NarepDefinitionStoreException e) {
                throw new CommandException("Exec command " + commandName() + " fail: " + e.getMessage(), e);
            }
        }
    }

    public void printHelp() {
        Options options = buildCommandlineOptions();
        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);
        hf.printHelp("narep <options> [source files]", options);
    }

    private void convertDefinition2Config(NarepDefinition definition, GeneratorController controller) {
        // convert to generator config
        String location = definition.getLocation();
        String packageName = definition.getPackageName();
        if(StringTinyUtils.isNotBlank(location) || StringTinyUtils.isNotBlank(packageName)) {
            GeneratorConfig generatorConfig = new GeneratorConfig();
            if(StringTinyUtils.isNotBlank(location)) {
                generatorConfig.setStoreLocation(location);
            }
            if(StringTinyUtils.isNotBlank(packageName)) {
                generatorConfig.setBasePackage(packageName);
            }
            controller.registerConfig(GeneratorConfig.CONFIG_NAME, generatorConfig);
        }

        // convert to log config
        boolean customLog = definition.isUseCustomLogger();
        if(customLog) {
            String loggerName = definition.getLoggerName();
            LogConfig logConfig = new LogConfig(loggerName, customLog);
            logConfig.setLoggerName(loggerName);
            controller.registerConfig(LogConfig.CONFIG_NAME, logConfig);
        }

        // convert to protocol config
        List<ProtocolField> protocolFields = definition.getProtocolFields();
        if(CollectionUtils.isNotEmpty(protocolFields)) {
            ProtocolConfig protocolConfig = new ProtocolConfig();
            for (ProtocolField field : protocolFields) {
                protocolConfig.addProtocolField(field);
            }
            controller.registerConfig(ProtocolConfig.CONFIG_NAME, protocolConfig);
        }
    }
}
