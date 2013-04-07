// import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
// import java.util.Hashtable;

public class ConstraintSystem implements Runnable {
    public static final ConstraintSystem instance = new ConstraintSystem();
    
    private List<ConstraintVariable> variables;
    private List<Constraint> constraints;
    // private Hashtable<String, ConstraintVariable> variables;
    
    protected ConstraintSystem() {
        variables = Collections.synchronizedList(
                new ArrayList<ConstraintVariable>());
        constraints = Collections.synchronizedList(
                new ArrayList<Constraint>());
        // variables = new Hashtable<String, ConstraintVariable>();
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            // ArrayList<ConstraintVariable> values = 
            //         (ArrayList<ConstraintVariable>) variables.values();
            // for (ConstraintVariable v : values) {
            
            try {
                synchronized (variables) {
                    for (ConstraintVariable v : variables) {
                        // System.out.println(v.getOldValue() + ", " + v.getValue());
                        if (v.isSource() && !v.getValue().equals(v.getOldValue())) {
                            // v.setInvalid();
                            v.populate();
                        }
                    }
                }
                Thread.sleep(1);
            } catch (Exception e) {
                System.out.println(e);
                // return;
            }
        }
    }

    public ConstraintSystem addConstraint(Constraint c) {
        if (c == null) {
            throw new IllegalArgumentException("Constraint cannot be null");
        }
        ConstraintVariable t = c.getTarget();
        List<ConstraintVariable> src = c.getSources();
        if (t == null) {
            throw new RuntimeException("No target");
        }
        if (src.isEmpty()) {
            throw new RuntimeException("No source(s)");
        }
        
        int index = variables.indexOf(t), i, size = src.size();
        if (index == -1) {
            variables.add(t);
        } else {
            t = variables.get(index);
            c.setTarget(t);
        }
        
        for (i = 0; i < size; i++) {
            ConstraintVariable s = src.get(i);
            index = variables.indexOf(s);
            if (index == -1) {
                variables.add(s);
            } else {
                s = variables.get(index);
                src.set(i, s);
                s.setSource(true);
            }
            s.addNext(t);
            t.addConstraint(s.hashCode(), c);
        }
        
        constraints.add(c);
        
        return this;
    }
}
