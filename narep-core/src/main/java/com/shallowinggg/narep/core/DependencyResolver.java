package com.shallowinggg.narep.core;

import java.util.List;

/**
 * 这个接口用来解析某个类或者接口生成器生成的类所依赖的类和接口
 * 是否存在，当然，这些被依赖的类和接口也由生成器生成。因此，
 * 它只用于检查所生成的代码的合法性，例如：由于编码错误，导致
 * 某个类依赖的类未被生成。
 *
 * @author shallowinggg
 */
public interface DependencyResolver {

    /**
     * 解析所依赖的类或者接口生成器是否存在。
     * <p>
     * 如果解析成功，那么返回与名称相对应的生成器集合；否则返回
     * 一个空列表以表明解析失败。
     * <p>
     * 注意：此方法弱化了解析是否成功的直白性，通过返回结果的
     * 表现形式表明解析是否成功。因此，传入的参数要求必须不能
     * 是一个空列表，否则返回结果将存在二义性。
     *
     * @param dependencyNames 依赖类或接口的生成器名称集合
     * @return 解析出的生成器列表。如果解析失败，那么将会返回一个空列表表示失败。
     */
    List<JavaCodeGenerator> resolve(List<String> dependencyNames);
}
