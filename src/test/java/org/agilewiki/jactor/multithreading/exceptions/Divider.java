package org.agilewiki.jactor.multithreading.exceptions;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.bind.SynchronousMethodBinding;
import org.agilewiki.jactor.components.Component;

public class Divider extends Component {
    @Override
    public void bindery() throws Exception {

        thisActor.bind(
                SyncDivide.class.getName(),
                new SynchronousMethodBinding<SyncDivide, Integer>() {
                    @Override
                    public Integer synchronousProcessRequest(Internals internals,
                                                             SyncDivide request)
                            throws Exception {
                        return request.getN() / request.getD();
                    }
                });

        thisActor.bind(
                Divide.class.getName(),
                new MethodBinding<Divide, Integer>() {
                    @Override
                    public void processRequest(Internals internals,
                                               Divide request,
                                               RP rp)
                            throws Exception {
                        rp.process(request.getN() / request.getD());
                    }
                });

        thisActor.bind(
                ISyncDivide.class.getName(),
                new SynchronousMethodBinding<ISyncDivide, Integer>() {
                    @Override
                    public Integer synchronousProcessRequest(Internals internals,
                                                             ISyncDivide request)
                            throws Exception {
                        SyncDivide syncDivide = new SyncDivide(request.getN(), request.getD());
                        try {
                            return syncDivide.call(internals, thisActor);
                        } catch (ArithmeticException x) {
                            return null;
                        }
                    }
                });

        thisActor.bind(
                IDivide.class.getName(),
                new MethodBinding<IDivide, Integer>() {
                    @Override
                    public void processRequest(Internals internals,
                                               IDivide request,
                                               RP rp)
                            throws Exception {
                        Divide divide = new Divide(request.getN(), request.getD());
                        divide.send(internals, thisActor, rp);
                    }
                });

    }
}
