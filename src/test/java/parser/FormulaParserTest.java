package parser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormulaParserTest {

    public enum FunctionType {
        CNTOP {
            public BigDecimal cntop() {
                return super.cntop();
            }
        },
        SUMOP {
            public BigDecimal sumop() {
                return super.sumop();
            }
        },          // 2.555
        RESERV {
            public BigDecimal reserv() {
                return super.reserv();
            }
        },         // 0.8888888
        ROUNDCOST {
            public BigDecimal roundcost(BigDecimal a) {
                return super.roundcost(a);
            }
        },      //  /4
        ROUNDOPERSUM {
            public BigDecimal roundopersum(BigDecimal a) {
                return super.roundopersum(a);
            }
            // round(2)
        };
        public BigDecimal cntop() {
            return new BigDecimal("-0.11");
        }

        public BigDecimal sumop() {
            return new BigDecimal("2.555");
        }

        public BigDecimal reserv() {
            return new BigDecimal("0.888888");
        }

        public BigDecimal roundcost(BigDecimal a) {
            BigDecimal divisor = new BigDecimal(4);
            return a.divide(divisor);
        }

        public BigDecimal roundopersum(BigDecimal a) {
            MathContext m = new MathContext(3);
            return a.round(m);
        }
    }

    static final String[] functionsWithArgs = {"ROUNDCOST", "ROUNDOPERSUM"};

    public static String validate(String formula) {       //  empty brackets, double spaces, all to upper case, spaces between

        formula = formula.replace("  ", " ");
        formula = formula.toUpperCase();
        formula = formula.replace("()", "");
//        formula = bracketSpaces(formula);

        return formula;
    }

//    public static String bracketSpaces(String formula) {
//        char[] formulaArray = formula.toCharArray();
//
//        for (int i = 0; i < formulaArray.length; i++) {
//            if (formulaArray[i] == '(' && i+1 != formulaArray.length && formulaArray[i+1] != ' ') {
//                formula = formula.substring(0, i) + " " + formula.substring(i);
//                System.out.println("new formula: " + formula);
//                return bracketSpaces(formula);
//            }
////            if (formulaArray[i] == ')' && i-1 > 0 && formulaArray[i-1] != ' ') {
////                formula = formula.substring(0, i) + " " + formula.substring(i);
////                return bracketSpaces(formula);
////            }
//        }
//        return formula;
//    }

//    public static String bracketSpaces(String formula) {
//        if ((formula.contains("(") || formula.contains(")"))
//                && !(formula.contains("( ") && formula.contains(" )"))) {
//            char[] formulaArray = formula.toCharArray();
//
//            int obIndex = formula.indexOf("(");
//            if (obIndex + 1 != formula.length() && formulaArray[obIndex + 1] != ' ') {
//                formula = formula.substring(0, obIndex + 1) + " " + formula.substring(obIndex + 1);
//                return bracketSpaces(formula);
//            }
//            int cbIndex = formula.indexOf(")");
//            if (cbIndex > 0 && formulaArray[cbIndex - 1] != ' ') {
//                formula = formula.substring(0, cbIndex) + " " + formula.substring(cbIndex);
//                return bracketSpaces(formula);
//            }
//        }
//        return formula;
//    }

//    public static String operationSpaces(String formula) {
//        if (formula.matches(".*(\\+|-|\\*|/).*")) {
//            char[] formulaArray = formula.toCharArray();
//
//        }
//        return formula;
//    }

    public static BigDecimal calculate(String formula) {
        if (formula.isEmpty()) {
            return null;
        }
        List<String> operations = formulaSplit(formula);

        return checkSimpleMath(operations);
    }

    public static List<String> formulaSplit(String formula) {
        List<String> operations = new ArrayList<>();

        if (formula.contains(" ")) {
            char[] formulaArray = formula.toCharArray();

            int operStart = 0;

            for (int i = 0; i < formulaArray.length; i++) {     //delimit for func with multiple args

                if (i+1 < formulaArray.length && i > 0
                        && formulaArray[i] == ' ' && (formulaArray[i-1] == '(')) {

                    String buffer = formula.substring(operStart, i);
                    if (Arrays.stream(functionsWithArgs).anyMatch(buffer::contains)) {

                        int leftBracket = 1;
                        int rightBracket = 0;

                        while (leftBracket != rightBracket && i < formulaArray.length) {

                            if (formulaArray[i] == '(') leftBracket++;
                            if (formulaArray[i] == ')') rightBracket++;
                            i++;
                        }

                        operations.add(formula.substring(operStart, i));
                        operStart = i+1;
                    }

                } else if (i + 1 < formulaArray.length && i > 0 && formulaArray[i] == ' ') {
                    operations.add(formula.substring(operStart, i));
                    operStart = i + 1;
                } else if (i + 1 == formulaArray.length && formulaArray[i] != ' ') {
                    operations.add(formula.substring(operStart, i + 1));
                }
            }
            return operations;
        } else {
            operations.add(formula);
            return operations;
        }
    }

    public static BigDecimal checkSimpleMath(List<String> operations) {//  start calculating
        BigDecimal res = new BigDecimal("0");

        for (int i = 0; i < operations.size(); i++) {
            String cur = operations.get(i);

            int key = typeCheck(cur);

            if (key == 1) {
                BigDecimal bufferDec = new BigDecimal(cur);
                res = res.add(bufferDec);
            }
            if (key == 2) {
                String nextOpStr = operations.get(i+1);

                BigDecimal nextOp = new BigDecimal(0);
                int key2 = typeCheck(nextOpStr);

                if (key2 == 1) {
                    nextOp = nextOp.add(new BigDecimal(nextOpStr));
                } else if (key2 == 3) {
                    nextOp = nextOp.add(functionUse(nextOpStr));
                } else {
                    System.out.println("Operations like -- or +- are not supported!");
                    return null;
                }

                if (cur.equals("+")) {
                    res = res.add(nextOp);
                    i++;
                } else if (cur.equals("-")) {
                    res = res.subtract(nextOp);
                    i++;
                } else if (cur.equals("*")) {
                    res = res.multiply(nextOp);
                    i++;
                } else if (cur.equals("/")) {
                    if (nextOp.doubleValue() == 0) {
                        System.out.println("Incorrect input of " + nextOp);
                        return null;
                    } else {
                        res = res.divide(nextOp);
                        i++;
                    }
                } else {
                    System.out.println("Incorrect input of " + cur);
                    return null;
                }
            }
            if (key == 3) {
                res = res.add(functionUse(cur));
            }
        }
        return res;
    }

    public static int typeCheck(String operation) {

        if (operation.matches("-?\\d+(\\.\\d+)?")) {
            return 1;
        } else if (operation.matches("^(\\+|-|\\*|/)$")) {
            return 2;
        } else if (operation.matches("^(CNTOP|SUMOP|RESERV|ROUNDCOST|ROUNDOPERSUM).*")) {
            return 3;
        } else {
            System.out.println("Cant't match operation type: " + operation);
        }
        return 900;
    }

    public static BigDecimal functionUse(String operation) {
        BigDecimal errorCode = new BigDecimal(5000);

        if (operation.equals("CNTOP")) {
            return FunctionType.CNTOP.cntop();

        } else if (operation.equals("SUMOP")) {
            return FunctionType.SUMOP.sumop();

        } else if (operation.equals("RESERV")) {
            return FunctionType.RESERV.reserv();

        } else if (operation.matches("^ROUNDCOST\\(.*.\\)")) {
            int iBegin = operation.indexOf("(");
            String arg = operation.substring(iBegin + 2, operation.length() - 2);
            BigDecimal argValue = calculate(arg);
            return FunctionType.ROUNDCOST.roundcost(argValue);

        } else if (operation.matches("^ROUNDOPERSUM\\(.*.\\)")) {
            int iBegin = operation.indexOf("(");
            String arg = operation.substring(iBegin + 2, operation.length() - 2);
            BigDecimal argValue = calculate(arg);
            return FunctionType.ROUNDOPERSUM.roundopersum(argValue);

        } else {
            System.out.println("Invalid function entered or no brackets found: " + operation);
        }
        return errorCode;
    }

    @Test
    void test1() {
        String f1 = "2 + CNTOP + CNTOP +  ROUNDOPERSUM( RESERV + 1) - roundcost(234 - 3.234234)";

        String f2 = validate(f1);

        System.out.println(f1);
        System.out.println(f2);

        BigDecimal result = calculate(f2);
        System.out.println(result);
    }
}