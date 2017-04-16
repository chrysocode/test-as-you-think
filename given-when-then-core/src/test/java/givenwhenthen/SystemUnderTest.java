package givenwhenthen;

public class SystemUnderTest {

    private String voidMethodResult;

    public String nonVoidMethod() {
        return "expected result";
    }

    public void voidMethod() {
        voidMethodResult = "expected result";
    }

    public String getVoidMethodResult() {
        return voidMethodResult;
    }
}