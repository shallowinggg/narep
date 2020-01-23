package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.GeneratorController;
import com.shallowinggg.narep.core.generators.protocol.CustomRemotingCommandCodeGenerator;
import com.shallowinggg.narep.core.lang.ProtocolField;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomRemotingCommandTest {

    @Test
    public void testBuildToStringMethod() {
        GeneratorController controller = new GeneratorController();
        controller.init();
        controller.registerProtocolField(new ProtocolField("ext", String.class, -1));

        CustomRemotingCommandCodeGenerator customRemotingCommand = new CustomRemotingCommandCodeGenerator();

        try {
            Class<?> clazz = CustomRemotingCommandCodeGenerator.class;
            Method buildToStringMethod = clazz.getDeclaredMethod("buildToStringMethod");
            buildToStringMethod.setAccessible(true);
            Assert.assertEquals("    @Override\n" +
                    "    public String toString() {\n" +
                    "        return \"RemotingCommand [code=\" + code\n" +
                    "                + \", flag(B)=\" + Integer.toBinaryString(flag)\n" +
                    "                + \", opaque=\" + opaque\n" +
                    "                + \", remark=\" + remark\n" +
                    "                + \", ext=\" + ext\n" +
                    "                + \", serializeTypeCurrentRPC=\" + serializeTypeCurrentRPC + \"]\";\n" +
                    "    }\n\n", buildToStringMethod.invoke(customRemotingCommand));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
