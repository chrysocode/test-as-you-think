package givenwhenthen;

public class SystemUnderTest {

    private GivenWhenThenDefinition givenWhenThenDefinition;
    private String state;

    public SystemUnderTest() {
        state = null;
    }

    public SystemUnderTest(GivenWhenThenDefinition givenWhenThenDefinition) {
        this.givenWhenThenDefinition = givenWhenThenDefinition;
    }

    private void whenAnEventHappens() {
        if (givenWhenThenDefinition != null) {
            givenWhenThenDefinition.whenAnEventHappensInRelationToAnActionOfTheConsumer();
        }
    }

    public String nonVoidMethod() {
        whenAnEventHappens();
        changeState();
        return "expected result";
    }

    public void voidMethod() {
        whenAnEventHappens();
        changeState();
    }

    public void fail() throws Throwable {
        whenAnEventHappens();
        throw new Exception("It fails!");
    }

    public String nonVoidFail() throws Throwable {
        whenAnEventHappens();
        throw new Exception("It fails!");
    }

    public void fail(Class<? extends Throwable> throwableClass) throws Throwable {
        whenAnEventHappens();
        throw throwableClass.newInstance();
    }

    private void changeState() {
        state = "state";
    }

    public void setGivenWhenThenDefinition(GivenWhenThenDefinition givenWhenThenDefinition) {
        this.givenWhenThenDefinition = givenWhenThenDefinition;
    }

    public String getState() {
        return state;
    }
}