package org.agilewiki.jactor.iteratorTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.RP;

/**
 * Test code.
 */
public class SyncTimingTest extends TestCase {
    public void testSync() throws Exception {
        final long c = 1L;

        //System.out.println("####################################################");
        //final long c = 1000000000L;
        //iterations per sec = 544,662,309

        final long t0 = System.currentTimeMillis();
        RP done = new RP() {
            @Override
            public void processResponse(Object unwrappedResponse) throws Exception {
                final long t1 = System.currentTimeMillis();
                long d = t1 - t0;
                if (d > 0) System.out.println("iterations per sec = " + c * 1000L / d);
                System.out.println("each iteration takes " + d * 1000000. / c + " nanoseconds");
            }
        };

        (new JAIterator() {
            long i;

            @Override
            protected void process(RP rp) throws Exception {
                i += 1;
                if (i < c) rp.processResponse(null);
                else rp.processResponse(this);
            }
        }).iterate(done);
    }
}
