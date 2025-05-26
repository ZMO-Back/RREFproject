package test;

import tensor.*; // 모든 tensor 패키지 클래스 임포트
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.io.FileWriter; // CSV 테스트용
import java.io.IOException;  // CSV 테스트용
import java.io.PrintWriter; // CSV 테스트용

public class Test {

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
        testMatrixAdvancedFunctions(); // 여기에 고급 기능 테스트 포함
        testMatrixElementaryOperations();
        testMatrixRREFDeterminantInverse();


        testTensorsStaticMethods();
        testExceptionHandling(); // 예외 발생 상황 테스트


        System.out.println("\nTensor Library Test Suite - COMPLETED");
    }

    public static void testScalarCreation() {
        printTitle("Scalar Creation (01, 02)");
        Scalar s1 = Factory.buildScalar("3.14159");
        System.out.println("s1 (from String '3.14159'): " + s1);

        Scalar s2 = Factory.buildScalar(0.0, 1.0); // 0.0 이상 1.0 미만 랜덤
        System.out.println("s2 (random 0.0 to 1.0): " + s2);
        Scalar s3 = Factory.buildScalar(-5.0, -4.0);
        System.out.println("s3 (random -5.0 to -4.0): " + s3);
    }

    public static void testScalarBasicFunctions() {
        printTitle("Scalar Basic Functions (12, 14s, 15s, 16, 17s)");
        Scalar s_a = Factory.buildScalar("10.5");
        Scalar s_b = Factory.buildScalar("20.0");
        Scalar s_a_copy = Factory.buildScalar("10.5");

        // 12. getValue
        System.out.println("s_a.getValue(): " + s_a.getValue());

        // 12. setValue
        System.out.println("s_b before setValue: " + s_b);
        s_b.setValue(new BigDecimal("25.5"));
        System.out.println("s_b after setValue(25.5): " + s_b);

        // 14s. toString (implicit in println)

        // 15s. equals
        System.out.println("s_a.equals(s_a_copy) (10.5 == 10.5): " + s_a.equals(s_a_copy)); // true
        System.out.println("s_a.equals(s_b) (10.5 == 25.5): " + s_a.equals(s_b));     // false

        // 16. compareTo
        System.out.println("s_a.compareTo(s_b) (10.5 vs 25.5): " + s_a.compareTo(s_b)); // < 0
        System.out.println("s_b.compareTo(s_a) (25.5 vs 10.5): " + s_b.compareTo(s_a)); // > 0
        System.out.println("s_a.compareTo(s_a_copy) (10.5 vs 10.5): " + s_a.compareTo(s_a_copy)); // 0

        // 17s. clone
        Scalar s_a_cloned = s_a.clone();
        System.out.println("s_a: " + s_a + ", s_a_cloned: " + s_a_cloned);
        System.out.println("s_a == s_a_cloned (identity): " + (s_a == s_a_cloned)); // false
        System.out.println("s_a.equals(s_a_cloned) (equality): " + s_a.equals(s_a_cloned)); // true
        s_a_cloned.setValue(new BigDecimal("100"));
        System.out.println("After s_a_cloned.setValue(100) -> s_a: " + s_a + ", s_a_cloned: " + s_a_cloned); // s_a 불변 확인
    }

    public static void testScalarOperations() {
        printTitle("Scalar Operations (18, 19 - non-static, 24, 25 - static)");
        Scalar s_x = Factory.buildScalar("5");
        Scalar s_y = Factory.buildScalar("3");
        Scalar s_z = Factory.buildScalar("2");

        // 18. add (non-static)
        System.out.println("s_x original: " + s_x);
        s_x.add(s_y); // s_x becomes 5+3=8
        System.out.println("s_x after s_x.add(s_y (3)): " + s_x);

        // 19. multiply (non-static)
        s_x.multiply(s_z); // s_x becomes 8*2=16
        System.out.println("s_x after s_x.multiply(s_z (2)): " + s_x);

        // Reset for static tests
        s_x = Factory.buildScalar("10");
        s_y = Factory.buildScalar("4");

        // 24. add (static)
        Scalar sum_static = Tensors.add(s_x, s_y);
        System.out.println("Scalar.add(s_x (10), s_y (4)): " + sum_static + " (s_x: " + s_x + ", s_y: " + s_y + ")");

        // 25. multiply (static)
        Scalar prod_static = Tensors.multiply(s_x, s_y);
        System.out.println("Scalar.multiply(s_x (10), s_y (4)): " + prod_static + " (s_x: " + s_x + ", s_y: " + s_y + ")");
    }


    public static void testVectorCreation() {
        printTitle("Vector Creation (03, 04, 05)");
        // 03. Specified value
        Vector v1 = Factory.buildVector(3, 7.7);
        System.out.println("v1 (3 elements, value 7.7): " + v1);

        // 04. Random values
        Vector v2 = Factory.buildVector(4, 10.0, 20.0); // 4 elements, random between 10.0 and 20.0
        System.out.println("v2 (4 elements, random 10.0-20.0): " + v2);

        // 05. From 1D array
        double[] arr_d = {1.1, 2.2, 3.3};
        Vector v3 = Factory.buildVector(arr_d);
        System.out.println("v3 (from double[] {1.1, 2.2, 3.3}): " + v3);

        Scalar[] arr_s = {Factory.buildScalar("100"), Factory.buildScalar("200")};
        Vector v4 = Factory.buildVector(arr_s);
        System.out.println("v4 (from Scalar[] {100, 200}): " + v4);
    }

    public static void testVectorBasicFunctions() {
        printTitle("Vector Basic Functions (11v, 13, 14v, 15v, 17v)");
        Vector vec = Factory.buildVector(new double[]{1.0, 2.0, 3.0});
        Vector vec_copy_val = Factory.buildVector(new double[]{1.0, 2.0, 3.0});
        Vector vec_other = Factory.buildVector(new double[]{1.0, 2.0, 4.0});

        // 13. getSize
        System.out.println("vec.getSize(): " + vec.getSize());

        // 11v. viewElement
        System.out.println("vec.viewElement(1): " + vec.viewElement(1)); // Should be 2.0

        // 11v. setElement
        System.out.println("vec before setElement(1, 99.0): " + vec);
        vec.setElement(1, Factory.buildScalar("99.0"));
        System.out.println("vec after setElement(1, 99.0): " + vec);
        System.out.println("vec.viewElement(1) after set: " + vec.viewElement(1)); // Should be 99.0

        // 14v. toString (implicit)

        // 15v. equals
        vec.setElement(1, Factory.buildScalar("2.0")); // Reset for equals test
        System.out.println("vec.equals(vec_copy_val) ([1,2,3] == [1,2,3]): " + vec.equals(vec_copy_val)); // true
        System.out.println("vec.equals(vec_other) ([1,2,3] == [1,2,4]): " + vec.equals(vec_other));       // false
        Vector vec_diff_size = Factory.buildVector(new double[]{1.0, 2.0});
        System.out.println("vec.equals(vec_diff_size) (size mismatch): " + vec.equals(vec_diff_size)); // false


        // 17v. clone
        Vector vec_cloned = vec.clone();
        System.out.println("vec: " + vec + ", vec_cloned: " + vec_cloned);
        System.out.println("vec == vec_cloned (identity): " + (vec == vec_cloned));             // false
        System.out.println("vec.equals(vec_cloned) (equality): " + vec.equals(vec_cloned));     // true
        vec_cloned.setElement(0, Factory.buildScalar("777"));
        System.out.println("After vec_cloned.setElement(0, 777) -> vec: " + vec + ", vec_cloned: " + vec_cloned); // vec 불변 확인
    }


    public static void testVectorOperations() {
        printTitle("Vector Operations (20, 21 - non-static, 26, 27 - static)");
        Vector v_a = Factory.buildVector(new double[]{1, 2, 3});
        Vector v_b = Factory.buildVector(new double[]{4, 5, 6});
        Scalar scal = Factory.buildScalar("3");

        // 20. add (non-static)
        System.out.println("v_a original: " + v_a);
        v_a.add(v_b); // v_a becomes [1+4, 2+5, 3+6] = [5, 7, 9]
        System.out.println("v_a after v_a.add(v_b {4,5,6}): " + v_a);

        // 21. multiply by scalar (non-static)
        v_a.multiply(scal); // v_a becomes [5*3, 7*3, 9*3] = [15, 21, 27]
        System.out.println("v_a after v_a.multiply(scal {3}): " + v_a);

        // Reset for static tests
        v_a = Factory.buildVector(new double[]{10, 20});
        v_b = Factory.buildVector(new double[]{3, 7});
        scal = Factory.buildScalar("2");

        // 26. add (static)
        Vector sum_static_v = Tensors.add(v_a, v_b);
        System.out.println("Vector.add(v_a {10,20}, v_b {3,7}): " + sum_static_v + " (v_a: " + v_a + ")");

        // 27. multiply by scalar (static)
        Vector prod_static_v = Tensors.multiply(v_a, scal);
        System.out.println("Vector.multiply(v_a {10,20}, scal {2}): " + prod_static_v + " (v_a: " + v_a + ")");
    }

    public static void testVectorAdvancedFunctions() {
        printTitle("Vector Advanced Functions (30, 31)");
        Vector vec_adv = Factory.buildVector(new double[]{10, 20, 30});
        System.out.println("Original vector vec_adv: " + vec_adv);

        // 30. toRowMatrix (1xN)
        Matrix rowMatrix = vec_adv.toRowMatrix();
        System.out.println("vec_adv.toRowMatrix(): \n" + rowMatrix);
        System.out.println("Row matrix size: " + Arrays.toString(rowMatrix.getSize())); // Should be [1, 3]

        // 31. toColMatrix (Nx1)
        Matrix colMatrix = vec_adv.toColMatrix();
        System.out.println("vec_adv.toColMatrix(): \n" + colMatrix);
        System.out.println("Column matrix size: " + Arrays.toString(colMatrix.getSize())); // Should be [3, 1]
    }


    public static void testMatrixCreation() {
        printTitle("Matrix Creation (06, 07, 08, 09, 10)");
        // 06. Specified value
        Matrix m1 = Factory.buildMatrix(2, 3, 1.0);
        System.out.println("m1 (2x3, value 1.0):\n" + m1);

        // 07. Random values
        Matrix m2 = Factory.buildMatrix(3, 2, 0.0, 10.0);
        System.out.println("m2 (3x2, random 0-10):\n" + m2);

        // 08. From CSV
        String csvFile = "test_matrix.csv";
        try {
            createTestCsvFile(csvFile, new String[]{"1,2,3", "4,5,6", "7,8,9.5"});
            Matrix m3 = Factory.buildMatrix(csvFile);
            System.out.println("m3 (from CSV '" + csvFile + "'):\n" + m3);
        } catch (IOException | CsvParseException e) {
            System.err.println("Error creating or parsing CSV for m3: " + e.getMessage());
        }

        String emptyCsvFile = "empty_test_matrix.csv";
        try {
            createTestCsvFile(emptyCsvFile, new String[]{}); // Empty file
            Matrix m_empty_csv = Factory.buildMatrix(emptyCsvFile);
            System.out.println("m_empty_csv (from empty CSV '" + emptyCsvFile + "') (size " + Arrays.toString(m_empty_csv.getSize()) + "):\n" + m_empty_csv);

            createTestCsvFile(emptyCsvFile, new String[]{",,"}); // File with only commas (empty elements)
            Matrix m_commas_csv = Factory.buildMatrix(emptyCsvFile);
            System.out.println("m_commas_csv (from CSV with ' ,, ') (size " + Arrays.toString(m_commas_csv.getSize()) + "):\n" + m_commas_csv);

        } catch (IOException | CsvParseException e) {
            System.err.println("Error with empty CSV tests: " + e.getMessage());
        }


        // 09. From 2D array
        double[][] arr_2d = {{1.1, 1.2}, {2.1, 2.2}, {3.1, 3.2}};
        Matrix m4 = Factory.buildMatrix(arr_2d);
        System.out.println("m4 (from double[3][2]):\n" + m4);

        // 10. Identity matrix
        Matrix m5_identity = Factory.buildMatrix(3); // 3x3 Identity
        System.out.println("m5_identity (3x3):\n" + m5_identity);

        Matrix m0_identity = Factory.buildMatrix(0); // 0x0 Identity
        System.out.println("m0_identity (0x0):\n" + m0_identity + " size: " + Arrays.toString(m0_identity.getSize()));

        // From Scalar[][]
        Scalar[][] scalar_arr_2d = {
                {Factory.buildScalar("10"), Factory.buildScalar("20")},
                {Factory.buildScalar("30"), Factory.buildScalar("40")}
        };
        Matrix m6 = Factory.buildMatrix(scalar_arr_2d);
        System.out.println("m6 (from Scalar[2][2]):\n" + m6);

        // Empty array
        Matrix m_empty_arr = Factory.buildMatrix(new double[0][0]);
        System.out.println("m_empty_arr (from new double[0][0]):\n" + m_empty_arr + " size: " + Arrays.toString(m_empty_arr.getSize()));
        Matrix m_empty_rows_arr = Factory.buildMatrix(new double[0][2]);
        System.out.println("m_empty_rows_arr (from new double[0][2]):\n" + m_empty_rows_arr + " size: " + Arrays.toString(m_empty_rows_arr.getSize()));
    }

    public static void testMatrixBasicFunctions() {
        printTitle("Matrix Basic Functions (11m, 13, 14m, 15m, 17m)");
        Matrix mat = Factory.buildMatrix(new double[][]{{1,2},{3,4}});
        Matrix mat_copy_val = Factory.buildMatrix(new double[][]{{1,2},{3,4}});
        Matrix mat_other_val = Factory.buildMatrix(new double[][]{{1,2},{3,5}});

        // 13. getSize
        System.out.println("mat.getSize(): " + Arrays.toString(mat.getSize())); // [2,2]

        // 11m. viewElement
        System.out.println("mat.viewElement(1,0): " + mat.viewElement(1,0)); // Should be 3

        // 11m. setElement
        System.out.println("mat before setElement(1,0, Factory.buildScalar(\"99\")):\n" + mat);
        mat.setElement(1,0, Factory.buildScalar("99"));
        System.out.println("mat after setElement(1,0, Factory.buildScalar(\"99\")):\n" + mat);
        System.out.println("mat.viewElement(1,0) after set: " + mat.viewElement(1,0)); // Should be 99

        // 14m. toString (implicit)

        // 15m. equals
        mat.setElement(1,0, Factory.buildScalar("3")); // Reset for equals
        System.out.println("mat.equals(mat_copy_val): " + mat.equals(mat_copy_val)); // true
        System.out.println("mat.equals(mat_other_val): " + mat.equals(mat_other_val)); // false
        Matrix mat_diff_size = Factory.buildMatrix(new double[][]{{1,2}});
        System.out.println("mat.equals(mat_diff_size) (size mismatch): " + mat.equals(mat_diff_size)); // false


        // 17m. clone
        Matrix mat_cloned = mat.clone();
        System.out.println("mat:\n" + mat + "mat_cloned:\n" + mat_cloned);
        System.out.println("mat == mat_cloned (identity): " + (mat == mat_cloned)); // false
        System.out.println("mat.equals(mat_cloned) (equality): " + mat.equals(mat_cloned)); // true
        mat_cloned.setElement(0,0, Factory.buildScalar("777"));
        System.out.println("After mat_cloned.setElement(0,0, 777):\nmat:\n" + mat + "\nmat_cloned:\n" + mat_cloned); // mat 불변
    }

    public static void testMatrixOperations() {
        printTitle("Matrix Operations (22, 23 - non-static, 28, 29 - static)");
        Matrix m_a = Factory.buildMatrix(new double[][]{{1,2},{3,4}});
        Matrix m_b = Factory.buildMatrix(new double[][]{{5,6},{7,8}});
        Matrix m_c = Factory.buildMatrix(new double[][]{{1,0},{0,1}}); // for multiplication

        // 22. add (non-static)
        System.out.println("m_a original:\n" + m_a);
        m_a.add(m_b); // m_a becomes [[1+5,2+6],[3+7,4+8]] = [[6,8],[10,12]]
        System.out.println("m_a after m_a.add(m_b):\n" + m_a);

        // 23. multiply (non-static, this = this * other)
        // m_a is now [[6,8],[10,12]]. m_c is Identity(2)
        System.out.println("m_a before multiply by Identity:\n" + m_a);
        m_a.multiplyRight(m_c); // m_a = m_a * I (should remain same)
        System.out.println("m_a after m_a.multiply(Identity):\n" + m_a);

        Matrix m_d = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6}}); // 2x3
        Matrix m_e = Factory.buildMatrix(new double[][]{{7,8},{9,10},{11,12}}); // 3x2
        // m_d will become m_d * m_e. Result is 2x2
        // [[1*7+2*9+3*11, 1*8+2*10+3*12], [4*7+5*9+6*11, 4*8+5*10+6*12]]
        // [[7+18+33, 8+20+36], [28+45+66, 32+50+72]]
        // [[58, 64], [139, 154]]
        System.out.println("m_d (2x3) original:\n" + m_d);
        m_d.multiplyRight(m_e);
        System.out.println("m_d after m_d.multiply(m_e (3x2)) (result should be 2x2):\n" + m_d);

        // Reset for static tests
        m_a = Factory.buildMatrix(new double[][]{{1,0},{0,1}});
        m_b = Factory.buildMatrix(new double[][]{{2,3},{4,5}});

        // 28. add (static)
        Matrix sum_static_m = Tensors.add(m_a, m_b);
        System.out.println("Matrix.add(m_a, m_b):\n" + sum_static_m + "\n(m_a unchanged:\n" + m_a + ")");

        // 29. multiply (static)
        Matrix prod_static_m = Tensors.multiply(m_a, m_b); // I * m_b = m_b
        System.out.println("Matrix.multiply(m_a (I), m_b):\n" + prod_static_m + "\n(m_a unchanged:\n" + m_a + ")");

        Matrix m1_mult = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 2x2
        Matrix m2_mult = Factory.buildMatrix(new double[][]{{2,0,1},{0,3,0}}); // 2x3
        // Expected: [[2,6,1],[6,12,3]]
        Matrix prod_static_m2 = Tensors.multiply(m1_mult, m2_mult);
        System.out.println("Matrix.multiply(m1_mult (2x2), m2_mult (2x3)) (result 2x3):\n" + prod_static_m2);
    }

    public static void testMatrixAdvancedFunctions() {
        printTitle("Matrix Advanced Functions (32-44)");
        Matrix mat1 = Factory.buildMatrix(new double[][]{{1,2},{3,4}});
        Matrix mat2 = Factory.buildMatrix(new double[][]{{5,6},{7,8}});
        Matrix mat_rect = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6}}); //2x3

        // 32. hstack (non-static, returns new)
        Matrix hstacked = mat1.hstack(mat2); // [[1,2,5,6],[3,4,7,8]]
        System.out.println("mat1.hstack(mat2):\n" + hstacked);

        // 33. vstack (non-static, returns new)
        Matrix vstacked = mat1.vstack(mat2); // [[1,2],[3,4],[5,6],[7,8]]
        System.out.println("mat1.vstack(mat2):\n" + vstacked);

        // 34. getRow
        System.out.println("mat_rect.getRow(0): " + mat_rect.getRow(0)); // [1,2,3]

        // 35. getCol
        System.out.println("mat_rect.getCol(1): " + mat_rect.getCol(1)); // [2,5]' (as vector)

        // 36. subMatrix
        Matrix sub = mat_rect.subMatrix(0,0,1,2); // row 0, col 1-2 -> [[2,3]]
        System.out.println("mat_rect.subMatrix(0,0,1,2):\n" + sub);

        // 37. minor
        Matrix m_for_minor = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}});
        Matrix minor_00 = m_for_minor.minor(0,0); // exclude row 0, col 0 -> [[5,6],[8,9]]
        System.out.println("m_for_minor.minor(0,0):\n" + minor_00);

        // 38. transpose
        System.out.println("mat_rect original:\n" + mat_rect);
        System.out.println("mat_rect.transpose():\n" + mat_rect.transpose()); // 3x2

        Matrix zero_row_mat = Factory.buildMatrix(new double[0][2]);
        System.out.println("zero_row_mat (0x2):\n" + zero_row_mat);
        System.out.println("zero_row_mat.transpose() (2x0):\n" + zero_row_mat.transpose() + " size: " + Arrays.toString(zero_row_mat.transpose().getSize()));


        // 39. trace
        System.out.println("mat1.trace() (1+4=5): " + mat1.trace());

        // 40. isSquare
        System.out.println("mat1.isSquare() (2x2): " + mat1.isSquare()); // true
        System.out.println("mat_rect.isSquare() (2x3): " + mat_rect.isSquare()); // false
        System.out.println("Factory.buildMatrix(0).isSquare() (0x0): " + Factory.buildMatrix(0).isSquare()); // true

        // 41. isUpperTriangular
        Matrix upper_tri = Factory.buildMatrix(new double[][]{{1,2,3},{0,4,5},{0,0,6}});
        Matrix not_upper_tri = Factory.buildMatrix(new double[][]{{1,2,3},{1,4,5},{0,0,6}});
        System.out.println("upper_tri.isUpperTriangular(): " + upper_tri.isUpperTriangular()); // true
        System.out.println("not_upper_tri.isUpperTriangular(): " + not_upper_tri.isUpperTriangular()); // false
        System.out.println("mat_rect.isUpperTriangular(): " + mat_rect.isUpperTriangular()); // false (not square)
        System.out.println("Factory.buildMatrix(0).isUpperTriangular() (0x0): " + Factory.buildMatrix(0).isUpperTriangular()); // true

        // 42. isLowerTriangular
        Matrix lower_tri = Factory.buildMatrix(new double[][]{{1,0,0},{2,3,0},{4,5,6}});
        Matrix not_lower_tri = Factory.buildMatrix(new double[][]{{1,0,1},{2,3,0},{4,5,6}});
        System.out.println("lower_tri.isLowerTriangular(): " + lower_tri.isLowerTriangular()); // true
        System.out.println("not_lower_tri.isLowerTriangular(): " + not_lower_tri.isLowerTriangular()); // false
        System.out.println("Factory.buildMatrix(0).isLowerTriangular() (0x0): " + Factory.buildMatrix(0).isLowerTriangular()); // true


        // 43. isIdentity
        Matrix iden = Factory.buildMatrix(2);
        System.out.println("iden (2x2).isIdentity(): " + iden.isIdentity()); // true
        System.out.println("mat1.isIdentity(): " + mat1.isIdentity()); // false
        System.out.println("Factory.buildMatrix(0).isIdentity() (0x0): " + Factory.buildMatrix(0).isIdentity()); // true

        // 44. isZeroMatrix
        Matrix zero_m = Factory.buildMatrix(2,3,0.0);
        System.out.println("zero_m.isZeroMatrix(): " + zero_m.isZeroMatrix()); // true
        System.out.println("mat1.isZeroMatrix(): " + mat1.isZeroMatrix()); // false
        System.out.println("Factory.buildMatrix(0,2,0.0).isZeroMatrix() (0x2): " + Factory.buildMatrix(0,2,0.0).isZeroMatrix()); // true

    }

    public static void testMatrixElementaryOperations() {
        printTitle("Matrix Elementary Row/Column Operations (45-50)");
        Matrix mat_ops = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}});
        System.out.println("Original mat_ops:\n" + mat_ops);

        // 45. swapRows
        mat_ops.swapRows(0,1);
        System.out.println("After swapRows(0,1):\n" + mat_ops); // [[4,5,6],[1,2,3],[7,8,9]]
        mat_ops.swapRows(0,1); // swap back

        // 46. swapCols
        mat_ops.swapCols(0,2);
        System.out.println("After swapCols(0,2):\n" + mat_ops); // [[3,2,1],[6,5,4],[9,8,7]]
        mat_ops.swapCols(0,2); // swap back

        // 47. multiplyRow
        mat_ops.multiplyRow(0, Factory.buildScalar("2"));
        System.out.println("After multiplyRow(0, Scalar(2)):\n" + mat_ops); // [[2,4,6],[4,5,6],[7,8,9]]
        mat_ops.multiplyRow(0, Factory.buildScalar("0.5")); // undo

        // 48. multiplyCol
        mat_ops.multiplyCol(1, Factory.buildScalar("3"));
        System.out.println("After multiplyCol(1, Scalar(3)):\n" + mat_ops); // [[1,6,3],[4,15,6],[7,24,9]]
        mat_ops.multiplyCol(1, Factory.buildScalar(BigDecimal.ONE.divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP).toString())); // undo, careful with precision


        // 49. addScaledRow (R1 = R1 + 2*R0) where R0 is current [1,2,3]
        // mat_ops is currently back to original approx.
        mat_ops = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // reset
        mat_ops.addScaledRow(1, 0, Factory.buildScalar("2")); // R1 = R1 + 2*R0
        // R1 = [4,5,6] + 2*[1,2,3] = [4,5,6] + [2,4,6] = [6,9,12]
        System.out.println("After addScaledRow(1,0, Scalar(2)) (R1=R1+2R0):\n" + mat_ops);
        // Expected: [[1,2,3],[6,9,12],[7,8,9]]

        // 50. addScaledCol (C1 = C1 + (-1)*C2)
        mat_ops = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // reset
        mat_ops.addScaledCol(1, 2, Factory.buildScalar("-1")); // C1 = C1 - C2
        // C1 = [2,5,8]' - [3,6,9]' = [-1,-1,-1]'
        System.out.println("After addScaledCol(1,2, Scalar(-1)) (C1=C1-C2):\n" + mat_ops);
        // Expected: [[1,-1,3],[4,-1,6],[7,-1,9]]
    }


    public static void testMatrixRREFDeterminantInverse() {
        printTitle("Matrix RREF, Determinant, Inverse (51-54)");

        Matrix m_rref_test = Factory.buildMatrix(new double[][]{{1,2,-1,-4},{2,3,-1,-11},{-2,0,-3,22}});
        // RREF of above is [[1,0,0,-8],[0,1,0,1],[0,0,1,-2]] (if my math is right)
        // Or with some fractions if using exact arithmetic
        System.out.println("Matrix for RREF test:\n" + m_rref_test);
        Matrix rref_m = m_rref_test.rref();
        System.out.println("RREF of m_rref_test:\n" + rref_m);

        // 52. isRREF
        System.out.println("m_rref_test.isRREF(): " + m_rref_test.isRREF()); // false (likely)
        System.out.println("rref_m.isRREF(): " + rref_m.isRREF());       // true (should be)
        Matrix iden3 = Factory.buildMatrix(3);
        System.out.println("Identity(3).isRREF(): " + iden3.isRREF()); // true

        Matrix m_det_inv = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // det = 1*4 - 2*3 = 4 - 6 = -2
        // 53. determinant
        Scalar det = m_det_inv.determinant();
        System.out.println("Determinant of [[1,2],[3,4]]: " + det + " (Expected: -2)");

        Matrix m_det3 = Factory.buildMatrix(new double[][]{{6,1,1},{4,-2,5},{2,8,7}}); // Det = -306
        System.out.println("Determinant of 3x3 matrix [[6,1,1],[4,-2,5],[2,8,7]]: " + m_det3.determinant());


        // 54. inverse
        // inv of [[1,2],[3,4]] is (1/-2) * [[4,-2],[-3,1]] = [[-2,1],[1.5,-0.5]]
        try {
            Matrix inv_m = m_det_inv.inverse();
            System.out.println("Inverse of [[1,2],[3,4]]:\n" + inv_m);
            Matrix product_check = Tensors.multiply(m_det_inv, inv_m);
            System.out.println("Original * Inverse (should be Identity):\n" + product_check);
        } catch (SingularMatrixException e) {
            System.err.println("Error calculating inverse: " + e.getMessage());
        }

        Matrix singular_m = Factory.buildMatrix(new double[][]{{1,2},{2,4}}); // det = 0
        try {
            System.out.println("Attempting inverse of singular matrix [[1,2],[2,4]]:");
            singular_m.inverse();
        } catch (SingularMatrixException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        // Test 0x0 matrix determinant and inverse
        Matrix zero_dim_matrix = Factory.buildMatrix(0);
        System.out.println("Determinant of 0x0 matrix: " + zero_dim_matrix.determinant());
        try {
            Matrix inv_zero_dim = zero_dim_matrix.inverse();
            System.out.println("Inverse of 0x0 matrix:\n" + inv_zero_dim + " size: " + Arrays.toString(inv_zero_dim.getSize()));
        } catch (Exception e) {
            System.err.println("Error with 0x0 inverse: " + e.getMessage());
        }

    }

    public static void testTensorsStaticMethods() {
        printTitle("Tensors Static Utility Methods");
        Scalar ts1 = Factory.buildScalar("100");
        Scalar ts2 = Factory.buildScalar("25");
        System.out.println("Tensors.add(" + ts1 + ", " + ts2 + "): " + Tensors.add(ts1, ts2));
        System.out.println("Tensors.multiply(" + ts1 + ", " + ts2 + "): " + Tensors.multiply(ts1, ts2));

        Vector tv1 = Factory.buildVector(new double[]{1,2});
        Vector tv2 = Factory.buildVector(new double[]{3,4});
        System.out.println("Tensors.add(" + tv1 + ", " + tv2 + "): " + Tensors.add(tv1, tv2));
        System.out.println("Tensors.multiply(" + tv1 + ", " + ts1 + "): " + Tensors.multiply(tv1, ts1));
        System.out.println("Tensors.multiply(" + ts1 + ", " + tv1 + "): " + Tensors.multiply(ts1, tv1));


        Matrix tm1 = Factory.buildMatrix(new double[][]{{1,0},{0,1}});
        Matrix tm2 = Factory.buildMatrix(new double[][]{{5,6},{7,8}});
        System.out.println("Tensors.add(tm1, tm2):\n" + Tensors.add(tm1, tm2));
        System.out.println("Tensors.multiply(tm1, tm2):\n" + Tensors.multiply(tm1, tm2));

        Matrix tm3 = Factory.buildMatrix(new double[][]{{1},{2}}); // 2x1
        Matrix tm4 = Factory.buildMatrix(new double[][]{{3},{4}}); // 2x1
        System.out.println("Tensors.hstack(tm3, tm4):\n" + Tensors.hstack(tm3, tm4));

        Matrix tm5 = Factory.buildMatrix(new double[][]{{1,2}}); // 1x2
        Matrix tm6 = Factory.buildMatrix(new double[][]{{3,4}}); // 1x2
        System.out.println("Tensors.vstack(tm5, tm6):\n" + Tensors.vstack(tm5, tm6));

        System.out.println("Tensors.identity(3):\n" + Tensors.identity(3));
        System.out.println("Tensors.zeros(2,3):\n" + Tensors.zeros(2,3));
        System.out.println("Tensors.ones(3,2):\n" + Tensors.ones(3,2));
        System.out.println("Tensors.filled(2,2, Factory.buildScalar(\"7\")):\n" + Tensors.filled(2,2, Factory.buildScalar("7")));
    }

    public static void testExceptionHandling() {
        printTitle("Exception Handling Tests");

        // Scalar creation
        try {
            System.out.print("Test: Factory.buildScalar(\"abc\") -> ");
            Factory.buildScalar("abc");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("Test: Factory.buildScalar(5,2) -> "); // ii >= jj
            Factory.buildScalar(5,2);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }


        // Vector dimension mismatch
        Vector v_exc1 = Factory.buildVector(2, 1.0);
        Vector v_exc2 = Factory.buildVector(3, 1.0);
        try {
            System.out.print("Test: v_exc1.add(v_exc2) (size mismatch) -> ");
            v_exc1.add(v_exc2);
        } catch (DimensionMismatchException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // Matrix dimension mismatch for add
        Matrix m_exc1 = Factory.buildMatrix(2, 2, 1.0);
        Matrix m_exc2 = Factory.buildMatrix(2, 3, 1.0);
        try {
            System.out.print("Test: m_exc1.add(m_exc2) (dim mismatch) -> ");
            m_exc1.add(m_exc2);
        } catch (DimensionMismatchException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // Matrix dimension mismatch for multiply
        Matrix m_exc3 = Factory.buildMatrix(2, 3, 1.0); // 2x3
        Matrix m_exc4 = Factory.buildMatrix(2, 2, 1.0); // 2x2, incompatible (3 != 2)
        try {
            System.out.print("Test: m_exc3.multiply(m_exc4) (dim mismatch) -> ");
            m_exc3.multiplyRight(m_exc4);
        } catch (DimensionMismatchException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // Index out of bounds
        try {
            System.out.print("Test: v_exc1.viewElement(5) -> ");
            v_exc1.viewElement(5);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("Test: m_exc1.viewElement(5,5) -> ");
            m_exc1.viewElement(5,5);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // CSV Parse Exception
        String badCsvFile = "bad_test_matrix.csv";
        try {
            createTestCsvFile(badCsvFile, new String[]{"1,2,3", "4,not_a_number,6"});
            System.out.print("Test: Factory.buildMatrix from bad CSV -> ");
            Factory.buildMatrix(badCsvFile);
        } catch (CsvParseException | IOException e) { // IOException for file creation
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            createTestCsvFile(badCsvFile, new String[]{"1,2,3", "4,5"}); // Inconsistent columns
            System.out.print("Test: Factory.buildMatrix from inconsistent CSV -> ");
            Factory.buildMatrix(badCsvFile);
        } catch (CsvParseException | IOException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }


        // NotSquareMatrixException for trace, determinant, inverse
        Matrix rect_m_exc = Factory.buildMatrix(2,3,1.0);
        try {
            System.out.print("Test: rect_m_exc.trace() -> ");
            rect_m_exc.trace();
        } catch (NotSquareMatrixException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("Test: rect_m_exc.determinant() -> ");
            rect_m_exc.determinant();
        } catch (NotSquareMatrixException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("Test: rect_m_exc.inverse() -> ");
            rect_m_exc.inverse();
        } catch (NotSquareMatrixException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // SingularMatrixException for inverse
        Matrix singular_m_exc = Factory.buildMatrix(new double[][]{{1,1},{1,1}});
        try {
            System.out.print("Test: singular_m_exc.inverse() -> ");
            singular_m_exc.inverse();
        } catch (SingularMatrixException e) {
            System.out.println("Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}
