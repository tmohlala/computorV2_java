import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Scanner;
import java.util.EmptyStackException;

class RPNCal {
    //Associativity constants for operators
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;

    //Supported operators
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
    static {
        // Map<"token", []{precedence, associativity}>
        OPERATORS.put("+", new int[] {0, LEFT_ASSOC});
        OPERATORS.put("-", new int[] {0, LEFT_ASSOC});
        OPERATORS.put("*", new int[] {5, LEFT_ASSOC});
        OPERATORS.put("/", new int[] {5, LEFT_ASSOC});
        OPERATORS.put("%", new int[] {5, LEFT_ASSOC});
        OPERATORS.put("^", new int[] {10, RIGHT_ASSOC});
    }

    /**
    * Test if a certain token is an operator .
    * @param token The token to be tested .
    * @return True if token is an operator . Otherwise False .
    */

    public static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    /**
     * Test if a certain string is an equation
     * @param str The string to be tested
     * @return True if string is an equation
     */

    public static boolean isEquation(String str) {
        for(int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)) && Character.isLetter(str.charAt(i))  && !(str.contains(".") || str.contains("/") ||
            str.contains("%") || str.contains("*") || str.contains("+") || str.contains("-") || str.contains("^"))) {
                return false;
            }
        }
        return true;
    }

    /**
    *Test the associativity of a certain operato token.
    *@param token The token to be tested (needs to be an operator)
    *@param type LEFT_ASSOC or RIGHT_ASSOC
    *@return True if the tokenType equals the input parameter type.
    */

    private static boolean isAssociative(String token, int type) {
        if(!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
        if(OPERATORS.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    /**
    * Compare precendece of two operators.
    * @param token1 The first operator .
    * @param token2 The second operator .
    * @return A negative number if token1 has a smaller precedence than token2,
    * 0 if the precendences of the two tokens are equal, a positive number
    * otherwise.
    */

    private static final int cmpPrecedence(String token1, String token2) {
        if(!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalid tokens:" + token1 + " " + token2);
        }
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }

    public static String[] infixToRPN(String[] inpuTokens) throws EmptyStackException {
        ArrayList<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();

        // For all the input tokens [S1] read the next token [S2]
        for(String token : inpuTokens) {
            // If token is an operator (x)[S3]
            if(isOperator(token)) {
                while(!stack.empty() && isOperator(stack.peek())) {
                    //[S4]
                    if((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(token, stack.peek()) <= 0) 
                    || (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(token, stack.peek()) < 0)) {
                        out.add(stack.pop()); //[S5] [S6]
                        continue;
                    }
                    break;
                }
                // Push the new operator on the stack [S7]
                stack.push(token);
            }
            else if(token.equals("(")) {
                stack.push(token); // [S8]
            }
            else if(token.equals(")")) {
                //[S9]
                while(!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop()); // [S10]
                }
                stack.pop(); // [S11]
            }
            else {
                out.add(token); // [S12]
            }
        }
        while (!stack.empty()) {
          out.add(stack.pop());
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

    public static boolean isNumber(String string) {
        int i = 0;

        if (string == null || string.isEmpty()) {
            return false;
        }
        if (string.charAt(0) == '-') {
            if (string.length() > 1) {
                i++;
            }
            else {
                return false;
            }
        }
        for (; i < string.length(); i++) {
            if (!Character.isDigit(string.charAt(i)) && !string.contains(".")) {
                return false;
            }
        }
        return true;
    }

    // Compute the equation
    public static double calculate(String[] input) throws ArithmeticException, IllegalArgumentException {
        Stack<Double> res = new Stack<Double>();
        double result = 0;

        for(String token : input) {
            if(isNumber(token)) {
                res.push(Double.parseDouble(token));
            }
            else if(isOperator(token)) {
                double a = res.pop();
                double b = res.pop();
                res.push(compute(a, b, token));
            }
            else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }
        result = res.pop();
        return result;
    }

    private static double compute(double a, double b, String opt) throws ArithmeticException {
        if(opt.equals("*")) {
            b *= a;
        }
        else if(opt.equals("/")) {
            b /= a;
        }
        else if(opt.equals("%")) {
            b = (int)b % (int)a;
        }
        else if(opt.equals("+")) {
            b += a;
        }
        else if(opt.equals("-")) {
            b -= a;
        }
        else if(opt.equals("^")) {
            double mul = b;
            for(int i = 1; i < a; i++) {
                b *= mul;
            }
        }
        return (b);
    }
}
