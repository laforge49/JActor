package org.agilewiki.jactor.multithreadingTest.exceptions;

/**
 * Test code.
 */
public class ISyncDivide extends SyncDivide {
    public ISyncDivide(int n, int d) {
        super(n, d);
    }

    @Override
    protected Integer _call(Divider targetActor) throws Exception {
        return targetActor.iSyncDivide(n, d);
    }
}
