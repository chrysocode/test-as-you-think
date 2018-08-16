package testasyouthink;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.function.CheckedRunnable;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static testasyouthink.TestAsYouThink.givenSutClass;

class GivenStdinAsFixtureTest {

    private GivenWhenThenDefinition gwtMock;

    @BeforeEach
    void prepareMocks() {
        gwtMock = mock(GivenWhenThenDefinition.class);
    }

    @AfterEach
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

    private CheckedRunnable prepareStdin(final Object input) {
        return () -> {
            InputStream stdinFake = new ByteArrayInputStream(String
                    .valueOf(input)
                    .getBytes());
            System.setIn(stdinFake);
            gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        };
    }

    private CheckedRunnable prepareStdin(final Collection<?> inputs) {
        return prepareStdin(inputs
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining("\n")));
    }

    private CheckedRunnable prepareStdin(final File input) throws IOException {
        return prepareStdin(Files
                .lines(input.toPath())
                .collect(Collectors.joining("\n")));
    }

    private CheckedRunnable prepareStdin(final Path input) throws IOException {
        return prepareStdin(Files
                .lines(input)
                .collect(Collectors.joining("\n")));
    }

    @Test
    void should_prepare_stdin_to_read_a_message() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(prepareStdin("expected"))
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
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 123, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void should_prepare_stdin_to_read_a_number_using_a_scanner(final int givenInputNumber) {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(prepareStdin(givenInputNumber))
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
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 123, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void should_prepare_stdin_to_read_a_number_using_a_buffered_reader(final int givenInputNumber) {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(prepareStdin(givenInputNumber))
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
    }

    @Test
    void should_prepare_stdin_to_read_messages() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(prepareStdin(asList("intput #1", "intput #2", "intput #3")))
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
    }

    @Test
    void should_prepare_stdin_to_read_different_kinds_of_data() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(prepareStdin(asList("input", 123, true)))
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
    }

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
                .given(prepareStdin(givenInputFile))
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
                .given(prepareStdin(givenInputPath))
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
    }
}
