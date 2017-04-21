package givenwhenthen;

import static givenwhenthen.GivenWhenThen.givenSutClass;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GivenSpecifiedFixturesTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        givenWhenThenDefinitionMock = strictMock(GivenWhenThenDefinition.class);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    private void orderedSteps(int numberOfGivenSteps) {
        givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
        expectLastCall().times(numberOfGivenSteps);
        givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();

        replay(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_specify_a_fixture() {
        // GIVEN
        orderedSteps(1);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given("what it makes this fixture specific to the current use case", () -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }).when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return sut.nonVoidMethod();
                }).then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                });
    }

    @Test
    public void should_specify_another_fixture() {
        // GIVEN
        orderedSteps(1);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given("what it makes this fixture specific to the current use case", sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    return sut.nonVoidMethod();
                }).then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                });
    }

    @Test
    public void should_specify_multiple_fixtures() {
        // GIVEN
        orderedSteps(3);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given("what it makes the first fixture specific to the current use case", () -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }).and("what it makes the second fixture specific to the current use case", () -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }).and("what it makes the third fixture specific to the current use case", sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    return sut.nonVoidMethod();
                }).then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                });
    }
}
