package tensor;

import java.math.BigDecimal;
import java.math.RoundingMode; // For random number precision

class ScalarImpl implements Scalar {
    private BigDecimal scalar; // 명세 12의 setValue와 non-static 연산을 위해 final 제거

    // 01
    ScalarImpl(String ss) {
        if (ss == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
        try {
            this.scalar = new BigDecimal(ss);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid string format for BigDecimal: " + ss, e);
        }
    }

    ScalarImpl(BigDecimal bd) { // BigDecimal을 직접 받는 생성자 추가 (내부 사용 편의)
        if(bd == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
        this.scalar = bd;
    }

    // 02
    ScalarImpl(double ii, double jj) {
        if (ii >= jj) {
            throw new IllegalArgumentException("Lower bound ii must be less than upper bound jj.");
        }
        double randomVal = Math.random() * (jj - ii) + ii;
        // BigDecimal로 변환 시 정밀도 문제 발생 가능성 있음. 적절한 스케일 지정 필요.
        this.scalar = BigDecimal.valueOf(randomVal).setScale(10, RoundingMode.HALF_UP); // 예시: 소수점 10자리
    }

    @Override
    public BigDecimal getValue() { // 12. 값 조회 (기존 getScalar()에서 명칭 변경)
        return this.scalar;
    }

    @Override
    public void setValue(BigDecimal value) { // 12. 값 지정
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
        this.scalar = value;
    }

    // 14s
    @Override
    public String toString() {
        return scalar.toPlainString(); // 공학적 표기법 대신 일반적인 숫자 문자열로 표시
    }

    // 15s
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Scalar)) return false; // getClass() 비교 대신 instanceof 사용 (인터페이스 호환)
        Scalar other = (Scalar) obj;
        // BigDecimal.equals는 스케일도 비교하므로, 값만 비교하려면 compareTo 사용
        return this.scalar.compareTo(other.getValue()) == 0;
    }

    @Override
    public int hashCode() {
        // equals를 값 기준으로 변경했으므로 hashCode도 값 기준으로 변경
        return this.scalar.stripTrailingZeros().hashCode();
    }

    // 16. 스칼라 값 대소 비교
    @Override
    public int compareTo(Scalar other) {
        return this.scalar.compareTo(other.getValue());
    }

    // 17s. 객체 복제 (deep copy)
    @Override
    public Scalar clone() {
        return new ScalarImpl(this.scalar); // 새 객체를 생성하여 BigDecimal 값을 복사하는 것이 더 명확
    }

    // 18. 스칼라 덧셈 (non-static, modifies self)
    @Override
    public Scalar add(Scalar other) {
        if (other == null) throw new IllegalArgumentException("Other scalar cannot be null for addition.");
        this.scalar = this.scalar.add(other.getValue());
        return this;
    }

    // 19. 스칼라 곱셈 (non-static, modifies self)
    @Override
    public Scalar multiply(Scalar other) {
        if (other == null) throw new IllegalArgumentException("Other scalar cannot be null for multiplication.");
        this.scalar = this.scalar.multiply(other.getValue());
        return this;
    }

    // Helper for triangular checks:
    @Override
    public boolean isZero() {
        return this.getValue().compareTo(BigDecimal.ZERO) == 0;
    }

    // Helper for triangular checks:
    @Override
    public boolean isOne() {
        return this.getValue().compareTo(BigDecimal.ONE) == 0;
    }
}
