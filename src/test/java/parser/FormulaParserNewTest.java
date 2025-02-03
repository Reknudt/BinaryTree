package parser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static parser.FormulaParserNewTest.FunctionType.functionsWithArgs;

public class FormulaParserNewTest {

    public enum FunctionType {
        CNTOP {
            @Override
            public BigDecimal apply() {
                return new BigDecimal("-0.11");
            }
        },
        SUMOP {
            @Override
            public BigDecimal apply() {
                return new BigDecimal("2.555");
            }
        },
        RESERV {
            @Override
            public BigDecimal apply() {
                return new BigDecimal("0.888888");
            }
        },
        ROUNDCOST {
            @Override
            public BigDecimal apply(BigDecimal a) {
                BigDecimal divisor = new BigDecimal(4);
                return a.divide(divisor, 4, BigDecimal.ROUND_HALF_EVEN);
            }
        },
        ROUNDOPERSUM {
            @Override
            public BigDecimal apply(BigDecimal a) {
                return a.setScale(2, ROUND_HALF_UP );
            }
        };

        public BigDecimal apply(BigDecimal a) {
            return a;
        }

        public BigDecimal apply() {
            return null;
        }

        static final String[] functionsWithArgs = {"ROUNDCOST", "ROUNDOPERSUM"};          //
    }

    public static String validate(String formula) {       //

        formula = formula.replace(" ", "");
        if (formula.isEmpty()) throw new IllegalArgumentException("String is empty");
        formula = formula.toUpperCase();
        formula = functionArgumentCheck(formula);
        formula = emptyBracketsReplacer(formula);
        formula = addBrackets(formula);

        startsWithCheck(formula);
        endsWithCheck(formula);
        isFormulaValid(formula);
        hasMultipleOperators(formula);
        isBracketWithNoOperator(formula);
        return formula;
    }

    public static String functionArgumentCheck(String formula) {
        List<String> tokens = tokenize(formula);

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (i > 0 && i < tokens.size()-1 && token.equals("(") && tokens.get(i+1).equals(")")
                && Arrays.stream(functionsWithArgs).toList().contains(tokens.get(i-1).replace("-",""))) {
                throw new IllegalArgumentException("Functions with arguments such as " + Arrays.stream(functionsWithArgs).toList()
                 + " must have argument");
            }
        }
        return formula;
    }

    public static String emptyBracketsReplacer(String formula) {
        formula = formula.replace("()", "");
        if (formula.contains("()")) return emptyBracketsReplacer(formula);
        return formula;
    }

    public static String addBrackets(String formula) {
        if (formula.contains("(") || formula.contains(")")) {
            int obCount = 0;
            int cbCount = 0;

            for(char c : formula.toCharArray()) {
                if (c == '(') obCount++;
                if (c == ')') cbCount++;
                if (cbCount > obCount) throw new IllegalArgumentException("Illegal ')' found");
            }
            if (cbCount < obCount) {
                while(cbCount < obCount) {
                    formula = formula.concat(")");
                    cbCount++;
                }
            } else if (cbCount > obCount) {
                throw new IllegalArgumentException("Too much close brackets found");
            }
        }
        return formula;
    }

    public static void startsWithCheck(String formula) {
        if (formula.startsWith("*") || formula.startsWith("/")) {
            throw new IllegalArgumentException("Formula can't start with '*' or '/'. " +
                    " Maybe you meant " + formula.substring(1));
        }
    }

    public static void endsWithCheck(String formula) {
        String endStr = formula.substring(formula.length() - 1);
        if (isOperator(endStr) || endStr.equals("(")) {
            throw new IllegalArgumentException("Formula can't end with operators like '+' or '(' : " + endStr);
        }
    }

    public static void isFormulaValid(String formula) {
        List<String> tokens = tokenize(formula);

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (!isOperator(token) && !isNumber(token) && !isFunction(token)
                && !token.equals("(") && !token.equals(")") && !token.equals("~")) {
                throw new IllegalArgumentException("Illegal operation entered: " + token);
            }
            if (Arrays.stream(functionsWithArgs).toList().contains(tokens.get(i).replace("-",""))
                    && i == tokens.size()-1) {
                throw new IllegalArgumentException("This function needs bracket '(' : " + token);
            }
            if (Arrays.stream(functionsWithArgs).toList().contains(tokens.get(i).replace("-",""))
                    && i < tokens.size() && !tokens.get(i+1).equals("(")) {
                throw new IllegalArgumentException("This function needs bracket '(' : " + token);
            }
        }
    }

    public static void hasMultipleOperators(String formula) {
        List<String> tokens = tokenize(formula);

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if ((isOperator(token) || token.equals("~")) && (isOperator(tokens.get(i+1)) || tokens.get(i+1).contains("-") || tokens.get(i+1).equals("~"))) {
                throw new IllegalArgumentException("Too many operators entered: " + token + tokens.get(i+1));
            }
        }
    }

    public static void isBracketWithNoOperator(String formula) {
        List<String> tokens = tokenize(formula);

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if ((token.equals("(") && i != 0)
                    && (!isOperator(tokens.get(i - 1))
                        && !tokens.get(i-1).equals("~")
                        && !tokens.get(i-1).equals("(")
                        && !Arrays.stream(functionsWithArgs).toList().contains(tokens.get(i - 1).replace("-","")))) {
                throw new IllegalArgumentException("Illegal operator placement " + tokens.get(i - 1) + token);
            }

            if (token.equals("(")
                    && i < tokens.size()
                    && (isOperator(tokens.get(i+1))
                    && !tokens.get(i+1).equals("~"))) {
                throw new IllegalArgumentException("Illegal operator placement " + token + tokens.get(i+1));
            }

            if (token.equals(")")) {
                if (i == 0) {
                    throw new IllegalArgumentException("Illegal ')' placement");
                }
                if (isOperator(tokens.get(i - 1))) {
                    throw new IllegalArgumentException("Illegal operator placement: " + tokens.get(i - 1) + token);
                }
                if (i < tokens.size() - 1 && (!isOperator(tokens.get(i + 1)) && !tokens.get(i + 1).equals(")"))) {
                    throw new IllegalArgumentException("Illegal  placement: " + token + tokens.get(i + 1));
                }
            }
        }
    }

    public static BigDecimal calculate(String expression) {
        List<String> tokens = tokenize(expression);
        Queue<String> postfix = infixToPostfix(tokens);
        return calculatePostfix(postfix);
    }

    private static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || (ch == '.' && !buffer.isEmpty()) || ch == '_') {
                buffer.append(ch);
            } else if (Character.isLetter(ch)) {
                buffer.append(ch);
            } else {
                if (!buffer.isEmpty()) {
                    tokens.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (ch == '-' && (i == 0 || expression.charAt(i - 1) == '(' || isOperator(String.valueOf(expression.charAt(i - 1))))) {
                    tokens.add("~");
                } else {
                    tokens.add(String.valueOf(ch));
                }
            }
        }

        if (!buffer.isEmpty()) {
            tokens.add(buffer.toString());
        }
        return tokens;
    }

    private static Queue<String> infixToPostfix(List<String> tokens) {
        Queue<String> output = new LinkedList<>();
        Deque<String> operators = new ArrayDeque<>();
        Map<String, Integer> precedence = Map.of(
                "~", 3,
                "*", 2, "/", 2,
                "+", 1, "-", 1
        );

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (isFunction(token)) {
                if (Arrays.stream(functionsWithArgs).toList().contains(token.replace("-",""))) {
                    operators.push(token);
                } else {
                    output.add(token);
                }
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                operators.pop();
                if (!operators.isEmpty() && isFunction(operators.peek())) {
                    output.add(operators.pop());
                }
            } else if (isOperator(token) || token.equals("~")) {
                while (!operators.isEmpty() && precedence.getOrDefault(operators.peek(), 0) >= precedence.get(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            }
        }
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }
        return output;
    }

    private static BigDecimal calculatePostfix(Queue<String> postfix) {
        Deque<BigDecimal> stack = new ArrayDeque<>();

        while (!postfix.isEmpty()) {
            String token = postfix.poll();
            if (isNumber(token)) {
                stack.push(new BigDecimal(token));
            } else if (isOperator(token)) {
                BigDecimal b = stack.pop();
                BigDecimal a = stack.pop();
                stack.push(applyOperator(a, b, token));
            } else if (token.equals("~")) {
                stack.push(stack.pop().negate());
            } else if (isFunction(token)) {
                FunctionType function = FunctionType.valueOf(token);
                BigDecimal result;
                if (Arrays.asList(FunctionType.functionsWithArgs).contains(token)) {
                    BigDecimal arg = stack.pop();
                    result = function.apply(arg);
                } else {
                    result = function.apply();
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }


    private static BigDecimal applyOperator(BigDecimal a, BigDecimal b, String operator) {
        switch (operator) {
            case "+":
                return a.add(b);
            case "-":
                return a.subtract(b);
            case "*":
                return a.multiply(b);
            case "/": {
                try {
                    return a.divide(b, 8, BigDecimal.ROUND_HALF_EVEN);
                } catch (ArithmeticException e) {
                    throw new ArithmeticException("Division by zero found");
                }
            }
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    private static boolean isNumber(String token) {
        try {
            new BigDecimal(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isOperator(String token) {
        return "+-*/".contains(token);
    }

    private static boolean isFunction(String token) {
        String baseToken = token.replace("-", "");
        try {
            FunctionType.valueOf(baseToken);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Test
    void test1() {
//        String f1 = "2/roundopersum(-(-(-(-(-2)))))";
//        String f1 = "33/(88*roundopersum(-(12-(-CNTOP)+7)*(1/SUMOP)-5))";
//        String f1 = "-(2 *(-(-(-1)))+4)/(-roundopersum(-(12-(-cntop)+7)*(1/sumop)-5))";
//        String f1 = "-ROUNDCOST(13.2342341341234/(-reserv*(-cntop-3.32457594759)+ROUNDOPERSUM(3.445353453*sumop)/1-23*3.222";
//        String f1 = "-(-roundcost(8.1/123)*23.2222)/(-1)*(3.33-2.1*cntop)/(-25.2)";
//        String f1 = "reserv*(-2+3)+cntop";
        String f1 = "4*CNTOP - (-5/(2))";
        System.out.println(f1);
        f1 = validate(f1);
        System.out.println(f1);
        BigDecimal result = calculate(f1);
        System.out.println(result);
    }
}