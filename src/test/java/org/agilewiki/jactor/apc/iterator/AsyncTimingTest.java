package org.agilewiki.jactor.apc.iterator;

import junit.framework.TestCase;
import org.agilewiki.jactor.apc.JAIterator;
import org.agilewiki.jactor.apc.ResponseProcessor;

public class AsyncTimingTest extends TestCase {
    public void testSync() throws Exception {
        final long c = 1L;

        //final long c = 1000000000L;
        //iterations per sec = 143410296
        //each iteration takes 6.973 nanoseconds

        final long t0 = System.currentTimeMillis();

        final ResponseProcessor done = new ResponseProcessor() {
            @Override
            public void process(Object unwrappedResponse) throws Exception {
                final long t1 = System.currentTimeMillis();
                long d = t1 - t0;
                if (d > 0) System.out.println("iterations per sec = " + c * 1000L / d);
                System.out.println("each iteration takes " + d * 1000000. / c + " nanoseconds");
            }
        };

        class It extends JAIterator {
            long i;
            ResponseProcessor rp;

            It(ResponseProcessor fin) throws Exception {
                super(fin);
            }

            @Override
            protected void process(ResponseProcessor rp) throws Exception {
                this.rp = rp;
            }
        }

        final It it = new It(done);
        while (it.i < c) {
            it.i += 1;
            it.rp.process(null);
        }
        it.rp.process(this);
    }
}
