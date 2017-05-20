package testasyouthink;

import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import org.junit.After;
import org.junit.Test;

import static testasyouthink.GivenWhenThen.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.verify;

public class GivenSpecifiedFixturesTest {

    private static final String EXPECTED_RESULT = "expected result";
    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_specify_a_fixture() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given("what it makes this fixture specific to the current use case",
                        () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return sut.nonVoidMethod();
                })
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                });
    }

    @Test
    public void should_specify_another_fixture() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given("what it makes this fixture specific to the current use case", sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                });
    }

    @Test
    public void should_specify_multiple_fixtures() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given("what it makes the first fixture specific to the current use case",
                        () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .and("what it makes the second fixture specific to the current use case",
                        () -> givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem())
                .and("what it makes the third fixture specific to the current use case", sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo(EXPECTED_RESULT);
                });
    }
}
