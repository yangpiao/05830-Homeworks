// import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Constraint {
    private ConstraintVariable target;
    private List<ConstraintVariable> sources;
    private Formula formula;
    
    public ConstraintVariable getTarget() {
        return target;
    }

    public Formula getFormula() {
        return formula;
    }

    public List<ConstraintVariable> getSources() {
        return sources;
    }

    public Constraint() {
        sources = Collections.synchronizedList(
                new ArrayList<ConstraintVariable>());
    }
    
    public Constraint setTarget(GraphicalObject obj, String prop) {
        target = new ConstraintVariable(obj, prop, false);
        return this;
    }
    
    public Constraint setTarget(ConstraintVariable target) {
        this.target = target;
        return this;
    }
    
    public Constraint addSource(GraphicalObject obj, String prop) {
        sources.add(new ConstraintVariable(obj, prop, true));
        return this;
    }
    
    public void addToSystem() {
        ConstraintSystem system = ConstraintSystem.instance;
        system.addConstraint(this);
        calculate();
        // return this;
    }
    
    public Constraint setFormula(Formula f) {
        if (f == null) {
            throw new IllegalArgumentException("Formula cannot be null");
        }
        formula = f;
        return this;
    }
    
    public void calculate() {
        formula.evaluate(target, sources);
        target.updateOldValue();
        for (ConstraintVariable v : sources) {
            v.updateOldValue();
        }
    }
    
    public interface Formula {
        // public Object evaluate(Object... deps);
        public void evaluate(ConstraintVariable target, 
                List<ConstraintVariable> sources);
    }
}


//public interface Constraint {
//    public Object evaluate(ConstraintVariable... variables);
//}
