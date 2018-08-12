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

package testasyouthink.function;

import java.util.Optional;
import java.util.function.Supplier;

public final class Memoized<$Value> implements Supplier<$Value> {

    private final Supplier<$Value> supplier;
    private Optional<$Value> value;

    private Memoized(Supplier<$Value> supplier) {
        this.supplier = supplier;
    }

    public static <$Value> Supplier<$Value> of(Supplier<$Value> supplier) {
        return new Memoized<>(supplier);
    }

    @Override
    public $Value get() {
        if (value == null) {
            value = Optional.ofNullable(supplier.get());
        }
        return value.orElse(null);
    }
}
