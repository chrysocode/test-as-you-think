/*-
 * #%L
 * Test As You Think
 * %%
 * Copyright (C) 2017 - 2018 Xavier Pigeon and TestAsYouThink contributors
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

package testasyouthink;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.function.CheckedConsumer;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static testasyouthink.TestAsYouThink.givenSutClass;

class ThenAssertionErrorsTest {

    @Nested
    class Then_verifying {

        @Test
        void should_get_an_assertion_error_from_a_runnable_step_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then(() -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_another_runnable_step_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then(() -> {})
                    .and(() -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_a_specified_runnable_step_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then("Expectations", () -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_another_specified_runnable_step_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then("An expectation", () -> {})
                    .and("Another expectation", () -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_a_boolean_supplier_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then(() -> false));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_another_boolean_supplier_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then(() -> true)
                    .and(() -> false));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_a_runnable_step_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(() -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_another_runnable_step_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(() -> {})
                    .and(() -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_a_runnable_step_with_its_specification_given_a_non_void_target_method
                () {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then("Expectations", () -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void
        should_get_an_assertion_error_from_another_runnable_step_with_its_specification_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then("Expectations", () -> {})
                    .and("Expectations", () -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }
    }

    @Nested
    class Then_verifying_the_SUT {

        @Test
        void should_get_an_assertion_error_from_a_sut_consumer_step_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then(sut -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_another_sut_consumer_step_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then(sut -> {})
                    .and(sut -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_a_sut_consumer_specified_step_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then("Expectations", sut -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_another_sut_consumer_specified_step_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .then("An expectation", sut -> {})
                    .and("Another expectation", sut -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }
    }

    @Nested
    class Then_verifying_the_result {

        @Test
        void should_get_an_assertion_error_from_a_result_predicate_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(result -> false));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_another_result_predicate_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(result -> true)
                    .and(result -> false));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_result_predicates_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(asList(result -> true, result -> false)));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_a_result_consumer_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then((CheckedConsumer<String>) result -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_another_result_consumer_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(result -> {})
                    .and((CheckedConsumer<String>) result -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_a_specified_result_consumer_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then("Expectations", result -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void should_get_an_assertion_error_from_another_specified_result_consumer_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(result -> {})
                    .and("Expectations", result -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }
    }

    @Nested
    class Then_verifying_both_the_SUT_and_the_result {

        @Test
        void
        should_get_an_assertion_error_from_result_and_sut_predicates_because_of_the_result_given_a_non_void_target_method() {

            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(result -> false, sut -> true));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void
        should_get_an_assertion_error_from_result_and_sut_predicates_because_of_the_sut_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(result -> true, sut -> false));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void
        should_get_an_assertion_error_from_result_and_sut_consumers_because_of_the_result_given_a_non_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(result -> false, sut -> true));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }

        @Test
        void
        should_get_an_assertion_error_from_result_and_sut_consumers_because_of_the_sut_given_a_non_void_target_method
                () {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> "result")
                    .then(result -> true, sut -> false));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }
    }

    @Nested
    class Then_verifying_the_standard_streams {

        @Test
        void should_get_an_assertion_error_from_a_stdout_as_a_file_consumer_given_a_void_target_method() {
            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .when(sut -> {})
                    .thenStandardOutput(result -> assertThat(true).isFalse()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(AssertionError.class)
                    .hasNoCause();
        }
    }
}
