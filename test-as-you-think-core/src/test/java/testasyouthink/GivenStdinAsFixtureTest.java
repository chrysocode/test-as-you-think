package testasyouthink;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mockito;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static testasyouthink.TestAsYouThink.givenSutClass;

class GivenStdinAsFixtureTest {

    @Test
    void should_prepare_stdin_to_read_a_message() {
        // GIVEN
        GivenWhenThenDefinition gwtMock = Mockito.mock(GivenWhenThenDefinition.class);

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

    @ParameterizedTest
    @ValueSource(ints = {0, 123, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void should_prepare_stdin_to_read_a_number_using_a_scanner(final int givenInputNumber) {
        // GIVEN
        GivenWhenThenDefinition gwtMock = Mockito.mock(GivenWhenThenDefinition.class);

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

    @ParameterizedTest
    @ValueSource(ints = {0, 123, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void should_prepare_stdin_to_read_a_number_using_a_buffered_reader(final int givenInputNumber) {
        // GIVEN
        GivenWhenThenDefinition gwtMock = Mockito.mock(GivenWhenThenDefinition.class);

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

    @Nested
    class Conversions {

        @Nested
        class ConversionWithRightAndLeftShiftOperators {

            byte[] toByteArray(int value) {
                return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
            }

            int fromByteArray(byte[] bytes) {
                return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
            }

            @ParameterizedTest
            @ValueSource(ints = {0, 123, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
            void should_convert(final int givenInputNumber) {
                assertThat(fromByteArray(toByteArray(givenInputNumber))).isEqualTo(givenInputNumber);
            }
        }

        @Nested
        class ConversionWithByteBuffer {

            byte[] toByteArray(int value) {
                return ByteBuffer
                        .allocate(4)
                        .putInt(value)
                        .array();
            }

            int fromByteArray(byte[] bytes) {
                return ByteBuffer
                        .wrap(bytes)
                        .getInt();
            }

            @ParameterizedTest
            @ValueSource(ints = {0, 123, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
            void should_convert(final int givenInputNumber) {
                assertThat(fromByteArray(toByteArray(givenInputNumber))).isEqualTo(givenInputNumber);
            }
        }
    }
}
