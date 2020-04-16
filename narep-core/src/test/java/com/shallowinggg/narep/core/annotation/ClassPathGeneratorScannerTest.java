package com.shallowinggg.narep.core.annotation;

import com.shallowinggg.narep.core.GeneratorController;
import com.shallowinggg.narep.core.common.CodeGeneratorManager;
import org.junit.Assert;
import org.junit.Test;

public class ClassPathGeneratorScannerTest {

    @Test
    public void testScan() {
        GeneratorController controller = new GeneratorController();
        controller.init();
        ClassPathGeneratorScanner scanner = new ClassPathGeneratorScanner();
        scanner.doScan("com.shallowinggg.narep.core.generators");
        CodeGeneratorManager manager = CodeGeneratorManager.getInstance();
        Assert.assertEquals(46, manager.size());
    }
}
