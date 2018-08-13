import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.rangeClosed;

public class PlayingWithKeyboard {

    public static void main(String[] args) {
        String message = "expected message";
        String givenMessages = rangeClosed(0, 9)
                .mapToObj(count -> String.format("%s #%d", message, count))
                .collect(joining("\n"));
        InputStream stdinFake = new ByteArrayInputStream(givenMessages.getBytes());
        System.setIn(stdinFake);

        try (BufferedReader actualStdin = new BufferedReader(new InputStreamReader(System.in))) {
            while (actualStdin.ready()) {
                System.out.print("Type: ");
                String actualInput = actualStdin.readLine();
                System.out.println(actualInput);
                if ("q".equals(actualInput)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
