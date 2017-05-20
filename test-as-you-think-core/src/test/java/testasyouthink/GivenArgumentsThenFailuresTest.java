package testasyouthink;

import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static testasyouthink.TestAsYouThink.givenSut;
import static org.easymock.EasyMock.*;

public class GivenArgumentsThenFailuresTest {

    private static final String GIVEN_STRING = "given argument";
    private static final int GIVEN_INTEGER = 201705;
    private static final boolean GIVEN_BOOLEAN = false;
    private static final String MISSING_EXCEPTION = "An expected exception must have been raised before!";

    private IMocksControl mocksControl;
    private SystemUnderTest systemUnderTestMock;
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        mocksControl = createStrictControl();
        systemUnderTestMock = mocksControl.createMock(SystemUnderTest.class);
        givenWhenThenDefinitionMock = mocksControl.createMock(GivenWhenThenDefinition.class);
    }

    @After
    public void verifyMocks() {
        // THEN
        mocksControl.verify();
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_void_method_with_one_parameter() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        systemUnderTestMock.failWithParameter(GIVEN_STRING);
        expectLastCall().andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .when(SystemUnderTest::failWithParameter)
                .then((Runnable) () -> {
                    throw new RuntimeException(MISSING_EXCEPTION);
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_non_void_method_with_one_parameter() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expect(systemUnderTestMock.nonVoidFailWithParameter(GIVEN_STRING)).andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .when(SystemUnderTest::nonVoidFailWithParameter)
                .then(() -> {
                    throw new RuntimeException(MISSING_EXCEPTION);
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_void_method_with_two_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        systemUnderTestMock.failWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER);
        expectLastCall().andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .when(SystemUnderTest::failWithTwoParameters)
                .then((Runnable) () -> {
                    throw new RuntimeException(MISSING_EXCEPTION);
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_non_void_method_with_two_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        expect(systemUnderTestMock.nonVoidFailWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER)).andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .when(SystemUnderTest::nonVoidFailWithTwoParameters)
                .then(() -> {
                    throw new RuntimeException(MISSING_EXCEPTION);
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_void_method_with_three_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        systemUnderTestMock.failWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER, GIVEN_BOOLEAN);
        expectLastCall().andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_BOOLEAN;
                })
                .when(SystemUnderTest::failWithThreeParameters)
                .then(sut -> {
                    throw new RuntimeException(MISSING_EXCEPTION);
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_non_void_method_with_three_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        expect(systemUnderTestMock.nonVoidFailWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER, GIVEN_BOOLEAN)).andThrow(
                new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_BOOLEAN;
                })
                .when(SystemUnderTest::nonVoidFailWithThreeParameters)
                .then(() -> {
                    throw new RuntimeException(MISSING_EXCEPTION);
                });
    }

    @Test
    public void should_verify_the_sut_fails_given_one_method_parameter() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        systemUnderTestMock.failWithParameter(GIVEN_STRING);
        expectLastCall().andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::failWithParameter)
                .thenItFails();
    }

    @Test
    public void should_verify_the_sut_fails_given_two_method_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        systemUnderTestMock.failWithTwoParameters(GIVEN_STRING, GIVEN_INTEGER);
        expectLastCall().andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::failWithTwoParameters)
                .thenItFails();
    }

    @Test
    public void should_verify_the_sut_fails_given_three_method_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        systemUnderTestMock.failWithThreeParameters(GIVEN_STRING, GIVEN_INTEGER, GIVEN_BOOLEAN);
        expectLastCall().andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_STRING;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_INTEGER;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return GIVEN_BOOLEAN;
                })
                .whenSutRunsOutsideOperatingConditions(SystemUnderTest::failWithThreeParameters)
                .thenItFails();
    }
}
