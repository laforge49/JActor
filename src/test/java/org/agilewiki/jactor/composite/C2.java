package org.agilewiki.jactor.composite;

import java.util.ArrayList;

public class C2 extends Component {
    @Override
    public ArrayList<Class> includes() {
        ArrayList<Class> rv = new ArrayList<Class>();
        rv.add(C1.class);
        return rv;
    }

    @Override
    public void close() {
        System.err.println("C2 closed");
    }
}
