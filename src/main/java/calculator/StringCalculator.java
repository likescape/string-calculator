package calculator;

import java.util.List;

/**
 * 진입점. 흐름 조율만 담당한다.
 * 파싱, 계산, 이력 관리를 직접 구현하지 말 것.
 * 각 책임은 별도 클래스에 위임할 것.
 */
public class StringCalculator {
    InputParser inputParser = new InputParser();
    Calculator calculator = new Calculator();
    CalculationHistory calculationHistory = new CalculationHistory();

    public StringCalculator() {}

    public double calculate(String input) {
        List<String> parseInput = inputParser.parse(input);
        double calculated_value = calculator.calculate(parseInput);
        calculationHistory.inputLog(input);

        return calculated_value;
    }

    public List<String> getHistory() {
        return calculationHistory.getHistory();
    }
}
