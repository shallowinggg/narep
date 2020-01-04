package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link CodeGenerator}管理器，注册{@link CodeGenerator}并且生成相应代码。
 *
 * @author shallowinggg
 */
public class CodeGeneratorManager {
    private static final Logger LOG = LoggerFactory.getLogger(CodeGeneratorManager.class);
    private static final CodeGeneratorManager INSTANCE = new CodeGeneratorManager();

    private Map<String, CodeGenerator> generators = new HashMap<>();

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


    private CodeGeneratorManager() {}

}
