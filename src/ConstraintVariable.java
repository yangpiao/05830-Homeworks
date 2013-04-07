import java.lang.reflect.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Collections;


public class ConstraintVariable {
    // private Object object;
    private GraphicalObject object;
    private String property;
    private Object oldValue;
    private boolean valid;
    private boolean source;
    
    private Method getter;
    private Method setter;
    private final Object[] emptyArgs = new Object[0];
    private List<ConstraintVariable> next;
    private Hashtable<Integer, Constraint> constraints;
    
    public GraphicalObject getObject() {
        return object;
    }

    public String getProperty() {
        return property;
    }
    
    public Object getValue() {
        return invokeGetter();
    }
    
    public Object setValue(Object value) {
        return invokeSetter(value);
    }

    public Object getOldValue() {
        return oldValue;
    }
    
    public void updateOldValue() {
        valid = true;
        oldValue = invokeGetter();
    }
    
    public boolean isSource() {
        return source;
    }

    public void setSource(boolean source) {
        this.source = source;
    }

    public boolean isValid() {
        return valid;
    }
    
    public void setValid() {
        valid = true;
    }
    
    public void setInvalid() {
        valid = false;
        for (ConstraintVariable v : next) {
            if (v.valid) {
                v.setInvalid();
            }
        }
    }
    
    public List<ConstraintVariable> getNext() {
        return next;
    }
    
    public void addNext(ConstraintVariable v) {
        if (!next.contains(v)) {
            next.add(v);
        }
    }
    
    public void addConstraint(int srcHash, Constraint c) {
        constraints.put(srcHash, c);
    }
    
    public void removeConstraint(int srcHash) {
        constraints.remove(srcHash);
    }
    
    public Constraint getConstraint(int srcHash) {
        return constraints.get(srcHash);
    }
    
    public Hashtable<Integer, Constraint> getConstraints() {
        return constraints;
    }
    
    public boolean equals(ConstraintVariable v) {
        return (object == v.object && property.equals(v.property));
    }
    public boolean identical(GraphicalObject obj, String prop) {
        return (object == obj && property.equals(prop));
    }
    
    // public String key() {
    //     return object.toString() + property;
    // }

    private void getMethods() {
        Class<? extends GraphicalObject> c = object.getClass();
        Method[] methods = c.getMethods();
        String gname = "get" + property;
        String sname = "set" + property;
        for (Method m : methods) {
            if (m.getName().equals(gname)) {
                getter = m;
                // break;
            }
            if (m.getName().equals(sname)) {
                setter = m;
                // break;
            }
        }
    }
    
    private Object invokeGetter() {
        try {
            return getter.invoke(object, emptyArgs);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    private Object invokeSetter(Object value) {
        try {
            return setter.invoke(object, new Object[] { value });
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    public ConstraintVariable(GraphicalObject obj, String prop) {
        this(obj, prop, false);
    }
    
    // public ConstraintVariable(Object obj, String prop) {
    public ConstraintVariable(GraphicalObject obj, String prop, boolean source) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (prop == null) {
            throw new IllegalArgumentException("Property cannot be null");
        }
        if (prop.trim().length() == 0) {
            throw new IllegalArgumentException("Property cannot be empty");
        }
        
        object = obj;
        
        char[] chars = prop.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        property = new String(chars);
        
        this.source = source;
        
        getMethods();
        if (getter == null) {
            throw new IllegalArgumentException("Getter for the property `" +
                    property + "` does not exist");
        }
        updateOldValue();
        
        // valid = true;
        next = Collections.synchronizedList(new ArrayList<ConstraintVariable>());
        constraints = new Hashtable<Integer, Constraint>();
    }
    
    public void populate() {
        if (!source) {
            return;
        }
        int hash = this.hashCode();
        for (ConstraintVariable v : next) {
            // if (!v.valid) {
                Constraint c = v.getConstraint(hash);
                c.calculate();
                v.populate();
            // }
        }
    }
    
    
    
    
    
    
    
    
    // test
    public static void main(String[] args) {
        OutlineRect rect = new OutlineRect(0, 10, 50, 80, Color.blue, 5);
        ConstraintVariable v1 = new ConstraintVariable(rect, "Width");
        ConstraintVariable v2 = new ConstraintVariable(rect, "y");
        ConstraintVariable v3 = new ConstraintVariable(rect, "color");
        ConstraintVariable v4 = new ConstraintVariable(rect, "lineThickness");
        ArrayList<ConstraintVariable> list = new ArrayList<ConstraintVariable>();
        list.add(v1);
        list.add(v2);
        list.add(v3);
        list.add(v4);
        // System.out.println(list.indexOf(v3));
        
        System.out.println(v2.getValue());
        v2.setValue(123);
        System.out.println(v2.getValue());
        
//        final OutlineRect r = rect;
//        GraphicalObject o = rect;
//        System.out.println(o.getClass());
//        Constraint c = new Constraint();
//        c.setTarget(rect, "width").addSource(rect, "x")
//        .setFormula(new Constraint.Formula() {
//            public void evaluate(ConstraintVariable... objects) {
//                r.setWidth((int) objects[1].getValue());
//            }
//        });
    }
}