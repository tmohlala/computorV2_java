import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.*;
import java.util.EmptyStackException;

class ComputorV2 {
    private static String _lhs = "";
    private static String _rhs = "";
    static Map<String, Double> vars = new HashMap<String, Double>();
    static Map<String, String> compVars = new HashMap<String, String>();

    /**
    * Checks if a String is empty ("") or null.
    * @param str  the String to check, may be null
    * @return true if the String is empty or null
    */

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Counts how many time a substring occurs in a large string
     * @param str larger string can be null
     * @param sub substring to be counted can be null
     * @return The number of occurences of substring, 0 if no occurence is either
     * string is null
     */

    public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    /**
     * Read the input from the user and separate the lhs
     * and rhs if there is an equal sign
     * @param input string from console
     */

    private static void readInput(String input) {
        String[] res = {};

        if(countMatches(input, "=") > 1) {
            System.out.println("Error: Invalid input!");
            _lhs = "";
            _rhs = "";
            return;
        }
        if(input.contains("=")) {
            String temp = input.replaceAll("\\s", "");
            res = temp.split("=");
            if(res.length == 2) {
                _lhs = res[0];
                _rhs = res[1];
            }
            if(isEmpty(_lhs)) {
                System.out.println("Error: invalid operation.");
            }
        }
        else if(!input.contains("=")) {
            _lhs = input;
        }
    }

     /**
     * Test if a certain string is a Variable
     * Variables contain alphabets and underscores only
     * @param str The string to be tested
     * @return True if string is an equation
     */

    private static boolean isVariable(String str) {
        for(int i = 0; i < str.length(); i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tokenize a string into an array
     * @param str String to tokenize
     * @return Array of tokens
     */

    private static String[] tokens(String str) {
        String[] tempTokens = str.split("");
        ArrayList<String> tempArr = new ArrayList<String>();
        int j = 0;
        int i = 0;

        for( String token : tempTokens) {
            if(RPNCal.isNumber(token)) {
                String temp = "";
                if(j > 0) {
                    temp += tempArr.get(tempArr.size() - 1);
                    tempArr.remove(tempArr.size() - 1);
                }
                j++;
                i = 0;
                temp += token;
                tempArr.add(temp);
            }
            else if(isVariable(token) && !RPNCal.isNumber(token)) {
                String temp = "";
                if(i > 0) {
                    temp += tempArr.get(tempArr.size() - 1);
                    tempArr.remove(tempArr.size() - 1);
                }
                i++;
                j = 0;
                temp += token;
                tempArr.add(temp);
            }
            else {
                j = 0;
                i = 0;
                tempArr.add(token);
            }
        }
        String[] tokens = new String[tempArr.size()];
        tempArr.toArray(tokens);
        return tokens;
    }

    /**
     * Create natural numbers variables and do operation on calculations.
     */

    private static void varCreateAndResolve() throws ArithmeticException, IllegalArgumentException, EmptyStackException {
        if(_lhs.equals("i")) {
            System.out.println("Error: Illegal assignment [i is unassignable].");
            return;
        }
        if(compVars.get(_lhs) != null) {
            compVars.remove(_lhs);
        }
        if(_lhs.length() != 0 && _rhs.length() != 0) {
            if((RPNCal.isEquation(_lhs) || isVariable(_lhs) ) && _rhs.equals("?")) {
                String[] tokens = tokens(_lhs);
                boolean flag = true;

                for(int i = 0; i < tokens.length; i++) {
                    if(isVariable(tokens[i])) {
                        if(vars.get(tokens[i]) != null) {
                            tokens[i] = Double.toString(vars.get(tokens[i]));
                        }
                        else {
                            System.out.println("Error: Illegal variable in equation!");
                            _lhs = "";
                            _rhs = "";
                            flag = false;
                            break;
                        }
                    }
                }
                if(flag) {
                    CreateVars obj = new CreateVars(tokens);
                    System.out.println(obj.getValue());
                    _rhs = "";
                    _lhs = "";
                    return;
                }
                else {
                     return;
                }
            }
            else if(!isVariable(_lhs) && _lhs.length() != 0) {
                System.out.println("Error: Illegal Variable Name / operation.");
                _lhs = "";
                _rhs = "";
                return;
            }
            if(RPNCal.isEquation(_rhs) || RPNCal.isNumber(_rhs)) {
                String[] tokens = tokens(_rhs);
                boolean flag = true;

                for(int i = 0; i < tokens.length; i++) {
                    if(isVariable(tokens[i])) {
                        if(vars.get(tokens[i]) != null) {
                            tokens[i] = Double.toString(vars.get(tokens[i]));
                        }
                        else {
                            System.out.println("Error: Illegal variable in equation!");
                            _lhs = "";
                            _rhs = "";
                            flag = false;
                            break;
                        }
                    }
                }
                if(flag) {
                    CreateVars obj = new CreateVars(_lhs, tokens);
                    if(RPNCal.isNumber(Double.toString((obj.getValue())))) {
                        vars.put(obj.getVarName(), obj.getValue());
                        System.out.println(obj.getValue());
                    }
                    else {
                        System.out.println("Error: illegal operation.");
                    }
                    _rhs = "";
                    _lhs = "";
                }
            }
            else {
                ;
            }
        }
    }

    /**
     * Create complex variable.
     */

    private static void createComplex() {
        Complex obj = new Complex(_rhs, _lhs);

        if(vars.get(_lhs) != null) {
            vars.remove(_lhs);
        }
        compVars.put(obj.getName(), obj.getComplex());
        System.out.println(compVars.get(_lhs));
    }

    /**
     * Check if a variable exists in variable maps.
     */

    private static void varCheck() {
        if((vars.get(_lhs)) != null) {
            double val = vars.get(_lhs);
            System.out.println(val);
        }
        else if(compVars.get(_lhs) != null) {
            String val = compVars.get(_lhs);
            System.out.println(val);
        }
        else {
             System.out.println("Error: Illegal variable [no value or illegal name]!");
        }
    }

    /**
     * Assigment of variable from a variable that exists
     * in maps.
     */

    private static void varToVarAssign() {
        if(vars.get(_rhs) != null) {
            vars.put(_lhs, vars.get(_rhs));
            System.out.println(vars.get(_lhs));
            _rhs = "";
            _lhs = "";
        }
        else if(compVars.get(_rhs) != null) {
           compVars.put(_lhs, compVars.get(_rhs));
            System.out.println(compVars.get(_lhs));
            _rhs = "";
            _lhs = "";
        }
        else {
            System.out.println("Error: Illegal assignment, rhs variable doesn't exist");
            _rhs = "";
            _lhs = "";
        }
    }

    public static void main(String[] args) {
        Scanner myVar = new Scanner(System.in);
        String input;

        while(true) {
            System.out.print("$>> ");
            input = myVar.nextLine();
            if(input.equals("exit") || input.equals("EXIT")) {
                return;
            }
            readInput(input);
            String complex = "[0-9]*i";
            Pattern checkComplex = Pattern.compile(complex);
            Matcher complexMatcher = checkComplex.matcher(_rhs);

            if((_lhs.length() > 0 && _rhs.length() == 0) || (isVariable(_lhs) && _rhs.equals("?"))) {
                varCheck();
                _lhs = "";
                _rhs = "";
            }
            else if(isVariable(_lhs) && isVariable(_rhs)) {
                varToVarAssign();
            }
            else if(complexMatcher.find()) {
                try {
                    createComplex();
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: illegal token.");
                }
                _lhs = "";
                _rhs = "";
            }
            else {
                try {
                    varCreateAndResolve();
                }
                catch (ArithmeticException e) {
                    System.out.println("Undefined: division/mod by zero");
                }
                catch(IllegalArgumentException e) {
                    System.out.println("Error: Incomplete operation.");
                }
                catch(EmptyStackException e) {
                    System.out.println("Error: Incomplete operation.");
                }
            }
        }
    }
}