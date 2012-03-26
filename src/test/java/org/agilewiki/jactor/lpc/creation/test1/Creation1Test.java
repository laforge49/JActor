package org.agilewiki.jactor.lpc.creation.test1;

import junit.framework.TestCase;

public class Creation1Test extends TestCase {
    public void test() {

        long c = 1;

        //long c = 1000000000;
        //iterations per second = 718,907,260

        loop(c);
        loop(c);
        long t0 = System.currentTimeMillis();
        loop(c);
        long t1 = System.currentTimeMillis();
        long d = t1 - t0;
        if (d > 0)
            System.out.println(1000 * c / d);
    }

    void loop(long c) {
        int i = 0;
        while (i < c) {
            i += 1;
        }
    }
}
