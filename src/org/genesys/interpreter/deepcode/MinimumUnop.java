package org.genesys.interpreter.deepcode;

import org.genesys.interpreter.Unop;
import org.genesys.utils.LibUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by yufeng on 6/4/17.
 */
public class MinimumUnop implements Unop {

    public Object apply(Object obj) {
        if (obj instanceof  Integer){
            assert ((Integer)obj == 256);
            return new ArrayList<>();
        }
        assert obj instanceof List : obj;
        List<Integer> list = LibUtils.cast(obj);
        if (list.isEmpty()) return 256;

        Optional<Integer> min = list.stream().reduce(Integer::min);
        return min.get();
    }

    public String toString() {
        return "MINIMUM";
    }
}
