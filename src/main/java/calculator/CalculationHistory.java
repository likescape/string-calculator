package calculator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 힌트: 연산 이력을 관리하는 클래스.
 * 일급 컬렉션으로 구현해볼 것.
 * 반드시 이 클래스를 사용할 필요는 없다. 자유롭게 설계할 것.
 */
public class CalculationHistory {
    private final Queue<String> history;
    //최대 10개까지 저장, 최근순으로 저장

    public CalculationHistory() {history = new LinkedList<>();}

    public void inputLog(String input) {
        history.add(input);
        while (history.size() > 10) {history.remove();}
    }

    public List<String> getHistory() {
        return history.stream().toList().reversed();
    }


}
