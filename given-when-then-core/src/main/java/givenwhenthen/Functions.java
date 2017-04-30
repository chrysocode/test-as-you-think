package givenwhenthen;

class Functions {

    <T> CheckedFunction<T, Throwable> returnThrowable(CheckedConsumer<T> checkedFunction) {
        return sut -> {
            Throwable result = null;
            try {
                checkedFunction.accept(sut);
            } catch (Throwable throwable) {
                result = throwable;
            }
            return result;
        };
    }
}
