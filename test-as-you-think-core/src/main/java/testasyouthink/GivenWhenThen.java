package testasyouthink;

import testasyouthink.GivenWhenThenDsl.PreparationStage.Given;

public class GivenWhenThen {

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSut($SystemUnderTest systemUnderTest) {
        return new GivenWhenSteps<>(systemUnderTest);
    }

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSutClass(Class<$SystemUnderTest> sutClass) {
        try {
            return new GivenWhenSteps<>(sutClass.newInstance());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
