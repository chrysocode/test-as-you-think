package givenwhenthen;

public class GivenWhenThen {

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSut($SystemUnderTest systemUnderTest) {
        return new Given<>(systemUnderTest);
    }
}
