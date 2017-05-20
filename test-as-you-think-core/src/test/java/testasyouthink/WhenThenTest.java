package testasyouthink;

import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static testasyouthink.GivenWhenThen.givenSut;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.verify;

public class WhenThenTest {

    private static final String EXPECTED_RESULT = "expected result";
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(0, 1, 1);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_follow_the_when_then_partial_sequence_given_a_non_void_method() {
        // WHEN
        givenSut(new SystemUnderTest(givenWhenThenDefinitionMock))
                .when(SystemUnderTest::nonVoidMethod)
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                });
    }

    @Test
    public void should_follow_the_when_then_partial_sequence_given_a_void_method() {
        // WHEN
        givenSut(new SystemUnderTest(givenWhenThenDefinitionMock))
                .when(SystemUnderTest::voidMethod)
                .then(() -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_follow_the_when_then_partial_sequence_given_a_sut_class_to_be_instantiated() {
        // WHEN
        GivenWhenThen
                .givenSutClass(SystemUnderTest.class)
                .when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return sut.nonVoidMethod();
                })
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                });
    }
}