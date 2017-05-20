package testasyouthink;

import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;
import org.junit.After;
import org.junit.ComparisonFailure;
import org.junit.Test;

import static testasyouthink.TestAsYouThink.givenSutClass;
import static testasyouthink.fixture.GivenWhenThenDefinition.orderedSteps;
import static java.util.Arrays.asList;
import static org.easymock.EasyMock.verify;

public class ThenPredicateTest {

    private GivenWhenThenDefinition givenWhenThenDefinitionMock;

    @After
    public void verifyMocks() {
        // THEN
        verify(givenWhenThenDefinitionMock);
    }

    @Test
    public void should_receive_a_then_step_as_a_result_predicate_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                })
                .and(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                })
                .and(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                });
    }

    @Test(expected = ComparisonFailure.class)
    public void should_receive_a_failing_then_step_as_a_predicate_on_the_result_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 1);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                });
    }

    @Test(expected = ComparisonFailure.class)
    public void should_receive_a_failing_andthen_step_as_a_predicate_on_the_result_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                })
                .and(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                });
    }

    @Test
    public void should_receive_a_then_step_as_a_predicate_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 3);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                })
                .and(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                })
                .and(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                });
    }

    @Test(expected = ComparisonFailure.class)
    public void should_receive_a_failing_then_step_as_a_predicate_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 1);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                });
    }

    @Test(expected = ComparisonFailure.class)
    public void should_receive_a_failing_and_then_step_as_a_predicate_given_a_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::voidMethod)
                .then(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                })
                .and(() -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                });
    }

    @Test
    public void should_receive_the_then_steps_as_predicates_on_the_result_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(asList(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                }, result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                }));
    }

    @Test(expected = ComparisonFailure.class)
    public void
    should_receive_the_then_steps_as_predicates_on_the_result_given_a_non_void_method_and_a_failing_then_step() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 2);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(asList(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                }, result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                }));
    }

    @Test
    public void should_receive_a_then_step_as_a_predicate_on_the_system_and_the_result_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 1);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then((sut, result) -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return "expected result".equals(result) && sut != null;
                });
    }

    @Test(expected = ComparisonFailure.class)
    public void
    should_receive_a_failing_then_step_as_a_predicate_on_the_system_and_the_result_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 1);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then((sut, result) -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                });
    }

    @Test
    public void should_receive_the_then_steps_as_predicates_on_the_system_or_the_result_given_a_non_void_method() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 2);

        // THEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                }, sut -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                });
    }

    @Test(expected = ComparisonFailure.class)
    public void
    should_receive_the_then_steps_as_predicates_on_the_system_or_the_result_given_a_non_void_method_and_a_failing_then_step_on_the_result() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 1);

        // THEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                }, sut -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                });
    }

    @Test(expected = ComparisonFailure.class)
    public void
    should_receive_the_then_steps_as_predicates_on_the_sut_or_the_result_given_a_non_void_method_and_a_failing_then_step_on_the_sut() {
        // GIVEN
        givenWhenThenDefinitionMock = orderedSteps(1, 2);

        // THEN
        givenSutClass(SystemUnderTest.class)
                .given(sut -> {
                    givenWhenThenDefinitionMock.givenAContextThatDefinesTheInitialStateOfTheSystem();
                    sut.setGivenWhenThenDefinition(givenWhenThenDefinitionMock);
                })
                .when(SystemUnderTest::nonVoidMethod)
                .then(result -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return true;
                }, sut -> {
                    givenWhenThenDefinitionMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    return false;
                });
    }
}
