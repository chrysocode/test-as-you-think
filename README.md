What you think is what you test... Not yet another testing API or framework!

# Fluent testing and added value

*TestAsYouThink* is an open source software library in Java for testing purposes. It is designed as a **fluent API** that will change the way development teams write their unit and integration tests. It aims to take control over the coding practices as **executable guidelines**, from beginners to experts, to get **high-quality tests**. Why should you adopt *TestAsYouThink*?
- It promotes good coding practices for testing on writing tests rather than before it with training or after it with code reviews.
- It enables to give a better structure based on compilable code rather than textual comments to the test code.
- It improves test code readability and may bring more conciseness.
- It is designed to be easy to use thanks to code completion.
- It builds new original features to test execution.

Why to name this API *TestAsYouThink*? The goal of *TestAsYouThink* is to map out the road from a new software functionality idea to its contractualized achievement as an executable test, while preserving product developers against known pitfalls. According to this perspective, any pitfall is likely to extend the developer's journey and to put him off his target. By anticipating such pitfalls, *TestAsYouThink* will be the best way to reduce the distance to proper, durable testing.

Moreover *TestAsYouThink* uses the Given-When-Then canvas as a formal guide to compose tests. This canvas originally comes from [Gherkin](https://sites.google.com/site/unclebobconsultingllc/the-truth-about-bdd) that is a grammatical protocol used in the [Behavior-Driven Development](https://en.wikipedia.org/wiki/Behavior-driven_development) method to write test scenarii in a business human-readable way by specifying a software behavior basing on concrete examples. Given-When-Then serves to divide any test into the three eponym steps. This canvas is implemented by the *TestAsYouThink* project to deliver a [DSL](https://en.wikipedia.org/wiki/Domain-specific_language) style fluent API.

# Getting Started

## Installation

Add *TestAsYouThink* as a dependency to your project with Maven, or download it from [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Ctest%20as%20you%20think).
```xml
<dependency>
    <groupId>com.github.xapn</groupId>
    <artifactId>test-as-you-think-core</artifactId>
    <version>0.3</version>
</dependency>
```

## Basics

Here is the minimal syntax to implement your test methods for a `SystemUnderTest` class.
```java
givenSutClass(SystemUnderTest.class)
.when(sut -> {})
.then(() -> {});
```

Let us complete the previous scenario with a very simple example of what you can do, while testing a non-void method of your system under test.
```java
import static testasyouthink.TestAsYouThink.givenSutClass;
...

givenSutClass(SystemUnderTest.class)
.given(sut -> {
    // Where you prepare the context that defines the initial state of the system under test (SUT).
    DataSet dataSet = new DataSet(...);
    sut.setAnyAttribute(...);
}).when(sut -> {
    // Where you make an event happen in relation to an action of a customer.
    return sut.nonVoidMethod(...);
}).then(result -> {
    // Where you verify the expectations are reached, by using your favorite assertion API.
    assertThat(result).isEqualTo(...);
});
```

Notice that:
- any Given-When-Then step can be implemented by a lambda expression or a method reference;
- you manipule the same SUT type from the beginning to the end, because the `sut` type is determined during the *Given* step, until the end;
- there is no need to instantiate the `sut` object, even if it is allowed by the `givenSut(sutInstance)` alternate end point, as below;
- the call to any `given()` method is optional;
- you manipule the same `result` type until the end, because the `result` type is determined during the *When* step;
- you cannot inadvertently make a fake test that would verify nothing, because any `then()` method is always a sequence termination.

Of course, it is also possible to test any void method, instead of a non-void one, like this. 
```java
import static testasyouthink.TestAsYouThink.givenSut;
...

givenSut(systemUnderTest)
.given(() -> {
    // Preparation of fixtures
}).when(sut -> {
    // Event or action
    sut.voidMethod(...);
}).then(() -> {
    // Verification of expectations
});
```

## Test Fixtures

### Separation of concerns with multiple Given steps

If your fixtures preparation may be divided into several blocks, you can make them materialize.
```java
givenSutClass(SystemUnderTest.class)
.given(() -> {
    // the first Given step
}).and(() -> { 
    // another Given step
}) // to be repeated as many times as you need
.when(sut -> { ... })
.then(result -> { ... });
```

For example, you can separate the preparation between the SUT and the other fixtures.
```java
givenSutClass(SystemUnderTest.class)
.given(sut -> {
    // SUT preparation in a Given step
}).and(() -> {
    // Other fixtures preparation in another Given step
}).when(sut -> { ... })
.then(result -> { ... });
```

If some fixtures are the arguments of the method to be tested, you may prefer the following alternate syntaxes.
```java
givenSutClass(SystemUnderTest.class)
.givenArgument("simple argument", anyValue)
.andArgument("argument to be built", () -> {
    // Where this argument is built.
}).andArgument("argument already ready to be used", DataProvider::choosenDataSet)
.when(SystemUnderTest::nonVoidMethodWithArguments)
.then(result -> { ... });
```
The arguments will be injected as argument values when the method to be tested is called. As you can guess, `Data::choosenDataSet` is a method reference.

### Specifying fixtures

You are encouraged to explain your intentions to share and remember them by specifying your test fixtures. What makes both the known state of the SUT and your data set specific to the current test case?
```java
givenSutClass(SystemUnderTest.class)
.given("a SUT in a known state", sut -> {
    // Put the SUT in a known state.
}).and("a specific data set", () -> {
    // Prepare other fixtures.
}).when(sut -> { ... })
.then(result -> { ... });
```

## Expectations

### Separation of concerns with multiple Then steps

You can separate expectation concerns if needed. The following example separates expectations between the result and the SUT.
```java
givenSutClass(SystemUnderTest.class)
.when(sut -> { return sut.nonVoidMethod(); })
.then(result -> {
    // Where the result meets expectations.
}, sut -> {
    // Where the SUT meets expectations.
});
```

You can also separate the result expectations in detached blocks.
```java
givenSutClass(SystemUnderTest.class)
.given(() -> { ... })
.when(sut -> { return sut.nonVoidMethod(); })
.then(result -> {
    // an expectation
}).and(result -> {
    // another expectation
});
```

### Expectations as predicates

You can write your expectations by providing one or more predicates instead of assertions.
```java
givenSutClass(SystemUnderTest.class)
.when(sut -> { return sut.nonVoidMethod(); })
.then(result -> { // a predicate related to the result
    return booleanExpressionAboutResult();
}, sut -> { // a predicate related to the SUT
    return booleanExpressionAboutSut();
});
```

### Specifying expectations

You are encouraged to explain the system under test behavior by specifying your expectations. What is the expected behavior in the current situtation?
```java
givenSutClass(SystemUnderTest.class)
.given(() -> { ... })
.when(sut -> { ... })
.then("first specified expectation", result -> {
    // Expectation as specified
}).and("second specified expectation", result -> {
    // Another expectation as specified
});
```

### Failures

If a method signature contains a `throws` clause with a checked, compile-time exception, it is not necessary to modify the testing method signature anymore by adding the same clause to it. This clause and its spreading are considered as a technical constaint without value in a executable specification approach. As a consequence, it becomes imperceptible for the test code, and above all for the software developer who can stay focused on his tests. Tests will continue to fail if any unexpected exception is raised.

#### Expected failures

Because the failure testing is an important part of your use cases, you can verify the behavior of the system under test when it is used ouside operating conditions.
```java
givenSutClass(SystemUnderTest.class)
.given(() -> { ... })
.whenSutRunsOutsideOperatingConditions(sut -> {
    // where an event causes a failure
}).thenItFails().becauseOf(ExpectedFailure.class).withMessage("expected message");
```

#### Unexpected failures

When an unexpected failure occurs - because of a regression for example -, the test fails by raising an `AssertionError`, because the defaut behavior consists of asserting no failure should happen, unless the software developer wants.

# Release Notes

## Version 0.3

- Rename the API to **TestAsYouThink**.
- Choose an open source license.
- Publish artifacts to Maven Central.
- Check version updates.

## Version 0.2

- Include a data as a method argument during the preparation phase.
- Include two data as method arguments during the preparation phase.
- Include three data as method arguments during the preparation phase.
- Receive method arguments directly as values.
- Specify method arguments.
- Verify failures while invoking methods with arguments.
- Verify the expected exception and the expected message separately.

## Version 0.1

- Write an unit or integration test by using the Given-When-Then canvas and full sequence.
- Delegate the system under test instantiation to the API.
- Reduce the syntactic sequence to a When-Then partial sequence (except the determining of the system under test).
- Specify fixtures in the Given step.
- Specify expectations in the Then step.
- Verify the expectations on the system under test, in addition to the result.
- Provide expectations as predicates.
- Verify failures.
- Separate preparations.
- Separate expectations.

# License

*Test As You Think* is distributed under the GNU LGPLv3 license. The LGPLv3 license is included in the LICENSE.txt file. More information about this license is available at http://www.gnu.org.
