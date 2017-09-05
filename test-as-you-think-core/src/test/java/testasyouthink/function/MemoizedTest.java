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

package testasyouthink.function;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MemoizedTest {

    @Test
    public void should_memoize_a_supplied_value() {
        // GIVEN
        Counter counter = Mockito.mock(Counter.class);
        Supplier<Double> memoized = Memoized.of(() -> {
            counter.count();
            return Math.random();
        });
        Double suppliedValue = memoized.get();

        // WHEN
        Double memoizedResult = memoized.get();

        // THEN
        assertThat(memoizedResult).isEqualTo(suppliedValue);
        verify(counter).count();
        verifyNoMoreInteractions(counter);
    }

    static class Counter {

        void count() {}
    }
}
