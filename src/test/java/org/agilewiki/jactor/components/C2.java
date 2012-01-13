package org.agilewiki.jactor.components;

import java.util.ArrayList;

public class C2 extends Component {
    @Override
    public ArrayList<Include> includes() {
        ArrayList<Include> rv = new ArrayList<Include>();
        rv.add(new Include(C1.class));
        return rv;
    }

    @Override
    public void close() {
        System.err.println("C2 closed");
    }
}
