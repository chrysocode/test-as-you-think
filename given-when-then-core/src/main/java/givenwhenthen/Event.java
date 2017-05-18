package givenwhenthen;

import givenwhenthen.function.CheckedConsumer;
import givenwhenthen.function.CheckedFunction;
import givenwhenthen.function.Functions;

import static org.assertj.core.api.Assertions.fail;

class Event<$SystemUnderTest, $Result> {

    private final Functions functions = Functions.INSTANCE;
    private final $SystemUnderTest systemUnderTest;
    private final CheckedFunction<$SystemUnderTest, $Result> whenStep;

    Event($SystemUnderTest systemUnderTest, CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        this.systemUnderTest = systemUnderTest;
        this.whenStep = whenStep;
    }

    Event($SystemUnderTest systemUnderTest, CheckedConsumer<$SystemUnderTest> whenStep) {
        this.systemUnderTest = systemUnderTest;
        this.whenStep = functions.toFunction(whenStep);
    }

    $Result happen() {
        $Result result = null;
        try {
            result = whenStep.apply(systemUnderTest);
        } catch (Throwable throwable) {
            fail("Unexpected exception happened!", throwable);
        }

        return result;
    }
}