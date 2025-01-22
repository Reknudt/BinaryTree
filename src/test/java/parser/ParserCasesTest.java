package parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserCasesTest {

    @ParameterizedTest
    @ValueSource(strings = {"CNTOP() + 2.34234", "32.121212  +  CNTOP  ", "cntop + 23.234235", "ROUNDCOST(3 - 23)"})
    void validateTest(String formula) {
        String vf = FormulaParserTest.validate(formula);
        System.out.println(vf);
    }

    @Test
    void calculateTest() {

        Assertions.assertAll(
                () -> assertEquals(BigDecimal.valueOf(2.11), FormulaParserTest.calculate("2.22 + CNTOP")),
                () -> assertEquals(BigDecimal.valueOf(-4), FormulaParserTest.calculate("- 6 + 2")),
                () -> assertEquals(BigDecimal.valueOf(12.6666), FormulaParserTest.calculate("6 * 2.1111")),
                () -> assertEquals(BigDecimal.valueOf(1), FormulaParserTest.calculate("ROUNDCOST( 8 ) - 1")),
                () -> assertEquals(BigDecimal.valueOf(-0.11), FormulaParserTest.calculate("- 1.22 + ROUNDOPERSUM( ROUNDCOST( 8.4444444 ) - 1 )")),
                () -> assertEquals(BigDecimal.valueOf(3.67), FormulaParserTest.calculate("2 + CNTOP + CNTOP + ROUNDOPERSUM( RESERV + 1 )"))
        );


//        assertEquals(BigDecimal.valueOf(2.11), FormulaParserTest.calculate("2.22 + CNTOP"));
//
////        assertEquals(BigDecimal.valueOf(9), FormulaParserTest.calculate("2.8888 + 6.1112"));
////        System.out.println("expected 9, actual: " + FormulaParserTest.calculate("2.8888 + 6.1112"));
//
//        assertEquals(BigDecimal.valueOf(-4), FormulaParserTest.calculate("- 6 + 2"));
//        assertEquals(BigDecimal.valueOf(12.6666), FormulaParserTest.calculate("6 * 2.1111"));
//        assertEquals(BigDecimal.valueOf(3), FormulaParserTest.calculate("6 / 2"));
////        assertEquals(BigDecimal.valueOf(2), FormulaParserTest.calculate("SUMOP - 0.555"));
////        assertEquals(BigDecimal.valueOf(1), FormulaParserTest.calculate("RESERV + 0.111112"));
//        assertEquals(BigDecimal.valueOf(1), FormulaParserTest.calculate("ROUNDCOST( 8 ) - 1"));
////        assertEquals(BigDecimal.valueOf(1), FormulaParserTest.calculate("ROUNDOPERSUM( 2.222222 ) - 1.22"));
//        assertEquals(BigDecimal.valueOf(-0.11), FormulaParserTest.calculate("- 1.22 + ROUNDOPERSUM( ROUNDCOST( 8.4444444 ) - 1 )"));
//        assertEquals(BigDecimal.valueOf(3.67), FormulaParserTest.calculate("2 + CNTOP + CNTOP + ROUNDOPERSUM( RESERV + 1 )"));
    }
}
