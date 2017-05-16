package givenwhenthen;

import givenwhenthen.fixture.GivenWhenThenDefinition;
import givenwhenthen.fixture.SystemUnderTest;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static givenwhenthen.GivenWhenThen.givenSut;
import static org.easymock.EasyMock.*;

public class GivenArgumentsThenFailuresTest {

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
        systemUnderTestMock.failWithParameter("given argument");
        expectLastCall().andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .when(SystemUnderTest::failWithParameter)
                .then((Runnable) () -> {
                    throw new RuntimeException("An expected exception must have been risen before!");
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_non_void_method_with_one_parameter() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expect(systemUnderTestMock.nonVoidFailWithParameter("given argument")).andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .when(SystemUnderTest::nonVoidFailWithParameter)
                .then(() -> {
                    throw new RuntimeException("An expected exception must have been risen before!");
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_void_method_with_two_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        systemUnderTestMock.failWithTwoParameters("given argument", 201705);
        expectLastCall().andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 201705;
                })
                .when(SystemUnderTest::failWithTwoParameters)
                .then((Runnable) () -> {
                    throw new RuntimeException("An expected exception must have been risen before!");
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_non_void_method_with_two_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(2);
        expect(systemUnderTestMock.nonVoidFailWithTwoParameters("given argument", 201705)).andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 201705;
                })
                .when(SystemUnderTest::nonVoidFailWithTwoParameters)
                .then(() -> {
                    throw new RuntimeException("An expected exception must have been risen before!");
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_void_method_with_three_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        systemUnderTestMock.failWithThreeParameters("given argument", 201705, false);
        expectLastCall().andThrow(new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 201705;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return false;
                })
                .when(SystemUnderTest::failWithThreeParameters)
                .then(sut -> {
                    throw new RuntimeException("An expected exception must have been risen before!");
                });
    }

    @Test(expected = AssertionError.class)
    public void should_fail_given_a_non_void_method_with_three_parameters() throws Throwable {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(3);
        expect(systemUnderTestMock.nonVoidFailWithThreeParameters("given argument", 201705, false)).andThrow(
                new Exception());
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return 201705;
                })
                .andArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return false;
                })
                .when(SystemUnderTest::nonVoidFailWithThreeParameters)
                .then(() -> {
                    throw new RuntimeException("An expected exception must have been risen before!");
                });
    }
}
