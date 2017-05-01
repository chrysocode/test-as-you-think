package givenwhenthen;

import givenwhenthen.fixture.GivenWhenThenDefinition;
import givenwhenthen.fixture.SystemUnderTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static givenwhenthen.GivenWhenThen.givenSutClass;
import static givenwhenthen.fixture.GivenWhenThenDefinition.orderedSteps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.verify;

public class ThenFailuresTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 0);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_verify_the_sut_fails() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::fail)
                .thenItFails();
    }

    @Test
    public void should_verify_the_sut_fails_by_raising_an_expected_exception() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .whenSutRunsOutsideOperatingConditions(sut -> sut.fail(IllegalStateException.class))
                .thenItFails(IllegalStateException.class);
    }

    @Test
    public void should_verify_the_sut_fails_by_raising_an_expected_exception_with_an_expected_message() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .whenSutRunsOutsideOperatingConditions(sut -> sut.fail(IllegalStateException.class, "expected message"))
                .thenItFails(IllegalStateException.class, "expected message");
    }

    @Test(expected = AssertionError.class)
    public void should_fail_because_of_an_unexpected_failure_given_a_non_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidFail)
                .then(result -> {
                    assertThat(result).isEqualTo("Unexpected failure must happen before this assertions.");
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_because_of_an_unexpected_failure_given_a_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when((CheckedConsumer<SystemUnderTest>) SystemUnderTest::fail)
                .then(result -> assertThat(result).isEqualTo("Unexpected failure must happen before this assertions."));
    }
}