package org.genesys.language;

import org.genesys.models.Example;
import org.genesys.models.Problem;
import org.genesys.type.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yufeng on 5/31/17.
 * Grammar for deepCoder.
 */
public class DeepCoderGrammar implements Grammar<AbstractType> {

    private AbstractType inputType;

    private AbstractType outputType;

    private List<InputType> inputTypes = new ArrayList<>();

    public DeepCoderGrammar(AbstractType inputType, AbstractType outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public DeepCoderGrammar(Problem p) {
        assert !p.getExamples().isEmpty();
        Example example = p.getExamples().get(0);
        List input = example.getInput();
        for (int i = 0; i < input.size(); i++) {
            Object elem = input.get(i);
            InputType in;
            if (elem instanceof List)
                in = new InputType(i, new ListType(new IntType()));
            else
                in = new InputType(i, new IntType());

        /* dynamically add input to grammar. */
            addInput(in);
        }
        Object output = example.getOutput();
        //output is either an integer or list.
        if (output instanceof List) {
            this.outputType = new ListType(new IntType());
        } else {
            this.outputType = new IntType();
        }
    }

    public void addInput(InputType in) {
        inputTypes.add(in);
    }

    @Override
    public AbstractType start() {
        return new InitType(this.outputType);
    }

    @Override
    public String getName() {
        return "DeepCoderGrammar";
    }

    @Override
    public List<Production<AbstractType>> getProductions() {
        return null;
    }

    @Override
    public List<Production<AbstractType>> productionsFor(AbstractType symbol) {
        List<Production<AbstractType>> productions = new ArrayList<>();
        if (symbol instanceof InitType) {
            InitType type = (InitType) symbol;
            productions.add(new Production<>(symbol, "root", type.goalType));
        } else if (symbol instanceof BoolType) {
            productions.add(new Production<>(symbol, "true"));
            productions.add(new Production<>(symbol, "false"));
        } else if (symbol instanceof IntType) {
            productions.add(new Production<>(symbol, "0"));
            productions.add(new Production<>(symbol, "1"));
            productions.add(new Production<>(symbol, "+1", symbol));
            productions.add(new Production<>(symbol, "-1", symbol));
            productions.add(new Production<>(symbol, "*3", symbol));
            productions.add(new Production<>(symbol, "maximum", new ListType(new IntType())));
            productions.add(new Production<>(symbol, "minimum", new ListType(new IntType())));
            productions.add(new Production<>(symbol, "sum", new ListType(new IntType())));
            productions.add(new Production<>(symbol, "head", new ListType(new IntType())));
            productions.add(new Production<>(symbol, "last", new ListType(new IntType())));

            productions.add(new Production<>(symbol, "count", new FunctionType(new IntType(), new BoolType()),
                    new ListType(new IntType())));

        } else if (symbol instanceof ListType) {
            ListType type = (ListType) symbol;
            AbstractType T = type.type;
            /* list can go to list */
            // filter (T -> Boolean) ::= (List<T> -> List<T>)
            productions.add(new Production<>(symbol, "filter", new FunctionType(T, new BoolType()),
                    new ListType(new IntType())));
            // map (I -> O) ::= (List<I> -> List<O>)
            productions.add(new Production<>(symbol, "map", new FunctionType(T, T),
                    new ListType(new IntType())));
            /* list can go to two list */
            // zipWith (List<T> -> List<T> -> int -> int -> int -> List<T>)
            productions.add(new Production<>(symbol, "zipWith", new FunctionType(new PairType(T, T), T),
                    new ListType(new IntType()), new ListType(new IntType())));

            /* other functions */
            productions.add(new Production<>(symbol, "sort", new ListType(new IntType())));
            productions.add(new Production<>(symbol, "reverse", new ListType(new IntType())));
            productions.add(new Production<>(symbol, "take", new ListType(new IntType()), new IntType()));
//            productions.add(new Production<>(symbol, "drop", new ListType(new IntType()), new IntType()));
            productions.add(new Production<>(symbol, "scanl", new FunctionType(new PairType(T, T), T),
                    new ListType(new IntType())));
        } else if (symbol instanceof FunctionType) {
            FunctionType type = (FunctionType) symbol;
            // l(a,b).(+ a b) ::= ((Integer, Integer) -> Integer)
            // l(a,b).(* a b) ::= ((Integer, Integer) -> Integer)
            if (type.inputType instanceof PairType && ((PairType) type.inputType).firstType instanceof IntType
                    && ((PairType) type.inputType).secondType instanceof IntType && type.outputType instanceof IntType) {
                productions.add(new Production<>(symbol, "l(a,b).(+ a b)"));
                productions.add(new Production<>(symbol, "l(a,b).(* a b)"));
                productions.add(new Production<>(symbol, "l(a,b).(% a b)"));
                productions.add(new Production<>(symbol, "l(a,b).(min a b)"));
            }
            // l(a,b).(> a b) ::= ((Integer, Integer) -> Boolean)
            // l(a,b).(< a b) ::= ((Integer, Integer) -> Boolean)
            // l(a,b).(>= a b) ::= ((Integer, Integer) -> Boolean)
            // l(a,b).(<= a b) ::= ((Integer, Integer) -> Boolean)
            if (type.inputType instanceof PairType && ((PairType) type.inputType).firstType instanceof IntType
                    && ((PairType) type.inputType).secondType instanceof IntType && type.outputType instanceof BoolType) {
                productions.add(new Production<>(symbol, "l(a,b).(> a b)"));
                productions.add(new Production<>(symbol, "l(a,b).(< a b)"));
                productions.add(new Production<>(symbol, "l(a,b).(== a b)"));
            }
            // l(a,b).(|| a b) ::= ((Boolean, Boolean) -> Boolean)
            // l(a,b).(&& a b) ::= ((Boolean, Boolean) -> Boolean)
            if (type.inputType instanceof PairType && ((PairType) type.inputType).firstType instanceof BoolType
                    && ((PairType) type.inputType).secondType instanceof BoolType && type.outputType instanceof BoolType) {
                productions.add(new Production<>(symbol, "l(a,b).(|| a b)"));
                productions.add(new Production<>(symbol, "l(a,b).(&& a b)"));
            }
            // l(a).(+ a b) (Integer) ::= (Integer -> Integer)
            // l(a).(* a b) (Integer) ::= (Integer -> Integer)
            if (type.inputType instanceof IntType && type.outputType instanceof IntType) {
                productions.add(new Production<>(symbol, "l(a).(+ a b)", new IntType()));
                productions.add(new Production<>(symbol, "l(a).(* a b)", new IntType()));
//                productions.add(new Production<>(symbol, "l(a).(% a b)", new IntType()));
            }
            // l(a).(> a b) (Integer) ::= (Integer -> Boolean)
            // l(a).(< a b) (Integer) ::= (Integer -> Boolean)
            if (type.inputType instanceof IntType && type.outputType instanceof BoolType) {
                productions.add(new Production<>(symbol, "l(a).(> a b)", new IntType()));
                productions.add(new Production<>(symbol, "l(a).(< a b)", new IntType()));
                productions.add(new Production<>(symbol, "l(a).(== a b)", new IntType()));
            }
        }

        /* Handle inputs */
        for (InputType input : inputTypes) {
            boolean flag1 = (symbol instanceof IntType) && (input.getType() instanceof IntType);
            boolean flag2 = (symbol instanceof ListType) && (input.getType() instanceof ListType);
            if (flag1 || flag2) {
                productions.add(new Production<>(symbol, "input" + input.getIndex()));
            }
        }
        return productions;
    }
}
