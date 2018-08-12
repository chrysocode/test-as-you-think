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

package testasyouthink;

import testasyouthink.execution.Execution;
import testasyouthink.function.Memoized;
import testasyouthink.preparation.Preparation;

import java.io.File;
import java.util.function.Supplier;

public class GivenWhenContext<$SystemUnderTest, $Result> {

    private final Preparation<$SystemUnderTest> preparation;
    private Supplier<$Result> result;

    GivenWhenContext(final Preparation<$SystemUnderTest> preparation,
            final Execution<$SystemUnderTest, $Result> execution) {
        this.preparation = preparation;
        result = Memoized.of(() -> {
            preparation.prepareFixtures();
            return execution.run();
        });
    }

    public void prepareFixturesSeparately() {
        preparation.prepareFixturesSeparately();
    }

    public void captureStandardStreamsSeparately() {
        preparation.captureStandardStreamsSeparately();
    }

    public void captureStandardStreamsTogether() {
        preparation.captureStandardStreamsTogether();
    }

    public $Result returnResultOrVoid() {
        return result.get();
    }

    public $SystemUnderTest getSystemUnderTest() {
        return preparation
                .supplySut()
                .get();
    }

    public File getStdoutAsFile() {
        return preparation
                .getStdoutPath()
                .toFile();
    }

    public File getStderrAsFile() {
        return preparation
                .getStderrPath()
                .toFile();
    }

    public File getStdStreamsAsFile() {
        return preparation
                .getStdStreamsPath()
                .toFile();
    }
}
