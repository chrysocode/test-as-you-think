package givenwhenthen;

public class GivenWhenThen {

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSut($SystemUnderTest systemUnderTest) {
        return new Given<>(systemUnderTest);
    }

    public static <$SystemUnderTest> Given<$SystemUnderTest> givenSutClass(Class<$SystemUnderTest> sutClass)
            throws InstantiationException, IllegalAccessException {
        return new Given<$SystemUnderTest>(sutClass.newInstance());
    }
}
