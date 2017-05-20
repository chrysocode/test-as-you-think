package givenwhenthen.fixture;

import static org.easymock.EasyMock.*;

public class GivenWhenThenDefinition {

    private static final int DEFAULT_NUMBER_OF_STEPS = 1;

    public static GivenWhenThenDefinition orderedSteps() {
        return orderedSteps(DEFAULT_NUMBER_OF_STEPS, DEFAULT_NUMBER_OF_STEPS, DEFAULT_NUMBER_OF_STEPS);
    }

    public static GivenWhenThenDefinition orderedSteps(int numberOfGivenSteps) {
        return orderedSteps(numberOfGivenSteps, DEFAULT_NUMBER_OF_STEPS, DEFAULT_NUMBER_OF_STEPS);
    }

    public static GivenWhenThenDefinition orderedSteps(int numberOfGivenSteps, int numberOfThenSteps) {
        return orderedSteps(numberOfGivenSteps, DEFAULT_NUMBER_OF_STEPS, numberOfThenSteps);
    }

    public static GivenWhenThenDefinition orderedSteps(int numberOfGivenSteps, int numberOfWhenSteps,
            int numberOfThenSteps) {
        GivenWhenThenDefinition mock = strictMock(GivenWhenThenDefinition.class);

        if (numberOfGivenSteps > 0) {
            mock.givenAContextThatDefinesTheInitialStateOfTheSystem();
            expectLastCall().times(numberOfGivenSteps);
        }
        if (numberOfWhenSteps > 0) {
            mock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
            expectLastCall().times(numberOfWhenSteps);
        }
        if (numberOfThenSteps > 0) {
            mock.thenTheActualResultIsInKeepingWithTheExpectedResult();
            expectLastCall().times(numberOfThenSteps);
        }

        replay(mock);
        return mock;
    }

    public void givenAContextThatDefinesTheInitialStateOfTheSystem() {}

    public void whenAnEventHappensInRelationToAnActionOfTheConsumer() {}

    public void thenTheActualResultIsInKeepingWithTheExpectedResult() {}
}
