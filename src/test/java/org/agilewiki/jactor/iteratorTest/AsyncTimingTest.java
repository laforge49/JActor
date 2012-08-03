package org.agilewiki.jactor.iteratorTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.RP;

/**
 * Test code.
 */
public class AsyncTimingTest extends TestCase {
    public void testSync() throws Exception {
        final long c = 1L;

        //System.out.println("####################################################");
        //final long c = 1000000000L;
        //iterations per sec = 2,142,245,507

        final long t0 = System.currentTimeMillis();

        final RP done = new RP() {
            @Override
            public void processResponse(Object unwrappedResponse) throws Exception {
                final long t1 = System.currentTimeMillis();
                long d = t1 - t0;
                if (d > 0) System.out.println("iterations per sec = " + c * 1000L / d);
                System.out.println("each iteration takes " + d * 1000000. / c + " nanoseconds");
            }
        };

        class It extends JAIterator {
            long i;
            RP rp;

            @Override
            protected void process(RP rp) throws Exception {
                this.rp = rp;
            }
        }

        final It it = new It();
        it.iterate(done);
        while (it.i < c) {
            it.i += 1;
            it.rp.processResponse(null);
        }
        it.rp.processResponse(this);
    }
}
