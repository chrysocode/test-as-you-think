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

import testasyouthink.GivenWhenThenDsl.VerificationStage.AndThenWithoutResult;
import testasyouthink.GivenWhenThenDsl.VerificationStage.ThenWithoutResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenWithoutResultStep<$SystemUnderTest> implements ThenWithoutResult<$SystemUnderTest>,
        AndThenWithoutResult<$SystemUnderTest> {

    private final GivenWhenContext<$SystemUnderTest, Void> context;

    public ThenWithoutResultStep(GivenWhenContext<$SystemUnderTest, Void> context) {
        this.context = context;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(Runnable thenStep) {
        context.returnResultOrVoid();
        thenStep.run();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification, Runnable thenStep) {
        context.returnResultOrVoid();
        thenStep.run();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(Consumer<$SystemUnderTest> thenStep) {
        context.returnResultOrVoid();
        thenStep.accept(context.getSystemUnderTest());
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(BooleanSupplier thenStep) {
        context.returnResultOrVoid();
        assertThat(thenStep.getAsBoolean()).isTrue();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(Runnable thenStep) {
        thenStep.run();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification, Runnable thenStep) {
        thenStep.run();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(Consumer<$SystemUnderTest> thenStep) {
        thenStep.accept(context.getSystemUnderTest());
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(BooleanSupplier thenStep) {
        assertThat(thenStep.getAsBoolean()).isTrue();
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> then(String expectationSpecification,
            Consumer<$SystemUnderTest> thenStep) {
        return then(thenStep);
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> and(String expectationSpecification,
            Consumer<$SystemUnderTest> thenStep) {
        thenStep.accept(context.getSystemUnderTest());
        return this;
    }

    @Override
    public AndThenWithoutResult<$SystemUnderTest> thenSutRepliesWithin(long timeLimit) {
        FutureTask<Void> futureTask = new FutureTask<>(() -> context.returnResultOrVoid());
        ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.execute(futureTask);
        try {
            futureTask.get(timeLimit, TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            throw new AssertionError("the current thread was interrupted while waiting", exception);
        } catch (ExecutionException exception) {
            throw new RuntimeException("Not yet implemented!");
        } catch (TimeoutException exception) {
            throw new AssertionError("test timed out after " + timeLimit + " milliseconds", exception);
        } finally {
            executorService.shutdownNow();
        }
        assertThat(executorService.isShutdown()).isTrue();
        return this;
    }
}
