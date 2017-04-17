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

@SuppressWarnings("unused")
public class ThenSpecifiedExpectationsTest {

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

    @Test
    public void should_specify_an_expectation_given_a_non_void_method() {
        // GIVEN
        ordered_steps: {
            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
            givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        }
        replay(givenWhenThenDefinitionMock);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    return sut.nonVoidMethod();
                }).then("what the focus of this expectation is", result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                }).go();
    }

    @Test
    public void should_specify_an_expectation_given_a_void_method() {
        // GIVEN
        ordered_steps: {
            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
            givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
        }
        replay(givenWhenThenDefinitionMock);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    sut.nonVoidMethod();
                }).then("what the focus of this expectation is", () -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                });
    }

    @Test
    public void should_specify_multiple_expectations() {
        // GIVEN
        ordered_steps: {
            givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
            givenWhenThenDefinitionMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
            expectLastCall().times(2);
        }
        replay(givenWhenThenDefinitionMock);

        // WHEN
        givenSutClass(SystemUnderTest.class) //
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                }).when(sut -> {
                    return sut.nonVoidMethod();
                }).then("what the focus of this expectation is", result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).contains("expected");
                }).and("what the focus of this expectation is", result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).contains("result");
                }).go();
    }
}
