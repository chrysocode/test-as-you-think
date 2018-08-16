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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class StdinPreparation implements Stdin {

    private static final String END_OF_LINE = "\n";
    private Collection<String> inputsToBeRead;

    StdinPreparation() {
        inputsToBeRead = new ArrayList<>();
    }

    void redirectStdin() {
        InputStream stdinFake = new ByteArrayInputStream(inputsToBeRead
                .stream()
                .collect(joining(END_OF_LINE))
                .getBytes());
        System.setIn(stdinFake);
    }

    @Override
    public void expectToRead(Object input) {
        inputsToBeRead.add(String.valueOf(input));
    }

    @Override
    public void expectToRead(Collection<?> inputs) {
        inputsToBeRead.addAll(inputs
                .stream()
                .map(String::valueOf)
                .collect(toList()));
    }

    @Override
    public void expectToRead(File input) {
        try {
            inputsToBeRead.addAll(lines(input.toPath()).collect(toList()));
        } catch (IOException e) {
            throw new RuntimeException("Not yet implemented!");
        }
    }

    @Override
    public void expectToRead(Path input) {
        try {
            inputsToBeRead.addAll(lines(input).collect(toList()));
        } catch (IOException e) {
            throw new RuntimeException("Not yet implemented!");
        }
    }
}
