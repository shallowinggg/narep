package com.shallowinggg.narep.admin.xml;

/**
 * 当{@link ProtocolDefinitionReader}发现一个不合法的定义时
 * 抛出此异常。
 *
 * @author shallowinggg
 */
public class ProtocolDefinitionStoreException extends RuntimeException {

    public ProtocolDefinitionStoreException(String msg) {
        super(msg);
    }

    public ProtocolDefinitionStoreException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
