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

    public void voidMethodWithArgument(String input) {
        voidMethod();
    }

    public String nonVoidMethodWithArgument(String input) {
        return nonVoidMethod();
    }

    public void voidMethodWithTwoArguments(String input1, Integer input2) {
        voidMethod();
    }

    public String nonVoidMethodWithTwoArguments(String input1, Integer input2) {
        return nonVoidMethod();
    }

    public void voidMethodWithThreeArguments(String input1, Integer input2, Boolean input3) {
        voidMethod();
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