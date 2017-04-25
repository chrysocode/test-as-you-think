package givenwhenthen;

import static org.assertj.core.api.Assertions.fail;

import java.util.List;
import java.util.function.Consumer;

class GivenWhenContext<$SystemUnderTest, $Result> {

    private $SystemUnderTest systemUnderTest;
    private List<Consumer<$SystemUnderTest>> givenSteps;
    private CheckedFunction<$SystemUnderTest, $Result> whenStep;

    GivenWhenContext($SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    $Result returnResultOrVoid() {
        if (givenSteps != null && !givenSteps.isEmpty()) {
            givenSteps.stream().forEach(step -> step.accept(systemUnderTest));
        }

        $Result result = null;
        try {
            result = whenStep.apply(systemUnderTest);
        } catch (Throwable throwable) {
            fail("Unexpected exception happened!", throwable);
        }

        return result;
    }

    $SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }

    void setGivenSteps(List<Consumer<$SystemUnderTest>> givenSteps) {
        this.givenSteps = givenSteps;
    }

    void setWhenStep(CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        this.whenStep = whenStep;
    }
}