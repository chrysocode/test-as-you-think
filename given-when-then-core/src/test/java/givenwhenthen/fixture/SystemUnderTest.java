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

    public void voidMethodWithParameter(String parameter) {
        voidMethod();
    }

    public String nonVoidMethodWithParameter(String parameter) {
        return nonVoidMethod();
    }

    public void voidMethodWithTwoParameters(String parameter1, Integer parameter2) {
        voidMethod();
    }

    public String nonVoidMethodWithTwoParameters(String parameter1, Integer parameter2) {
        return nonVoidMethod();
    }

    public void voidMethodWithThreeParameters(String parameter1, Integer parameter2, Boolean parameter3) {
        voidMethod();
    }

    public String nonVoidMethodWithThreeParameters(String parameter1, Integer parameter2, Boolean parameter3) {
        return nonVoidMethod();
    }

    public void failWithParameter(String parameter) throws Throwable {}

    public void failWithTwoParameters(String parameter1, Integer parameter2) throws Throwable {}

    public void failWithThreeParameters(String parameter1, Integer parameter2, Boolean parameter3) throws Throwable {}

    public String nonVoidFailWithParameter(String parameter) throws Throwable {
        return null;
    }

    public String nonVoidFailWithTwoParameters(String parameter1, Integer parameter2) throws Throwable {
        return null;
    }

    public String nonVoidFailWithThreeParameters(String parameter1, Integer parameter2,
            Boolean parameter3) throws Throwable {
        return null;
    }

    public void voidMethodWithThrowsClause() throws Throwable {}

    public String nonVoidMethodWithThrowsClause() throws Throwable {
        return null;
    }

    public String methodWithThrowsClause() throws Throwable {
        return null;
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