package givenwhenthen;

import givenwhenthen.fixture.ExpectedException;
import givenwhenthen.fixture.GivenWhenThenDefinition;
import givenwhenthen.fixture.SystemUnderTest;
import givenwhenthen.fixture.UnexpectedException;
import givenwhenthen.function.CheckedConsumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static givenwhenthen.GivenWhenThen.givenSut;
import static givenwhenthen.GivenWhenThen.givenSutClass;
import static givenwhenthen.fixture.GivenWhenThenDefinition.orderedSteps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

public class ThenFailuresTest {

    private static final String EXPECTED_MESSAGE = "expected message";
    private static final String MISSING_EXCEPTION = "An expected exception must have been raised before!";
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
                .becauseOf(IllegalStateException.class);
    }

    @Test
    public void should_fail_given_an_unexpected_exception() throws Throwable {
        // GIVEN
        reset(givenWhenThenDefinitionMock);
        replay(givenWhenThenDefinitionMock);

        SystemUnderTest systemUnderTestMock = strictMock(SystemUnderTest.class);
        expect(systemUnderTestMock.methodWithThrowsClause()).andThrow(new UnexpectedException());
        replay(systemUnderTestMock);

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::methodWithThrowsClause)
                .thenItFails()
                .becauseOf(ExpectedException.class));

        // THEN
        assertThat(thrown).isInstanceOf(AssertionError.class);
        verify(systemUnderTestMock);
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
                .thenItFails()
                .becauseOf(IllegalStateException.class)
                .withMessage(EXPECTED_MESSAGE);
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
                    throw new RuntimeException(MISSING_EXCEPTION);
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
                    throw new RuntimeException(MISSING_EXCEPTION);
                });
    }
}