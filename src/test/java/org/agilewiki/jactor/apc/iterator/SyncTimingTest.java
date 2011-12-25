package org.agilewiki.jactor.apc.iterator;

import junit.framework.TestCase;
import org.agilewiki.jactor.apc.JAIterator;
import org.agilewiki.jactor.apc.ResponseProcessor;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class SyncTimingTest extends TestCase {
    public void testSync() throws Exception {
        final long c = 1L;

        //final long c = 1000000000L;
        //iterations per sec = 218962119
        //each iteration takes 4.567 nanoseconds

        final long t0 = System.currentTimeMillis();
        ResponseProcessor done = new ResponseProcessor() {
            @Override
            public void process(Object unwrappedResponse) throws Exception {
                final long t1 = System.currentTimeMillis();
                long d = t1 - t0;
                if (d > 0) System.out.println("iterations per sec = "+c*1000L/d);
                System.out.println("each iteration takes "+d*1000000./c+" nanoseconds");
            }
        };
        
        new JAIterator(done) {
            long i;

            @Override
            protected void process(ResponseProcessor rp) throws Exception {
                i += 1;
                if (i < c) rp.process(null);
                else rp.process(this);
            }
        };
    }
}
