import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import java.util.Scanner;
import java.util.EmptyStackException;

class CreateVars {
    private String _varName;
    private double _value;
    private String[] _tokens;
    static RPNCal obj = new RPNCal();

    CreateVars(String varName, String[] tokens) {
        _varName = varName;
        _tokens = tokens;
    }

    CreateVars(String [] tokens) {
        _tokens = tokens;
    }

    private void setValue() throws ArithmeticException, IllegalArgumentException, EmptyStackException {
        if(_tokens.length > 1) {
            String[] output = RPNCal.infixToRPN(_tokens);
            _value = RPNCal.calculate(output);
        }
        else if(_tokens.length == 1 && RPNCal.isNumber(_tokens[0])) {
            _value = Double.parseDouble(_tokens[0]);
        }
        else {
            _value = 0;
        }
    }

    public String getVarName() {
        return _varName;
    }

    public double getValue() throws ArithmeticException, IllegalArgumentException, EmptyStackException {
        setValue();
        return _value;
    }
}