package testasyouthink;

import testasyouthink.fixture.ExpectedException;
import testasyouthink.fixture.SystemUnderTest;
import testasyouthink.fixture.UnexpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static testasyouthink.TestAsYouThink.givenSut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

public class ThenFailuresTest {

    private static final String EXPECTED_MESSAGE = "expected message";
    private static final String MISSING_EXCEPTION = "An expected exception must have been raised before!";
    private static final String UNEXPECTED_MESSAGE = "unexpected message";
    private SystemUnderTest systemUnderTestMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        systemUnderTestMock = strictMock(SystemUnderTest.class);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(systemUnderTestMock);
    }

    @Test
    public void should_verify_the_sut_fails() throws Throwable {
        // GIVEN
        expect(systemUnderTestMock.methodWithThrowsClause()).andThrow(new Exception());
        replay(systemUnderTestMock);

        // WHEN
        givenSut(systemUnderTestMock)
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::methodWithThrowsClause)
                .thenItFails();
    }

    @Test
    public void should_verify_the_sut_fails_by_raising_an_expected_exception() throws Throwable {
        // GIVEN
        expect(systemUnderTestMock.methodWithThrowsClause()).andThrow(new ExpectedException());
        replay(systemUnderTestMock);

        // WHEN
        givenSut(systemUnderTestMock)
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::methodWithThrowsClause)
                .thenItFails()
                .becauseOf(ExpectedException.class);
    }

    @Test
    public void should_fail_given_an_unexpected_exception() throws Throwable {
        // GIVEN
        expect(systemUnderTestMock.methodWithThrowsClause()).andThrow(new UnexpectedException());
        replay(systemUnderTestMock);

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::methodWithThrowsClause)
                .thenItFails()
                .becauseOf(ExpectedException.class));

        // THEN
        assertThat(thrown).isInstanceOf(AssertionError.class);
    }

    @Test
    public void should_verify_the_sut_fails_by_raising_an_expected_exception_with_an_expected_message() throws
            Throwable {
        // GIVEN
        expect(systemUnderTestMock.methodWithThrowsClause()).andThrow(new ExpectedException(EXPECTED_MESSAGE));
        replay(systemUnderTestMock);

        // WHEN
        givenSut(systemUnderTestMock)
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::methodWithThrowsClause)
                .thenItFails()
                .becauseOf(ExpectedException.class)
                .withMessage(EXPECTED_MESSAGE);
    }

    @Test
    public void should_fail_given_an_unexpected_message() throws Throwable {
        // GIVEN
        expect(systemUnderTestMock.methodWithThrowsClause()).andThrow(new ExpectedException(UNEXPECTED_MESSAGE));
        replay(systemUnderTestMock);

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::methodWithThrowsClause)
                .thenItFails()
                .becauseOf(ExpectedException.class)
                .withMessage(EXPECTED_MESSAGE));

        // THEN
        assertThat(thrown).isInstanceOf(AssertionError.class);
    }

    @Test
    public void should_fail_given_a_non_void_method() throws Throwable {
        // GIVEN
        expect(systemUnderTestMock.nonVoidMethodWithThrowsClause()).andThrow(new Exception());
        replay(systemUnderTestMock);

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                .when(SystemUnderTest::nonVoidMethodWithThrowsClause)
                .then(() -> {
                    throw new RuntimeException(MISSING_EXCEPTION);
                }));

        // THEN
        assertThat(thrown).isInstanceOf(AssertionError.class);
    }

    @Test
    public void should_fail_given_a_void_method() throws Throwable {
        // GIVEN
        systemUnderTestMock.voidMethodWithThrowsClause();
        expectLastCall().andThrow(new Exception());
        replay(systemUnderTestMock);

        // WHEN
        Throwable thrown = catchThrowable(() -> givenSut(systemUnderTestMock)
                .when(SystemUnderTest::voidMethodWithThrowsClause)
                .then((Runnable) () -> {
                    throw new RuntimeException(MISSING_EXCEPTION);
                }));

        // THEN
        assertThat(thrown).isInstanceOf(AssertionError.class);
    }
}