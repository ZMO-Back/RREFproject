package tensor;

import java.util.List;

public interface Matrix extends Cloneable {
    // 내부 List<Vector> 직접 반환 대신, 더 제어된 접근자 고려 가능
    // List<Vector> getRows(); // 명칭 변경 getMatrix -> getRows
    // 또는 각 행을 Vector로 반환하는 메소드, 또는 2차원 배열로 Scalar 반환
    // 현재 MatrixImpl은 List<Vector>를 내부적으로 사용하므로, 이를 활용

    List<Vector> getVectors();

    // 11m. 특정 위치 요소 지정
    void setElement(int rowIndex, int colIndex, Scalar value);

    // 11m. 특정 위치 요소 조회
    Scalar viewElement(int rowIndex, int colIndex);

    // 13. 행렬 크기 조회 (행 개수, 열 개수)
    int[] getSize(); // 명칭 변경 viewSize -> getSize

    // 17m. 객체 복제
    Matrix clone();

    // --- Non-static operations (modifies self) ---
    // 22. 행렬 덧셈
    Matrix add(Matrix other);

    // 23. 행렬 곱셈 (this = this * other)
    Matrix multiplyLeft(Matrix other);
    // 23. 행렬 곱셈 (this = other * this). 명세에 "모두 지원"
    // non-static으로 자신을 바꾸려면 multiplyLeft(Matrix otherLeft) 같은 이름이 더 적절할 수 있음.
    // 또는 multiply가 boolean 인자(multiplyOnLeft)를 받도록 설계할 수도 있음.
    // 여기서는 multiply(Matrix other)를 this * other로 정의하고,
    // 필요시 Tensors 클래스에서 static multiply(A, B) 와 multiply(B, A)를 다르게 호출하도록 유도.
    // 혹은, 인터페이스에 multiplyLeft(Matrix otherLeft) 추가.
    // 여기서는 multiply(Matrix other)는 this * other로, Tensors에서 순서를 바꿔 호출하는 것으로 가정.

    Matrix multiplyRight(Matrix other);

    // --- Advanced Matrix Functionality (mostly non-static, returning new or modifying self as specified) ---

    // 32. 가로 합치기 (non-static, modifies self or returns new - 명세는 "합쳐질 수 있다", 반환형 명시 없음. 여기서는 새 행렬 반환)
    Matrix hstack(Matrix other);

    // 33. 세로 합치기 (non-static, 새 행렬 반환)
    Matrix vstack(Matrix other);

    // 34. 특정 행을 벡터 형태로 추출
    Vector getRow(int rowIndex);

    // 35. 특정 열을 벡터 형태로 추출
    Vector getCol(int colIndex);

    // 36. 특정 범위의 부분 행렬 추출 (새 행렬 반환)
    Matrix subMatrix(int startRow, int endRow, int startCol, int endCol);

    // 37. 특정 행/열 제외 부분 행렬 (minor) (새 행렬 반환)
    Matrix minor(int excludeRowIndex, int excludeColIndex);

    // 38. 전치행렬 (새 행렬 반환)
    Matrix transpose();

    // 39. 대각 요소의 합 (nxn 행렬)
    Scalar trace();

    // 40. 정사각 행렬 여부 판별
    boolean isSquare();

    // 41. 상삼각 행렬 여부 판별 (nxn 행렬)
    boolean isUpperTriangular();

    // 42. 하삼각 행렬 여부 판별 (nxn 행렬)
    boolean isLowerTriangular();

    // 43. 단위 행렬 여부 판별 (nxn 행렬)
    boolean isIdentity();

    // 44. 영 행렬 여부 판별
    boolean isZeroMatrix();

    // --- Elementary Row/Column Operations (modifies self) ---
    // 45. 특정 두 행의 위치 교환
    void swapRows(int rowIndex1, int rowIndex2);

    // 46. 특정 두 열의 위치 교환
    void swapCols(int colIndex1, int colIndex2);

    // 47. 특정 행에 상수배
    void multiplyRow(int rowIndex, Scalar scalar);

    // 48. 특정 열에 상수배
    void multiplyCol(int colIndex, Scalar scalar);

    // 49. 특정 행에 다른 행의 상수배를 더함 (targetRow += scalar * sourceRow)
    void addScaledRow(int targetRowIndex, int sourceRowIndex, Scalar scalar);

    // 50. 특정 열에 다른 열의 상수배를 더함 (targetCol += scalar * sourceCol)
    void addScaledCol(int targetColIndex, int sourceColIndex, Scalar scalar);

    // 51. RREF 행렬 구해서 반환 (새 행렬 반환)
    Matrix rref();

    // 52. 자신이 RREF 행렬인지 여부 판별
    boolean isRREF();

    // 53. 행렬식 (nxn 행렬)
    Scalar determinant();

    // 54. 역행렬 (nxn 행렬, 새 행렬 반환)
    Matrix inverse();


    // --- Default static methods (returns new) ---
    // 28. 두 행렬 덧셈
    //static Matrix add(Matrix m1, Matrix m2) {
        //int[] size1 = m1.getSize();
        //int[] size2 = m2.getSize();
        //if (size1[0] != size2[0] || size1[1] != size2[1]) {
            //throw new DimensionMismatchException("Matrices must have the same dimensions for addition.");
        //}
        //Matrix result = m1.clone(); // Create a new matrix based on m1
        //for (int i = 0; i < size1[0]; i++) {
            //for (int j = 0; j < size1[1]; j++) {
                // result.setElement(i, j, Scalar.add(result.viewElement(i, j), m2.viewElement(i, j))); // clone 기반이므로
                //result.setElement(i, j, Scalar.add(m1.viewElement(i, j), m2.viewElement(i, j)));
            //}
        //}
        //return result;
    //}

    // 29. 두 행렬 곱셈
    //static Matrix multiply(Matrix m1, Matrix m2) {
        //int[] size1 = m1.getSize(); // m1 is rows1 x cols1
        //int[] size2 = m2.getSize(); // m2 is rows2 x cols2
        //if (size1[1] != size2[0]) {
            //throw new DimensionMismatchException("Number of columns in the first matrix must equal the number of rows in the second matrix for multiplication. m1_cols: " + size1[1] + ", m2_rows: " + size2[0]);
        //}
        //int resultRows = size1[0];
        //int resultCols = size2[1];
        //Scalar[][] resultData = new Scalar[resultRows][resultCols];

        //for (int i = 0; i < resultRows; i++) {
            //for (int j = 0; j < resultCols; j++) {
                //Scalar sum = Factory.buildScalar("0"); // Accumulator for the dot product
                //int size1;
        //for (int k = 0; k < size1[1]; k++) { // size1[1] is equal to size2[0]
                    //Scalar val1 = m1.viewElement(i, k);
                    //Scalar val2 = m2.viewElement(k, j);
                    //.add(Scalar.multiply(val1, val2)); // Scalar.multiply는 새 Scalar 반환, sum.add는 sum 자신을 변경
                //}
                //resultData[i][j] = sum;
            //}
        //}
        //return Factory.buildMatrix(resultData); // Factory.buildMatrix(Scalar[][]) 필요
    //}

    // 32. static 가로 합치기
    //static Matrix hstack(Matrix m1, Matrix m2) {
        //int[] size1 = m1.getSize();
        //int[] size2 = m2.getSize();
        //if (size1[0] != size2[0]) {
            //throw new DimensionMismatchException("Matrices must have the same number of rows for horizontal stacking. m1_rows: " + size1[0] + ", m2_rows: " + size2[0]);
        //}
        //int resultRows = size1[0];
        //int resultCols = size1[1] + size2[1];
        //Scalar[][] resultData = new Scalar[resultRows][resultCols];

        //for (int i = 0; i < resultRows; i++) {
            //for (int j = 0; j < size1[1]; j++) {
                //resultData[i][j] = m1.viewElement(i, j).clone();
            //}
            //for (int j = 0; j < size2[1]; j++) {
                //resultData[i][size1[1] + j] = m2.viewElement(i, j).clone();
            //}
        //}
        //return Factory.buildMatrix(resultData);
    //}

    // 33. static 세로 합치기
    //static Matrix vstack(Matrix m1, Matrix m2) {
        //int[] size1 = m1.getSize();
        //int[] size2 = m2.getSize();
        //if (size1[1] != size2[1]) {
            //throw new DimensionMismatchException("Matrices must have the same number of columns for vertical stacking. m1_cols: " + size1[1] + ", m2_cols: " + size2[1]);
        //}
        //int resultRows = size1[0] + size2[0];
        //int resultCols = size1[1];
        //Scalar[][] resultData = new Scalar[resultRows][resultCols];

        //for (int i = 0; i < size1[0]; i++) {
            //for (int j = 0; j < resultCols; j++) {
                //resultData[i][j] = m1.viewElement(i, j).clone();
            //}
        //}
        //for (int i = 0; i < size2[0]; i++) {
            //for (int j = 0; j < resultCols; j++) {
                //resultData[size1[0] + i][j] = m2.viewElement(i, j).clone();
            //}
        //}
        //return Factory.buildMatrix(resultData);
    //}
}
