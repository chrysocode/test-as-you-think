/*-
 * #%L
 * Test As You Think
 * %%
 * Copyright (C) 2017 - 2018 Xavier Pigeon and TestAsYouThink contributors
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

package testasyouthink.verification;

import org.assertj.core.api.AbstractAssert;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RunnableAssert extends AbstractAssert<RunnableAssert, Runnable> {

    private RunnableAssert(Runnable actual) {
        super(actual, RunnableAssert.class);
    }

    public static RunnableAssert assertThat(Runnable actual) {
        return new RunnableAssert(actual);
    }

    public RunnableAssert spendsAtMost(long timeLimit) {
        FutureTask<Void> futureTask = new FutureTask<>(() -> {
            actual.run();
            return null;
        });
        ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.execute(futureTask);
        try {
            futureTask.get(timeLimit, TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            throw new AssertionError("the current thread was interrupted while waiting", exception);
        } catch (ExecutionException exception) {
            throw new AssertionError("the computation threw an exception", exception);
        } catch (TimeoutException exception) {
            throw new AssertionError("test timed out after " + timeLimit + " milliseconds", exception);
        } finally {
            executorService.shutdownNow();
        }
        return this;
    }

    public RunnableAssert spendsAtMost(Duration duration) {
        spendsAtMost(duration.toMillis());
        return this;
    }
}
