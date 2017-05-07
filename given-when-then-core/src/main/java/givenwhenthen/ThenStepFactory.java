package givenwhenthen;

enum ThenStepFactory {

    INSTANCE;

    <$SystemUnderTest, $Result> ThenStep<$SystemUnderTest, $Result> createThenStep(Preparation<$SystemUnderTest>
                                                                                           preparation,
                                                                                   CheckedFunction<$SystemUnderTest,
                                                                                           $Result> whenStep) {
        Event<$SystemUnderTest, $Result> event = new Event<>(preparation.getSystemUnderTest(), whenStep);
        GivenWhenContext<$SystemUnderTest, $Result> context = new GivenWhenContext<>(preparation, event);
        return new ThenStep<>(context);
    }

    <$SystemUnderTest> ThenWithoutResultStep<$SystemUnderTest> createThenWithoutResultStep
            (Preparation<$SystemUnderTest> preparation, CheckedConsumer<$SystemUnderTest> whenStep) {
        Event<$SystemUnderTest, Void> event = new Event<>(preparation.getSystemUnderTest(), whenStep);
        GivenWhenContext<$SystemUnderTest, Void> context = new GivenWhenContext<>(preparation, event);
        return new ThenWithoutResultStep<>(context);
    }
}
