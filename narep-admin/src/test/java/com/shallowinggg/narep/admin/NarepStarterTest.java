package com.shallowinggg.narep.admin;

import org.junit.Test;

public class NarepStarterTest {

    @Test
    public void testMain() {

        System.out.println("---------- no args ----------");
        String[] args = new String[0];
        NarepStarter.main(args);

        System.out.println();
        System.out.println("---------- -h args ----------");
        args = new String[] {"-h"};
        NarepStarter.main(args);

        System.out.println();
        System.out.println("---------- -error args ----------");
        args = new String[] {"-error"};
        NarepStarter.main(args);

        System.out.println();
        System.out.println("---------- -c args ----------");
        args = new String[] {"-c"};
        NarepStarter.main(args);
    }

    @Test
    public void testDefaultProtocol() {
        String[] args = new String[] { "-c", "test.xml"};
        NarepStarter.main(args);
    }

    @Test
    public void testCustomProtocol() {
        String[] args = new String[] { "-c", "test-custom.xml"};
        NarepStarter.main(args);
    }

}
