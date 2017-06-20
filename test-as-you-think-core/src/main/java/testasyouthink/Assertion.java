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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class Assertion {

    private Runnable runnable;

    Assertion assertThat(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    void spendAtMost(long timeLimit) {
        FutureTask<Void> futureTask = new FutureTask<>(() -> {
            runnable.run();
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
    }
}
