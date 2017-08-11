/*-
 * #%L
 * Test As You Think
 * %%
 * Copyright (C) 2017 Xavier Pigeon and TestAsYouThink contributors
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.execution.ExecutionError;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.UnexpectedException;
import testasyouthink.function.CheckedSuppliers.CheckedStringSupplier;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static testasyouthink.TestAsYouThink.resultOf;
import static testasyouthink.fixture.Specifications.ExpectedMessage.EXPECTED_EXECUTION_FAILURE_MESSAGE;

public class ResultOfEventTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultOfEventTest.class);
    private GivenWhenThenDefinition gwtMock;

    @Before
    public void prepareFixtures() {
        gwtMock = mock(GivenWhenThenDefinition.class);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(gwtMock).whenAnEventHappensInRelationToAnActionOfTheConsumer();
        verifyNoMoreInteractions(gwtMock);
    }

    @Test
    public void should_verify_an_actual_character_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Character.valueOf('b');
        }).isBetween('a', 'c')).hasSameClassAs(assertThat('a'));
    }

    @Test
    public void should_verify_an_actual_string_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return "expected result";
        })
                .isEqualTo("expected result")
                .contains("result")).hasSameClassAs(assertThat(""));
    }

    @Test
    public void should_fail_to_verify_an_actual_string_is_conform_to_an_expected_result() {
        // WHEN
        Throwable thrown = catchThrowable(() -> resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return "actual";
        }).isEqualTo("expected"));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown).isInstanceOf(AssertionError.class);
    }

    @Test
    public void should_fail_to_execute_because_of_an_unexpected_failure() {
        // WHEN
        Throwable thrown = catchThrowable(() -> resultOf((CheckedStringSupplier) () -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            throw new UnexpectedException();
        }).isEqualTo("expected"));

        // THEN
        LOGGER.debug("Stack trace", thrown);
        assertThat(thrown)
                .isInstanceOf(ExecutionError.class)
                .hasMessage(EXPECTED_EXECUTION_FAILURE_MESSAGE);
    }

    @Test
    public void should_verify_an_actual_byte_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Byte.valueOf("6");
        }).isEqualTo(Byte.valueOf("6"))).hasSameClassAs(assertThat((byte) 6));
    }

    @Test
    public void should_verify_an_actual_short_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Short.valueOf("123");
        }).isEqualTo(Short.valueOf("123"))).hasSameClassAs(assertThat(Short.valueOf("123")));
    }

    @Test
    public void should_verify_an_actual_integer_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Integer.valueOf(123);
        }).isEqualTo(Integer.valueOf(123))).hasSameClassAs(assertThat(123));
    }

    @Test
    public void should_verify_an_actual_long_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return 123L;
        }).isEqualTo(123L)).hasSameClassAs(assertThat(123L));
    }

    @Test
    public void should_verify_an_actual_float_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Float.valueOf("12.34");
        }).isEqualTo(Float.valueOf("12.34"))).hasSameClassAs(assertThat(12.34f));
    }

    @Test
    public void should_verify_an_actual_double_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Double.valueOf("12.34");
        }).isEqualTo(Double.valueOf("12.34"))).hasSameClassAs(assertThat(12.34d));
    }

    @Test
    public void should_verify_an_actual_big_integer_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return BigInteger.valueOf(1234);
        }).isEqualTo(BigInteger.valueOf(1234))).hasSameClassAs(assertThat(BigInteger.valueOf(1234)));
    }

    @Test
    public void should_verify_an_actual_big_decimal_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return new BigDecimal("12.34");
        }).isEqualTo(new BigDecimal("12.34"))).hasSameClassAs(assertThat(BigDecimal.ONE));
    }

    @Test
    public void should_verify_an_actual_optional_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Optional.of("optional");
        }).isPresent()).hasSameClassAs(assertThat(Optional.empty()));
    }

    @Test
    public void should_verify_an_actual_optional_int_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return OptionalInt.of(123);
        }).isPresent()).hasSameClassAs(assertThat(OptionalInt.empty()));
    }

    @Test
    public void should_verify_an_actual_optional_long_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return OptionalLong.of(123);
        }).isPresent()).hasSameClassAs(assertThat(OptionalLong.empty()));
    }

    @Test
    public void should_verify_an_actual_optional_double_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return OptionalDouble.of(123);
        }).isPresent()).hasSameClassAs(assertThat(OptionalDouble.empty()));
    }

    @Test
    public void should_verify_an_actual_boolean_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return false;
        }).isFalse()).hasSameClassAs(assertThat(false));
    }

    @Test
    public void should_verify_an_actual_date_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return new Date(123456);
        }).hasSameTimeAs(new Date(123456))).hasSameClassAs(assertThat(new Date(123456)));
    }

    @Test
    public void should_verify_an_actual_local_date_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return LocalDate.of(2017, 8, 9);
        }).isAfter(LocalDate.of(2017, 8, 1))).hasSameClassAs(assertThat(LocalDate.of(2017, 8, 9)));
    }

    @Test
    public void should_verify_an_actual_local_date_time_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return LocalDateTime.of(2017, 8, 9, 12, 30);
        }).isAfter(LocalDateTime.of(2017, 8, 9, 10, 15))).hasSameClassAs(
                assertThat(LocalDateTime.of(2017, 8, 9, 12, 30)));
    }

    @Test
    public void should_verify_an_actual_local_time_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return LocalTime.of(12, 30);
        }).isAfter(LocalTime.of(10, 15))).hasSameClassAs(assertThat(LocalTime.of(12, 30)));
    }

    @Test
    public void should_verify_an_actual_instant_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Instant.ofEpochSecond(123456);
        }).isAfter(Instant.ofEpochSecond(100000))).hasSameClassAs(assertThat(Instant.ofEpochSecond(123456)));
    }

    @Test
    public void should_verify_an_actual_file_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return new File(File.separator);
        }).exists()).hasSameClassAs(assertThat(new File(File.separator)));
    }

    @Test
    public void should_verify_an_actual_path_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return new File(File.separator).toPath();
        }).isAbsolute()).hasSameClassAs(assertThat(new File(File.separator).toPath()));
    }

    @Test
    public void should_verify_an_actual_uri_is_conform_to_an_expected_result() throws URISyntaxException {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return new URI("uri");
        }).hasNoParameters()).hasSameClassAs(assertThat(new URI("uri")));
    }

    @Test
    public void should_verify_an_actual_url_is_conform_to_an_expected_result() throws MalformedURLException {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return new URL("http://uri");
        }).hasNoParameters()).hasSameClassAs(assertThat(new URL("http://uri")));
    }

    @Test
    public void should_verify_an_actual_iterable_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return (Iterable<String>) Arrays.asList("one", "two", "three");
        }).contains("two")).hasSameClassAs(assertThat((Iterable<String>) new ArrayList<String>()));
    }

    @Test
    public void should_verify_an_actual_iterator_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Arrays
                    .asList("one", "two", "three")
                    .iterator();
        }).doesNotContain("four")).hasSameClassAs(assertThat(new ArrayList<>().iterator()));
    }

    @Test
    public void should_verify_an_actual_list_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return Arrays.asList("one", "two", "three");
        }).hasSize(3)).hasSameClassAs(assertThat(new ArrayList<>()));
    }

    @Test
    public void should_verify_an_actual_map_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return new HashMap<Integer, String>() {{
                put(1, "one");
                put(2, "two");
                put(3, "three");
            }};
        }).hasSize(3)).hasSameClassAs(assertThat(new HashMap<>()));
    }

    @Test
    public void should_verify_an_actual_atomic_boolean_is_conform_to_an_expected_result() {
        assertThat(resultOf(() -> {
            gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            return new AtomicBoolean(true);
        }).isTrue()).hasSameClassAs(assertThat(new AtomicBoolean()));
    }
}
