package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PUBLIC_STATIC_FINAL;

/**
 * @author shallowinggg
 */
@Generator
public class RemotingSysResponseCodeCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingSysResponseCode";
    private static final String SUB_PACKAGE = "protocol";

    public RemotingSysResponseCodeCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);

        List<FieldInfo> fields = new ArrayList<>(4);
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "int", "SUCCESS", "0"));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "int", "SYSTEM_ERROR", "1"));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "int", "SYSTEM_BUSY", "2"));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "int", "REQUEST_CODE_NOT_SUPPORTED", "3"));
        setFields(fields);
    }


}
