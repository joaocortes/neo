package org.genesys.interpreter.deepcode;

import org.genesys.interpreter.Unop;
import org.genesys.utils.LibUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by yufeng on 6/4/17.
 */
public class SortUnop implements Unop {

    public Object apply(Object obj) {
        if (obj instanceof  Integer){
            assert ((Integer)obj == 256);
            return new ArrayList<>();
        }
        assert obj instanceof List : obj;
        List<Integer> list = LibUtils.cast(obj);
        // Make a deep copy
        List<Integer> sorted = new ArrayList<>();
        for (Integer i : list)
            sorted.add(i);
        Collections.sort(sorted);
        return sorted;
    }

    public String toString() {
        return "SORT";
    }
}
