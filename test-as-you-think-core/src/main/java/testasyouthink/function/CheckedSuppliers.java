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

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CheckedSuppliers {

    interface CheckedCharacterSupplier extends CheckedSupplier<Character> {}

    interface CheckedStringSupplier extends CheckedSupplier<String> {}

    interface CheckedByteSupplier extends CheckedSupplier<Byte> {}

    interface CheckedShortSupplier extends CheckedSupplier<Short> {}

    interface CheckedIntegerSupplier extends CheckedSupplier<Integer> {}

    interface CheckedLongSupplier extends CheckedSupplier<Long> {}

    interface CheckedFloatSupplier extends CheckedSupplier<Float> {}

    interface CheckedDoubleSupplier extends CheckedSupplier<Double> {}

    interface CheckedBigIntegerSupplier extends CheckedSupplier<BigInteger> {}

    interface CheckedBigDecimalSupplier extends CheckedSupplier<BigDecimal> {}

    interface CheckedBooleanSupplier extends CheckedSupplier<Boolean> {}

    interface CheckedDateSupplier extends CheckedSupplier<Date> {}

    interface CheckedLocalDateSupplier extends CheckedSupplier<LocalDate> {}

    interface CheckedInstantSupplier extends CheckedSupplier<Instant> {}

    interface CheckedFileSupplier extends CheckedSupplier<File> {}

    interface CheckedPathSupplier extends CheckedSupplier<Path> {}

    interface CheckedListSupplier<T> extends CheckedSupplier<List<T>> {}
}
