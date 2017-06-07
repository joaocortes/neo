package org.genesys.language;

/**
 * Created by yufeng on 5/26/17.
 * Production: src -> (function) inputs
 */
public class Production <T> {

    public final String function;

    public final T[] inputs;

    public final T source;

    public String[] spec;

    @SafeVarargs
    public Production(T src, String function, T ... inputs) {
        this.source = src;
        this.function = function;
        this.inputs = inputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Production<?> that = (Production<?>) o;

        return function != null ? function.equals(that.function) : that.function == null;
    }

    @Override
    public int hashCode() {
        return function != null ? function.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.function);
        if(this.inputs.length > 0) {
            sb.append("(");
            for(T t : this.inputs) {
                sb.append(t.toString()).append(", ");
            }
            sb.delete(sb.length()-2, sb.length());
            sb.append(")");
        }
        return sb.toString();
    }
}
