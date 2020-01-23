package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;
import com.shallowinggg.narep.core.lang.ProtocolField;

import java.util.ArrayList;
import java.util.List;

/**
 * 通信协议配置
 *
 * @author shallowinggg
 */
public class ProtocolConfig implements Config {
    public static final String CONFIG_NAME = "protocol";
    public static final int DEFAULT_FIELDS_SIZE = 4;

    private final boolean compress;

    private final List<ProtocolField> protocolFields = new ArrayList<>();

    {
        // required protocol fields
        protocolFields.add(new ProtocolField("code", int.class, 2));
        protocolFields.add(new ProtocolField("flag", int.class, 1));
        protocolFields.add(new ProtocolField("opaque", int.class, 4));
        protocolFields.add(new ProtocolField("remark", String.class, -1));
    }

    public ProtocolConfig() {
        this(true);
    }

    public ProtocolConfig(boolean compress) {
        this.compress = compress;
    }

    public void addProtocolField(ProtocolField protocolField) {
        this.protocolFields.add(protocolField);
    }

    public boolean isCompress() {
        return compress;
    }

    public List<ProtocolField> getProtocolFields() {
        return protocolFields;
    }

}
