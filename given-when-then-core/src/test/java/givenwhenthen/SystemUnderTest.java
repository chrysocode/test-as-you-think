package givenwhenthen;

public class SystemUnderTest {

    private GivenWhenThenDefinition givenWhenThenDefinition;

    public SystemUnderTest(GivenWhenThenDefinition givenWhenThenDefinition) {
        this.givenWhenThenDefinition = givenWhenThenDefinition;
    }

    public String nonVoidMethod() {
        givenWhenThenDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        return "expected result";
    }

    public void voidMethod() {
        givenWhenThenDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer();
    }
}