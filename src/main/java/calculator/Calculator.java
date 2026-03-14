package calculator;


import java.nio.DoubleBuffer;
import java.util.List;

public class Calculator {
    public Calculator() {}

    private double calculate(OperationType operator, double result, String s) {
        double b = Double.parseDouble(s);
        if(operator == OperationType.PLUS){
            return result + b;
        }
        if(operator == OperationType.MINUS){
            return result - b;
        }
        if(operator == OperationType.TIMES){
            return result * b;
        }
        if(operator == OperationType.DIVIDES){

            if(b == 0){
                throw new IllegalArgumentException("0으로 나눌 수 없습니다");
            }
            return result / b;
        }

        throw new IllegalArgumentException("Calculator does not support " + operator);
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
