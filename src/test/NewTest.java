package test;

import tensor.*; // 모든 tensor 패키지 클래스 임포트
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.io.FileWriter; // CSV 테스트용
import java.io.IOException;  // CSV 테스트용
import java.io.PrintWriter; // CSV 테스트용

public class NewTest {

    // Helper to print test titles
    private static void printTitle(String title) {
        System.out.println("\n=========================================================");
        System.out.println("===== " + title + " =====");
        System.out.println("=========================================================");
    }

    // Helper for CSV file creation
    private static String createTestCsvFile(String fileName, String[] content) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (String line : content) {
                out.println(line);
            }
        }
        return fileName;
    }

    public static void main(String[] args) {
        System.out.println("Tensor Library Test Suite - STARTED");
        System.out.println("Current Date/Time: " + java.time.LocalDateTime.now());

        testScalarCreation();
        testScalarBasicFunctions();
        testScalarOperations();

        testVectorCreation();
        testVectorBasicFunctions();
        testVectorOperations();
        testVectorAdvancedFunctions();

        testMatrixCreation();
        testMatrixBasicFunctions();
        testMatrixOperations();
        testMatrixAdvancedFunctions(); // This covers 32-44
        testMatrixElementaryOperations(); // This covers 45-50
        testMatrixRREFDeterminantInverse(); // This covers 51-54

        testTensorsStaticMethods(); // Tests methods in Tensors.java which call spec items 24-29, 32, 33
        testExceptionHandling(); // Tests various exception conditions related to many spec items

        System.out.println("\nTensor Library Test Suite - COMPLETED");
    }

    public static void testScalarCreation() {
        printTitle("Scalar Creation (01, 02)");
        // Test.java 요구사항: Factory를 이용하여 스칼라 객체를 얻어낸다.
        // Test.java 요구사항: 스칼라 객체의 참조 타입은 인터페이스 타입만을 사용한다.
        Scalar s1 = Factory.buildScalar("3.14159"); // 명세 01: 값을 지정하여 스칼라 생성
        System.out.println("s1 (from String '3.14159'): " + s1); // 명세 14s: 스칼라 출력

        Scalar s2 = Factory.buildScalar(0.0, 1.0); // 명세 02: i 이상 j 미만의 무작위 값을 요소로 하는 스칼라 생성
        System.out.println("s2 (random 0.0 to 1.0): " + s2); // 명세 14s
        Scalar s3 = Factory.buildScalar(-5.0, -4.0); // 명세 02
        System.out.println("s3 (random -5.0 to -4.0): " + s3); // 명세 14s
    }

    public static void testScalarBasicFunctions() {
        printTitle("Scalar Basic Functions (12, 14s, 15s, 16, 17s)");
        Scalar s_a = Factory.buildScalar("10.5"); // 명세 01
        Scalar s_b = Factory.buildScalar("20.0"); // 명세 01
        Scalar s_a_copy = Factory.buildScalar("10.5"); // 명세 01

        // 명세 12: (only 스칼라) 값을 지정/조회할 수 있다.
        System.out.println("s_a.getValue(): " + s_a.getValue()); // 12. 값 조회
        System.out.println("s_b before setValue: " + s_b); // 14s. toString
        s_b.setValue(new BigDecimal("25.5")); // 12. 값 지정
        System.out.println("s_b after setValue(25.5): " + s_b); // 14s. toString

        // 명세 14s. toString (implicit in println)

        // 명세 15s. (@Override equals()) 객체의 동등성 판단
        System.out.println("s_a.equals(s_a_copy) (10.5 == 10.5): " + s_a.equals(s_a_copy)); // 15s
        System.out.println("s_a.equals(s_b) (10.5 == 25.5): " + s_a.equals(s_b)); // 15s

        // 명세 16 (implements Comparable) 스칼라의 경우 값의 대소 비교
        System.out.println("s_a.compareTo(s_b) (10.5 vs 25.5): " + s_a.compareTo(s_b)); // 16
        System.out.println("s_b.compareTo(s_a) (25.5 vs 10.5): " + s_b.compareTo(s_a)); // 16
        System.out.println("s_a.compareTo(s_a_copy) (10.5 vs 10.5): " + s_a.compareTo(s_a_copy)); // 16

        // 명세 17s. (@Override clone()) 객체 복제 (deep copy)
        Scalar s_a_cloned = s_a.clone(); // 17s
        System.out.println("s_a: " + s_a + ", s_a_cloned: " + s_a_cloned); // 14s
        System.out.println("s_a == s_a_cloned (identity): " + (s_a == s_a_cloned));
        System.out.println("s_a.equals(s_a_cloned) (equality): " + s_a.equals(s_a_cloned)); // 15s
        s_a_cloned.setValue(new BigDecimal("100")); // 12. 값 지정 (on cloned object)
        System.out.println("After s_a_cloned.setValue(100) -> s_a: " + s_a + ", s_a_cloned: " + s_a_cloned); // 14s (s_a 불변 확인)
    }

    public static void testScalarOperations() {
        printTitle("Scalar Operations (18, 19 - non-static, 24, 25 - static)");
        Scalar s_x = Factory.buildScalar("5"); // 명세 01
        Scalar s_y = Factory.buildScalar("3"); // 명세 01
        Scalar s_z = Factory.buildScalar("2"); // 명세 01

        // 명세 18: 스칼라는 다른 스칼라와 덧셈이 가능하다 (non-static, modifies self)
        System.out.println("s_x original: " + s_x); // 14s
        s_x.add(s_y); // 18
        System.out.println("s_x after s_x.add(s_y (3)): " + s_x); // 14s

        // 명세 19: 스칼라는 다른 스칼라와 곱셈이 가능하다 (non-static, modifies self)
        s_x.multiply(s_z); // 19
        System.out.println("s_x after s_x.multiply(s_z (2)): " + s_x); // 14s

        // Reset for static tests
        s_x = Factory.buildScalar("10"); // 명세 01
        s_y = Factory.buildScalar("4");  // 명세 01

        // 명세 24: 전달받은 두 스칼라의 덧셈이 가능하다 (static, returns new)
        Scalar sum_static = Tensors.add(s_x, s_y); // 24 (Interface static method)
        System.out.println("Scalar.add(s_x (10), s_y (4)): " + sum_static + " (s_x: " + s_x + ", s_y: " + s_y + ")"); // 14s

        // 명세 25: 전달받은 두 스칼라의 곱셈이 가능하다 (static, returns new)
        Scalar prod_static = Tensors.multiply(s_x, s_y); // 25 (Interface static method)
        System.out.println("Scalar.multiply(s_x (10), s_y (4)): " + prod_static + " (s_x: " + s_x + ", s_y: " + s_y + ")"); // 14s
    }


    public static void testVectorCreation() {
        printTitle("Vector Creation (03, 04, 05)");
        // Test.java 요구사항: Factory를 이용하여 벡터 객체를 얻어낸다.
        // Test.java 요구사항: 벡터 객체의 참조 타입은 인터페이스 타입만을 사용한다.
        // 명세 03: 지정한 하나의 값을 모든 요소의 값으로 하는 n-차원 벡터 생성
        Vector v1 = Factory.buildVector(3, 7.7); // 03
        System.out.println("v1 (3 elements, value 7.7): " + v1); // 명세 14v: 벡터 출력

        // 명세 04: i 이상 j 미만의 무작위 값을 요소로 하는 n-차원 벡터 생성
        Vector v2 = Factory.buildVector(4, 10.0, 20.0); // 04
        System.out.println("v2 (4 elements, random 10.0-20.0): " + v2); // 14v

        // 명세 05: 1차원 배열로부터 n-차원 벡터 생성
        double[] arr_d = {1.1, 2.2, 3.3};
        Vector v3 = Factory.buildVector(arr_d); // 05
        System.out.println("v3 (from double[] {1.1, 2.2, 3.3}): " + v3); // 14v

        // 추가된 Factory 메소드 테스트 (명세에는 double[]만 언급, Scalar[]도 코드에 존재)
        Scalar[] arr_s = {Factory.buildScalar("100"), Factory.buildScalar("200")}; // Helper for test
        Vector v4 = Factory.buildVector(arr_s); // Factory.buildVector(Scalar[])
        System.out.println("v4 (from Scalar[] {100, 200}): " + v4); // 14v
    }

    public static void testVectorBasicFunctions() {
        printTitle("Vector Basic Functions (11v, 13, 14v, 15v, 17v)");
        Vector vec = Factory.buildVector(new double[]{1.0, 2.0, 3.0}); // 명세 05
        Vector vec_copy_val = Factory.buildVector(new double[]{1.0, 2.0, 3.0}); // 명세 05
        Vector vec_other = Factory.buildVector(new double[]{1.0, 2.0, 4.0}); // 명세 05

        // 명세 13: (벡터) 차원(길이) 조회
        System.out.println("vec.getSize(): " + vec.getSize()); // 13

        // 명세 11v: (only 벡터) 특정 위치의 요소를 지정/조회할 수 있다.
        System.out.println("vec.viewElement(1): " + vec.viewElement(1)); // 11v. 조회
        System.out.println("vec before setElement(1, 99.0): " + vec); // 14v
        vec.setElement(1, Factory.buildScalar("99.0")); // 11v. 지정
        System.out.println("vec after setElement(1, 99.0): " + vec); // 14v
        System.out.println("vec.viewElement(1) after set: " + vec.viewElement(1)); // 11v. 조회

        // 명세 14v. toString (implicit)

        // 명세 15v. (@Override equals()) 객체의 동등성 판단
        vec.setElement(1, Factory.buildScalar("2.0")); // Reset for equals test (11v)
        System.out.println("vec.equals(vec_copy_val) ([1,2,3] == [1,2,3]): " + vec.equals(vec_copy_val)); // 15v
        System.out.println("vec.equals(vec_other) ([1,2,3] == [1,2,4]): " + vec.equals(vec_other)); // 15v
        Vector vec_diff_size = Factory.buildVector(new double[]{1.0, 2.0}); // 05
        System.out.println("vec.equals(vec_diff_size) (size mismatch): " + vec.equals(vec_diff_size)); // 15v (size mismatch)


        // 명세 17v. (@Override clone()) 객체 복제 (deep copy)
        Vector vec_cloned = vec.clone(); // 17v
        System.out.println("vec: " + vec + ", vec_cloned: " + vec_cloned); // 14v
        System.out.println("vec == vec_cloned (identity): " + (vec == vec_cloned));
        System.out.println("vec.equals(vec_cloned) (equality): " + vec.equals(vec_cloned)); // 15v
        vec_cloned.setElement(0, Factory.buildScalar("777")); // 11v (on cloned)
        System.out.println("After vec_cloned.setElement(0, 777) -> vec: " + vec + ", vec_cloned: " + vec_cloned); // 14v (vec 불변 확인)
    }


    public static void testVectorOperations() {
        printTitle("Vector Operations (20, 21 - non-static, 26, 27 - static)");
        Vector v_a = Factory.buildVector(new double[]{1, 2, 3}); // 05
        Vector v_b = Factory.buildVector(new double[]{4, 5, 6}); // 05
        Scalar scal = Factory.buildScalar("3"); // 01

        // 명세 20: 벡터는 다른 벡터와 덧셈이 가능하다 (non-static, modifies self)
        System.out.println("v_a original: " + v_a); // 14v
        v_a.add(v_b); // 20
        System.out.println("v_a after v_a.add(v_b {4,5,6}): " + v_a); // 14v

        // 명세 21: 벡터는 다른 스칼라와 곱셈이 가능하다 (non-static, modifies self)
        v_a.multiply(scal); // 21
        System.out.println("v_a after v_a.multiply(scal {3}): " + v_a); // 14v

        // Reset for static tests
        v_a = Factory.buildVector(new double[]{10, 20}); // 05
        v_b = Factory.buildVector(new double[]{3, 7});   // 05
        scal = Factory.buildScalar("2");                // 01

        // 명세 26: 전달받은 두 벡터의 덧셈이 가능하다 (static, returns new)
        Vector sum_static_v = Tensors.add(v_a, v_b); // 26 (Interface static method)
        System.out.println("Vector.add(v_a {10,20}, v_b {3,7}): " + sum_static_v + " (v_a: " + v_a + ")"); // 14v

        // 명세 27: 전달받은 스칼라와 벡터의 곱셈이 가능하다 (static, returns new)
        Vector prod_static_v = Tensors.multiply(v_a, scal); // 27 (Interface static method)
        System.out.println("Vector.multiply(v_a {10,20}, scal {2}): " + prod_static_v + " (v_a: " + v_a + ")"); // 14v
    }

    public static void testVectorAdvancedFunctions() {
        printTitle("Vector Advanced Functions (30, 31)");
        Vector vec_adv = Factory.buildVector(new double[]{10, 20, 30}); // 05
        System.out.println("Original vector vec_adv: " + vec_adv); // 14v

        // 명세 31: n-차원 벡터 객체는 자신으로부터 1xn 행렬을 생성하여 반환할 수 있다. (toRowMatrix)
        Matrix rowMatrix = vec_adv.toRowMatrix(); // 31
        System.out.println("vec_adv.toRowMatrix(): \n" + rowMatrix); // 14m (Matrix toString)
        System.out.println("Row matrix size: " + Arrays.toString(rowMatrix.getSize())); // 13 (Matrix getSize)

        // 명세 30: n-차원 벡터 객체는 자신으로부터 nx1 행렬을 생성하여 반환할 수 있다. (toColMatrix)
        Matrix colMatrix = vec_adv.toColMatrix(); // 30
        System.out.println("vec_adv.toColMatrix(): \n" + colMatrix); // 14m
        System.out.println("Column matrix size: " + Arrays.toString(colMatrix.getSize())); // 13 (Matrix getSize)
    }


    public static void testMatrixCreation() {
        printTitle("Matrix Creation (06, 07, 08, 09, 10)");
        // Test.java 요구사항: Factory를 이용하여 행렬 객체를 얻어낸다.
        // Test.java 요구사항: 행렬 객체의 참조 타입은 인터페이스 타입만을 사용한다.

        // 명세 06: 지정한 하나의 값을 모든 요소의 값으로 하는 m x n 행렬 생성
        Matrix m1 = Factory.buildMatrix(2, 3, 1.0); // 06
        System.out.println("m1 (2x3, value 1.0):\n" + m1); // 명세 14m: 행렬 출력

        // 명세 07: i 이상 j 미만의 무작위 값을 요소로 하는 m x n 행렬 생성
        Matrix m2 = Factory.buildMatrix(3, 2, 0.0, 10.0); // 07
        System.out.println("m2 (3x2, random 0-10):\n" + m2); // 14m

        // 명세 08: csv 파일로부터 m x n 행렬 생성
        String csvFile = "test_matrix.csv";
        try {
            createTestCsvFile(csvFile, new String[]{"1,2,3", "4,5,6", "7,8,9.5"}); // Helper for test
            Matrix m3 = Factory.buildMatrix(csvFile); // 08
            System.out.println("m3 (from CSV '" + csvFile + "'):\n" + m3); // 14m
        } catch (IOException | CsvParseException e) {
            System.err.println("Error creating or parsing CSV for m3: " + e.getMessage());
        }

        String emptyCsvFile = "empty_test_matrix.csv";
        try {
            createTestCsvFile(emptyCsvFile, new String[]{}); // Empty file
            // Matrix m_empty_csv = Factory.buildMatrix(emptyCsvFile); // 08 (empty file case) // Removed 0x0 matrix creation from empty CSV
            // System.out.println("m_empty_csv (from empty CSV '" + emptyCsvFile + "') (size " + Arrays.toString(m_empty_csv.getSize()) + "):\n" + m_empty_csv); // 13 (Matrix getSize), 14m

            createTestCsvFile(emptyCsvFile, new String[]{",,"}); // File with only commas
            Matrix m_commas_csv = Factory.buildMatrix(emptyCsvFile); // 08 (commas only case)
            System.out.println("m_commas_csv (from CSV with ' ,, ') (size " + Arrays.toString(m_commas_csv.getSize()) + "):\n" + m_commas_csv); // 13, 14m
        } catch (IOException | CsvParseException e) {
            System.err.println("Error with empty CSV tests: " + e.getMessage());
        }


        // 명세 09: 2차원 배열로부터 m x n 행렬 생성
        double[][] arr_2d = {{1.1, 1.2}, {2.1, 2.2}, {3.1, 3.2}};
        Matrix m4 = Factory.buildMatrix(arr_2d); // 09
        System.out.println("m4 (from double[3][2]):\n" + m4); // 14m

        // 명세 10: 단위 행렬 생성
        Matrix m5_identity = Factory.buildMatrix(3); // 10
        System.out.println("m5_identity (3x3):\n" + m5_identity); // 14m

        // Removed 0x0 identity matrix creation.
        // Matrix m0_identity = Factory.buildMatrix(0); // 10 (0-dimension)
        // System.out.println("m0_identity (0x0):\n" + m0_identity + " size: " + Arrays.toString(m0_identity.getSize())); // 14m, 13

        // 추가된 Factory 메소드 테스트 (명세에는 double[][]만 언급, Scalar[][]도 코드에 존재)
        Scalar[][] scalar_arr_2d = {
                {Factory.buildScalar("10"), Factory.buildScalar("20")}, // Helper
                {Factory.buildScalar("30"), Factory.buildScalar("40")}  // Helper
        };
        Matrix m6 = Factory.buildMatrix(scalar_arr_2d); // Factory.buildMatrix(Scalar[][])
        System.out.println("m6 (from Scalar[2][2]):\n" + m6); // 14m

        // Removed 0x0 matrix from array.
        // Matrix m_empty_arr = Factory.buildMatrix(new double[0][0]); // 09 (0x0 from array)
        // System.out.println("m_empty_arr (from new double[0][0]):\n" + m_empty_arr + " size: " + Arrays.toString(m_empty_arr.getSize())); // 14m, 13

        // Removed 0xN matrix from array.
        // Matrix m_empty_rows_arr = Factory.buildMatrix(new double[0][2]); // 09 (0xN from array)
        // System.out.println("m_empty_rows_arr (from new double[0][2]):\n" + m_empty_rows_arr + " size: " + Arrays.toString(m_empty_rows_arr.getSize())); // 14m, 13
    }

    public static void testMatrixBasicFunctions() {
        printTitle("Matrix Basic Functions (11m, 13, 14m, 15m, 17m)");
        Matrix mat = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 09
        Matrix mat_copy_val = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 09
        Matrix mat_other_val = Factory.buildMatrix(new double[][]{{1,2},{3,5}}); // 09

        // 명세 13: (행렬) 행개수, 열개수 조회
        System.out.println("mat.getSize(): " + Arrays.toString(mat.getSize())); // 13

        // 명세 11m: (only 행렬) 특정 위치의 요소를 지정/조회할 수 있다.
        System.out.println("mat.viewElement(1,0): " + mat.viewElement(1,0)); // 11m. 조회
        System.out.println("mat before setElement(1,0, Factory.buildScalar(\"99\")):\n" + mat); // 14m
        mat.setElement(1,0, Factory.buildScalar("99")); // 11m. 지정
        System.out.println("mat after setElement(1,0, Factory.buildScalar(\"99\")):\n" + mat); // 14m
        System.out.println("mat.viewElement(1,0) after set: " + mat.viewElement(1,0)); // 11m. 조회

        // 명세 14m. toString (implicit)

        // 명세 15m. (@Override equals()) 객체의 동등성 판단
        mat.setElement(1,0, Factory.buildScalar("3")); // Reset for equals (11m)
        System.out.println("mat.equals(mat_copy_val): " + mat.equals(mat_copy_val)); // 15m
        System.out.println("mat.equals(mat_other_val): " + mat.equals(mat_other_val)); // 15m
        Matrix mat_diff_size = Factory.buildMatrix(new double[][]{{1,2}}); // 09
        System.out.println("mat.equals(mat_diff_size) (size mismatch): " + mat.equals(mat_diff_size)); // 15m (size mismatch)


        // 명세 17m. (@Override clone()) 객체 복제 (deep copy)
        Matrix mat_cloned = mat.clone(); // 17m
        System.out.println("mat:\n" + mat + "mat_cloned:\n" + mat_cloned); // 14m
        System.out.println("mat == mat_cloned (identity): " + (mat == mat_cloned));
        System.out.println("mat.equals(mat_cloned) (equality): " + mat.equals(mat_cloned)); // 15m
        mat_cloned.setElement(0,0, Factory.buildScalar("777")); // 11m (on cloned)
        System.out.println("After mat_cloned.setElement(0,0, 777):\nmat:\n" + mat + "\nmat_cloned:\n" + mat_cloned); // 14m (mat 불변 확인)
    }

    public static void testMatrixOperations() {
        printTitle("Matrix Operations (22, 23 - non-static, 28, 29 - static)");
        Matrix m_a = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 09
        Matrix m_b = Factory.buildMatrix(new double[][]{{5,6},{7,8}}); // 09
        Matrix m_c = Factory.buildMatrix(new double[][]{{1,0},{0,1}}); // 09 (Identity for testing)

        // 명세 22: 행렬은 다른 행렬과 덧셈이 가능하다 (non-static, modifies self)
        System.out.println("m_a original:\n" + m_a); // 14m
        m_a.add(m_b); // 22
        System.out.println("m_a after m_a.add(m_b):\n" + m_a); // 14m

        // 명세 23: 행렬은 다른 행렬과 곱셈이 가능하다 (non-static, modifies self, this = this * other)
        System.out.println("m_a before multiply by Identity:\n" + m_a); // 14m
        m_a.multiply(m_c); // 23 (m_a = m_a * I)
        System.out.println("m_a after m_a.multiply(Identity):\n" + m_a); // 14m

        Matrix m_d = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6}}); // 09 (2x3)
        Matrix m_e = Factory.buildMatrix(new double[][]{{7,8},{9,10},{11,12}}); // 09 (3x2)
        System.out.println("m_d (2x3) original:\n" + m_d); // 14m
        m_d.multiply(m_e); // 23 (m_d = m_d * m_e, result 2x2)
        System.out.println("m_d after m_d.multiply(m_e (3x2)) (result should be 2x2):\n" + m_d); // 14m
        // Note: 명세 23 also mentions "다른 행렬이 왼쪽 행렬로서 곱해지는 경우 (this = other * this)"
        // This specific non-static case for `this` modification is not directly tested here as `multiply` implements `this = this * other`.

        // Reset for static tests
        m_a = Factory.buildMatrix(new double[][]{{1,0},{0,1}}); // 09
        m_b = Factory.buildMatrix(new double[][]{{2,3},{4,5}}); // 09

        // 명세 28: 전달받은 두 행렬의 덧셈이 가능하다 (static, returns new)
        Matrix sum_static_m = Tensors.add(m_a, m_b); // 28 (Interface static method)
        System.out.println("Matrix.add(m_a, m_b):\n" + sum_static_m + "\n(m_a unchanged:\n" + m_a + ")"); // 14m

        // 명세 29: 전달받은 두 행렬의 곱셈이 가능하다 (static, returns new)
        Matrix prod_static_m = Tensors.multiply(m_a, m_b); // 29 (Interface static method, I * m_b)
        System.out.println("Matrix.multiply(m_a (I), m_b):\n" + prod_static_m + "\n(m_a unchanged:\n" + m_a + ")"); // 14m

        Matrix m1_mult = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 09 (2x2)
        Matrix m2_mult = Factory.buildMatrix(new double[][]{{2,0,1},{0,3,0}}); // 09 (2x3)
        Matrix prod_static_m2 = Tensors.multiply(m1_mult, m2_mult); // 29
        System.out.println("Matrix.multiply(m1_mult (2x2), m2_mult (2x3)) (result 2x3):\n" + prod_static_m2); // 14m
    }

    public static void testMatrixAdvancedFunctions() {
        printTitle("Matrix Advanced Functions (32-44)");
        Matrix mat1 = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 09
        Matrix mat2 = Factory.buildMatrix(new double[][]{{5,6},{7,8}}); // 09
        Matrix mat_rect = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6}}); // 09 (2x3)

        // 명세 32: 행렬은 다른 행렬과 가로로 합쳐질 수 있다 (non-static, returns new)
        Matrix hstacked = mat1.hstack(mat2); // 32 (non-static, internally calls static)
        System.out.println("mat1.hstack(mat2):\n" + hstacked); // 14m
        // Static version also covered by Tensors.hstack later

        // 명세 33: 행렬은 다른 행렬과 세로로 합쳐질 수 있다 (non-static, returns new)
        Matrix vstacked = mat1.vstack(mat2); // 33 (non-static, internally calls static)
        System.out.println("mat1.vstack(mat2):\n" + vstacked); // 14m
        // Static version also covered by Tensors.vstack later

        // 명세 34: 행렬은 특정 행을 벡터 형태로 추출
        System.out.println("mat_rect.getRow(0): " + mat_rect.getRow(0)); // 34

        // 명세 35: 행렬은 특정 열을 벡터 형태로 추출
        System.out.println("mat_rect.getCol(1): " + mat_rect.getCol(1)); // 35

        // 명세 36: 행렬은 특정 범위의 부분 행렬을 추출 (start/end row/col indices)
        Matrix sub = mat_rect.subMatrix(0,0,1,2); // 36 (extracts row 0, columns 1-2)
        System.out.println("mat_rect.subMatrix(0,0,1,2):\n" + sub); // 14m

        // 명세 37: 행렬은 특정 하나의 행과 하나의 열을 제외한 부분 행렬 (minor)
        Matrix m_for_minor = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // 09
        Matrix minor_00 = m_for_minor.minor(0,0); // 37
        System.out.println("m_for_minor.minor(0,0):\n" + minor_00); // 14m

        // 명세 38: 행렬은 전치행렬을 (새로 생성하여) 구해줄 수 있다
        System.out.println("mat_rect original:\n" + mat_rect); // 14m
        System.out.println("mat_rect.transpose():\n" + mat_rect.transpose()); // 38

        // Removed 0xN matrix transpose test.
        // Matrix zero_row_mat = Factory.buildMatrix(new double[0][2]); // 09 (0xN)
        // System.out.println("zero_row_mat (0x2):\n" + zero_row_mat); // 14m
        // System.out.println("zero_row_mat.transpose() (2x0):\n" + zero_row_mat.transpose() + " size: " + Arrays.toString(zero_row_mat.transpose().getSize())); // 38, 13

        // 명세 39: 행렬은 대각 요소의 합을 구해줄 수 있다 (nxn 행렬) (trace)
        System.out.println("mat1.trace() (1+4=5): " + mat1.trace()); // 39

        // 명세 40: 행렬은 자신이 정사각 행렬인지 여부를 판별
        System.out.println("mat1.isSquare() (2x2): " + mat1.isSquare()); // 40
        System.out.println("mat_rect.isSquare() (2x3): " + mat_rect.isSquare()); // 40
        // Removed 0x0 matrix test.
        // System.out.println("Factory.buildMatrix(0).isSquare() (0x0): " + Factory.buildMatrix(0).isSquare()); // 40, 10

        // 명세 41: 행렬은 자신이 상삼각 행렬인지 여부 판별 (nxn 행렬)
        Matrix upper_tri = Factory.buildMatrix(new double[][]{{1,2,3},{0,4,5},{0,0,6}}); // 09
        Matrix not_upper_tri = Factory.buildMatrix(new double[][]{{1,2,3},{1,4,5},{0,0,6}}); // 09
        System.out.println("upper_tri.isUpperTriangular(): " + upper_tri.isUpperTriangular()); // 41
        System.out.println("not_upper_tri.isUpperTriangular(): " + not_upper_tri.isUpperTriangular()); // 41
        System.out.println("mat_rect.isUpperTriangular(): " + mat_rect.isUpperTriangular()); // 41 (not square)
        // Removed 0x0 matrix test.
        // System.out.println("Factory.buildMatrix(0).isUpperTriangular() (0x0): " + Factory.buildMatrix(0).isUpperTriangular()); // 41, 10

        // 명세 42: 행렬은 자신이 하삼각 행렬인지 여부 판별 (nxn 행렬)
        Matrix lower_tri = Factory.buildMatrix(new double[][]{{1,0,0},{2,3,0},{4,5,6}}); // 09
        Matrix not_lower_tri = Factory.buildMatrix(new double[][]{{1,0,1},{2,3,0},{4,5,6}}); // 09
        System.out.println("lower_tri.isLowerTriangular(): " + lower_tri.isLowerTriangular()); // 42
        System.out.println("not_lower_tri.isLowerTriangular(): " + not_lower_tri.isLowerTriangular()); // 42
        // Removed 0x0 matrix test.
        // System.out.println("Factory.buildMatrix(0).isLowerTriangular() (0x0): " + Factory.buildMatrix(0).isLowerTriangular()); // 42, 10

        // 명세 43: 행렬은 자신이 단위 행렬인지 여부 판별 (nxn 행렬)
        Matrix iden = Factory.buildMatrix(2); // 10
        System.out.println("iden (2x2).isIdentity(): " + iden.isIdentity()); // 43
        System.out.println("mat1.isIdentity(): " + mat1.isIdentity()); // 43
        // Removed 0x0 matrix test.
        // System.out.println("Factory.buildMatrix(0).isIdentity() (0x0): " + Factory.buildMatrix(0).isIdentity()); // 43, 10

        // 명세 44: 행렬은 자신이 영 행렬인지 여부 판별
        Matrix zero_m = Factory.buildMatrix(2,3,0.0); // 06
        System.out.println("zero_m.isZeroMatrix(): " + zero_m.isZeroMatrix()); // 44
        System.out.println("mat1.isZeroMatrix(): " + mat1.isZeroMatrix()); // 44
        // Removed 0xN matrix test.
        // System.out.println("Factory.buildMatrix(0,2,0.0).isZeroMatrix() (0x2): " + Factory.buildMatrix(0,2,0.0).isZeroMatrix()); // 44, 06
    }

    public static void testMatrixElementaryOperations() {
        printTitle("Matrix Elementary Row/Column Operations (45-50)");
        Matrix mat_ops = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // 09
        System.out.println("Original mat_ops:\n" + mat_ops); // 14m

        // 명세 45: 특정 두 행의 위치를 맞교환
        mat_ops.swapRows(0,1); // 45
        System.out.println("After swapRows(0,1):\n" + mat_ops); // 14m
        mat_ops.swapRows(0,1); // Swap back for subsequent tests

        // 명세 46: 특정 두 열의 위치를 맞교환
        mat_ops.swapCols(0,2); // 46
        System.out.println("After swapCols(0,2):\n" + mat_ops); // 14m
        mat_ops.swapCols(0,2); // Swap back

        // 명세 47: 특정 행에 상수배(스칼라)
        mat_ops.multiplyRow(0, Factory.buildScalar("2")); // 47
        System.out.println("After multiplyRow(0, Scalar(2)):\n" + mat_ops); // 14m
        mat_ops.multiplyRow(0, Factory.buildScalar("0.5")); // Undo

        // 명세 48: 특정 열에 상수배(스칼라)
        mat_ops.multiplyCol(1, Factory.buildScalar("3")); // 48
        System.out.println("After multiplyCol(1, Scalar(3)):\n" + mat_ops); // 14m
        mat_ops.multiplyCol(1, Factory.buildScalar(BigDecimal.ONE.divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP).toString())); // Undo

        // 명세 49: 특정 행에 다른 행의 상수배를 더함
        mat_ops = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // Reset (09)
        mat_ops.addScaledRow(1, 0, Factory.buildScalar("2")); // 49 (R1 = R1 + 2*R0)
        System.out.println("After addScaledRow(1,0, Scalar(2)) (R1=R1+2R0):\n" + mat_ops); // 14m

        // 명세 50: 특정 열에 다른 열의 상수배를 더함
        mat_ops = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // Reset (09)
        mat_ops.addScaledCol(1, 2, Factory.buildScalar("-1")); // 50 (C1 = C1 - C2)
        System.out.println("After addScaledCol(1,2, Scalar(-1)) (C1=C1-C2):\n" + mat_ops); // 14m
    }


    public static void testMatrixRREFDeterminantInverse() {
        printTitle("Matrix RREF, Determinant, Inverse (51-54)");
        Matrix m_rref_test = Factory.buildMatrix(new double[][]{{1,2,-1,-4},{2,3,-1,-11},{-2,0,-3,22}}); // 09
        System.out.println("Matrix for RREF test:\n" + m_rref_test); // 14m

        // 명세 51: 자신으로부터 RREF행렬을 구해서 반환 (새 행렬 반환)
        Matrix rref_m = m_rref_test.rref(); // 51
        System.out.println("RREF of m_rref_test:\n" + rref_m); // 14m

        // 명세 52: 자신이 RREF 행렬인지 여부 판별
        System.out.println("m_rref_test.isRREF(): " + m_rref_test.isRREF()); // 52
        System.out.println("rref_m.isRREF(): " + rref_m.isRREF()); // 52
        Matrix iden3 = Factory.buildMatrix(3); // 10
        System.out.println("Identity(3).isRREF(): " + iden3.isRREF()); // 52

        Matrix m_det_inv = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 09

        // 명세 53: 자신의 행렬식을 구해줄 수 있다 (nxn 행렬)
        Scalar det = m_det_inv.determinant(); // 53
        System.out.println("Determinant of [[1,2],[3,4]]: " + det + " (Expected: -2)"); // 14s

        Matrix m_det3 = Factory.buildMatrix(new double[][]{{6,1,1},{4,-2,5},{2,8,7}}); // 09
        System.out.println("Determinant of 3x3 matrix [[6,1,1],[4,-2,5],[2,8,7]]: " + m_det3.determinant()); // 53, 14s

        // 명세 54: 자신의 역행렬을 구해줄 수 있다 (nxn 행렬)
        try {
            Matrix inv_m = m_det_inv.inverse(); // 54
            System.out.println("Inverse of [[1,2],[3,4]]:\n" + inv_m); // 14m
            Matrix product_check = Tensors.multiply(m_det_inv, inv_m); // 29 (Static multiply for verification)
            System.out.println("Original * Inverse (should be Identity):\n" + product_check); // 14m, 43 (isIdentity for check)
        } catch (SingularMatrixException e) {
            System.err.println("Error calculating inverse: " + e.getMessage());
        }

        Matrix singular_m = Factory.buildMatrix(new double[][]{{1,2},{2,4}}); // 09 (Singular matrix)
        try {
            System.out.println("Attempting inverse of singular matrix [[1,2],[2,4]]:");
            singular_m.inverse(); // 54 (Expected to throw SingularMatrixException)
        } catch (SingularMatrixException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        // Removed 0x0 matrix tests for determinant and inverse.
        // Matrix zero_dim_matrix = Factory.buildMatrix(0); // 10 (0x0 matrix)
        // System.out.println("Determinant of 0x0 matrix: " + zero_dim_matrix.determinant()); // 53
        // try {
        //     Matrix inv_zero_dim = zero_dim_matrix.inverse(); // 54
        //     System.out.println("Inverse of 0x0 matrix:\n" + inv_zero_dim + " size: " + Arrays.toString(inv_zero_dim.getSize())); // 14m, 13
        // } catch (Exception e) {
        //     System.err.println("Error with 0x0 inverse: " + e.getMessage());
        // }
    }

    public static void testTensorsStaticMethods() {
        printTitle("Tensors Static Utility Methods");
        // Tensors.java 요구사항: 모든 메소드가 public static. 위의 디폴트 static 메소드들을 호출.
        Scalar ts1 = Factory.buildScalar("100"); // 01
        Scalar ts2 = Factory.buildScalar("25");  // 01
        // Tensors calls Scalar.add (명세 24)
        System.out.println("Tensors.add(" + ts1 + ", " + ts2 + "): " + Tensors.add(ts1, ts2));
        // Tensors calls Scalar.multiply (명세 25)
        System.out.println("Tensors.multiply(" + ts1 + ", " + ts2 + "): " + Tensors.multiply(ts1, ts2));

        Vector tv1 = Factory.buildVector(new double[]{1,2}); // 05
        Vector tv2 = Factory.buildVector(new double[]{3,4}); // 05
        // Tensors calls Vector.add (명세 26)
        System.out.println("Tensors.add(" + tv1 + ", " + tv2 + "): " + Tensors.add(tv1, tv2));
        // Tensors calls Vector.multiply (명세 27)
        System.out.println("Tensors.multiply(" + tv1 + ", " + ts1 + "): " + Tensors.multiply(tv1, ts1));
        System.out.println("Tensors.multiply(" + ts1 + ", " + tv1 + "): " + Tensors.multiply(ts1, tv1)); // Overloaded version

        Matrix tm1 = Factory.buildMatrix(new double[][]{{1,0},{0,1}}); // 09
        Matrix tm2 = Factory.buildMatrix(new double[][]{{5,6},{7,8}}); // 09
        // Tensors calls Matrix.add (명세 28)
        System.out.println("Tensors.add(tm1, tm2):\n" + Tensors.add(tm1, tm2));
        // Tensors calls Matrix.multiply (명세 29)
        System.out.println("Tensors.multiply(tm1, tm2):\n" + Tensors.multiply(tm1, tm2));

        Matrix tm3 = Factory.buildMatrix(new double[][]{{1},{2}}); // 09 (2x1)
        Matrix tm4 = Factory.buildMatrix(new double[][]{{3},{4}}); // 09 (2x1)
        // Tensors calls Matrix.hstack (명세 32, static part)
        System.out.println("Tensors.hstack(tm3, tm4):\n" + Tensors.hstack(tm3, tm4));

        Matrix tm5 = Factory.buildMatrix(new double[][]{{1,2}}); // 09 (1x2)
        Matrix tm6 = Factory.buildMatrix(new double[][]{{3,4}}); // 09 (1x2)
        // Tensors calls Matrix.vstack (명세 33, static part)
        System.out.println("Tensors.vstack(tm5, tm6):\n" + Tensors.vstack(tm5, tm6));

        // Tensors utility methods (명세 외 추가된 Tensors 메소드들)
        // Tensors.identity calls Factory.buildMatrix(dimension) (명세 10)
        System.out.println("Tensors.identity(3):\n" + Tensors.identity(3));
        // Tensors.zeros calls Factory.buildMatrix(rows,cols,0.0) (명세 06)
        System.out.println("Tensors.zeros(2,3):\n" + Tensors.zeros(2,3));
        // Tensors.ones calls Factory.buildMatrix(rows,cols,1.0) (명세 06)
        System.out.println("Tensors.ones(3,2):\n" + Tensors.ones(3,2));
        // Tensors.filled calls Factory.buildMatrix(Scalar[][]) (명세 09 - 유사, 내부적으로 Scalar[][] 사용)
        System.out.println("Tensors.filled(2,2, Factory.buildScalar(\"7\")):\n" + Tensors.filled(2,2, Factory.buildScalar("7")));
    }

    public static void testExceptionHandling() {
        printTitle("Exception Handling Tests");
        // 예외 클래스 정의 및 사용 테스트

        // Scalar creation exceptions (related to 01, 02)
        try {
            System.out.print("Test: Factory.buildScalar(\"abc\") -> ");
            Factory.buildScalar("abc"); // Expected: IllegalArgumentException from BigDecimal
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("Test: Factory.buildScalar(5,2) -> "); // ii >= jj (related to 02)
            Factory.buildScalar(5,2); // Expected: IllegalArgumentException from ScalarImpl constructor
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // Vector dimension mismatch (related to 20, 26)
        Vector v_exc1 = Factory.buildVector(2, 1.0); // 03
        Vector v_exc2 = Factory.buildVector(3, 1.0); // 03
        try {
            System.out.print("Test: v_exc1.add(v_exc2) (size mismatch) -> ");
            v_exc1.add(v_exc2); // Expected: DimensionMismatchException
        } catch (DimensionMismatchException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // Matrix dimension mismatch for add (related to 22, 28)
        Matrix m_exc1 = Factory.buildMatrix(2, 2, 1.0); // 06
        Matrix m_exc2 = Factory.buildMatrix(2, 3, 1.0); // 06
        try {
            System.out.print("Test: m_exc1.add(m_exc2) (dim mismatch) -> ");
            m_exc1.add(m_exc2); // Expected: DimensionMismatchException
        } catch (DimensionMismatchException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // Matrix dimension mismatch for multiply (related to 23, 29)
        Matrix m_exc3 = Factory.buildMatrix(2, 3, 1.0); // 06 (2x3)
        Matrix m_exc4 = Factory.buildMatrix(2, 2, 1.0); // 06 (2x2)
        try {
            System.out.print("Test: m_exc3.multiply(m_exc4) (dim mismatch) -> ");
            m_exc3.multiply(m_exc4); // Expected: DimensionMismatchException (cols of m1 != rows of m2)
        } catch (DimensionMismatchException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // Index out of bounds (related to 11v, 11m)
        try {
            System.out.print("Test: v_exc1.viewElement(5) -> ");
            v_exc1.viewElement(5); // Expected: IndexOutOfBoundsException
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("Test: m_exc1.viewElement(5,5) -> ");
            m_exc1.viewElement(5,5); // Expected: IndexOutOfBoundsException
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // CSV Parse Exception (related to 08)
        String badCsvFile = "bad_test_matrix.csv";
        try {
            createTestCsvFile(badCsvFile, new String[]{"1,2,3", "4,not_a_number,6"}); // Helper
            System.out.print("Test: Factory.buildMatrix from bad CSV -> ");
            Factory.buildMatrix(badCsvFile); // Expected: CsvParseException (due to NumberFormatException)
        } catch (CsvParseException | IOException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            createTestCsvFile(badCsvFile, new String[]{"1,2,3", "4,5"}); // Inconsistent columns
            System.out.print("Test: Factory.buildMatrix from inconsistent CSV -> ");
            Factory.buildMatrix(badCsvFile); // Expected: CsvParseException
        } catch (CsvParseException | IOException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // NotSquareMatrixException (related to 39, 53, 54)
        Matrix rect_m_exc = Factory.buildMatrix(2,3,1.0); // 06
        try {
            System.out.print("Test: rect_m_exc.trace() -> ");
            rect_m_exc.trace(); // 39 Expected: NotSquareMatrixException
        } catch (NotSquareMatrixException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("Test: rect_m_exc.determinant() -> ");
            rect_m_exc.determinant(); // 53 Expected: NotSquareMatrixException
        } catch (NotSquareMatrixException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("Test: rect_m_exc.inverse() -> ");
            rect_m_exc.inverse(); // 54 Expected: NotSquareMatrixException
        } catch (NotSquareMatrixException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // SingularMatrixException (related to 54)
        Matrix singular_m_exc = Factory.buildMatrix(new double[][]{{1,1},{1,1}}); // 09 (Determinant is 0)
        try {
            System.out.print("Test: singular_m_exc.inverse() -> ");
            singular_m_exc.inverse(); // 54 Expected: SingularMatrixException
        } catch (SingularMatrixException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}
