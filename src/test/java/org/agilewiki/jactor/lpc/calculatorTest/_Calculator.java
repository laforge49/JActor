package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.TargetActor;

public interface _Calculator extends TargetActor {
    public void clear(Clear request, RP rp) throws Exception;

    public void get(Get request, RP rp) throws Exception;

    public void set(Set request, RP rp) throws Exception;

    public void add(Add request, RP rp) throws Exception;

    public void subtract(Subtract request, RP rp) throws Exception;

    public void multiply(Multiply request, RP rp) throws Exception;

    public void divide(Divide request, RP rp) throws Exception;
}
