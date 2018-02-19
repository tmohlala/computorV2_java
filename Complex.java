import java.util.ArrayList;

class Complex {
    private String[] _tokens;
    private String _arg;
    private String _name;
    private String _result = "";

    Complex(String arg, String name) {
        this._arg = arg.replaceAll("\\s", "");
        this._name = name;
    }

    void tokens() {
        _tokens = _arg.split("");
    }

    private void formatComplex() throws IllegalArgumentException {
        tokens();
        int i = 0;
        int j = 0;
        ArrayList<String> temp = new  ArrayList<String>();

        for( String token : _tokens) {
            if(token.contains("*")) {
                continue;
            }
            if(RPNCal.isNumber(token) || token.contains("i")) {
                String str = "";
                if(j > 0) {
                    str += temp.get(temp.size() - 1);
                    temp.remove(temp.size() - 1);
                }
                j++;
                i = 0;
                str += token;
                temp.add(str);
            }
            else if(token.contains("+") || token.contains("-") || token.contains("*")) {
                j = 0;
                i = 0;
                temp.add(token);
            }
            else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }
        _tokens = new String[temp.size()];
        temp.toArray(_tokens);
        i = 0;
        String sign = "";
        String temp1 = "";
        String temp2 = "";

        for(String token : _tokens) {
            if(RPNCal.isOperator(token)) {
                sign = token;
            }
            else {
                if(token.contains("i")) {
                    if(sign.length() > 0) {
                        temp1 += sign + " ";
                        sign = "";
                    }
                    else {
                        if(i == 0) {
                            temp1 += "+ ";
                        }
                    }
                    temp1 += token;
                }
                else {
                    if(sign.length() > 0 && !sign.contains("+")) {
                        temp2 += sign + " ";
                    }
                    temp2 += token;
                }
            }
            i++;
        }
        _result += temp2 + " " + temp1;
    }

    public String getComplex() {
        formatComplex();
        return this._result;
    }

    public String getName() {
        return this._name;
    }
}