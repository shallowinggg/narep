package com.shallowinggg.narep.admin.xml;

/**
 * 当{@link NarepDefinitionReader}发现一个不合法的定义时
 * 抛出此异常。
 *
 * @author shallowinggg
 */
public class NarepDefinitionStoreException extends RuntimeException {

    public NarepDefinitionStoreException(String msg) {
        super(msg);
    }

    public NarepDefinitionStoreException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
