package testasyouthink;

class GivenWhenContext<$SystemUnderTest, $Result> {

    private final Preparation<$SystemUnderTest> preparation;
    private final Event<$SystemUnderTest, $Result> event;
    private $Result result;

    GivenWhenContext(Preparation<$SystemUnderTest> preparation, Event<$SystemUnderTest, $Result> event) {
        this.preparation = preparation;
        this.event = event;
    }

    $Result returnResultOrVoid() {
        if (result == null) {
            preparation.prepareFixtures();
            result = event.happen();
        }
        return result;
    }

    $SystemUnderTest getSystemUnderTest() {
        return preparation.getSystemUnderTest();
    }
}