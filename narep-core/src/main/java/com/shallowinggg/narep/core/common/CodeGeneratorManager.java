package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.DependencyResolver;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.exception.FileGenerateException;
import com.shallowinggg.narep.core.util.Conditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * This class is designed to store all {@link CodeGenerator} instances,
 * and implements interface {@link DependencyResolver} for
 * {@link JavaCodeGenerator} use.
 * <p>
 * Due to the interface {@link JavaCodeGenerator} is the sub interface of
 * {@link CodeGenerator}, the class also provide the function of resolving
 * all {@link JavaCodeGenerator} instances by {@link #resolve(List)} method.
 * <p>
 * Aside from the function introduced above, the class can be used to
 * generate all files with CodeGenerators stored.
 *
 * @author shallowinggg
 */
public class CodeGeneratorManager implements DependencyResolver {
    private static final Logger LOG = LoggerFactory.getLogger(CodeGeneratorManager.class);
    private static final CodeGeneratorManager INSTANCE = new CodeGeneratorManager();

    private Map<String, CodeGenerator> generators = new HashMap<>();

    /**
     * Get the unique instance of class CodeGeneratorManager
     *
     * @return unique instance
     */
    public static CodeGeneratorManager getInstance() {
        return INSTANCE;
    }

    @Override
    public List<JavaCodeGenerator> resolve(List<String> dependencyNames) {
        Conditions.notEmpty(dependencyNames, "dependencyNames must not empty");
        List<JavaCodeGenerator> dependencies = new ArrayList<>();
        boolean success = true;

        for (String registerName : dependencyNames) {
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

    /**
     * Resolve all JavaCodeGenerator instances stored in this repository.
     * <p>
     * The method will keep executing even if one or more JavaCodeGenerator
     * instance resolve failed. If all instances are resolved successfully,
     * it will return true, otherwise return false. Error messages in this
     * progress should be reported by {@link JavaCodeGenerator#resolveDependencies(DependencyResolver)}
     * method.
     *
     * @return {@literal true} if all JavaCodeGenerator resolved successfully
     */
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

    /**
     * Generate all files and write them to the disk.
     * <p>
     * If write progress exec fail, this method will be interrupted and
     * throw Exception {@link FileGenerateException}.
     */
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
        Conditions.hasText(name, "generator name must no be blank");
        Conditions.notNull(generator, "generator must not be null");

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
