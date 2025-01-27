package parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserCasesTest {

    @ParameterizedTest
    @ValueSource(strings = {"33/(88-(12-(-cntop)+7)*(1/sumop)-5)"})//"CNTOP() + 2.34234()", "  32.121212  +  CNTOP  ", "cntop + 23.234235", "-2+3",
//            "2*(3-ROUNDCOST(3 - 23", "--2+3", "2-3+", "2-3/", "2*-23.234)", "*3-2", "(CNTOP-2)CNTOP",
//            "CNTOP(2-2)", "2.234.234-2", "---CNTOP", "(1-2)(1+2)", "SUB-CNTOP",
//            "CNTOP(1-1)", "(1-1)CNTOP", "/3+2", "(/2+3)","(234-)", "    ",
//            "28*(23.324234-cntop- Roundopersum (-234.345345345+(34.23/ ( -1)) /2)",
//            "-  roundcost(-2.234234234)/cntop+2.234-(-reserv.*(cntop())", "-roundopersum()+2.234",
//            "((22-2*3.343)+2334.234)-cntop", "(())*2.234", "111.111(((())))"})
    void validateTest(String formula) {
        FormulaParserNewTest formulaParser = new FormulaParserNewTest();
        String vf = formulaParser.validate(formula);
        System.out.println(vf);
    }

    @Test
    void calculateTest() {
        BigDecimal a1 = new BigDecimal("533333280803.30855853353085");
        BigDecimal a2 = new BigDecimal("-13.23");

        Assertions.assertAll(                                                                                                   //чек
                () -> assertEquals(0, BigDecimal.valueOf(2.11).compareTo(FormulaParserNewTest.calculate("2.22+CNTOP"))),
                () -> assertEquals(0, BigDecimal.valueOf(-4).compareTo(FormulaParserNewTest.calculate("-6+2"))),
                () -> assertEquals(0, BigDecimal.valueOf(12.6666).compareTo(FormulaParserNewTest.calculate("6*2.1111"))),

                () -> assertEquals(0, BigDecimal.valueOf(-0.11).compareTo(FormulaParserNewTest.calculate("CNTOP"))),
                () -> assertEquals(0, BigDecimal.valueOf(2.555).compareTo(FormulaParserNewTest.calculate("SUMOP"))),
                () -> assertEquals(0, BigDecimal.valueOf(0.888888).compareTo(FormulaParserNewTest.calculate("RESERV"))),
                () -> assertEquals(0, BigDecimal.valueOf(2322312.22).compareTo(FormulaParserNewTest.calculate("ROUNDOPERSUM(2322312.219999999"))),
                () -> assertEquals(0, BigDecimal.valueOf(4.040404).compareTo(FormulaParserNewTest.calculate("ROUNDCOST(16.161616)"))),

                () -> assertEquals(0, a1.compareTo(FormulaParserNewTest.calculate("ROUNDCOST(2133333123213.2342341341234)"))),
                () -> assertEquals(0, a2.compareTo(FormulaParserNewTest.calculate("ROUNDOPERSUM(-13.234234564)"))),

                () -> assertEquals(0, BigDecimal.valueOf(-3).compareTo(FormulaParserNewTest.calculate("-ROUNDCOST(8)-1"))),
                () -> assertEquals(0, BigDecimal.valueOf(0).compareTo(FormulaParserNewTest.calculate("ROUNDCOST(0)"))),
                () -> assertEquals(0, BigDecimal.valueOf(0).compareTo(FormulaParserNewTest.calculate("ROUNDOPERSUM(0)"))),
                () -> assertEquals(0, BigDecimal.valueOf(2.33).compareTo(FormulaParserNewTest.calculate("1.22+ROUNDOPERSUM(ROUNDCOST(8.4444444)-1)"))),
                () -> assertEquals(0, BigDecimal.valueOf(3.67).compareTo(FormulaParserNewTest.calculate("2+CNTOP+CNTOP+ROUNDOPERSUM(RESERV+1)")))
        );


    }


    @ParameterizedTest
    @CsvSource(value = {"23.23, 23.2344", "111111111111111.12, 111111111111111.123", "1111111111111111.11, 1111111111111111.11111233123","324.24, 324.2363333334234"})
    void roundopersumCheck(String expected, String actual) {

        BigDecimal a = new BigDecimal(expected, MathContext.DECIMAL32);
        BigDecimal b = FormulaParserNewTest.FunctionType.ROUNDOPERSUM.apply(new BigDecimal(actual, MathContext.DECIMAL32));
        assertEquals(0, a.compareTo(b));
    }
}
