package calculator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static calculator.Constants.*;


/**
 * 힌트: 입력 문자열 파싱을 담당하는 클래스.
 * 구분자 추출, 숫자 토큰 분리, 연산자 추출 등을 여기서 처리할 수 있다.
 * 반드시 이 클래스를 사용할 필요는 없다. 자유롭게 설계할 것.
 */
public class InputParser {
    public InputParser() {}

    private boolean isOperator(String input) {
        if (!input.startsWith("op=")) return true; // 접두사 없으면 검사 패스

        // ^op= : op=로 시작해서
        // .    : 아무 기호나 한 글자 오고 (이건 isSupportedOperator에서 자세히 볼 거임)
        // [|]  : 반드시 파이프(|)가 와야 함
        // .* : 그 뒤엔 뭐가 와도 상관없음
        return input.matches("^op=.[|].*");
    }


    // 1. 숫자가 아닌 값이 섞여 있는지 확인 (예: "abc", "12a")
    private boolean isNumeric(String input, List<String> separators) {
        String numberPart = input.startsWith("op=") ? input.substring(5) : input;
        String delimRegex = separators.stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));

        // 모든 토큰이 "부호(선택) + 숫자" 형태인지 확인
        return Pattern.compile(delimRegex)
                .splitAsStream(numberPart)
                .filter(s -> !s.isBlank())
                .allMatch(s -> s.matches("-?\\d+")); // -가 있든 없든 일단 숫자인지 확인
    }

    // 2. 음수가 포함되어 있는지 확인 (예: "-1", "-123")
    private boolean isPositive(String input, List<String> separators) {
        String numberPart = input.startsWith("op=") ? input.substring(5) : input;
        String delimRegex = separators.stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));

        // 이미 isNumeric을 통과했다고 가정하고, 하이픈(-)이 포함된 토큰이 있는지 확인
        return Pattern.compile(delimRegex)
                .splitAsStream(numberPart)
                .filter(s -> !s.isBlank())
                .noneMatch(s -> s.startsWith("-")); // 하나라도 -로 시작하면 false
    }

    private boolean isSupportedOperator(String input) {
        // 1. op= 로 시작하지 않으면 검사할 연산자가 없으므로 무조건 통과(true)
        if (!input.startsWith("op=")) {
            return true;
        }

        // 3. 기호 추출 (index 3번)
        String symbol = String.valueOf(input.charAt(3));

        // 4. Enum의 모든 값을 뒤져서 매칭되는 기호가 있는지 확인
        // stream을 쓰면 한 줄로 "이 중에 하나라도 있니?"라고 물어볼 수 있습니다.
        return java.util.Arrays.stream(OperationType.values())
                .anyMatch(type -> type.getSymbol().equals(symbol));
    }

    private boolean isSupportedSeparators(String input, List<String> separators) {
        String numberPart = input.startsWith("op=") ? input.substring(5) : input;

        // 숫자를 모두 제거하고 남은 문자들(구분자들)이 separators에 포함되는지 확인
        String delimitersOnly = numberPart.replaceAll("\\d", "");
        return delimitersOnly.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .allMatch(separators::contains);
    }

    private boolean isRightOperator(String input, List<String> separators) {

        if (!isNumeric(input, separators)) {
            throw new IllegalArgumentException("숫자가 아닌 값이 포함되어 있습니다.");
        }
        if (!isPositive(input, separators)) {
            throw new IllegalArgumentException("음수는 입력할 수 없습니다.");
        }
        if (!isOperator(input)) {
            throw new IllegalArgumentException("연산자 지정 방식이 잘못되었습니다.");
        }
        if (!isSupportedOperator(input)) {
            String symbol = String.valueOf(input.charAt(3));
            throw new IllegalArgumentException("지원하지 않는 연산자입니다: " + symbol);
        }
        if (!isSupportedSeparators(input, separators)) {
            throw new IllegalArgumentException("지원하지 않는 구분자가 포함되어 있습니다.");
        }


        return true; // 모든 검문을 무사히 통과하면 true 리턴
    }
    private List<String> separateInput(String input, List<String> separators) {
        List<String> tokens = new ArrayList<>();

        // 1. 캡처 그룹 정의
        Pattern pattern = Pattern.compile("^op=([+\\-*/])\\|(.*)$");
        Matcher matcher = pattern.matcher(input);

        String op = "+";
        String numberPart = input;

        // 2. 매칭 시 그룹별로 추출
        if (matcher.find()) {
            op = matcher.group(1);          // 첫 번째 괄호: 연산자 기호
            numberPart = matcher.group(2);  // 두 번째 괄호: 숫자들
        }

        tokens.add(op); // 맨 앞에 연산자 추가

        // 3. 구분자 정규식 생성 및 쪼개기
        String delimRegex = separators.stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));

        Pattern.compile(delimRegex)
                .splitAsStream(numberPart)
                .filter(s -> !s.isBlank())
                .forEach(tokens::add);

        return List.copyOf(tokens);
    }


    public List<String> parse(String input){
        if(input == null || input.isEmpty()) return List.of(Constants.EMPTY_INPUT_VALUE);

        List<String> inputs = input.lines()
                .toList(); // Java 16+ 기준
        List<String> operators = new ArrayList<>();

        for(int i = 0; i < inputs.size()-1; i++){
            operators.add(inputs.get(i).substring(2).trim());

        }
        input = inputs.getLast();
        operators.addAll(NORMAL_SEPERATORS);

        if(input.length() == 1 && '0' <= input.charAt(0) && input.charAt(0) <= '9') {return List.of(input);}
        if(isRightOperator(input,operators)){return separateInput(input, operators);}

        return null;
    }

}
