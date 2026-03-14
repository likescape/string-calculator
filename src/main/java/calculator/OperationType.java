package calculator;

/**
 * 힌트: 연산자 종류를 나타내는 enum.
 * +, -, *, / 를 표현하고, 각 연산자에 맞는 계산을 수행할 수 있다.
 * 반드시 이 클래스를 사용할 필요는 없다. 자유롭게 설계할 것.
 */
public enum OperationType {
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDES("/");

    private final String symbol;

    OperationType(String symbol) {
        this.symbol = symbol;
    }

    // 기호를 넣으면 Enum 타입을 찾아주는 메서드 (핵심!)
    public static OperationType fromSymbol(String symbol) {
        for (OperationType type : values()) {
            if (type.symbol.equals(symbol)) {
                return type;
            }
        }
        // 만약 매칭되는 게 없으면 기본값(더하기)을 주거나 예외를 던집니다.
        return PLUS;
    }

    public String getSymbol() {
        return symbol;
    }
}

