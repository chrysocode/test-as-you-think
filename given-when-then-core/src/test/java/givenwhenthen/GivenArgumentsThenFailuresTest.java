package givenwhenthen;

import givenwhenthen.fixture.GivenWhenThenDefinition;
import givenwhenthen.fixture.SystemUnderTest;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static givenwhenthen.GivenWhenThen.givenSut;
import static org.assertj.core.api.Assertions.fail;
import static org.easymock.EasyMock.createStrictControl;
import static org.easymock.EasyMock.expectLastCall;

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
    public void should_fail_given_a_void_method_with_one_parameter() {
        // GIVEN
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        try {
            systemUnderTestMock.failWithParameter("given argument");
            expectLastCall().andThrow(new Exception("It fails!"));
        } catch (Throwable throwable) {
            fail("Unexpected failure!");
        }
        mocksControl.replay();

        // WHEN
        givenSut(systemUnderTestMock)
                .givenArgument(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    return "given argument";
                })
                .when(SystemUnderTest::failWithParameter)
                .then(result -> {});
    }
}
