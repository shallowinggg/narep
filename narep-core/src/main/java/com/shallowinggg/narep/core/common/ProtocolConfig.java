package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;
import com.shallowinggg.narep.core.lang.ProtocolField;

import java.util.LinkedList;
import java.util.List;

/**
 * 通信协议配置
 *
 * @author shallowinggg
 */
public class ProtocolConfig implements Config {
    public static final String CONFIG_NAME = "protocol";
    public static final int DEFAULT_FIELDS_SIZE = 4;

    private final List<ProtocolField> protocolFields = new LinkedList<>();

    @Override
    public void init() {
        // required protocol fields
        protocolFields.add(0, new ProtocolField("remark", String.class, -1));
        protocolFields.add(0, new ProtocolField("opaque", int.class, 4));
        protocolFields.add(0, new ProtocolField("flag", int.class, 1));
        protocolFields.add(0, new ProtocolField("code", int.class, 2));
    }

    public void addProtocolField(ProtocolField protocolField) {
        this.protocolFields.add(protocolField);
    }

    public List<ProtocolField> getProtocolFields() {
        return protocolFields;
    }

    @Override
    public String toString() {
        return "ProtocolConfig{" +
                "protocolFields=" + protocolFields +
                '}';
    }
}
