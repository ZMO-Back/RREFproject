package test;

import tensor.*; // 모든 tensor 패키지 클래스 임포트
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.io.FileWriter; // CSV 테스트용
import java.io.IOException;  // CSV 테스트용
import java.io.PrintWriter; // CSV 테스트용

public class NNewTest {

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
        testMatrixAdvancedFunctions();
        testMatrixElementaryOperations();
        testMatrixRREFDeterminantInverse();

        testTensorsStaticMethods();
        testExceptionHandling();

        System.out.println("\nTensor Library Test Suite - COMPLETED");
    }

    public static void testScalarCreation() {
        printTitle("Scalar Creation (01, 02)");
        // Test.java 요구사항: Factory를 이용하여 스칼라 객체를 얻어낸다.
        // Test.java 요구사항: 스칼라 객체의 참조 타입은 인터페이스 타입만을 사용한다.
        Scalar s1 = Factory.buildScalar("3.14159"); // 명세 01: 값을 지정하여 스칼라 생성
        // 명세 01: 값을 지정하여 스칼라 생성, 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 01, 14s 값을 지정하여 스칼라 생성, 출력 : s1 (from String '3.14159'): " + s1);

        Scalar s2 = Factory.buildScalar(0.0, 1.0); // 명세 02: i 이상 j 미만의 무작위 값을 요소로 하는 스칼라 생성 
        // 명세 02: i 이상 j 미만의 무작위 값을 요소로 하는 스칼라 생성, 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 02, 14s i 이상 j 미만의 무작위 값을 요소로 하는 스칼라 생성, 출력 : s2 (random 0.0 to 1.0): " + s2);
        Scalar s3 = Factory.buildScalar(-5.0, -4.0); // 명세 02: i 이상 j 미만의 무작위 값을 요소로 하는 스칼라 생성
        // 명세 02: i 이상 j 미만의 무작위 값을 요소로 하는 스칼라 생성, 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 02, 14s i 이상 j 미만의 무작위 값을 요소로 하는 스칼라 생성, 출력 : s3 (random -5.0 to -4.0): " + s3);
    }

    public static void testScalarBasicFunctions() {
        printTitle("Scalar Basic Functions (12, 14s, 15s, 16, 17s)");
        Scalar s_a = Factory.buildScalar("10.5"); // 명세 01: 값을 지정하여 스칼라 생성 
        Scalar s_b = Factory.buildScalar("20.0"); // 명세 01: 값을 지정하여 스칼라 생성
        Scalar s_a_copy = Factory.buildScalar("10.5"); // 명세 01: 값을 지정하여 스칼라 생성

        // 명세 12: (only 스칼라) 값을 지정/조회할 수 있다.
        System.out.println("명세 12 (only 스칼라) 값 조회 : s_a.getValue(): " + s_a.getValue());
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14s 스칼라 출력 : s_b before setValue: " + s_b);
        s_b.setValue(new BigDecimal("25.5")); // 명세 12: (only 스칼라) 값을 지정/조회할 수 있다. 
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 12, 14s (only 스칼라) 값 지정 후 출력 : s_b after setValue(25.5): " + s_b); 

        // 명세 15s: (@Override equals()) 객체의 동등성 판단
        System.out.println("명세 15s (@Override equals()) 객체의 동등성 판단 : s_a.equals(s_a_copy) (10.5 == 10.5): " + s_a.equals(s_a_copy)); 
        // 명세 15s: (@Override equals()) 객체의 동등성 판단
        System.out.println("명세 15s (@Override equals()) 객체의 동등성 판단 : s_a.equals(s_b) (10.5 == 25.5): " + s_a.equals(s_b)); 

        // 명세 16 (implements Comparable) 스칼라의 경우 값의 대소 비교
        System.out.println("명세 16 (implements Comparable) 스칼라 값 대소 비교 : s_a.compareTo(s_b) (10.5 vs 25.5): " + s_a.compareTo(s_b)); 
        // 명세 16 (implements Comparable) 스칼라의 경우 값의 대소 비교
        System.out.println("명세 16 (implements Comparable) 스칼라 값 대소 비교 : s_b.compareTo(s_a) (25.5 vs 10.5): " + s_b.compareTo(s_a)); 
        // 명세 16 (implements Comparable) 스칼라의 경우 값의 대소 비교
        System.out.println("명세 16 (implements Comparable) 스칼라 값 대소 비교 : s_a.compareTo(s_a_copy) (10.5 vs 10.5): " + s_a.compareTo(s_a_copy)); 

        // 명세 17s: (@Override clone()) 객체 복제 (deep copy)
        Scalar s_a_cloned = s_a.clone(); 
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 17s, 14s (@Override clone()) 객체 복제 후 출력 : s_a: " + s_a + ", s_a_cloned: " + s_a_cloned); 
        System.out.println("명세 17s (@Override clone()) 객체 복제 (동일 객체 여부) : s_a == s_a_cloned (identity): " + (s_a == s_a_cloned)); 
        // 명세 15s: (@Override equals()) 객체의 동등성 판단
        System.out.println("명세 17s, 15s (@Override clone()) 객체 복제 (동등성 판단) : s_a.equals(s_a_cloned) (equality): " + s_a.equals(s_a_cloned)); 
        s_a_cloned.setValue(new BigDecimal("100")); // 명세 12: (only 스칼라) 값을 지정/조회할 수 있다. (복제된 객체에) 
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 17s, 12, 14s (@Override clone()) 복제 객체 수정 후 원본/복제본 출력 (원본 불변 확인) : After s_a_cloned.setValue(100) -> s_a: " + s_a + ", s_a_cloned: " + s_a_cloned); 
    }

    public static void testScalarOperations() {
        printTitle("Scalar Operations (18, 19 - non-static, 24, 25 - static)"); 
        Scalar s_x = Factory.buildScalar("5"); // 명세 01: 값을 지정하여 스칼라 생성 
        Scalar s_y = Factory.buildScalar("3"); // 명세 01: 값을 지정하여 스칼라 생성 
        Scalar s_z = Factory.buildScalar("2"); // 명세 01: 값을 지정하여 스칼라 생성 

        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14s 스칼라 출력 : s_x original: " + s_x); 
        s_x.add(s_y); // 명세 18: 스칼라는 다른 스칼라와 덧셈이 가능하다 (non-static, modifies self) 
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 18, 14s 스칼라 덧셈(non-static) 후 출력 : s_x after s_x.add(s_y (3)): " + s_x); 

        s_x.multiply(s_z); // 명세 19: 스칼라는 다른 스칼라와 곱셈이 가능하다 (non-static, modifies self) 
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 19, 14s 스칼라 곱셈(non-static) 후 출력 : s_x after s_x.multiply(s_z (2)): " + s_x); 

        // Reset for static tests
        s_x = Factory.buildScalar("10"); // 명세 01: 값을 지정하여 스칼라 생성 
        s_y = Factory.buildScalar("4"); // 명세 01: 값을 지정하여 스칼라 생성 

        // 명세 24: (Tensors) 전달받은 두 스칼라의 덧셈이 가능하다 (static, returns new)
        Scalar sum_static = Tensors.add(s_x, s_y); 
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 24, 14s (Tensors) 두 스칼라 덧셈(static) 결과 출력 : Tensors.add(s_x (10), s_y (4)): " + sum_static + " (s_x: " + s_x + ", s_y: " + s_y + ")"); 

        // 명세 25: (Tensors) 전달받은 두 스칼라의 곱셈이 가능하다 (static, returns new)
        Scalar prod_static = Tensors.multiply(s_x, s_y); 
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 25, 14s (Tensors) 두 스칼라 곱셈(static) 결과 출력 : Tensors.multiply(s_x (10), s_y (4)): " + prod_static + " (s_x: " + s_x + ", s_y: " + s_y + ")"); 
    }


    public static void testVectorCreation() {
        printTitle("Vector Creation (03, 04, 05)"); 
        // Test.java 요구사항: Factory를 이용하여 벡터 객체를 얻어낸다.
        // Test.java 요구사항: 벡터 객체의 참조 타입은 인터페이스 타입만을 사용한다.
        Vector v1 = Factory.buildVector(3, 7.7); // 명세 03: 지정한 하나의 값을 모든 요소의 값으로 하는 n-차원 벡터 생성 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 03, 14v 지정 값 벡터 생성, 출력 : v1 (3 elements, value 7.7): " + v1); 

        Vector v2 = Factory.buildVector(4, 10.0, 20.0); // 명세 04: i 이상 j 미만의 무작위 값을 요소로 하는 n-차원 벡터 생성 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 04, 14v 무작위 값 벡터 생성, 출력 : v2 (4 elements, random 10.0-20.0): " + v2); 

        double[] arr_d = {1.1, 2.2, 3.3}; 
        Vector v3 = Factory.buildVector(arr_d); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 05, 14v 1차원 배열 벡터 생성, 출력 : v3 (from double[] {1.1, 2.2, 3.3}): " + v3); 

        Scalar[] arr_s = {Factory.buildScalar("100"), Factory.buildScalar("200")}; 
        Vector v4 = Factory.buildVector(arr_s); // Factory.buildVector(Scalar[]) 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14v (Scalar 배열 기반) 벡터 출력 : v4 (from Scalar[] {100, 200}): " + v4); 
    }

    public static void testVectorBasicFunctions() {
        printTitle("Vector Basic Functions (11v, 13, 14v, 15v, 17v)"); 
        Vector vec = Factory.buildVector(new double[]{1.0, 2.0, 3.0}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        Vector vec_copy_val = Factory.buildVector(new double[]{1.0, 2.0, 3.0}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        Vector vec_other = Factory.buildVector(new double[]{1.0, 2.0, 4.0}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 

        // 명세 13: (벡터) 차원(길이) 조회
        System.out.println("명세 13 (벡터) 차원(길이) 조회 : vec.getSize(): " + vec.getSize()); 

        // 명세 11v: (only 벡터) 특정 위치의 요소를 지정/조회할 수 있다.
        System.out.println("명세 11v (only 벡터) 특정 위치 요소 조회 : vec.viewElement(1): " + vec.viewElement(1)); 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14v 벡터 출력 (setElement 전) : vec before setElement(1, 99.0): " + vec); 
        vec.setElement(1, Factory.buildScalar("99.0")); // 명세 11v: (only 벡터) 특정 위치의 요소를 지정/조회할 수 있다. 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 11v, 14v (only 벡터) 특정 위치 요소 지정 후 출력 : vec after setElement(1, 99.0): " + vec); 
        // 명세 11v: (only 벡터) 특정 위치의 요소를 지정/조회할 수 있다.
        System.out.println("명세 11v (only 벡터) 특정 위치 요소 조회 (set 후) : vec.viewElement(1) after set: " + vec.viewElement(1)); 

        // 명세 15v: (@Override equals()) 객체의 동등성 판단
        vec.setElement(1, Factory.buildScalar("2.0")); // Reset for equals test (11v) 
        System.out.println("명세 15v (@Override equals()) 벡터 객체 동등성 판단 : vec.equals(vec_copy_val) ([1,2, == [1,2,): " + vec.equals(vec_copy_val)); 
        System.out.println("명세 15v (@Override equals()) 벡터 객체 동등성 판단 : vec.equals(vec_other) ([1,2, == [1,2,): " + vec.equals(vec_other)); 
        Vector vec_diff_size = Factory.buildVector(new double[]{1.0, 2.0}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        System.out.println("명세 15v (@Override equals()) 벡터 객체 동등성 판단 (크기 다름) : vec.equals(vec_diff_size) (size mismatch): " + vec.equals(vec_diff_size)); 


        // 명세 17v: (@Override clone()) 객체 복제 (deep copy)
        Vector vec_cloned = vec.clone(); 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 17v, 14v (@Override clone()) 벡터 객체 복제 후 출력 : vec: " + vec + ", vec_cloned: " + vec_cloned); 
        System.out.println("명세 17v (@Override clone()) 벡터 객체 복제 (동일 객체 여부) : vec == vec_cloned (identity): " + (vec == vec_cloned)); 
        // 명세 15v: (@Override equals()) 객체의 동등성 판단
        System.out.println("명세 17v, 15v (@Override clone()) 벡터 객체 복제 (동등성 판단) : vec.equals(vec_cloned) (equality): " + vec.equals(vec_cloned)); 
        vec_cloned.setElement(0, Factory.buildScalar("777")); // 명세 11v: (only 벡터) 특정 위치의 요소를 지정/조회할 수 있다. (복제된 객체에) 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 17v, 11v, 14v (@Override clone()) 복제 객체 수정 후 원본/복제본 출력 (원본 불변 확인) : After vec_cloned.setElement(0, 777) -> vec: " + vec + ", vec_cloned: " + vec_cloned); 
    }


    public static void testVectorOperations() {
        printTitle("Vector Operations (20, 21 - non-static, 26, 27 - static)"); 
        Vector v_a = Factory.buildVector(new double[]{1, 2, 3}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        Vector v_b = Factory.buildVector(new double[]{4, 5, 6}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        Scalar scal = Factory.buildScalar("3"); // 명세 01: 값을 지정하여 스칼라 생성 

        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14v 벡터 출력 : v_a original: " + v_a); 
        v_a.add(v_b); // 명세 20: 벡터는 다른 벡터와 덧셈이 가능하다 (non-static, modifies self) 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 20, 14v 벡터 덧셈(non-static) 후 출력 : v_a after v_a.add(v_b {4,5,6}): " + v_a); 

        v_a.multiply(scal); // 명세 21: 벡터는 다른 스칼라와 곱셈이 가능하다 (non-static, modifies self) 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 21, 14v 벡터 스칼라 곱셈(non-static) 후 출력 : v_a after v_a.multiply(scal {3}): " + v_a); 

        // Reset for static tests
        v_a = Factory.buildVector(new double[]{10, 20}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        v_b = Factory.buildVector(new double[]{3, 7}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        scal = Factory.buildScalar("2"); // 명세 01: 값을 지정하여 스칼라 생성 

        // 명세 26: (Tensors) 전달받은 두 벡터의 덧셈이 가능하다 (static, returns new)
        Vector sum_static_v = Tensors.add(v_a, v_b); 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 26, 14v (Tensors) 두 벡터 덧셈(static) 결과 출력 : Tensors.add(v_a {10,20}, v_b {3,7}): " + sum_static_v + " (v_a: " + v_a + ")"); 

        // 명세 27: (Tensors) 전달받은 스칼라와 벡터의 곱셈이 가능하다 (static, returns new)
        Vector prod_static_v = Tensors.multiply(v_a, scal); 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 27, 14v (Tensors) 스칼라와 벡터 곱셈(static) 결과 출력 : Tensors.multiply(v_a {10,20}, scal {2}): " + prod_static_v + " (v_a: " + v_a + ")"); 
    }

    public static void testVectorAdvancedFunctions() {
        printTitle("Vector Advanced Functions (30, 31)"); 
        Vector vec_adv = Factory.buildVector(new double[]{10, 20, 30}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14v 벡터 출력 : Original vector vec_adv: " + vec_adv); 

        // 명세 31: n-차원 벡터 객체는 자신으로부터 1xn 행렬(행벡터)을 생성하여 반환할 수 있다.
        Matrix rowMatrix = vec_adv.toRowMatrix(); 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 31, 14m 벡터 -> 행행렬 변환 후 출력 : vec_adv.toRowMatrix(): \n" + rowMatrix); 
        // 명세 13: (행렬) 크기(행 개수, 열 개수) 조회
        System.out.println("명세 13 (행렬) 크기 조회 : Row matrix size: " + Arrays.toString(rowMatrix.getSize())); 

        // 명세 30: n-차원 벡터 객체는 자신으로부터 nx1 행렬(열벡터)을 생성하여 반환할 수 있다.
        Matrix colMatrix = vec_adv.toColMatrix(); 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 30, 14m 벡터 -> 열행렬 변환 후 출력 : vec_adv.toColMatrix(): \n" + colMatrix); 
        // 명세 13: (행렬) 크기(행 개수, 열 개수) 조회
        System.out.println("명세 13 (행렬) 크기 조회 : Column matrix size: " + Arrays.toString(colMatrix.getSize())); 
    }


    public static void testMatrixCreation() {
        printTitle("Matrix Creation (06, 07, 08, 09, 10)"); 
        // Test.java 요구사항: Factory를 이용하여 행렬 객체를 얻어낸다.
        // Test.java 요구사항: 행렬 객체의 참조 타입은 인터페이스 타입만을 사용한다.
        Matrix m1 = Factory.buildMatrix(2, 3, 1.0); // 명세 06: 지정한 하나의 값을 모든 요소의 값으로 하는 m x n 행렬 생성 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 06, 14m 지정 값 행렬 생성, 출력 : m1 (2x3, value 1.0):\n" + m1); 

        Matrix m2 = Factory.buildMatrix(3, 2, 0.0, 10.0); // 명세 07: i 이상 j 미만의 무작위 값을 요소로 하는 m x n 행렬 생성 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 07, 14m 무작위 값 행렬 생성, 출력 : m2 (3x2, random 0-10):\n" + m2); 

        String csvFile = "test_matrix.csv"; 
        try {
            createTestCsvFile(csvFile, new String[]{"1,2,3", "4,5,6", "7,8,9.5"}); 
            Matrix m3 = Factory.buildMatrix(csvFile); // 명세 08: csv 파일로부터 m x n 행렬 생성 
            // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
            System.out.println("명세 08, 14m CSV 파일 행렬 생성, 출력 : m3 (from CSV '" + csvFile + "'):\n" + m3); 
        } catch (IOException | CsvParseException e) {
            System.err.println("Error creating or parsing CSV for m3: " + e.getMessage()); 
        }

        String emptyCsvFile = "empty_test_matrix.csv"; 
        try {
            createTestCsvFile(emptyCsvFile, new String[]{}); 
            // Matrix m_empty_csv = Factory.buildMatrix(emptyCsvFile); // 명세 08: csv 파일로부터 m x n 행렬 생성 (빈 파일 케이스) 
            // System.out.println("명세 08, 13, 14m 빈 CSV 파일 행렬 생성, 크기 및 내용 출력 : m_empty_csv (from empty CSV '" + emptyCsvFile + "') (size " + Arrays.toString(m_empty_csv.getSize()) + "):\n" + m_empty_csv); 

            createTestCsvFile(emptyCsvFile, new String[]{",,"}); 
            Matrix m_commas_csv = Factory.buildMatrix(emptyCsvFile); // 명세 08: csv 파일로부터 m x n 행렬 생성 (콤마만 있는 케이스) 
            // 명세 13: (행렬) 크기(행 개수, 열 개수) 조회, 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
            System.out.println("명세 08, 13, 14m CSV (콤마만) 행렬 생성, 크기 및 내용 출력 : m_commas_csv (from CSV with ' ,, ') (size " + Arrays.toString(m_commas_csv.getSize()) + "):\n" + m_commas_csv); 
        } catch (IOException | CsvParseException e) {
            System.err.println("Error with empty CSV tests: " + e.getMessage()); 
        }

        double[][] arr_2d = {{1.1, 1.2}, {2.1, 2.2}, {3.1, 3.2}}; 
        Matrix m4 = Factory.buildMatrix(arr_2d); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 09, 14m 2차원 배열 행렬 생성, 출력 : m4 (from double[[):\n" + m4); 

        Matrix m5_identity = Factory.buildMatrix(3); // 명세 10: 단위 행렬 생성 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 10, 14m 단위 행렬 생성, 출력 : m5_identity (3x3):\n" + m5_identity); 

        // Removed 0x0 identity matrix creation.
        // Matrix m0_identity = Factory.buildMatrix(0); // 명세 10: 단위 행렬 생성 (0차원) 
        // System.out.println("명세 10, 13, 14m 0x0 단위 행렬 생성, 크기 및 내용 출력 : m0_identity (0x0):\n" + m0_identity + " size: " + Arrays.toString(m0_identity.getSize())); 

        Scalar[][] scalar_arr_2d = {
                {Factory.buildScalar("10"), Factory.buildScalar("20")},
                {Factory.buildScalar("30"), Factory.buildScalar("40")}
        }; 
        Matrix m6 = Factory.buildMatrix(scalar_arr_2d); // Factory.buildMatrix(Scalar[][]) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14m (Scalar 배열 기반) 행렬 출력 : m6 (from Scalar[[):\n" + m6); 

        // Removed 0x0 matrix from array.
        // Matrix m_empty_arr = Factory.buildMatrix(new double[[); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (0x0 배열) 
        // System.out.println("명세 09, 13, 14m 0x0 배열 행렬 생성, 크기 및 내용 출력 : m_empty_arr (from new double[[):\n" + m_empty_arr + " size: " + Arrays.toString(m_empty_arr.getSize())); 

        // Removed 0xN matrix from array.
        // Matrix m_empty_rows_arr = Factory.buildMatrix(new double[[); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (0xN 배열) 
        // System.out.println("명세 09, 13, 14m 0xN 배열 행렬 생성, 크기 및 내용 출력 : m_empty_rows_arr (from new double[[):\n" + m_empty_rows_arr + " size: " + Arrays.toString(m_empty_rows_arr.getSize())); 
    }

    public static void testMatrixBasicFunctions() {
        printTitle("Matrix Basic Functions (11m, 13, 14m, 15m, 17m)"); 
        Matrix mat = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix mat_copy_val = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix mat_other_val = Factory.buildMatrix(new double[][]{{1,2},{3,5}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다. (디버깅용 추가 출력)
        System.out.println("명세 14m 행렬 출력 (mat 초기값) : mat"+mat); 
        System.out.println("명세 14m 행렬 출력 (mat_copy_val 초기값) : mat2"+mat_copy_val); 
        System.out.println("명세 14m 행렬 출력 (mat_other_val 초기값) : mat3"+mat_other_val); 
        // 명세 13: (행렬) 크기(행 개수, 열 개수) 조회
        System.out.println("명세 13 (행렬) 크기 조회 : mat.getSize(): " + Arrays.toString(mat.getSize())); 

        // 명세 11m: (only 행렬) 특정 위치의 요소를 지정/조회할 수 있다.
        System.out.println("명세 11m (only 행렬) 특정 위치 요소 조회 : mat.viewElement(1,0): " + mat.viewElement(1,0)); 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14m 행렬 출력 (setElement 전) : mat before setElement(1,0, Factory.buildScalar(\"99\")):\n" + mat); 
        mat.setElement(1,0, Factory.buildScalar("99")); // 명세 11m: (only 행렬) 특정 위치의 요소를 지정/조회할 수 있다. 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 11m, 14m (only 행렬) 특정 위치 요소 지정 후 출력 : mat after setElement(1,0, Factory.buildScalar(\"99\")):\n" + mat); 
        // 명세 11m: (only 행렬) 특정 위치의 요소를 지정/조회할 수 있다.
        System.out.println("명세 11m (only 행렬) 특정 위치 요소 조회 (set 후) : mat.viewElement(1,0) after set: " + mat.viewElement(1,0)); 

        // 명세 15m: (@Override equals()) 객체의 동등성 판단
        mat.setElement(1,0, Factory.buildScalar("3")); // Reset for equals (11m) 
        System.out.println("명세 15m (@Override equals()) 행렬 객체 동등성 판단 : mat.equals(mat_copy_val): " + mat.equals(mat_copy_val)); 
        System.out.println("명세 15m (@Override equals()) 행렬 객체 동등성 판단 : mat.equals(mat_other_val): " + mat.equals(mat_other_val)); 
        Matrix mat_diff_size = Factory.buildMatrix(new double[][]{{1,2}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        System.out.println("명세 15m (@Override equals()) 행렬 객체 동등성 판단 (크기 다름) : mat.equals(mat_diff_size) (size mismatch): " + mat.equals(mat_diff_size)); 


        // 명세 17m: (@Override clone()) 객체 복제 (deep copy)
        Matrix mat_cloned = mat.clone(); 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 17m, 14m (@Override clone()) 행렬 객체 복제 후 출력 : mat:\n" + mat + "mat_cloned:\n" + mat_cloned); 
        System.out.println("명세 17m (@Override clone()) 행렬 객체 복제 (동일 객체 여부) : mat == mat_cloned (identity): " + (mat == mat_cloned)); 
        // 명세 15m: (@Override equals()) 객체의 동등성 판단
        System.out.println("명세 17m, 15m (@Override clone()) 행렬 객체 복제 (동등성 판단) : mat.equals(mat_cloned) (equality): " + mat.equals(mat_cloned)); 
        mat_cloned.setElement(0,0, Factory.buildScalar("777")); // 명세 11m: (only 행렬) 특정 위치의 요소를 지정/조회할 수 있다. (복제된 객체에) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 17m, 11m, 14m (@Override clone()) 복제 객체 수정 후 원본/복제본 출력 (원본 불변 확인) : After mat_cloned.setElement(0,0, 777):\nmat:\n" + mat + "\nmat_cloned:\n" + mat_cloned); 
    }

    public static void testMatrixOperations() {
        printTitle("Matrix Operations (22, 23 - non-static, 28, 29 - static)"); 
        Matrix m_a = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix m_b = Factory.buildMatrix(new double[][]{{5,6},{7,8}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix m_c = Factory.buildMatrix(new double[][]{{1,0},{0,1}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (단위행렬 테스트용) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다. (디버깅용 추가 출력)
        System.out.println("명세 14m 행렬 출력 (m_a 초기값) : m_a"+m_a); 
        System.out.println("명세 14m 행렬 출력 (m_b 초기값) : m_b"+m_b); 
        System.out.println("명세 14m 행렬 출력 (m_c 단위행렬) : m_c Identity"+m_c); 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14m 행렬 출력 : m_a original:\n" + m_a); 
        m_a.add(m_b); // 명세 22: 행렬은 다른 행렬과 덧셈이 가능하다 (non-static, modifies self) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 22, 14m 행렬 덧셈(non-static) 후 출력 : m_a after m_a.add(m_b):\n" + m_a); 

        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14m 행렬 출력 (multiplyRight 전) : m_a before multiply by Identity:\n" + m_a); 
        m_a.multiplyRight(m_c); // 명세 23: 행렬은 다른 행렬과 곱셈이 가능하다 (non-static, this = this * other) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 23 (this*other), 14m 행렬 곱셈(non-static) 후 출력 : m_a after m_a.multiplyRight(Identity):\n" + m_a); 

        Matrix m_d = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (2x3) 
        Matrix m_e = Factory.buildMatrix(new double[][]{{7,8},{9,10},{11,12}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (3x2) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14m 행렬 출력 : m_d (2x3) original:\n" + m_d); 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14m 행렬 출력 : m_e (3x2) original:\n" + m_e); 
        m_d.multiplyRight(m_e); // 명세 23: 행렬은 다른 행렬과 곱셈이 가능하다 (non-static, this = this * other) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 23 (this*other), 14m 행렬 곱셈(non-static) 후 출력 (2x3 * 3x2 -> 2x2) : m_d after m_d.multiplyRight(m_e (3x2)) (result should be 2x2):\n" + m_d); 

        // 명세 23: 행렬은 다른 행렬과 곱셈이 가능하다 (non-static, this = other * this)
        m_d.multiplyLeft(m_d); // m_d is 2x2, m_d is 2x2 -> result 2x2 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 23 (other*this), 14m 행렬 자기 자신과 곱셈(non-static, left) 후 출력 : m_d mult by itself (left):\n" + m_d); 
        // m_e is 3x2, m_d becomes 2x2 after previous op. For multiplyLeft(m_e), m_e is 'other', m_d is 'this'.
        // So, this will be m_e(3x2) * m_d(2x2) -> result 3x2. 'this' (m_d) will be updated.
        Matrix m_d_before_left_mult_e = m_d.clone(); // For clarity
        m_d.multiplyLeft(m_e); // this(m_d) = other(m_e) * this(m_d_before_left_mult_e) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 23 (other*this), 14m 행렬 곱셈(non-static, left, 3x2 * 2x2 -> 3x2) 후 출력 : m_d after m_d.multiplyLeft(m_e (3x2)) (result 3x2):\n" + m_d); 

        // Reset for static tests
        m_a = Factory.buildMatrix(new double[][]{{1,0},{0,1}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        m_b = Factory.buildMatrix(new double[][]{{2,3},{4,5}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다. (디버깅용 추가 출력)
        System.out.println("명세 14m 행렬 출력 (m_a 재설정) : m_a"+m_a); 
        System.out.println("명세 14m 행렬 출력 (m_b 재설정) : m_b"+m_b); 
        // 명세 28: (Tensors) 전달받은 두 행렬의 덧셈이 가능하다 (static, returns new)
        Matrix sum_static_m = Tensors.add(m_a, m_b); 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 28, 14m (Tensors) 두 행렬 덧셈(static) 결과 출력 : Tensors.add(m_a, m_b):\n" + sum_static_m + "\n(m_a unchanged:\n" + m_a + ")"); 

        // 명세 29: (Tensors) 전달받은 두 행렬의 곱셈이 가능하다 (static, returns new)
        Matrix prod_static_m = Tensors.multiply(m_a, m_b); 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 29, 14m (Tensors) 두 행렬 곱셈(static) 결과 출력 : Tensors.multiply(m_a (I), m_b):\n" + prod_static_m + "\n(m_a unchanged:\n" + m_a + ")"); 

        Matrix m1_mult = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (2x2) 
        Matrix m2_mult = Factory.buildMatrix(new double[][]{{2,0,1},{0,3,0}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (2x3) 
        Matrix prod_static_m2 = Tensors.multiply(m1_mult, m2_mult); // 명세 29: (Tensors) 전달받은 두 행렬의 곱셈이 가능하다 (static, returns new) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 29, 14m (Tensors) 두 행렬 곱셈(static, 2x2 * 2x3 -> 2x3) 결과 출력 : Tensors.multiply(m1_mult (2x2), m2_mult (2x3)) (result 2x3):\n" + prod_static_m2); 
    }

    public static void testMatrixAdvancedFunctions() {
        printTitle("Matrix Advanced Functions (32-44)"); 
        Matrix mat1 = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix mat2 = Factory.buildMatrix(new double[][]{{5,6},{7,8}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix mat_rect = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (2x3) 

        Matrix hstacked = mat1.hstack(mat2); // 명세 32: 행렬은 다른 행렬과 가로로 합쳐질 수 있다 (non-static, returns new) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 32, 14m 가로 합치기(non-static) 후 출력 : mat1.hstack(mat2):\n" + hstacked); 

        Matrix vstacked = mat1.vstack(mat2); // 명세 33: 행렬은 다른 행렬과 세로로 합쳐질 수 있다 (non-static, returns new) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 33, 14m 세로 합치기(non-static) 후 출력 : mat1.vstack(mat2):\n" + vstacked); 

        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다. (getRow 결과가 Vector이므로)
        System.out.println("명세 34, 14v 특정 행 추출 후 출력 : mat_rect.getRow(0): " + mat_rect.getRow(0)); 

        // 명세 14v: 벡터 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다. (getCol 결과가 Vector이므로)
        System.out.println("명세 35, 14v 특정 열 추출 후 출력 : mat_rect.getCol(1): " + mat_rect.getCol(1)); 

        Matrix sub = mat_rect.subMatrix(0,0,1,2); // 명세 36: 행렬은 특정 범위의 부분 행렬을 추출 (start/end row/col indices) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 36, 14m 부분 행렬 추출 후 출력 : mat_rect.subMatrix(0,0,1,2):\n" + sub); 

        Matrix m_for_minor = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix minor_00 = m_for_minor.minor(0,0); // 명세 37: 행렬은 특정 하나의 행과 하나의 열을 제외한 부분 행렬 (minor) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 37, 14m 마이너 행렬 추출 후 출력 : m_for_minor.minor(0,0):\n" + minor_00); 

        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14m 행렬 출력 (전치 전) : mat_rect original:\n" + mat_rect); 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 38, 14m 전치행렬 생성 후 출력 : mat_rect.transpose():\n" + mat_rect.transpose()); 

        // Removed 0xN matrix transpose test.
        // Matrix zero_row_mat = Factory.buildMatrix(new double[[); 
        // System.out.println("명세 09, 14m 0xN 행렬 생성, 출력 : zero_row_mat (0x2):\n" + zero_row_mat); 
        // System.out.println("명세 38, 13, 14m 0xN 행렬 전치, 크기 및 내용 출력 : zero_row_mat.transpose() (2x0):\n" + zero_row_mat.transpose() + " size: " + Arrays.toString(zero_row_mat.transpose().getSize())); 

        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다. (trace 결과가 Scalar이므로)
        System.out.println("명세 39, 14s 대각 요소 합(trace) 출력 : mat1.trace() (1+4=5): " + mat1.trace()); 

        System.out.println("명세 40 정사각 행렬 여부 판별 : mat1.isSquare() (2x2): " + mat1.isSquare()); 
        System.out.println("명세 40 정사각 행렬 여부 판별 : mat_rect.isSquare() (2x3): " + mat_rect.isSquare()); 
        // Removed 0x0 matrix test.
        // System.out.println("명세 40, 10 0x0 행렬 정사각 여부 판별 : Factory.buildMatrix(0).isSquare() (0x0): " + Factory.buildMatrix(0).isSquare()); 

        Matrix upper_tri = Factory.buildMatrix(new double[][]{{1,2,3},{0,4,5},{0,0,6}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix not_upper_tri = Factory.buildMatrix(new double[][]{{1,2,3},{1,4,5},{0,0,6}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        System.out.println("명세 41 상삼각 행렬 여부 판별 : upper_tri.isUpperTriangular(): " + upper_tri.isUpperTriangular()); 
        System.out.println("명세 41 상삼각 행렬 여부 판별 : not_upper_tri.isUpperTriangular(): " + not_upper_tri.isUpperTriangular()); 
        System.out.println("명세 41 상삼각 행렬 여부 판별 (정사각 아님) : mat_rect.isUpperTriangular(): " + mat_rect.isUpperTriangular()); 
        // Removed 0x0 matrix test.
        // System.out.println("명세 41, 10 0x0 행렬 상삼각 여부 판별 : Factory.buildMatrix(0).isUpperTriangular() (0x0): " + Factory.buildMatrix(0).isUpperTriangular()); 

        Matrix lower_tri = Factory.buildMatrix(new double[][]{{1,0,0},{2,3,0},{4,5,6}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix not_lower_tri = Factory.buildMatrix(new double[][]{{1,0,1},{2,3,0},{4,5,6}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        System.out.println("명세 42 하삼각 행렬 여부 판별 : lower_tri.isLowerTriangular(): " + lower_tri.isLowerTriangular()); 
        System.out.println("명세 42 하삼각 행렬 여부 판별 : not_lower_tri.isLowerTriangular(): " + not_lower_tri.isLowerTriangular()); 
        // Removed 0x0 matrix test.
        // System.out.println("명세 42, 10 0x0 행렬 하삼각 여부 판별 : Factory.buildMatrix(0).isLowerTriangular() (0x0): " + Factory.buildMatrix(0).isLowerTriangular()); 

        Matrix iden = Factory.buildMatrix(2); // 명세 10: 단위 행렬 생성 
        System.out.println("명세 43 단위 행렬 여부 판별 : iden (2x2).isIdentity(): " + iden.isIdentity()); 
        System.out.println("명세 43 단위 행렬 여부 판별 : mat1.isIdentity(): " + mat1.isIdentity()); 
        // Removed 0x0 matrix test.
        // System.out.println("명세 43, 10 0x0 행렬 단위 행렬 여부 판별 : Factory.buildMatrix(0).isIdentity() (0x0): " + Factory.buildMatrix(0).isIdentity()); 

        Matrix zero_m = Factory.buildMatrix(2,3,0.0); // 명세 06: 지정한 하나의 값을 모든 요소의 값으로 하는 m x n 행렬 생성 
        System.out.println("명세 44 영 행렬 여부 판별 : zero_m.isZeroMatrix(): " + zero_m.isZeroMatrix()); 
        System.out.println("명세 44 영 행렬 여부 판별 : mat1.isZeroMatrix(): " + mat1.isZeroMatrix()); 
        // Removed 0xN matrix test.
        // System.out.println("명세 44, 06 0xN 행렬 영 행렬 여부 판별 : Factory.buildMatrix(0,2,0.0).isZeroMatrix() (0x2): " + Factory.buildMatrix(0,2,0.0).isZeroMatrix()); 
    }

    public static void testMatrixElementaryOperations() {
        printTitle("Matrix Elementary Row/Column Operations (45-50)"); 
        Matrix mat_ops = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14m 행렬 출력 (기본 연산 전) : Original mat_ops:\n" + mat_ops); 

        mat_ops.swapRows(0,1); // 명세 45: 특정 두 행의 위치를 맞교환 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 45, 14m 두 행 교환 후 출력 : After swapRows(0,1):\n" + mat_ops); 
        mat_ops.swapRows(0,1); // Swap back for subsequent tests 

        mat_ops.swapCols(0,2); // 명세 46: 특정 두 열의 위치를 맞교환 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 46, 14m 두 열 교환 후 출력 : After swapCols(0,2):\n" + mat_ops); 
        mat_ops.swapCols(0,2); // Swap back 

        mat_ops.multiplyRow(0, Factory.buildScalar("2")); // 명세 47: 특정 행에 상수배(스칼라) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 47, 14m 특정 행 상수배 후 출력 : After multiplyRow(0, Scalar(2)):\n" + mat_ops); 
        mat_ops.multiplyRow(0, Factory.buildScalar("0.5")); // Undo 

        mat_ops.multiplyCol(1, Factory.buildScalar("3")); // 명세 48: 특정 열에 상수배(스칼라) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 48, 14m 특정 열 상수배 후 출력 : After multiplyCol(1, Scalar(3)):\n" + mat_ops); 
        mat_ops.multiplyCol(1, Factory.buildScalar(BigDecimal.ONE.divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP).toString())); // Undo 

        mat_ops = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (리셋) 
        mat_ops.addScaledRow(1, 0, Factory.buildScalar("2")); // 명세 49: 특정 행에 다른 행의 상수배를 더함 (R1 = R1 + 2*R0) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 49, 14m 특정 행에 다른 행 상수배 더한 후 출력 : After addScaledRow(1,0, Scalar(2)) (R1=R1+2R0):\n" + mat_ops); 

        mat_ops = Factory.buildMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (리셋) 
        mat_ops.addScaledCol(1, 2, Factory.buildScalar("-1")); // 명세 50: 특정 열에 다른 열의 상수배를 더함 (C1 = C1 - C2) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 50, 14m 특정 열에 다른 열 상수배 더한 후 출력 : After addScaledCol(1,2, Scalar(-1)) (C1=C1-C2):\n" + mat_ops); 
    }


    public static void testMatrixRREFDeterminantInverse() {
        printTitle("Matrix RREF, Determinant, Inverse (51-54)"); 
        Matrix m_rref_test = Factory.buildMatrix(new double[][]{{1,2,-1,-4},{2,3,-1,-11},{-2,0,-3,22}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 14m 행렬 출력 (RREF 테스트용) : Matrix for RREF test:\n" + m_rref_test); 

        Matrix rref_m = m_rref_test.rref(); // 명세 51: 자신으로부터 RREF행렬을 구해서 반환 (새 행렬 반환) 
        // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
        System.out.println("명세 51, 14m RREF 변환 후 출력 : RREF of m_rref_test:\n" + rref_m); 

        System.out.println("명세 52 RREF 행렬 여부 판별 (원본) : m_rref_test.isRREF(): " + m_rref_test.isRREF()); 
        System.out.println("명세 52 RREF 행렬 여부 판별 (RREF 결과) : rref_m.isRREF(): " + rref_m.isRREF()); 
        Matrix iden3 = Factory.buildMatrix(3); // 명세 10: 단위 행렬 생성 
        System.out.println("명세 52, 10 단위 행렬 RREF 여부 판별 : Identity(3).isRREF(): " + iden3.isRREF()); 

        Matrix m_det_inv = Factory.buildMatrix(new double[][]{{1,2},{3,4}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 

        Scalar det = m_det_inv.determinant(); // 명세 53: 자신의 행렬식을 구해줄 수 있다 (nxn 행렬) 
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다. (determinant 결과가 Scalar이므로)
        System.out.println("명세 53, 14s 행렬식 계산 결과 출력 : Determinant of [[1,,[3,]: " + det + " (Expected: -2)"); 

        Matrix m_det3 = Factory.buildMatrix(new double[][]{{6,1,1},{4,-2,5},{2,8,7}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        // 명세 14s: 스칼라 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다. (determinant 결과가 Scalar이므로)
        System.out.println("명세 53, 14s 3x3 행렬식 계산 결과 출력 : Determinant of 3x3 matrix [[6,1,,[4,-2,,[2,8,]: " + m_det3.determinant()); 

        try {
            Matrix inv_m = m_det_inv.inverse(); // 명세 54: 자신의 역행렬을 구해줄 수 있다 (nxn 행렬) 
            // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
            System.out.println("명세 54, 14m 역행렬 계산 결과 출력 : Inverse of [[1,,[3,]:\n" + inv_m); 
            // 명세 29: (Tensors) 전달받은 두 행렬의 곱셈이 가능하다 (static, returns new)
            Matrix product_check = Tensors.multiply(m_det_inv, inv_m); 
            // 명세 14m: 행렬 객체는 자신의 값을 문자열로 변환하여 반환할 수 있다.
            // 명세 43: 행렬은 자신이 단위 행렬인지 여부 판별 (nxn 행렬)
            System.out.println("명세 54, 29, 14m, 43 원본*역행렬 결과 출력 (단위행렬 여부 확인) : Original * Inverse (should be Identity):\n" + product_check + " isIdentity: " + product_check.isIdentity()); 
        } catch (SingularMatrixException e) {
            System.err.println("Error calculating inverse: " + e.getMessage()); 
        }

        Matrix singular_m = Factory.buildMatrix(new double[][]{{1,2},{2,4}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (특이 행렬) 
        try {
            System.out.println("명세 54 특이 행렬 역행렬 시도 : Attempting inverse of singular matrix [[1,,[2,]:"); 
            singular_m.inverse(); // Expected to throw SingularMatrixException 
        } catch (SingularMatrixException e) {
            System.out.println("명세 54 특이 행렬 역행렬 예외 발생 확인 : Caught expected exception: " + e.getMessage()); 
        }

        // Removed 0x0 matrix tests for determinant and inverse.
        // Matrix zero_dim_matrix = Factory.buildMatrix(0); 
        // System.out.println("명세 53, 10 0x0 행렬 행렬식 : Determinant of 0x0 matrix: " + zero_dim_matrix.determinant()); 
        // try {
        //     Matrix inv_zero_dim = zero_dim_matrix.inverse(); 
        //     System.out.println("명세 54, 10, 13, 14m 0x0 행렬 역행렬, 크기 및 내용 출력 : Inverse of 0x0 matrix:\n" + inv_zero_dim + " size: " + Arrays.toString(inv_zero_dim.getSize())); 
        // } catch (Exception e) {
        //     System.err.println("Error with 0x0 inverse: " + e.getMessage()); 
        // }
    }

    public static void testTensorsStaticMethods() {
        printTitle("Tensors Static Utility Methods"); 
        // Tensors.java 요구사항: 모든 메소드가 public static. 위의 디폴트 static 메소드들을 호출.
        Scalar ts1 = Factory.buildScalar("100"); // 명세 01: 값을 지정하여 스칼라 생성 
        Scalar ts2 = Factory.buildScalar("25"); // 명세 01: 값을 지정하여 스칼라 생성 
        // 명세 24: 전달받은 두 스칼라의 덧셈이 가능하다 (static, returns new)
        System.out.println("명세 24 (Tensors.add -> Scalar.add) 스칼라 덧셈 : Tensors.add(" + ts1 + ", " + ts2 + "): " + Tensors.add(ts1, ts2)); 
        // 명세 25: 전달받은 두 스칼라의 곱셈이 가능하다 (static, returns new)
        System.out.println("명세 25 (Tensors.multiply -> Scalar.multiply) 스칼라 곱셈 : Tensors.multiply(" + ts1 + ", " + ts2 + "): " + Tensors.multiply(ts1, ts2)); 
        Vector tv1 = Factory.buildVector(new double[]{1,2}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        Vector tv2 = Factory.buildVector(new double[]{3,4}); // 명세 05: 1차원 배열로부터 n-차원 벡터 생성 
        // 명세 26: 전달받은 두 벡터의 덧셈이 가능하다 (static, returns new)
        System.out.println("명세 26 (Tensors.add -> Vector.add) 벡터 덧셈 : Tensors.add(" + tv1 + ", " + tv2 + "): " + Tensors.add(tv1, tv2)); 
        // 명세 27: 전달받은 스칼라와 벡터의 곱셈이 가능하다 (static, returns new)
        System.out.println("명세 27 (Tensors.multiply -> Vector.multiply) 벡터-스칼라 곱셈 : Tensors.multiply(" + tv1 + ", " + ts1 + "): " + Tensors.multiply(tv1, ts1)); 
        System.out.println("명세 27 (Tensors.multiply -> Vector.multiply) 스칼라-벡터 곱셈 (오버로딩) : Tensors.multiply(" + ts1 + ", " + tv1 + "): " + Tensors.multiply(ts1, tv1)); 

        Matrix tm1 = Factory.buildMatrix(new double[][]{{1,0},{0,1}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        Matrix tm2 = Factory.buildMatrix(new double[][]{{5,6},{7,8}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 
        // 명세 28: 전달받은 두 행렬의 덧셈이 가능하다 (static, returns new)
        System.out.println("명세 28 (Tensors.add -> Matrix.add) 행렬 덧셈 : Tensors.add(tm1, tm2):\n" + Tensors.add(tm1, tm2)); 
        // 명세 29: 전달받은 두 행렬의 곱셈이 가능하다 (static, returns new)
        System.out.println("명세 29 (Tensors.multiply -> Matrix.multiply) 행렬 곱셈 : Tensors.multiply(tm1, tm2):\n" + Tensors.multiply(tm1, tm2)); 
        Matrix tm3 = Factory.buildMatrix(new double[][]{{1},{2}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (2x1) 
        Matrix tm4 = Factory.buildMatrix(new double[][]{{3},{4}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (2x1) 
        // 명세 32: 행렬은 다른 행렬과 가로로 합쳐질 수 있다 (static)
        System.out.println("명세 32 (Tensors.hstack -> Matrix.hstack) 행렬 가로 합치기 : Tensors.hstack(tm3, tm4):\n" + Tensors.hstack(tm3, tm4)); 
        Matrix tm5 = Factory.buildMatrix(new double[][]{{1,2}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (1x2) 
        Matrix tm6 = Factory.buildMatrix(new double[][]{{3,4}}); // 명세 09: 2차원 배열로부터 m x n 행렬 생성 (1x2) 
        // 명세 33: 행렬은 다른 행렬과 세로로 합쳐질 수 있다 (static)
        System.out.println("명세 33 (Tensors.vstack -> Matrix.vstack) 행렬 세로 합치기 : Tensors.vstack(tm5, tm6):\n" + Tensors.vstack(tm5, tm6)); 
        // Tensors utility methods (명세 외 추가된 Tensors 메소드들)
        // Tensors.identity -> 명세 10: 단위 행렬 생성
        System.out.println("Tensors 유틸리티 (identity -> 명세 10) : Tensors.identity(3):\n" + Tensors.identity(3)); 
        // Tensors.zeros -> 명세 06: 지정한 하나의 값을 모든 요소의 값으로 하는 m x n 행렬 생성 (0.0으로 채움)
        System.out.println("Tensors 유틸리티 (zeros -> 명세 06) : Tensors.zeros(2,3):\n" + Tensors.zeros(2,3)); 
        // Tensors.ones -> 명세 06: 지정한 하나의 값을 모든 요소의 값으로 하는 m x n 행렬 생성 (1.0으로 채움)
        System.out.println("Tensors 유틸리티 (ones -> 명세 06) : Tensors.ones(3,2):\n" + Tensors.ones(3,2)); 
        // Tensors.filled -> (유사) 명세 09: 2차원 배열로부터 m x n 행렬 생성 (내부적으로 Scalar[][] 사용)
        System.out.println("Tensors 유틸리티 (filled -> 유사 명세 09) : Tensors.filled(2,2, Factory.buildScalar(\"7\")):\n" + Tensors.filled(2,2, Factory.buildScalar("7"))); 
    }

    public static void testExceptionHandling() {
        printTitle("Exception Handling Tests"); 
        // 예외 클래스 정의 및 사용 테스트

        // Scalar creation exceptions
        try {
            System.out.print("명세 01 (오류 테스트) 잘못된 문자열 스칼라 생성 시도 : Test: Factory.buildScalar(\"abc\") -> "); 
            Factory.buildScalar("abc"); // Expected: IllegalArgumentException from BigDecimal 
        } catch (IllegalArgumentException e) {
            System.out.println("명세 01 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage()); 
        }
        try {
            System.out.print("명세 02 (오류 테스트) 잘못된 범위 무작위 스칼라 생성 시도 : Test: Factory.buildScalar(5,2) -> "); 
            Factory.buildScalar(5,2); // Expected: IllegalArgumentException from ScalarImpl constructor 
        } catch (IllegalArgumentException e) {
            System.out.println("명세 02 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage()); 
        }

        // Vector dimension mismatch
        Vector v_exc1 = Factory.buildVector(2, 1.0); // 명세 03 
        Vector v_exc2 = Factory.buildVector(3, 1.0); // 명세 03 
        try {
            System.out.print("명세 20, 26 (오류 테스트) 벡터 덧셈 차원 불일치 시도 : Test: v_exc1.add(v_exc2) (size mismatch) -> "); 
            v_exc1.add(v_exc2); // Expected: DimensionMismatchException 
        } catch (DimensionMismatchException e) {
            System.out.println("명세 20, 26 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage()); 
        }

        // Matrix dimension mismatch for add
        Matrix m_exc1 = Factory.buildMatrix(2, 2, 1.0); // 명세 06 
        Matrix m_exc2 = Factory.buildMatrix(2, 3, 1.0); // 명세 06 
        try {
            System.out.print("명세 22, 28 (오류 테스트) 행렬 덧셈 차원 불일치 시도 : Test: m_exc1.add(m_exc2) (dim mismatch) -> "); 
            m_exc1.add(m_exc2); // Expected: DimensionMismatchException 
        } catch (DimensionMismatchException e) {
            System.out.println("명세 22, 28 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage()); 
        }

        // Matrix dimension mismatch for multiply
        Matrix m_exc3 = Factory.buildMatrix(2, 3, 1.0); // 명세 06 (2x3) 
        Matrix m_exc4 = Factory.buildMatrix(2, 2, 1.0); // 명세 06 (2x2) 
        try {
            System.out.print("명세 23, 29 (오류 테스트) 행렬 곱셈 차원 불일치 시도 : Test: m_exc3.multiplyRight(m_exc4) (dim mismatch) -> "); 
            m_exc3.multiplyRight(m_exc4); // Expected: DimensionMismatchException (cols of m1 != rows of m2) 
        } catch (DimensionMismatchException e) {
            System.out.println("명세 23, 29 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // Index out of bounds
        try {
            System.out.print("명세 11v (오류 테스트) 벡터 요소 접근 범위 초과 시도 : Test: v_exc1.viewElement(5) -> ");
            v_exc1.viewElement(5); // Expected: IndexOutOfBoundsException
        } catch (IndexOutOfBoundsException e) {
            System.out.println("명세 11v (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("명세 11m (오류 테스트) 행렬 요소 접근 범위 초과 시도 : Test: m_exc1.viewElement(5,5) -> ");
            m_exc1.viewElement(5,5); // Expected: IndexOutOfBoundsException
        } catch (IndexOutOfBoundsException e) {
            System.out.println("명세 11m (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // CSV Parse Exception
        String badCsvFile = "bad_test_matrix.csv";
        try {
            createTestCsvFile(badCsvFile, new String[]{"1,2,3", "4,not_a_number,6"});
            System.out.print("명세 08 (오류 테스트) 잘못된 형식의 CSV 파일 파싱 시도 : Test: Factory.buildMatrix from bad CSV -> ");
            Factory.buildMatrix(badCsvFile); // Expected: CsvParseException (due to NumberFormatException)
        } catch (CsvParseException | IOException e) {
            System.out.println("명세 08 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            createTestCsvFile(badCsvFile, new String[]{"1,2,3", "4,5"}); // Inconsistent columns
            System.out.print("명세 08 (오류 테스트) 일관되지 않은 열 개수의 CSV 파일 파싱 시도 : Test: Factory.buildMatrix from inconsistent CSV -> ");
            Factory.buildMatrix(badCsvFile); // Expected: CsvParseException
        } catch (CsvParseException | IOException e) {
            System.out.println("명세 08 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // NotSquareMatrixException
        Matrix rect_m_exc = Factory.buildMatrix(2,3,1.0); // 명세 06
        try {
            System.out.print("명세 39 (오류 테스트) 정사각 행렬 아닌 경우 trace 시도 : Test: rect_m_exc.trace() -> "); 
            rect_m_exc.trace(); // Expected: NotSquareMatrixException
        } catch (NotSquareMatrixException e) {
            System.out.println("명세 39 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage()); 
        }
        try {
            System.out.print("명세 53 (오류 테스트) 정사각 행렬 아닌 경우 determinant 시도 : Test: rect_m_exc.determinant() -> ");
            rect_m_exc.determinant(); // Expected: NotSquareMatrixException 
        } catch (NotSquareMatrixException e) {
            System.out.println("명세 53 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            System.out.print("명세 54 (오류 테스트) 정사각 행렬 아닌 경우 inverse 시도 : Test: rect_m_exc.inverse() -> ");
            rect_m_exc.inverse(); // Expected: NotSquareMatrixException
        } catch (NotSquareMatrixException e) {
            System.out.println("명세 54 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // SingularMatrixException
        Matrix singular_m_exc = Factory.buildMatrix(new double[][]{{1,1},{1,1}}); // 명세 09 (Determinant is 0)
        try {
            System.out.print("명세 54 (오류 테스트) 특이 행렬 inverse 시도 : Test: singular_m_exc.inverse() -> ");
            singular_m_exc.inverse(); // Expected: SingularMatrixException
        } catch (SingularMatrixException e) {
            System.out.println("명세 54 (오류 테스트) 예외 발생 확인 : Caught expected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}
