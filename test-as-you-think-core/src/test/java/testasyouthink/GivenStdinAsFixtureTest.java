package testasyouthink;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
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

    @Test
    void should_prepare_stdin_to_read_a_message() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(() -> {
                    String givenInputMessage = "expected";
                    InputStream stdinFake = new ByteArrayInputStream(givenInputMessage.getBytes());
                    System.setIn(stdinFake);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    System.out.print("Type: ");
                    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                    String actualMessage = stdin.readLine();
                    stdin.close();
                    System.out.println(String.format("\nMessage: %s", actualMessage));
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return actualMessage;
                })
                .then(result -> {
                    assertThat(result).isEqualTo("expected");
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    void should_prepare_stdin_to_read_messages() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(() -> {
                    List<String> givenInputMessages = asList("intput #1", "intput #2", "intput #3");
                    InputStream stdinFake = new ByteArrayInputStream(givenInputMessages
                            .stream()
                            .collect(Collectors.joining("\n"))
                            .getBytes());
                    System.setIn(stdinFake);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    System.out.print("Type: ");
                    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                    List<String> actualMessages = new ArrayList<>();
                    while (stdin.ready()) {
                        String actualMessage = stdin.readLine();
                        System.out.println(String.format("\nMessage: %s", actualMessage));
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

    @ParameterizedTest
    @ValueSource(ints = {0, 123, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void should_prepare_stdin_to_read_a_number_using_a_scanner(final int givenInputNumber) {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(() -> {
                    String message = String.valueOf(givenInputNumber);
                    InputStream stdinFake = new ByteArrayInputStream(message.getBytes());
                    System.setIn(stdinFake);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    System.out.print("Type: ");
                    Scanner scanner = new Scanner(System.in);
                    Integer actualNumber = scanner.nextInt();
                    System.out.println(String.format("\nNumber: %d", actualNumber));
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
                .given(() -> {
                    String message = String.valueOf(givenInputNumber);
                    InputStream stdinFake = new ByteArrayInputStream(message.getBytes());
                    System.setIn(stdinFake);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    System.out.print("Type: ");
                    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                    Integer actualNumber = Integer.parseInt(stdin.readLine());
                    System.out.println(String.format("\nNumber: %d", actualNumber));
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
    void should_prepare_stdin_to_read_different_kinds_of_data() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(() -> {
                    List<Object> givenInputs = asList("input", 123, true);
                    InputStream stdinFake = new ByteArrayInputStream(givenInputs
                            .stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining("\n"))
                            .getBytes());
                    System.setIn(stdinFake);
                    gwtMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                })
                .when(sut -> {
                    System.out.print("Type: ");
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
}
