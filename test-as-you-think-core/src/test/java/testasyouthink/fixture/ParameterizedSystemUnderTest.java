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

package testasyouthink.fixture;

public class ParameterizedSystemUnderTest<$Parameter1, $Parameter2, $Parameter3> {

    public void voidMethodWithParameter($Parameter1 parameter) {}

    public String nonVoidMethodWithParameter($Parameter1 parameter) {
        return null;
    }

    public void voidMethodWithTwoParameters($Parameter1 parameter1, $Parameter2 parameter2) {}

    public String nonVoidMethodWithTwoParameters($Parameter1 parameter1, $Parameter2 parameter2) {
        return null;
    }

    public void voidMethodWithThreeParameters($Parameter1 parameter1, $Parameter2 parameter2, $Parameter3 parameter3) {}

    public String nonVoidMethodWithThreeParameters($Parameter1 parameter1, $Parameter2 parameter2,
            $Parameter3 parameter3) {
        return null;
    }

    public void failWithParameter($Parameter1 parameter) throws Throwable {}

    public void failWithTwoParameters($Parameter1 parameter1, $Parameter2 parameter2) throws Throwable {}

    public void failWithThreeParameters($Parameter1 parameter1, $Parameter2 parameter2,
            $Parameter3 parameter3) throws Throwable {}

    public String nonVoidFailWithParameter($Parameter1 parameter) throws Throwable {
        return null;
    }

    public String nonVoidFailWithTwoParameters($Parameter1 parameter1, $Parameter2 parameter2) throws Throwable {
        return null;
    }

    public String nonVoidFailWithThreeParameters($Parameter1 parameter1, $Parameter2 parameter2,
            $Parameter3 parameter3) throws Throwable {
        return null;
    }
}

