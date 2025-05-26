package tensor;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections; // For unmodifiable list if needed
import java.util.stream.Collectors;

public class VectorImpl implements Vector {
    private List<Scalar> vector; // final 제거하여 내부 요소 변경 및 연산 결과 반영 가능하게 함

    // 03
    VectorImpl(int nn, double dd) {
        if (nn <= 0) throw new IllegalArgumentException("Vector dimension nn must be positive.");
        this.vector = new ArrayList<>(Collections.nCopies(nn, Factory.buildScalar(Double.toString(dd))));
    }

    // 04
    VectorImpl(int nn, double ii, double jj) {
        if (nn <= 0) throw new IllegalArgumentException("Vector dimension nn must be positive.");
        this.vector = new ArrayList<>(nn);
        for (int i = 0; i < nn; i++) {
            this.vector.add(Factory.buildScalar(ii, jj)); // ScalarImpl에서 랜덤 생성 로직 사용
        }
    }

    // 05
    VectorImpl(double[] arr) {
        if (arr == null || arr.length == 0) throw new IllegalArgumentException("Input array cannot be null or empty.");
        this.vector = new ArrayList<>(arr.length);
        for (double val : arr) {
            this.vector.add(Factory.buildScalar(Double.toString(val)));
        }
    }

    // 내부 사용 또는 Factory 확장을 위한 생성자
    VectorImpl(List<Scalar> scalarList) {
        if (scalarList == null || scalarList.isEmpty()) {
            throw new IllegalArgumentException("Scalar list cannot be null or empty for VectorImpl construction.");
        }
        // Deep copy of scalars to ensure encapsulation and immutability of passed list
        this.vector = new ArrayList<>(scalarList.size());
        for (Scalar s : scalarList) {
            this.vector.add(s.clone()); // 각 스칼라도 복제
        }
    }


    @Override
    public List<Scalar> getScalars() { // getVector에서 변경
        // Defensive copy to prevent external modification of internal list
        // return new ArrayList<>(this.vector);
        // 또는 불변 뷰 반환
        // return Collections.unmodifiableList(this.vector);
        // 명세에서 Factory 외에는 Impl 클래스가 등장하지 않아야 하고,
        // 내부 List를 반환해야 MatrixImpl 등에서 직접 접근 가능.
        // 하지만 이렇게 하면 캡슐화가 깨짐. 여기서는 명세의 "물리적으로는 Collection"을 따르고
        // 연산 시에는 clone 등을 활용하는 것으로 가정.
        // 또는, 인터페이스에서 List<Scalar>를 반환하는 대신, 더 제한적인 접근자(예: iterator)를 제공하는 것도 방법.
        // 현재는 직접 리스트를 반환하도록 유지 (MatrixImpl과의 호환성 및 초기 명세 해석)
        return this.vector;
    }

    // 11v. 특정 위치 요소 지정
    @Override
    public void setElement(int index, Scalar value) {
        if (value == null) throw new IllegalArgumentException("Scalar value cannot be null.");
        if (index < 0 || index >= this.vector.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for vector size " + this.vector.size());
        }
        this.vector.set(index, value.clone()); // 복제해서 저장
    }

    // 11v. 특정 위치 요소 조회
    @Override
    public Scalar viewElement(int index) {
        if (index < 0 || index >= this.vector.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for vector size " + this.vector.size());
        }
        return this.vector.get(index).clone(); // 복제해서 반환 (외부에서 변경 방지)
    }

    // 13. 차원(길이) 조회
    @Override
    public int getSize() { // viewSize에서 변경
        return this.vector.size();
    }

    // 14v. 객체를 콘솔에 출력
    @Override
    public String toString() {
        return this.vector.stream()
                .map(Scalar::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    // 15v. 객체의 동등성 판단
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Vector)) return false;
        Vector other = (Vector) obj;
        if (this.getSize() != other.getSize()) return false;
        for (int i = 0; i < this.getSize(); i++) {
            if (!this.viewElement(i).equals(other.viewElement(i))) { // viewElement 사용 (내부 scalar 직접 비교 회피)
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (Scalar element : this.vector) {
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }
        result = 31 * result + Integer.hashCode(getSize());
        return result;
    }


    // 17v. 객체 복제 (deep copy)
    @Override
    public Vector clone() {
        try {
            VectorImpl cloned = (VectorImpl) super.clone();
            cloned.vector = new ArrayList<>(this.vector.size());
            for (Scalar s : this.vector) {
                cloned.vector.add(s.clone()); // 각 Scalar 객체도 복제
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e); // Should not happen
        }
    }

    // 20. 벡터 덧셈 (non-static, modifies self)
    @Override
    public Vector add(Vector other) {
        if (other == null) throw new IllegalArgumentException("Other vector cannot be null.");
        if (this.getSize() != other.getSize()) {
            throw new DimensionMismatchException("Vectors must have the same size for addition. Self size: " + this.getSize() + ", Other size: " + other.getSize());
        }
        for (int i = 0; i < this.getSize(); i++) {
            // this.vector.get(i).add(other.viewElement(i)); // Scalar.add는 ScalarImpl의 값을 바꿈
            Scalar current = this.vector.get(i);
            current.add(other.viewElement(i)); // Scalar.add가 this를 반환하므로, setElement는 필요 없음.
            // 단, Scalar.add가 새 객체를 반환하면 this.vector.set(i, resultOfAdd) 필요
        }
        return this;
    }

    // 21. 스칼라 곱셈 (non-static, modifies self)
    @Override
    public Vector multiply(Scalar scalar) {
        if (scalar == null) throw new IllegalArgumentException("Scalar cannot be null.");
        for (int i = 0; i < this.getSize(); i++) {
            // this.vector.get(i).multiply(scalar);
            Scalar current = this.vector.get(i);
            current.multiply(scalar);
        }
        return this;
    }

    // 30. n-차원 벡터 객체는 자신으로부터 1xn 행렬을 생성하여 반환 (Row Vector)
    @Override
    public Matrix toRowMatrix() {
        // MatrixImpl에 List<Vector>를 받는 생성자 또는 Scalar[][]를 받는 생성자가 필요.
        // 여기서는 Scalar[][]를 만들어 Matrix 생성자에 전달하는 방식을 택하거나,
        // MatrixImpl에 Vector를 받아 1xN 행렬로 만드는 생성자를 추가할 수 있음.
        // Factory를 통해 생성해야 하므로, Factory.buildMatrix(Scalar[][]) 가 필요.
        Scalar[][] data = new Scalar[1][this.getSize()];
        for (int i = 0; i < this.getSize(); i++) {
            data[0][i] = this.vector.get(i).clone(); // 값 복사
        }
        return Factory.buildMatrix(data); // Factory에 이 생성자 위임 메소드 추가 필요
    }

    // 31. n-차원 벡터 객체는 자신으로부터 nx1 행렬을 생성하여 반환 (Column Vector)
    @Override
    public Matrix toColMatrix() {
        Scalar[][] data = new Scalar[this.getSize()][1];
        for (int i = 0; i < this.getSize(); i++) {
            data[i][0] = this.vector.get(i).clone(); // 값 복사
        }
        return Factory.buildMatrix(data); // Factory에 이 생성자 위임 메소드 추가 필요
    }
}
