package testasyouthink;

import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static testasyouthink.GivenWhenThen.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.strictMock;
import static org.easymock.EasyMock.verify;

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
    public void should_specify_a_result_expectation_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 1);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then("what the focus of this expectation is", result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).isEqualTo("expected result");
                });
    }

    @Test
    public void should_specify_an_expectation_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 1);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then("what the focus of this expectation is",
                        () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_specify_separated_expectations_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then("what the focus of this expectation is", result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).contains("expected");
                })
                .and("what the focus of this expectation is", result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).contains("result");
                })
                .and("what the focus of this expectation is",
                        () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_specify_separated_expectations_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::voidMethod)
                .then("what the focus of this expectation is",
                        () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                .and("what the focus of this other expectation is",
                        () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                .and("what the focus of this other expectation is",
                        () -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }

    @Test
    public void should_specify_separated_sut_expectations_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::voidMethod)
                .then("what the focus of this expectation is",
                        sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                .and("what the focus of this other expectation is",
                        sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult())
                .and("what the focus of this other expectation is",
                        sut -> givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult());
    }
}
