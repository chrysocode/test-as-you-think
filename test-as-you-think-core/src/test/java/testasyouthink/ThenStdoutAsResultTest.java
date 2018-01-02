package testasyouthink;

import org.junit.Test;
import testasyouthink.fixture.SystemUnderTest;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static testasyouthink.TestAsYouThink.givenSutClass;

public class ThenStdoutAsResultTest {

    @Test
    public void should_verify_the_standard_output_as_a_result_given_a_void_method() {
        givenSutClass(SystemUnderTest.class)
                .given(() -> {
                    Path stdoutPath = Files.createFile(Paths.get("actual_result.txt"));
                    PrintStream stdoutStream = new PrintStream(stdoutPath.toString());
                    System.setOut(stdoutStream);
                })
                .when(sut -> {
                    System.out.println("Stdout as a result");
                })
                .then(() -> assertThat(Paths
                        .get("actual_result.txt")
                        .toFile()).hasContent("Stdout as a result"));

        Paths
                .get("actual_result.txt")
                .toFile()
                .deleteOnExit();
    }
}
