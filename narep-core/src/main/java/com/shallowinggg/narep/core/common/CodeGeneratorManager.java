package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.DependencyResolver;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.generators.defaults.RPCHookCodeGenerator;
import com.shallowinggg.narep.core.generators.defaults.RemotingServiceCodeGenerator;
import com.shallowinggg.narep.core.generators.exception.*;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * {@link CodeGenerator}管理器，注册所有文件对应的{@link CodeGenerator}
 * 并且生成文件。
 *
 * @author shallowinggg
 */
public class CodeGeneratorManager implements DependencyResolver {
    private static final Logger LOG = LoggerFactory.getLogger(CodeGeneratorManager.class);
    private static final CodeGeneratorManager INSTANCE = new CodeGeneratorManager();

    private Map<String, CodeGenerator> generators = new HashMap<>();

    /**
     * 获取CodeGeneratorManager单例
     *
     * @return 单例
     */
    public static CodeGeneratorManager getInstance() {
        return INSTANCE;
    }

    @Override
    public List<JavaCodeGenerator> resolve(List<String> dependenciesName) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(dependenciesName),
                "dependenciesName must not be null or empty");
        List<JavaCodeGenerator> dependencies = new ArrayList<>();
        boolean success = true;

        for (String registerName : dependenciesName) {
            CodeGenerator generator = generators.get(registerName);
            if (!(generator instanceof JavaCodeGenerator)) {
                LOG.error("resolve dependencies fail, dependency: {} don't exist",
                        registerName);
                success = false;
            } else {
                dependencies.add((JavaCodeGenerator) generator);
            }
        }

        if (success) {
            return dependencies;
        } else {
            return Collections.emptyList();
        }
    }

    public void register(String name, CodeGenerator generator) {
        Conditions.checkArgument(StringTinyUtils.isNotBlank(name), "generator name must no be blank");
        Conditions.checkArgument(generator != null, "generator must not be null");

        generators.put(name, generator);
        if (LOG.isDebugEnabled()) {
            LOG.debug("register CodeGenerator: {}", name);
        }
    }

    public boolean containsGenerator(String generatorName) {
        Conditions.hasText(generatorName, "generatorName must not be empty");
        return generators.containsKey(generatorName);
    }

    public void registerDefaultCodeGenerators() {
        CodeGenerator rpcHook = new RPCHookCodeGenerator();
        CodeGenerator remotingService = new RemotingServiceCodeGenerator();

    }

    public void registerExceptionCodeGenerators() {
        CodeGenerator remotingException = new RemotingExceptionCodeGenerator();
        CodeGenerator remotingCommandException = new RemotingCommandExceptionCodeGenerator();
        CodeGenerator remotingConnectException = new RemotingConnectExceptionCodeGenerator();
        CodeGenerator remotingSendRequestException = new RemotingSendRequestExceptionCodeGenerator();
        CodeGenerator remotingTimeoutException = new RemotingTimeoutExceptionCodeGenerator();
        CodeGenerator remotingTooMuchException = new RemotingTooMuchRequestExceptionCodeGenerator();
        register(remotingException.fileName(), remotingException);
        register(remotingCommandException.fileName(), remotingCommandException);
        register(remotingConnectException.fileName(), remotingConnectException);
        register(remotingSendRequestException.fileName(), remotingSendRequestException);
        register(remotingTimeoutException.fileName(), remotingTimeoutException);
        register(remotingTooMuchException.fileName(), remotingTooMuchException);
    }


    private CodeGeneratorManager() {
    }

}
