package tensor;

import java.math.BigDecimal;

public interface Scalar extends Comparable<Scalar> {
    // 12. 값 조회
    BigDecimal getValue();

    // 12. 값 지정
    void setValue(BigDecimal value); // 변경: 인터페이스에 추가 (구현 클래스에서 final이므로, 이 메소드는 새로운 Scalar 객체를 반환하도록 하거나, ScalarImpl의 value가 final이 아니어야 함. 명세상 "연산 결과는 자신의 새로운 값이 된다"는 non-static 연산에 대한 것이고, setValue는 직접 값을 바꾸는 것이므로 ScalarImpl의 final을 제거하는 방향으로 수정)

    // 17s. 객체 복제
    Scalar clone();

    // 18. 스칼라 덧셈 (non-static, modifies self)
    Scalar add(Scalar other);

    // 19. 스칼라 곱셈 (non-static, modifies self)
    Scalar multiply(Scalar other);

    public boolean isZero();

    public boolean isOne();

    // --- Default static methods ---
    // 24. 두 스칼라 덧셈 (static, returns new)
    //static Scalar add(Scalar s1, Scalar s2) {
        //return Factory.buildScalar(s1.getValue().add(s2.getValue()).toString());
    //}

    // 25. 두 스칼라 곱셈 (static, returns new)
    //static Scalar multiply(Scalar s1, Scalar s2) {
        //return Factory.buildScalar(s1.getValue().multiply(s2.getValue()).toString());
    //}
}
