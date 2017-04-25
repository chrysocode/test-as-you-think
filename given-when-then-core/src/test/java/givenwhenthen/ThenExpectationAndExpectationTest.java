package givenwhenthen;

import static givenwhenthen.GivenWhenThen.givenSutClass;
import static givenwhenthen.GivenWhenThenDefinition.orderedSteps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ThenExpectationAndExpectationTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @Before
    public void prepareFixtures() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 3);
    }

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_verify_result_expectations_separately_given_a_non_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }).when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    return sut.nonVoidMethod();
                }).then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).startsWith("expected");
                }).and(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).contains(" ");
                }).and(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).endsWith("result");
                });
    }

    @Test
    public void should_verify_expectations_separately_given_a_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(() -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }).when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    sut.voidMethod();
                }).then(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                }).and(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                }).and(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_verify_sut_expectations_separately_given_a_void_method() {
        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                }).when(sut -> {
                    givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    sut.voidMethod();
                }).then(sut -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                }).and(sut -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                }).and(sut -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }
}
