package givenwhenthen.fixture;

public class SystemUnderTest {

    private GivenWhenThenDefinition givenWhenThenDefinition;
    private String state;

    public SystemUnderTest() {}

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

    public <$Input> void voidMethodWithArgument($Input input) {
        voidMethod();
    }

    public <$Input> String nonVoidMethodWithArgument($Input input) {
        return nonVoidMethod();
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

    public void fail(Class<? extends Throwable> throwableClass, String message) throws Throwable {
        whenAnEventHappens();
        throw (Throwable) Class
                .forName(throwableClass.getName())
                .getConstructor(String.class)
                .newInstance(message);
    }

    private void changeState() {
        state = "state updated";
    }

    public void setGivenWhenThenDefinition(GivenWhenThenDefinition givenWhenThenDefinition) {
        this.givenWhenThenDefinition = givenWhenThenDefinition;
    }

    public String getState() {
        return state;
    }
}