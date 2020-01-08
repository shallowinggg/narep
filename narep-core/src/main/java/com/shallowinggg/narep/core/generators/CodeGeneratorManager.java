package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.generators.defaults.RPCHookCodeGenerator;
import com.shallowinggg.narep.core.generators.defaults.RemotingServiceCodeGenerator;
import com.shallowinggg.narep.core.generators.exception.*;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link CodeGenerator}管理器，注册所有文件对应的{@link CodeGenerator}
 * 并且生成文件。
 *
 * @author shallowinggg
 */
public class CodeGeneratorManager {
    private static final Logger LOG = LoggerFactory.getLogger(CodeGeneratorManager.class);
    private static final CodeGeneratorManager INSTANCE = new CodeGeneratorManager();

    private Map<String, CodeGenerator> generators = new HashMap<>();
    private GeneratorConfig generatorConfig;

    /**
     * 获取CodeGeneratorManager单例
     *
     * @return 单例
     */
    public CodeGeneratorManager getInstance() {
        return INSTANCE;
    }

    public void register(String name, CodeGenerator generator) {
        Conditions.checkArgument(StringTinyUtils.isNotBlank(name), "file name must no be blank");
        Conditions.checkArgument(generator != null, "generator must not be null");

        generators.put(name, generator);
        if(LOG.isDebugEnabled()) {
            LOG.debug("register CodeGenerator: " + name);
        }
    }

    public void registerDefaultCodeGenerators() {
        CodeGenerator remotingCommand = null;

        JavaCodeGenerator rpcHook = new RPCHookCodeGenerator(generatorConfig, Collections.singletonList(remotingCommand));
        JavaCodeGenerator remotingService = new RemotingServiceCodeGenerator(generatorConfig);

    }

    public void registerExceptionCodeGenerators() {
        JavaCodeGenerator remotingException = new RemotingExceptionCodeGenerator(generatorConfig);
        JavaCodeGenerator remotingCommandException = new RemotingCommandExceptionCodeGenerator(generatorConfig);
        JavaCodeGenerator remotingConnectException = new RemotingConnectExceptionCodeGenerator(generatorConfig);
        JavaCodeGenerator remotingSendRequestException = new RemotingSendRequestExceptionCodeGenerator(generatorConfig);
        JavaCodeGenerator remotingTimeoutException = new RemotingTimeoutExceptionCodeGenerator(generatorConfig);
        JavaCodeGenerator remotingTooMuchException = new RemotingTooMuchRequestExceptionCodeGenerator(generatorConfig);
        register(remotingException.fullQualifiedName(), remotingException);
        register(remotingCommandException.fullQualifiedName(), remotingCommandException);
        register(remotingConnectException.fullQualifiedName(), remotingConnectException);
        register(remotingSendRequestException.fullQualifiedName(), remotingSendRequestException);
        register(remotingTimeoutException.fullQualifiedName(), remotingTimeoutException);
        register(remotingTooMuchException.fullQualifiedName(), remotingTooMuchException);
    }

    public void setGeneratorConfig(GeneratorConfig config) {
        String sourceStoreLocation = CodeGeneratorHelper.buildNecessaryFolders(config.getStoreLocation());
        config.setStoreLocation(sourceStoreLocation);
        this.generatorConfig = config;
    }


    private CodeGeneratorManager() {}

}
