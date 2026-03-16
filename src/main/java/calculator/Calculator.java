package calculator;


import java.nio.DoubleBuffer;
import java.util.List;

public class Calculator {
    public Calculator() {}

    private double calculate(OperationType operator, double result, String s) {
        double b = Double.parseDouble(s);

        return operator.operate(result, b);
    }

    public double calculate(List<String> calculateList) {

        if(calculateList.size() == 1) {
            String s = calculateList.getFirst();
            return Double.parseDouble(s);
        }

        OperationType operator = OperationType.fromSymbol(calculateList.get(0));

        double result = Double.parseDouble(calculateList.get(1));

        for(int i = 2; i < calculateList.size(); i++) {
            String s = calculateList.get(i);
            result = calculate(operator, result, s);
        }

        return (Math.floor(result * 10) / 10.0);
    }

}
