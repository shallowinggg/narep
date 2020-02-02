package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.DependencyResolver;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.exception.FileGenerateException;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
                LOG.error("resolve dependencies fail, dependency: {} don't exist", registerName);
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

    public boolean resolve() {
        AtomicInteger error = new AtomicInteger(0);
        Stream.of(generators.values())
                .flatMap(Collection::stream)
                .filter(codeGenerator -> codeGenerator instanceof JavaCodeGenerator)
                .map(codeGenerator -> (JavaCodeGenerator) codeGenerator)
                .forEach(javaCodeGenerator -> {
                    boolean result = javaCodeGenerator.resolveDependencies(this);
                    if (!result) {
                        error.incrementAndGet();
                    }
                });
        return error.get() == 0;
    }

    public void generate() {
        Stream.of(generators.values())
                .flatMap(Collection::stream)
                .forEach(generator -> {
                    try {
                        generator.write();
                    } catch (IOException e) {
                        throw new FileGenerateException("write file: " + generator.fileName() + " fail", e);
                    }
                });
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

    public int size() {
        return generators.size();
    }


    private CodeGeneratorManager() {
    }

}
