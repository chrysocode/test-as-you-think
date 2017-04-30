package givenwhenthen;

import static org.assertj.core.api.Assertions.fail;

class GivenWhenContext<$SystemUnderTest, $Result> {

    private final Preparation<$SystemUnderTest> preparation;
    private CheckedFunction<$SystemUnderTest, $Result> whenStep;

    GivenWhenContext(Preparation<$SystemUnderTest> preparation) {
        this.preparation = preparation;
    }

    $Result returnResultOrVoid() {
        preparation.prepareFixtures();

        $Result result = null;
        try {
            result = whenStep.apply(preparation.getSystemUnderTest());
        } catch (Throwable throwable) {
            fail("Unexpected exception happened!", throwable);
        }

        return result;
    }

    $SystemUnderTest getSystemUnderTest() {
        return preparation.getSystemUnderTest();
    }

    void setWhenStep(CheckedFunction<$SystemUnderTest, $Result> whenStep) {
        this.whenStep = whenStep;
    }
}