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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.preparation.PreparationError;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;
import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static testasyouthink.TestAsYouThink.givenSutClass;

class GivenStdinAsFixtureTest {

    private GivenWhenThenDefinition gwtMock;

    @BeforeEach
    void prepareMocks() {
        gwtMock = mock(GivenWhenThenDefinition.class);
    }

    void verifyMocks() {
        // THEN
        InOrder inOrder = inOrder(gwtMock);
        inOrder
                .verify(gwtMock)
                .givenAContextThatDefinesTheInitialStateOfTheSystem();
        inOrder
                .verify(gwtMock)
                .whenAnEventHappensInRelationToAnActionOfTheConsumer();
        inOrder
                .verify(gwtMock)
                .thenTheActualResultIsInKeepingWithTheExpectedResult();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void should_prepare_stdin_to_read_a_message() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStandardInputStream(stdin -> {
                    stdin.expectToRead("expected");
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                    String actualMessage = stdin.readLine();
                    stdin.close();
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return actualMessage;
                })
                .then(result -> {
                    assertThat(result).isEqualTo("expected");
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        verifyMocks();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 123, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void should_prepare_stdin_to_read_a_number_using_a_scanner(final int givenInputNumber) {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStandardInputStream(stdin -> {
                    stdin.expectToRead(givenInputNumber);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    Scanner scanner = new Scanner(System.in);
                    Integer actualNumber = scanner.nextInt();
                    scanner.close();
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return actualNumber;
                })
                .then(result -> {
                    assertThat(result).isEqualTo(givenInputNumber);
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        verifyMocks();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 123, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void should_prepare_stdin_to_read_a_number_using_a_buffered_reader(final int givenInputNumber) {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStandardInputStream(stdin -> {
                    stdin.expectToRead(givenInputNumber);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                    Integer actualNumber = Integer.parseInt(stdin.readLine());
                    stdin.close();
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return actualNumber;
                })
                .then(result -> {
                    assertThat(result).isEqualTo(givenInputNumber);
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        verifyMocks();
    }

    @Test
    void should_prepare_stdin_to_read_messages() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStandardInputStream(stdin -> {
                    stdin.expectToRead(asList("intput #1", "intput #2", "intput #3"));
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                    List<String> actualMessages = new ArrayList<>();
                    while (stdin.ready()) {
                        String actualMessage = stdin.readLine();
                        actualMessages.add(actualMessage);
                    }
                    stdin.close();
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return actualMessages;
                })
                .then(result -> {
                    assertThat(result).containsExactly("intput #1", "intput #2", "intput #3");
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        verifyMocks();
    }

    @Test
    void should_prepare_stdin_to_read_different_kinds_of_data() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStandardInputStream(stdin -> {
                    stdin.expectToRead(asList("input", 123, true));
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    Scanner scanner = new Scanner(System.in);
                    List<Object> actualData = new ArrayList<>();
                    actualData.add(scanner.next());
                    actualData.add(scanner.nextInt());
                    actualData.add(scanner.nextBoolean());
                    scanner.close();
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return actualData;
                })
                .then(result -> {
                    assertThat(result).containsExactly("input", 123, true);
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        verifyMocks();
    }

    @Test
    void should_prepare_stdin_in_several_times_to_read_different_kinds_of_data() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStandardInputStream(stdin -> {
                    stdin.expectToRead("input");
                    stdin.expectToRead(123);
                    stdin.expectToRead(true);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    Scanner scanner = new Scanner(System.in);
                    List<Object> actualData = new ArrayList<>();
                    actualData.add(scanner.next());
                    actualData.add(scanner.nextInt());
                    actualData.add(scanner.nextBoolean());
                    scanner.close();
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return actualData;
                })
                .then(result -> {
                    assertThat(result).containsExactly("input", 123, true);
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        verifyMocks();
    }

    @Test
    void should_prepare_stdin_to_read_a_file_path() throws IOException {
        // GIVEN
        final Path givenInputPath = Files.createTempFile("inputs", ".txt");
        PrintWriter writer = new PrintWriter(new FileWriter(givenInputPath.toFile()));
        rangeClosed(1, 5)
                .mapToObj(count -> "line #" + count + "\n")
                .forEach(writer::write);
        writer.flush();

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStandardInputStream(stdin -> {
                    stdin.expectToRead(givenInputPath);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    Scanner scanner = new Scanner(System.in);
                    List<String> actualMessages = new ArrayList<>();
                    while (scanner.hasNext()) {
                        String actualMessage = scanner.nextLine();
                        actualMessages.add(actualMessage);
                    }
                    scanner.close();
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return actualMessages;
                })
                .then(result -> {
                    assertThat(result).containsExactly("line #1", "line #2", "line #3", "line #4", "line #5");
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });

        // THEN
        verifyMocks();
    }

    @Nested
    class Given_an_input_file {

        @Test
        void should_prepare_stdin_to_read_a_file() throws IOException {
            // GIVEN
            final File givenInputFile = Files
                    .createTempFile("inputs", ".txt")
                    .toFile();
            PrintWriter writer = new PrintWriter(new FileWriter(givenInputFile));
            rangeClosed(1, 5)
                    .mapToObj(count -> "line #" + count + "\n")
                    .forEach(writer::write);
            writer.flush();

            // WHEN
            givenSutClass(SystemUnderTest.class)
                    .givenStandardInputStream(stdin -> {
                        stdin.expectToRead(givenInputFile);
                        gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    })
                    .when(sut -> {
                        Scanner scanner = new Scanner(System.in);
                        List<String> actualMessages = new ArrayList<>();
                        while (scanner.hasNext()) {
                            String actualMessage = scanner.nextLine();
                            actualMessages.add(actualMessage);
                        }
                        scanner.close();
                        gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                        return actualMessages;
                    })
                    .then(result -> {
                        assertThat(result).containsExactly("line #1", "line #2", "line #3", "line #4", "line #5");
                        gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    });

            // THEN
            verifyMocks();
        }

        @Test
        void should_fail_to_prepare_stdin_to_read_a_file() throws IOException {
            // GIVEN
            File givenInputFile = Files
                    .createTempFile("empty", ".txt")
                    .toFile();
            PowerMockito.mockStatic(Files.class);
            PowerMockito
                    .when(Files.lines(Mockito.any(Path.class)))
                    .thenThrow(IOException.class);

            // WHEN
            Throwable thrown = catchThrowable(() -> givenSutClass(SystemUnderTest.class)
                    .givenStandardInputStream(stdin -> {
                        gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                        stdin.expectToRead(givenInputFile);
                    })
                    .whenSutRuns(sut -> gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer())
                    .then(() -> gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult()));

            // THEN
            assertThat(thrown)
                    .isInstanceOf(PreparationError.class)
                    .hasMessage("Fails to prepare the standard input stream!")
                    .hasCauseInstanceOf(IOException.class);
            verify(gwtMock).givenAContextThatDefinesTheInitialStateOfTheSystem();
            verifyNoMoreInteractions(gwtMock);
        }
    }
}
