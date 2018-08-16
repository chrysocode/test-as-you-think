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

package testasyouthink.preparation;

import testasyouthink.GivenWhenThenDsl.Fixture.Stdin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;

public class StdinPreparation implements Stdin {

    private static final String END_OF_LINE = "\n";

    @Override
    public void expectToRead(Object input) {
        InputStream stdinFake = new ByteArrayInputStream(String
                .valueOf(input)
                .getBytes());
        System.setIn(stdinFake);
    }

    @Override
    public void expectToRead(Collection<?> inputs) {
        expectToRead(inputs
                .stream()
                .map(String::valueOf)
                .collect(joining(END_OF_LINE)));
    }

    @Override
    public void expectToRead(File input) {
        try {
            expectToRead(lines(input.toPath()).collect(joining(END_OF_LINE)));
        } catch (IOException e) {
            throw new RuntimeException("Not yet implemented!");
        }
    }
}
