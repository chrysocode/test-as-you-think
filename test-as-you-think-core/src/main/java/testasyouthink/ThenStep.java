/*-
 * #%L
 * Test As You Think
 * %%
 * Copyright (C) 2017 Xavier Pigeon and TestAsYouThink contributors
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

package testasyouthink;

import testasyouthink.GivenWhenThenDsl.VerificationStage.AndThen;
import testasyouthink.GivenWhenThenDsl.VerificationStage.Then;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailure;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailureWithExpectedException;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenFailureWithExpectedMessage;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenStep<$SystemUnderTest, $Result> implements Then<$SystemUnderTest, $Result>,
        AndThen<$SystemUnderTest, $Result>, ThenFailure, ThenFailureWithExpectedException,
        ThenFailureWithExpectedMessage {

    private final GivenWhenContext<$SystemUnderTest, $Result> context;
    private $Result result;

    ThenStep(GivenWhenContext<$SystemUnderTest, $Result> context) {
        this.context = context;
    }

    private $Result result() {
        return result == null ? result = context.returnResultOrVoid() : result;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(Consumer<$Result> thenStep) {
        thenStep.accept(result());
        return this;
    }

    @Override
    public void then(BiConsumer<$SystemUnderTest, $Result> thenStep) {
        thenStep.accept(context.getSystemUnderTest(), context.returnResultOrVoid());
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(Runnable thenStep) {
        result();
        thenStep.run();
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Consumer<$Result> thenStep) {
        then(thenStep);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(String expectationSpecification, Runnable thenStep) {
        then(thenStep);
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> then(Predicate<$Result> thenStep) {
        assertThat(thenStep.test(result())).isTrue();
        return this;
    }

    @Override
    public void then(List<Predicate<$Result>> thenSteps) {
        assertThat(thenSteps
                .stream()
                .reduce(Predicate::and)
                .get()
                .test(context.returnResultOrVoid())).isTrue();
    }

    @Override
    public void then(BiPredicate<$SystemUnderTest, $Result> thenStep) {
        assertThat(thenStep.test(context.getSystemUnderTest(), context.returnResultOrVoid())).isTrue();
    }

    @Override
    public void then(Predicate<$Result> thenStepAboutResult, Predicate<$SystemUnderTest> thenStepAboutSystemUnderTest) {
        then(thenStepAboutResult);
        assertThat(thenStepAboutSystemUnderTest.test(context.getSystemUnderTest())).isTrue();
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(Consumer<$Result> thenStep) {
        thenStep.accept(result());
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(Runnable thenStep) {
        thenStep.run();
        return this;
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Consumer<$Result> thenStep) {
        return then(expectationSpecification, thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(Predicate<$Result> thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThen<$SystemUnderTest, $Result> and(String expectationSpecification, Runnable thenStep) {
        return then(expectationSpecification, thenStep);
    }

    @Override
    public ThenFailureWithExpectedException thenItFails() {
        assertThat(context.returnResultOrVoid()).isInstanceOf(Throwable.class);
        return this;
    }

    @Override
    public ThenFailureWithExpectedMessage becauseOf(Class<? extends Throwable> expectedThrowableClass) {
        assertThat(context.returnResultOrVoid()).isInstanceOf(expectedThrowableClass);
        return this;
    }

    @Override
    public void withMessage(String expectedMessage) {
        assertThat(((Throwable) context.returnResultOrVoid()).getMessage()).isEqualTo(expectedMessage);
    }
}
