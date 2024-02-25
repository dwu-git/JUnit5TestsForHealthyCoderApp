package com.healthycoderapp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BMICalculatorTest {
    private String environment = "dev";

    //must be static
    @BeforeAll
    static void openConnection() {
        System.out.println("Before All");
    }

    //must be static
    @AfterAll
    static void closeConnection() {
        System.out.println("After All");
    }

    @Nested
    class IsDietRecommendedTests {

        @ParameterizedTest
        @ValueSource(doubles = {78.95, 77.87})
        void shouldReturnTrueWhenDietIsRecommended(Double coderWeight) {

            // given/arrange
            double weight = coderWeight;
            double height = 1.72;

            // when/act
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then/assert
            assertTrue(recommended);
        }

        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.0, 1.78"})
        void shouldReturnTrueWhenDietIsRecommended2(Double coderWeight, Double coderHeight) {

            // given/arrange
            double weight = coderWeight;
            double height = coderHeight;

            // when/act
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then/assert
            assertTrue(recommended);
        }

        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void shouldReturnTrueWhenDietIsRecommended3(Double coderWeight, Double coderHeight) {

            // given/arrange
            double weight = coderWeight;
            double height = coderHeight;

            // when/act
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then/assert
            assertTrue(recommended);
        }

        @Test
        void shouldReturnFalseWhenDietIsNotRecommended() {

            // given
            double weight = 50.0;
            double height = 1.92;

            // when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then
            assertFalse(recommended);
        }

        @Test
        void shouldThrowArithmeticExceptionWhenHeightZero() {

            // given
            double weight = 89.0;
            double height = 0.0;

            // when
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            // then
            assertThrows(ArithmeticException.class, executable);
        }
    }

    @Nested
    @DisplayName("{{{}}} sample inner class display name")
    class FindCoderWithWorstBMITests {

        @Test
        @DisplayName(">>>>> sample method display name")
        @DisabledOnOs(OS.WINDOWS)
        void shouldReturnCoderWithWorstMBI() {

            // given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.82, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));

            // when
            Coder coderWithWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertAll(
                    () -> assertEquals(1.82, coderWithWorstBMI.getHeight()),
                    () -> assertEquals(98.0, coderWithWorstBMI.getWeight())
            );
        }

        @Test
        void shouldReturnNullBMICoderWhenListEmpty() {

            // given
            List<Coder> coders = new ArrayList<>();

            // when
            Coder coderWithWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertNull(coderWithWorstBMI);
        }

        @Test
        void shouldReturnCoderWithWorstBMIIn1MsWhenCoderListHas1000Elements() {

            // given
            assumeTrue(BMICalculatorTest.this.environment.equals("dev"));
            List<Coder> coders = new ArrayList<>();
            for (int i = 0; i < 10000; i++)
                coders.add(new Coder(1.0 + i, 10.0 + i));

            // when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertTimeout(Duration.ofMillis(500), executable);
        }
    }

    @Nested
    class GetBMIScoresTests {

        @Test
        void shouldReturnCorrectBMIScoreArrayWhenCoderListNotEmpty() {

            // given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.82, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));
            double[] expected = {18.11, 29.59, 19.53};

            // when
            double[] bmiScores = BMICalculator.getBMIScores(coders);

            // then
            assertArrayEquals(expected, bmiScores);
        }
    }
}