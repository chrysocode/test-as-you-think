package givenwhenthen;

import givenwhenthen.fixture.GivenWhenThenDefinition;
import givenwhenthen.fixture.SystemUnderTest;
import givenwhenthen.function.CheckedConsumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static givenwhenthen.GivenWhenThen.givenSutClass;
import static givenwhenthen.fixture.GivenWhenThenDefinition.orderedSteps;
import static org.easymock.EasyMock.verify;

public class ThenFailuresTest {

    private static final String EXPECTED_MESSAGE = "expected message";
    private static final String AN_EXPECTED_EXCEPTION_MUST_HAVE_BEEN_RAISED_BEFORE = "An expected exception must " +
            "have" + " been raised before!";
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
                .thenItFails()
                .byThrowing(IllegalStateException.class);
    }

    @Test
    public void should_verify_the_sut_fails_by_raising_an_expected_exception_with_an_expected_message() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .whenSutRunsOutsideOperatingConditions(sut -> sut.fail(IllegalStateException.class, EXPECTED_MESSAGE))
                .thenItFails(IllegalStateException.class, EXPECTED_MESSAGE);
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_non_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidFail)
                .then(() -> {
                    throw new RuntimeException(AN_EXPECTED_EXCEPTION_MUST_HAVE_BEEN_RAISED_BEFORE);
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when((CheckedConsumer<SystemUnderTest>) SystemUnderTest::fail)
                .then((Runnable) () -> {
                    throw new RuntimeException(AN_EXPECTED_EXCEPTION_MUST_HAVE_BEEN_RAISED_BEFORE);
                });
    }
}