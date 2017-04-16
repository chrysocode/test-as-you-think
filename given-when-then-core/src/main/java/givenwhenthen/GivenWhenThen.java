package givenwhenthen;

public class GivenWhenThen {

    public static <S> Given<S> givenSut(S systemUnderTest) {
        return new Given<>(systemUnderTest);
    }
}
