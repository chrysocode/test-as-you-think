package givenwhenthen;

public class SystemUnderTest {

    private GivenWhenThenDefinition givenWhenThenDefinition;

    public SystemUnderTest() {}

    public SystemUnderTest(GivenWhenThenDefinition givenWhenThenDefinition) {
        this.givenWhenThenDefinition = givenWhenThenDefinition;
    }

    public String nonVoidMethod() {
        if (givenWhenThenDefinition != null) {
            givenWhenThenDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        }
        return "expected result";
    }

    public void voidMethod() {
        givenWhenThenDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer();
    }

    public void setGivenWhenThenDefinition(GivenWhenThenDefinition givenWhenThenDefinition) {
        this.givenWhenThenDefinition = givenWhenThenDefinition;
    }
}