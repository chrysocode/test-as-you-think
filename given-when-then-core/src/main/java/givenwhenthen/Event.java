package givenwhenthen;

import static org.assertj.core.api.Assertions.fail;

class Event<$SystemUnderTest, $Result> {

    private final $SystemUnderTest systemUnderTest;
    private final CheckedFunction<$SystemUnderTest, $Result> whenStep;

    Event($SystemUnderTest systemUnderTest, CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        this.systemUnderTest = systemUnderTest;
        this.whenStep = whenStep;
    }

    Event($SystemUnderTest systemUnderTest, CheckedConsumer<$SystemUnderTest> whenStep) {
        this.systemUnderTest = systemUnderTest;
        this.whenStep = toCheckedFunction(whenStep);
    }

    private CheckedFunction<$SystemUnderTest, $Result> toCheckedFunction(CheckedConsumer<$SystemUnderTest> whenStep) {
        return sut -> {
            whenStep.accept(sut);
            return null;
        };
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