package givenwhenthen;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.strictMock;

public class GivenWhenThenDefinition {

    public static GivenWhenThenDefinition orderedSteps() {
        return orderedSteps(1, 1, 1);
    }

    public static GivenWhenThenDefinition orderedSteps(int numberOfGivenSteps) {
        return orderedSteps(numberOfGivenSteps, 1, 1);
    }

    public static GivenWhenThenDefinition orderedSteps(int numberOfGivenSteps, int numberOfThenSteps) {
        return orderedSteps(numberOfGivenSteps, 1, numberOfThenSteps);
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
