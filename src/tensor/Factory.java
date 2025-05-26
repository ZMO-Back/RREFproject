package tensor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; // 추가

public class Factory {
    // Scalar
    public static Scalar buildScalar(String ss) {
        return new ScalarImpl(ss);
    }

    // BigDecimal을 직접 받는 buildScalar 추가 (내부용 또는 고급 사용)
    public static Scalar buildScalar(BigDecimal bd) {
        return new ScalarImpl(bd);
    }

    public static Scalar buildScalar(double ii, double jj) { // 명세는 int, int 였으나 ScalarImpl은 double, double
        return new ScalarImpl(ii, jj);
    }

    // Vector
    public static Vector buildVector(int nn, double dd) {
        return new VectorImpl(nn, dd);
    }

    public static Vector buildVector(int nn, double ii, double jj) {
        return new VectorImpl(nn, ii, jj);
    }

    public static Vector buildVector(double[] arr) {
        return new VectorImpl(arr);
    }

    // Scalar 배열로부터 Vector 생성
    public static Vector buildVector(Scalar[] arr) {
        if (arr == null) throw new IllegalArgumentException("Scalar array for vector cannot be null.");
        List<Scalar> scalarList = new ArrayList<>();
        Collections.addAll(scalarList, arr);
        if (scalarList.isEmpty() && arr.length > 0) { // arr이 {null, null} 같은 경우 scalarList는 비지만 길이는 있음.
            // VectorImpl(List<Scalar>) 에서 null 요소 체크 필요.
            // 여기서는 ScalarImpl 생성자가 null을 받지 않으므로, arr에 null이 오면 안됨.
            // ScalarImpl 생성시 예외 발생하므로, arr에 null이 없다고 가정하거나, 여기서 null 체크.
            // 여기서는 VectorImpl(List<Scalar>)가 알아서 처리하도록 위임.
        }
        return new VectorImpl(scalarList); // VectorImpl에 List<Scalar> 받는 생성자 사용
    }


    // List<Scalar>로부터 Vector 생성
    public static Vector buildVector(List<Scalar> scalarList) {
        return new VectorImpl(scalarList); // VectorImpl에 List<Scalar> 받는 생성자 사용
    }


    // Matrix
    public static Matrix buildMatrix(int mm, int nn, double dd) {
        return new MatrixImpl(mm, nn, dd);
    }

    // 랜덤값 행렬
    public static Matrix buildMatrix(int mm, int nn, double ii, double jj) {
        return new MatrixImpl(mm, nn, ii, jj);
    }

    // CSV 파일로부터 행렬 생성
    public static Matrix buildMatrix(String csvFilePath) throws CsvParseException {
        return new MatrixImpl(csvFilePath);
    }

    // 2차원 double 배열로부터 행렬 생성
    public static Matrix buildMatrix(double[][] arr) {
        return new MatrixImpl(arr);
    }

    // Scalar 2차원 배열로부터 행렬 생성
    public static Matrix buildMatrix(Scalar[][] data) {
        return new MatrixImpl(data);
    }

    // 단위 행렬 생성
    public static Matrix buildMatrix(int dimension) {
        return new MatrixImpl(dimension);
    }

    //
    public static Matrix buildMatrix(List<Vector> rows) {
        return new MatrixImpl(rows);
    }

//    // List<Vector> 로부터 Matrix 생성 (내부용, MatrixImpl clone 등에서 활용)
//    // Test.java에서 직접 사용하지는 않음 (구현 클래스 노출 방지)
//    static Matrix buildMatrixInternal(List<Vector> rows, boolean deepCopy) {
//        return new MatrixImpl(rows);
//    }
}
