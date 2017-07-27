package org.genesys.clients;

import com.google.gson.Gson;
import org.genesys.interpreter.DeepCoderInterpreter;
import org.genesys.interpreter.Interpreter;
import org.genesys.language.DeepCoderGrammar;
import org.genesys.models.Problem;
import org.genesys.synthesis.Checker;
import org.genesys.synthesis.NeoSynthesizer;
import org.genesys.synthesis.DummyChecker;
import org.genesys.synthesis.NeoSynthesizer;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by ruben on 7/6/17.
 */
public class DeepCoderMainNeo {

    public static void main(String[] args) throws FileNotFoundException {
        String json = "./problem/DeepCoder/prog5.json";
        if (args.length != 0) json = args[0];
        Gson gson = new Gson();
        Problem dcProblem = gson.fromJson(new FileReader(json), Problem.class);
        System.out.println("Run DeepCoder main..." + dcProblem);

        DeepCoderGrammar grammar = new DeepCoderGrammar(dcProblem);
        Checker checker = new DummyChecker();
        Interpreter interpreter = new DeepCoderInterpreter();
        NeoSynthesizer synth;
        if (args.length == 2) {
            int depth = Integer.valueOf(args[1]);
            synth = new NeoSynthesizer(grammar, dcProblem, checker, interpreter, depth);
        } else {
            synth = new NeoSynthesizer(grammar, dcProblem, checker, interpreter);
        }
        synth.synthesize();
    }
}