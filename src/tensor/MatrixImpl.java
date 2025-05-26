package tensor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.math.RoundingMode;

public class MatrixImpl implements Matrix {
    private List<Vector> matrixRows; // 필드명 변경 (matrix -> matrixRows)
// 생성자들은 대부분 유지, CSV, Scalar[][] 추가 필요

    // 06: 기존 생성자 (mm: 행 개수, nn: 열 개수, dd: 초기값)
    MatrixImpl(int mm, int nn, double dd) {
        if (mm <= 0 || nn <= 0) throw new IllegalArgumentException("Matrix dimensions must be positive.");
        this.matrixRows = new ArrayList<>(mm);
        for (int i = 0; i < mm; i++) {
            this.matrixRows.add(Factory.buildVector(nn, dd)); // Factory 사용
        }
    }

    // 07: 랜덤값 행렬
    MatrixImpl(int mm, int nn, double ii, double jj) {
        if (mm <= 0 || nn <= 0) throw new IllegalArgumentException("Matrix dimensions must be positive.");
        this.matrixRows = new ArrayList<>(mm);
        for (int i = 0; i < mm; i++) {
            this.matrixRows.add(Factory.buildVector(nn, ii, jj)); // Factory 사용
        }
    }

    // 08: CSV 파일로부터 생성
    MatrixImpl(String csvFilePath) throws CsvParseException {
        this.matrixRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            int numCols = -1;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                String[] values = line.split(",");
                if (numCols == -1) {
                    numCols = values.length;
                } else if (numCols != values.length) {
                    throw new CsvParseException("CSV file has inconsistent number of columns. Expected " + numCols + " but found " + values.length + " in line: " + line);
                }
                Scalar[] rowScalars = new Scalar[values.length];
                for (int i = 0; i < values.length; i++) {
                    try {
                        rowScalars[i] = Factory.buildScalar(values[i].trim());
                    } catch (IllegalArgumentException e) {
                        throw new CsvParseException("Error parsing value '" + values[i] + "' in CSV file at line: " + line, e);
                    }
                }
                // VectorImpl에 Scalar[]를 받는 생성자가 있다면 사용, 없다면 List<Scalar>로 변환 후 사용
                // Factory.buildVector(Scalar[]) 가 있다면 더 좋음
                List<Scalar> scalarList = new ArrayList<>();
                Collections.addAll(scalarList, rowScalars);
                if (scalarList.isEmpty() && numCols > 0) { // 행은 있는데 모든 열이 비어있는 경우 방지 (split 결과에 따라)
                    // 모든 값이 비어있었다면 scalarList는 비어있을 수 있다. numCols가 0이 아니라면 오류.
                    // 이 부분은 CSV 파일의 형식에 따라 더 엄격하게 처리할 수 있다.
                    // 예: " ," 같은 라인이면 values는 [" ", ""] 가 될 수 있음.
                    if (numCols > 0) throw new CsvParseException("Empty row data with expected columns: " + numCols);
                }
                // 빈 행렬을 허용하지 않는다면, 여기서 matrixRows.isEmpty() && numCols == 0 같은 경우도 체크
                if (!scalarList.isEmpty()) { // 값이 하나라도 있는 경우에만 벡터 추가
                    this.matrixRows.add(Factory.buildVector(scalarList)); // Factory.buildVector(List<Scalar>) 필요
                } else if (values.length > 0 && line.trim().matches(",*")) { // " ,,," 같은 경우
                    // 모든 요소가 비어있는 것으로 간주하고 0으로 채울지, 오류로 처리할지 정책 필요
                    // 여기서는 0으로 채우는 Vector를 생성
                    this.matrixRows.add(Factory.buildVector(numCols, 0.0));
                }

            }
            if (this.matrixRows.isEmpty() && numCols > 0) { // 파일은 있으나 데이터가 없는 경우 (예: 헤더만 있거나 빈 줄만)
                // 이 경우 0xN 또는 Mx0 행렬이 될 수 있는데, 보통 데이터가 없으면 빈 행렬 (0x0) 또는 오류.
                // 여기서는 행이 없으면 빈 행렬로 처리.
            } else if (this.matrixRows.isEmpty() && numCols <= 0){
                // 파일 내용이 아예 없거나, ",,," 같은 내용도 없이 완전히 빈 파일일 때
                // 이 경우 0x0 행렬로 간주. Factory.buildVector가 빈 리스트로 호출되면 예외 발생하므로 주의.
            }


        } catch (IOException e) {
            throw new CsvParseException("Error reading CSV file: " + csvFilePath, e);
        }
        if (this.matrixRows.isEmpty()) { // 만약 빈 파일이거나 내용이 없어 matrixRows가 비었다면,
            // 0x0 행렬로 간주하거나, 최소 1x1을 요구한다면 예외 발생.
            // 여기서는 빈 행렬(0x0)을 허용. getSize()에서 [0,0] 반환.
            // this.matrixRows remains an empty list. getSize() will handle this.
        }
    }


    // 09: 2차원 double 배열로부터 생성
    MatrixImpl(double[][] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Size 0 not allowed");
        }
        int numRows = arr.length;
        int numCols = (arr[0] == null) ? 0 : arr[0].length; // 첫 행 기준으로 열 개수 결정

        this.matrixRows = new ArrayList<>(numRows);
        for (int i = 0; i < numRows; i++) {
            if (arr[i] == null || arr[i].length != numCols) {
                throw new IllegalArgumentException("All rows in the 2D array must have the same number of columns and not be null.");
            }
            this.matrixRows.add(Factory.buildVector(arr[i])); // Factory.buildVector(double[]) 사용
        }
    }

    // Scalar[][] 로부터 생성 (내부 및 Factory용)
    MatrixImpl(Scalar[][] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Size 0 not allowed");
        }
        int numRows = data.length;
        int numCols = (data[0] == null) ? 0 : data[0].length;

        this.matrixRows = new ArrayList<>(numRows);
        for (int i = 0; i < numRows; i++) {
            if (data[i] == null || data[i].length != numCols) {
                throw new IllegalArgumentException("All rows in the Scalar[][] array must have the same number of columns and not be null.");
            }
            List<Scalar> rowScalars = new ArrayList<>(numCols);
            for (int j = 0; j < numCols; j++) {
                if (data[i][j] == null) throw new IllegalArgumentException("Scalar elements in data array cannot be null.");
                rowScalars.add(data[i][j].clone()); // 복제해서 저장
            }
            this.matrixRows.add(Factory.buildVector(rowScalars)); // Factory.buildVector(List<Scalar>) 필요
        }
    }


    // 10: 단위 행렬 생성
    MatrixImpl(int mm) { // mm: 크기 (정사각 행렬)
        if (mm <= 0) throw new IllegalArgumentException("Dimension for identity matrix must be positive.");
        this.matrixRows = new ArrayList<>(mm);
        for (int i = 0; i < mm; i++) {
            Scalar[] rowScalars = new Scalar[mm];
            for (int j = 0; j < mm; j++) {
                rowScalars[j] = Factory.buildScalar((i == j) ? "1" : "0");
            }
            this.matrixRows.add(Factory.buildVector(rowScalars)); // Factory.buildVector(Scalar[]) 필요
        }
    }

    // List<Vector>를 직접 받는 생성자 (내부용, clone 등에서 사용)
    MatrixImpl(List<Vector> rows) { // deepCopy 플래그 추가
        if (rows == null) throw new IllegalArgumentException("Row list cannot be null.");
        // 행별로 열 개수가 동일한지 검증 필요
        if (!rows.isEmpty()) {
            int cols = rows.get(0).getSize();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i).getSize() != cols) {
                    throw new DimensionMismatchException("All rows must have the same number of columns.");
                }
            }
        }
        this.matrixRows = new ArrayList<>(rows.size());
        for (Vector rowVec : rows) {
            this.matrixRows.add(rowVec.clone()); // 각 Vector도 복제
        }
    }

    @Override
    public List<Vector> getVectors(){
        return this.matrixRows;
    }

    // 11m. 특정 위치 요소 지정
    @Override
    public void setElement(int rowIndex, int colIndex, Scalar value) {
        if (value == null) throw new IllegalArgumentException("Scalar value cannot be null.");
        int[] size = getSize();
        if (rowIndex < 0 || rowIndex >= size[0] || colIndex < 0 || colIndex >= size[1]) {
            throw new IndexOutOfBoundsException("Index (" + rowIndex + "," + colIndex + ") is out of bounds for matrix size " + size[0] + "x" + size[1]);
        }
        this.matrixRows.get(rowIndex).setElement(colIndex, value); // Vector.setElement가 알아서 복제
    }

    // 11m. 특정 위치 요소 조회
    @Override
    public Scalar viewElement(int rowIndex, int colIndex) {
        int[] size = getSize();
        if (rowIndex < 0 || rowIndex >= size[0] || colIndex < 0 || colIndex >= size[1]) {
            if (size[0] == 0 || size[1] == 0) { // 0xN 또는 Mx0 행렬의 경우
                throw new IndexOutOfBoundsException("Cannot view element in an empty or zero-dimension matrix.");
            }
            throw new IndexOutOfBoundsException("Index (" + rowIndex + "," + colIndex + ") is out of bounds for matrix size " + size[0] + "x" + size[1]);
        }
        return this.matrixRows.get(rowIndex).viewElement(colIndex); // Vector.viewElement가 알아서 복제 반환
    }

    // 13. 행렬 크기 조회
    @Override
    public int[] getSize() {
        if (matrixRows.isEmpty()) {
            return new int[]{0, 0}; // 0x0 행렬
        }
        // 모든 행이 동일한 열 개수를 가진다고 가정 (생성자에서 보장)
        return new int[]{matrixRows.size(), matrixRows.get(0).getSize()};
    }


    // 14m. 객체를 콘솔에 출력
    @Override
    public String toString() {
        if (matrixRows.isEmpty()) {
            return "[[]]"; // 또는 "[]" for 0x0 matrix
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < matrixRows.size(); i++) {
            sb.append("  ").append(matrixRows.get(i).toString());
            if (i < matrixRows.size() - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // 15m. 객체의 동등성 판단
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Matrix)) return false;
        Matrix other = (Matrix) obj;

        int[] thisSize = this.getSize();
        int[] otherSize = other.getSize();
        if (thisSize[0] != otherSize[0] || thisSize[1] != otherSize[1]) return false;

        if (thisSize[0] == 0) return true; // 두 0xN 또는 Nx0 (또는 0x0) 행렬은 같음

        for (int i = 0; i < thisSize[0]; i++) {
            for (int j = 0; j < thisSize[1]; j++) {
                if (!this.viewElement(i, j).equals(other.viewElement(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        int[] size = getSize();
        result = 31 * result + size[0];
        result = 31 * result + size[1];
        if (size[0] > 0 && size[1] > 0) {
            for (int i = 0; i < size[0]; i++) {
                for (int j = 0; j < size[1]; j++) {
                    result = 31 * result + viewElement(i, j).hashCode();
                }
            }
        }
        return result;
    }


    // 17m. 객체 복제 (deep copy)
    @Override
    public Matrix clone() {
        try {
            MatrixImpl cloned = (MatrixImpl) super.clone();
            // List<Vector>를 직접 복사하는 생성자 사용
            cloned.matrixRows = new ArrayList<>(this.matrixRows.size());
            for(Vector rowVec : this.matrixRows) {
                cloned.matrixRows.add(rowVec.clone()); // 각 Vector 객체도 복제
            }
            return cloned;
            // 또는 MatrixImpl(this.matrixRows, true) // deepCopy 플래그가 있는 생성자 활용
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e); // Should not happen
        }
    }

    // 22. 행렬 덧셈 (non-static, modifies self)
    @Override
    public Matrix add(Matrix other) {
        if (other == null) throw new IllegalArgumentException("Other matrix cannot be null.");
        int[] thisSize = this.getSize();
        int[] otherSize = other.getSize();
        if (thisSize[0] != otherSize[0] || thisSize[1] != otherSize[1]) {
            throw new DimensionMismatchException("Matrices must have the same dimensions for addition. Self: "
                    + thisSize[0] + "x" + thisSize[1] + ", Other: " + otherSize[0] + "x" + otherSize[1]);
        }
        if (thisSize[0] == 0) return this; // 0xN 행렬 + 0xN 행렬 = 0xN 행렬

        for (int i = 0; i < thisSize[0]; i++) {
            for (int j = 0; j < thisSize[1]; j++) {
                Scalar currentVal = this.viewElement(i,j); // 현재 값 (복제본)
                Scalar otherVal = other.viewElement(i,j); // 다른 행렬 값 (복제본)
                currentVal.add(otherVal); // currentVal의 내부 값이 변경됨
                this.setElement(i,j, currentVal); // 변경된 값으로 다시 설정 (setElement가 복제해서 저장)
            }
        }
        return this;
    }

    // 23. 행렬 곱셈 (this = this * other)
    @Override
    public Matrix multiplyLeft(Matrix other) {
        if (other == null) throw new IllegalArgumentException("Other matrix cannot be null.");

        this.matrixRows = Tensors.multiply(other, this).getVectors();
        return this;
//        int[] thisSize = this.getSize(); // this is R1 x C1
//        int[] otherSize = other.getSize(); // other is R2 x C2
//
//        if (thisSize[1] != otherSize[0]) {
//            throw new DimensionMismatchException(
//                    "Number of columns in this matrix (" + thisSize[1] +
//                            ") must equal the number of rows in the other matrix (" + otherSize[0] + ") for multiplication."
//            );
//        }
//
//        // 결과 행렬의 크기는 R1 x C2
//        int resultRows = thisSize[0];
//        int resultCols = otherSize[1];
//
//        if (resultRows == 0 || resultCols == 0) { // 결과가 0xN 또는 Mx0 행렬인 경우
//            this.matrixRows = new ArrayList<>();
//            if (resultRows > 0) { // Mx0 결과 (열이 0개)
//                for (int i = 0; i < resultRows; i++) {
//                    this.matrixRows.add(Factory.buildVector(new Scalar[0])); // 빈 벡터 추가
//                }
//            }
//            // 만약 resultRows가 0이면 this.matrixRows는 그냥 빈 리스트 (0xN 행렬)
//            return this;
//        }
//
//
//        Scalar[][] resultData = new Scalar[resultRows][resultCols];
//
//        for (int i = 0; i < resultRows; i++) {
//            for (int j = 0; j < resultCols; j++) {
//                Scalar sum = Factory.buildScalar("0");
//                for (int k = 0; k < thisSize[1]; k++) { // thisSize[1] == otherSize[0]
//                    // sum = sum + this(i,k) * other(k,j)
//                    // Scalar.add 와 Scalar.multiply 는 새 객체를 반환하지 않고 자신을 수정함.
//                    // 따라서 다음과 같이 사용:
//                    // Scalar term = this.viewElement(i,k).clone(); // 원본 보존 위해 복제
//                    // term.multiply(other.viewElement(k,j));
//                    // sum.add(term);
//                    // 또는 static 메소드 사용:
//                    sum.add(Tensors.multiply(this.viewElement(i, k), other.viewElement(k, j)));
//                }
//                resultData[i][j] = sum;
//            }
//        }
//        // this의 내용을 resultData로 교체
//        // MatrixImpl(Scalar[][]) 생성자를 호출하여 새 객체를 만들고 그 내부 데이터로 교체하는 방식은
//        // this 참조 자체를 바꿀 수 없으므로, this의 내부 List<Vector>를 새로 구성해야 함.
//        List<Vector> newRows = new ArrayList<>(resultRows);
//        for (int i = 0; i < resultRows; i++) {
//            List<Scalar> rowScalars = new ArrayList<>(resultCols);
//            for (int j = 0; j < resultCols; j++) {
//                rowScalars.add(resultData[i][j]);
//            }
//            newRows.add(Factory.buildVector(rowScalars)); // Factory.buildVector(List<Scalar>) 필요
//        }
//        this.matrixRows = newRows;
//        return this;
    }

    @Override
    public Matrix multiplyRight(Matrix other){
        this.matrixRows = Tensors.multiply(this, other).getVectors();
        return this;
    }

// --- Advanced Matrix Functionality ---

    // 32. 가로 합치기 (non-static, returns new matrix)
    @Override
    public Matrix hstack(Matrix other) {
        if (other == null) throw new IllegalArgumentException("Other matrix for hstack cannot be null.");
        return Tensors.hstack(this, other); // static 메소드 활용
    }

    // 33. 세로 합치기 (non-static, returns new matrix)
    @Override
    public Matrix vstack(Matrix other) {
        if (other == null) throw new IllegalArgumentException("Other matrix for vstack cannot be null.");
        return Tensors.vstack(this, other); // static 메소드 활용
    }


    // 34. 특정 행을 벡터 형태로 추출
    @Override
    public Vector getRow(int rowIndex) {
        int[] size = getSize();
        if (rowIndex < 0 || rowIndex >= size[0]) {
            throw new IndexOutOfBoundsException("Row index " + rowIndex + " is out of bounds for matrix with " + size[0] + " rows.");
        }
        if (size[1] == 0) { // Mx0 행렬의 경우
            return Factory.buildVector(new Scalar[0]); // 빈 벡터 반환
        }
        return this.matrixRows.get(rowIndex).clone(); // Vector를 복제해서 반환
    }

    // 35. 특정 열을 벡터 형태로 추출
    @Override
    public Vector getCol(int colIndex) {
        int[] size = getSize();
        if (colIndex < 0 || colIndex >= size[1]) {
            throw new IndexOutOfBoundsException("Column index " + colIndex + " is out of bounds for matrix with " + size[1] + " columns.");
        }
        if (size[0] == 0) { // 0xN 행렬의 경우
            return Factory.buildVector(new Scalar[0]); // 빈 벡터 반환
        }

        List<Scalar> colScalars = new ArrayList<>(size[0]);
        for (int i = 0; i < size[0]; i++) {
            colScalars.add(this.viewElement(i, colIndex).clone()); // viewElement가 복제본 반환, 다시 복제 필요 없음.
            // this.matrixRows.get(i).viewElement(colIndex)
        }
        return Factory.buildVector(colScalars); // Factory.buildVector(List<Scalar>)
    }

    // 36. 특정 범위의 부분 행렬 추출
    @Override
    public Matrix subMatrix(int startRow, int endRow, int startCol, int endCol) {
        int[] currentSize = getSize();
        if (startRow < 0 || startRow >= currentSize[0] || endRow < startRow || endRow >= currentSize[0] ||
                startCol < 0 || startCol >= currentSize[1] || endCol < startCol || endCol >= currentSize[1]) {
            throw new IndexOutOfBoundsException("Submatrix indices are out of bounds or invalid.");
        }

        int numRows = endRow - startRow + 1;
        int numCols = endCol - startCol + 1;
        Scalar[][] subMatrixData = new Scalar[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                subMatrixData[i][j] = this.viewElement(startRow + i, startCol + j).clone();
            }
        }
        return Factory.buildMatrix(subMatrixData);
    }

    // 37. 특정 행/열 제외 부분 행렬 (minor)
    @Override
    public Matrix minor(int excludeRowIndex, int excludeColIndex) {
        int[] currentSize = getSize();
        if (excludeRowIndex < 0 || excludeRowIndex >= currentSize[0] ||
                excludeColIndex < 0 || excludeColIndex >= currentSize[1]) {
            throw new IndexOutOfBoundsException("Exclude indices are out of bounds.");
        }
        if (currentSize[0] <= 1 || currentSize[1] <= 1) {
            // 1xN, Mx1, 1x1 행렬에서 minor를 구하면 빈 행렬 또는 에러. 여기선 빈 행렬.
            // 또는 InvalidOperationException("Cannot compute minor for matrix with 1 row or 1 column.")
            return Factory.buildMatrix(new Scalar[0][0]);
        }


        int numRows = currentSize[0] - 1;
        int numCols = currentSize[1] - 1;
        Scalar[][] minorData = new Scalar[numRows][numCols];

        for (int i = 0, r = 0; i < currentSize[0]; i++) {
            if (i == excludeRowIndex) continue;
            for (int j = 0, c = 0; j < currentSize[1]; j++) {
                if (j == excludeColIndex) continue;
                minorData[r][c] = this.viewElement(i, j).clone();
                c++;
            }
            r++;
        }
        return Factory.buildMatrix(minorData);
    }

    // 38. 전치행렬
    @Override
    public Matrix transpose() {
        int[] currentSize = getSize();
        int numRows = currentSize[0];
        int numCols = currentSize[1];

        if (numRows == 0 || numCols == 0) { // 0xN 또는 Mx0 행렬의 전치는 Nx0 또는 0xM
            return Factory.buildMatrix(new Scalar[numCols][numRows]); // 크기만 바뀐 빈 행렬
        }

        Scalar[][] transposedData = new Scalar[numCols][numRows];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                transposedData[j][i] = this.viewElement(i, j).clone();
            }
        }
        return Factory.buildMatrix(transposedData);
    }

    // 39. 대각 요소의 합 (trace)
    @Override
    public Scalar trace() {
        if (!isSquare()) {
            throw new NotSquareMatrixException("Trace is defined only for square matrices.");
        }
        int[] size = getSize();
        if (size[0] == 0) return Factory.buildScalar("0"); // 0x0 행렬의 trace는 0

        Scalar sum = Factory.buildScalar("0");
        for (int i = 0; i < size[0]; i++) {
            sum.add(this.viewElement(i, i));
        }
        return sum;
    }

    // 40. 정사각 행렬 여부
    @Override
    public boolean isSquare() {
        int[] size = getSize();
        return size[0] == size[1]; // 0x0도 정사각으로 간주 가능
    }

    // 41. 상삼각 행렬
    @Override
    public boolean isUpperTriangular() {
        if (!isSquare()) {
            // throw new NotSquareMatrixException("Upper triangular check is for square matrices.");
            return false; // 정사각이 아니면 상삼각이 아님
        }
        int[] size = getSize();
        if (size[0] == 0) return true; // 0x0은 상삼각

        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < i; j++) { // 주 대각선 아래 요소 (row > col)
                if (!this.viewElement(i, j).isZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    // 42. 하삼각 행렬
    @Override
    public boolean isLowerTriangular() {
        if (!isSquare()) {
            // throw new NotSquareMatrixException("Lower triangular check is for square matrices.");
            return false;
        }
        int[] size = getSize();
        if (size[0] == 0) return true; // 0x0은 하삼각

        for (int i = 0; i < size[0]; i++) {
            for (int j = i + 1; j < size[1]; j++) { // 주 대각선 위 요소 (col > row)
                if (this.viewElement(i, j).isZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    // 43. 단위 행렬
    @Override
    public boolean isIdentity() {
        if (!isSquare()) return false;
        int[] size = getSize();
        if (size[0] == 0) return true; // 0x0 단위행렬? (보통 최소 1x1부터 정의). 여기선 true로.

        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                Scalar element = this.viewElement(i, j);
                if (i == j) { // Diagonal
                    if (element.getValue().compareTo(BigDecimal.ONE) != 0) return false;
                } else { // Off-diagonal
                    if (!element.isZero()) return false;
                }
            }
        }
        return true;
    }

    // 44. 영 행렬
    @Override
    public boolean isZeroMatrix() {
        int[] size = getSize();
        if (size[0] == 0 || size[1] == 0) return true; // 0xN, Mx0, 0x0은 영행렬

        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                if (!this.viewElement(i, j).isZero()) {
                    return false;
                }
            }
        }
        return true;
    }

    // --- Elementary Row/Column Operations (modifies self) ---
// 45. 특정 두 행의 위치 교환
    @Override
    public void swapRows(int rowIndex1, int rowIndex2) {
        int numRows = getSize()[0];
        if (rowIndex1 < 0 || rowIndex1 >= numRows || rowIndex2 < 0 || rowIndex2 >= numRows) {
            throw new IndexOutOfBoundsException("Row indices for swap are out of bounds.");
        }
        if (rowIndex1 == rowIndex2) return;
        Collections.swap(this.matrixRows, rowIndex1, rowIndex2);
    }

    // 46. 특정 두 열의 위치 교환
    @Override
    public void swapCols(int colIndex1, int colIndex2) {
        int[] size = getSize();
        int numRows = size[0];
        int numCols = size[1];
        if (colIndex1 < 0 || colIndex1 >= numCols || colIndex2 < 0 || colIndex2 >= numCols) {
            throw new IndexOutOfBoundsException("Column indices for swap are out of bounds.");
        }
        if (colIndex1 == colIndex2) return;
        if (numRows == 0) return; // 행이 없으면 열 교환 의미 없음

        for (int i = 0; i < numRows; i++) {
            Vector row = this.matrixRows.get(i);
            Scalar temp = row.viewElement(colIndex1); // viewElement는 복제본 반환
            row.setElement(colIndex1, row.viewElement(colIndex2));
            row.setElement(colIndex2, temp);
        }
    }

    // 47. 특정 행에 상수배
    @Override
    public void multiplyRow(int rowIndex, Scalar scalar) {
        if (scalar == null) throw new IllegalArgumentException("Scalar for multiplyRow cannot be null.");
        int numRows = getSize()[0];
        if (rowIndex < 0 || rowIndex >= numRows) {
            throw new IndexOutOfBoundsException("Row index for multiplyRow is out of bounds.");
        }
        if (getSize()[1] == 0) return; // 열이 없으면 의미 없음

        Vector row = this.matrixRows.get(rowIndex);
        // Vector.multiply(Scalar)는 non-static이며 자신을 변경함
        row.multiply(scalar.clone()); // scalar 원본 불변성 위해 clone
    }

    // 48. 특정 열에 상수배
    @Override
    public void multiplyCol(int colIndex, Scalar scalar) {
        if (scalar == null) throw new IllegalArgumentException("Scalar for multiplyCol cannot be null.");
        int[] size = getSize();
        int numRows = size[0];
        int numCols = size[1];
        if (colIndex < 0 || colIndex >= numCols) {
            throw new IndexOutOfBoundsException("Column index for multiplyCol is out of bounds.");
        }
        if (numRows == 0) return;

        for (int i = 0; i < numRows; i++) {
            Scalar currentElement = this.viewElement(i, colIndex);
            currentElement.multiply(scalar.clone()); // scalar.multiply는 자신을 변경
            this.setElement(i, colIndex, currentElement); // 변경된 scalar로 다시 설정
        }
    }

    // 49. 특정 행에 다른 행의 상수배를 더함 (targetRow += scalar * sourceRow)
    @Override
    public void addScaledRow(int targetRowIndex, int sourceRowIndex, Scalar scalar) {
        if (scalar == null) throw new IllegalArgumentException("Scalar for addScaledRow cannot be null.");
        int numRows = getSize()[0];
        if (targetRowIndex < 0 || targetRowIndex >= numRows || sourceRowIndex < 0 || sourceRowIndex >= numRows) {
            throw new IndexOutOfBoundsException("Row indices for addScaledRow are out of bounds.");
        }
        if (getSize()[1] == 0) return; // 열이 없으면 의미 없음
        if (targetRowIndex == sourceRowIndex) { // targetRow += scalar * targetRow -> targetRow *= (1+scalar)
            Scalar onePlusScalar = Factory.buildScalar("1").add(scalar.clone());
            this.multiplyRow(targetRowIndex, onePlusScalar);
            return;
        }

        Vector targetRow = this.matrixRows.get(targetRowIndex);
        Vector sourceRowScaled = this.matrixRows.get(sourceRowIndex).clone(); // 원본 sourceRow 보존
        sourceRowScaled.multiply(scalar.clone()); // sourceRow * scalar

        targetRow.add(sourceRowScaled); // targetRow = targetRow + sourceRowScaled
    }

    // 50. 특정 열에 다른 열의 상수배를 더함 (targetCol += scalar * sourceCol)
    @Override
    public void addScaledCol(int targetColIndex, int sourceColIndex, Scalar scalar) {
        if (scalar == null) throw new IllegalArgumentException("Scalar for addScaledCol cannot be null.");
        int[] size = getSize();
        int numRows = size[0];
        int numCols = size[1];
        if (targetColIndex < 0 || targetColIndex >= numCols || sourceColIndex < 0 || sourceColIndex >= numCols) {
            throw new IndexOutOfBoundsException("Column indices for addScaledCol are out of bounds.");
        }
        if (numRows == 0) return;
        if (targetColIndex == sourceColIndex) {
            Scalar onePlusScalar = Factory.buildScalar("1").add(scalar.clone());
            this.multiplyCol(targetColIndex, onePlusScalar);
            return;
        }

        for (int i = 0; i < numRows; i++) {
            Scalar targetElement = this.viewElement(i, targetColIndex); // 복제본
            Scalar sourceElementScaled = this.viewElement(i, sourceColIndex).clone(); // 복제본
            sourceElementScaled.multiply(scalar.clone());

            targetElement.add(sourceElementScaled);
            this.setElement(i, targetColIndex, targetElement);
        }
    }


    // 51. RREF 행렬 구해서 반환 (새 행렬 반환)
    @Override
    public Matrix rref() {
        Matrix tempMatrix = this.clone(); // 원본 변경 안 함
        int lead = 0;
        int rowCount = tempMatrix.getSize()[0];
        int columnCount = tempMatrix.getSize()[1];

        if (rowCount == 0 || columnCount == 0) return tempMatrix; // 빈 행렬은 그대로

        for (int r = 0; r < rowCount; r++) {
            if (lead >= columnCount) {
                break;
            }
            int i = r;
            while (tempMatrix.viewElement(i, lead).isZero()) {
                i++;
                if (i == rowCount) {
                    i = r;
                    lead++;
                    if (lead == columnCount) {
                        // 모든 남은 열이 0인 경우, RREF 계산 종료
                        return tempMatrix;
                    }
                }
            }
            tempMatrix.swapRows(i, r);

            Scalar pivot = tempMatrix.viewElement(r, lead);
            if (!pivot.isZero() && !pivot.isOne()) {
                // 행을 pivot으로 나누기: row[r] = row[r] / pivot
                // Scalar inversePivot = one.clone().divide(pivot); // Scalar에 divide 메소드 필요
                // 임시: BigDecimal 직접 사용

                Scalar inversePivot = Factory.buildScalar(BigDecimal.ONE.divide(pivot.getValue(), 10, RoundingMode.HALF_UP).toString()); // 정밀도 문제 주의
                tempMatrix.multiplyRow(r, inversePivot);
            }


            for (int j = 0; j < rowCount; j++) {
                if (j != r) {
                    Scalar val = tempMatrix.viewElement(j, lead).clone(); // 복제
                    // row[j] = row[j] - val * row[r]
                    // tempMatrix.addScaledRow(j, r, val.multiply(Factory.buildScalar("-1")));
                    val.multiply(Factory.buildScalar("-1")); // val = -val
                    tempMatrix.addScaledRow(j,r,val);
                }
            }
            lead++;
        }
        return tempMatrix;
    }


    // 52. 자신이 RREF 행렬인지 여부 판별
    @Override
    public boolean isRREF() {
        int[] size = getSize();
        int rows = size[0];
        int cols = size[1];

        if (rows == 0) return true; // 0xN 행렬은 RREF (자명하게)
        if (cols == 0 && rows > 0) return true; // Mx0 행렬도 RREF (자명하게)

//        Scalar zero = Factory.buildScalar("0");
//        Scalar one = Factory.buildScalar("1");
        int lead = -1; // 이전 행의 선행 1의 열 인덱스

        for (int r = 0; r < rows; r++) {
            int currentLead = -1;
            // 1. 각 행의 첫 번째 non-zero 원소(leading entry)는 1이어야 한다. (피봇)
            // 2. 모든 피봇은 이전 행의 피봇보다 오른쪽에 있어야 한다.
            // 3. 피봇을 포함하는 열에서 피봇을 제외한 모든 원소는 0이어야 한다.
            // 4. 모든 원소가 0인 행은 맨 아래에 위치해야 한다. (1,2,3을 만족하면 자동)

            for (int c = 0; c < cols; c++) {
                Scalar currentElement = viewElement(r, c);
                if (!currentElement.isZero()) { // 첫 non-zero 원소 발견
                    if (!currentElement.isOne()) return false; // 조건 1 위반 (피봇이 1이 아님)
                    currentLead = c; // 현재 행의 피봇 열 인덱스
                    break;
                }
            }

            if (currentLead != -1) { // 현재 행에 피봇이 있는 경우
                if (currentLead <= lead) return false; // 조건 2 위반 (피봇 위치가 이전 행보다 왼쪽이거나 같음)
                lead = currentLead; // 현재 행의 피봇 열 인덱스 업데이트

                // 조건 3 검사: 피봇 열의 다른 모든 원소가 0인지
                for (int i = 0; i < rows; i++) {
                    if (i != r && !viewElement(i, currentLead).isZero()) {
                        return false; // 조건 3 위반
                    }
                }
            } else { // 현재 행이 모두 0인 경우 (피봇 없음)
                // 이 행 아래의 모든 행도 0이어야 함
                for (int next_r = r + 1; next_r < rows; next_r++) {
                    for (int c = 0; c < cols; c++) {
                        if (!viewElement(next_r, c).isZero()) return false; // 0행 아래 non-zero 행 발견
                    }
                }
                // 현재 행부터 맨 아래까지 모두 0이면 RREF 조건 만족하며 종료
                return true;
            }
        }
        return true; // 모든 검사 통과
    }


    // 53. 행렬식 (nxn 행렬)
    @Override
    public Scalar determinant() {
        if (!isSquare()) {
            throw new NotSquareMatrixException("Determinant is defined only for square matrices.");
        }
        int[] size = getSize();
        int n = size[0];

        if (n == 0) return Factory.buildScalar("1"); // 0x0 행렬의 행렬식은 1 (관례) 또는 예외

        if (n == 1) {
            return this.viewElement(0, 0).clone();
        }
        if (n == 2) {
            // ad - bc
            Scalar a = this.viewElement(0, 0);
            Scalar b = this.viewElement(0, 1);
            Scalar c = this.viewElement(1, 0);
            Scalar d = this.viewElement(1, 1);
            // (a*d) - (b*c)
            Scalar ad = Tensors.multiply(a.clone(), d.clone());
            Scalar bc = Tensors.multiply(b.clone(), c.clone());
            return Tensors.add(ad, bc.multiply(Factory.buildScalar("-1"))); // ad - bc
        }

        // Laplace expansion (cofactor expansion)
        Scalar det = Factory.buildScalar("0");
        Scalar sign = Factory.buildScalar("1");

        for (int j = 0; j < n; j++) { // 첫 번째 행을 따라 확장
            Matrix minorMatrix = this.minor(0, j);
            Scalar cofactorElement = this.viewElement(0, j).clone();
            cofactorElement.multiply(minorMatrix.determinant());
            cofactorElement.multiply(sign.clone());

            det.add(cofactorElement);
            sign.multiply(Factory.buildScalar("-1")); // 부호 변경 (-1)^(0+j)
        }
        return det;
    }

    // 54. 역행렬 (nxn 행렬, 새 행렬 반환)
    @Override
    public Matrix inverse() {
        if (!isSquare()) {
            throw new NotSquareMatrixException("Inverse is defined only for square matrices.");
        }
        int[] size = getSize();
        int n = size[0];
        if (n == 0) { // 0x0 행렬의 역행렬은 0x0
            return Factory.buildMatrix(new Scalar[0][0]);
        }

        Scalar det = this.determinant();
        if (det.getValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new SingularMatrixException("Matrix is singular, inverse does not exist (determinant is zero).");
        }

        // Adjoint matrix / determinant method
        // 역행렬 = (1/det(A)) * adj(A)
        // adj(A)는 수반 행렬 (cofactor matrix의 전치)

        Scalar[][] adjData = new Scalar[n][n];
        Scalar oneOverDet;
        try {
            // Scalar에 divide 메소드가 없다면 BigDecimal 직접 사용
            BigDecimal detValue = det.getValue();
            // BigDecimal.ONE.divide(detValue) // 정확한 나누기가 안될 수 있음. 스케일 지정 필요.
            // 여기서는 1/det을 나중에 각 요소에 곱하는 방식으로 진행
            oneOverDet = Factory.buildScalar(BigDecimal.ONE.divide(detValue, 20, RoundingMode.HALF_UP).toString()); // 스케일 조절 필요
        } catch (ArithmeticException e) {
            throw new SingularMatrixException("Cannot compute inverse, error in dividing by determinant (possibly due to precision). Determinant: " + det.toString());
        }


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Matrix minorMatrix = this.minor(i, j);
                Scalar cofactor = minorMatrix.determinant();
                // 부호 (-1)^(i+j)
                if ((i + j) % 2 != 0) {
                    cofactor.multiply(Factory.buildScalar("-1"));
                }
                // adj(A)는 cofactor matrix의 전치이므로, (j,i) 위치에 저장
                adjData[j][i] = cofactor;
            }
        }

        Matrix adjMatrix = Factory.buildMatrix(adjData);

        // 각 요소를 det으로 나누기 (또는 1/det 곱하기)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Scalar current = adjMatrix.viewElement(i,j); // 복제본
                current.multiply(oneOverDet.clone());
                adjMatrix.setElement(i,j, current);
            }
        }
        return adjMatrix;
    }

    // 추가: getMatrix() 에 대한 명세가 있었음. List<Vector> 반환.
// Matrix 인터페이스에서는 getRows() 등으로 변경 제안했었으나, 원래 명세대로라면...
// public List<Vector> getMatrix() { return Collections.unmodifiableList(this.matrixRows); }
// 하지만, 이렇게하면 MatrixImpl 내부 구조가 노출됨.
// MatrixImpl 내에서만 사용하거나, 꼭 필요하다면 복제본을 반환하는 것이 안전.
// 여기서는 인터페이스에 해당 메소드를 명시적으로 추가하지 않았으므로 구현 생략.
// 만약 Matrix.java에 `List<Vector> getRawRows();` 와 같이 명시되었다면 구현.
// 기존 코드의 Matrix.java에는 `List<Vector> getMatrix()`가 있었으므로, 이를 구현한다면:
    public List<Vector> getMatrix(){ // 원래 Matrix.java 인터페이스에 있던 메소드
        // 캡슐화를 위해 방어적 복사
        List<Vector> defensiveCopy = new ArrayList<>(this.matrixRows.size());
        for(Vector v : this.matrixRows) {
            defensiveCopy.add(v.clone());
        }
        return defensiveCopy;
        // 또는 불변 뷰: return Collections.unmodifiableList(this.matrixRows);
        // (이 경우 내부 Vector 객체가 변경될 가능성은 여전히 있음. Vector.clone()이 더 안전)
    }
}
