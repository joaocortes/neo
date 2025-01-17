package org.genesys.interpreter.L2;

import org.genesys.interpreter.Binop;
import org.genesys.interpreter.Unop;
import org.genesys.type.AbstractList;
import org.genesys.type.Cons;
import org.genesys.type.EmptyList;

/**
 * Created by yufeng on 5/31/17.
 */
public class Foldr implements Unop {
    private final Binop binop;
    private final Object acc;
    private final Object val;

    public Foldr(Binop binop, Object acc, Object val) {
        this.binop = binop;
        this.acc = acc;
        this.val = val;
    }

    public Object apply(Object obj) {
        return this.applyRec(obj, this.val);
    }

    private Object applyRec(Object first, Object second) {
        AbstractList list = (AbstractList) first;
        if (list instanceof EmptyList) {
            return second;
        } else {
            Cons cons = (Cons) list;
            return this.binop.apply(cons.obj, this.applyRec(cons.list, second));
        }
    }

    public String toString() {
        return "l(x).(foldRight " + this.binop.toString() + " x)";
    }
}
