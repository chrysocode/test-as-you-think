package givenwhenthen;

class GivenWhenContext<$SystemUnderTest, $Result> {

    private final Preparation<$SystemUnderTest> preparation;
    private final Event<$SystemUnderTest, $Result> event;

    GivenWhenContext(Preparation<$SystemUnderTest> preparation, Event<$SystemUnderTest, $Result> event) {
        this.preparation = preparation;
        this.event = event;
    }

    $Result returnResultOrVoid() {
        preparation.prepareFixtures();
        return event.happen();
    }

    $SystemUnderTest getSystemUnderTest() {
        return preparation.getSystemUnderTest();
    }
}