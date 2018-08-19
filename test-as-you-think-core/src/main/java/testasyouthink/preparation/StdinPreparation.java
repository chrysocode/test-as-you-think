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
import testasyouthink.function.CheckedConsumer;
import testasyouthink.function.Functions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static java.lang.Thread.currentThread;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class StdinPreparation implements Stdin {

    private static final String END_OF_LINE = "\n";
    private static final String PREPARATION_FAILS = "Fails to prepare the standard input stream!";
    private final Functions functions = Functions.INSTANCE;
    private Collection<String> inputsToBeRead;

    StdinPreparation() {
        inputsToBeRead = new ArrayList<>();
    }

    <$SystemUnderTest> Consumer<$SystemUnderTest> buildStdin(CheckedConsumer<Stdin> givenStep) {
        return functions.toConsumer(() -> {
            try {
                givenStep.accept(this);
            } catch (Throwable throwable) {
                throw new PreparationError(PREPARATION_FAILS, throwable);
            }
            redirectStdin();
        });
    }

    private void redirectStdin() {
        InputStream stdinFake = new MultipleStdinInOne(inputsToBeRead
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
    public void expectToRead(File input) throws IOException {
        inputsToBeRead.addAll(lines(input.toPath()).collect(toList()));
    }

    @Override
    public void expectToRead(Path input) throws IOException {
        inputsToBeRead.addAll(lines(input).collect(toList()));
    }

    private static class MultipleStdinInOne extends ByteArrayInputStream {

        private static final Map<Long, MultipleStdinInOne> STREAMS = new ConcurrentHashMap<>();
        private ByteArrayInputStream byteArrayInputStream;

        MultipleStdinInOne(byte[] buf) {
            super(buf);
            byteArrayInputStream = new ByteArrayInputStream(buf);
            put(this);
        }

        MultipleStdinInOne(byte[] buf, int offset, int length) {
            super(buf, offset, length);
            byteArrayInputStream = new ByteArrayInputStream(buf, offset, length);
            put(this);
        }

        private static ByteArrayInputStream put(MultipleStdinInOne multipleStdinInOne) {
            return STREAMS.put(currentThread().getId(), multipleStdinInOne);
        }

        private static ByteArrayInputStream currentStream() {
            return STREAMS.get(currentThread().getId()).byteArrayInputStream;
        }

        @Override
        public synchronized int read() {
            return currentStream().read();
        }

        @Override
        public synchronized int read(byte[] b, int off, int len) {
            return currentStream().read(b, off, len);
        }

        @Override
        public synchronized long skip(long n) {
            return currentStream().skip(n);
        }

        @Override
        public synchronized int available() {
            return currentStream().available();
        }

        @Override
        public boolean markSupported() {
            return currentStream().markSupported();
        }

        @Override
        public void mark(int readAheadLimit) {
            currentStream().mark(readAheadLimit);
        }

        @Override
        public synchronized void reset() {
            currentStream().reset();
        }

        @Override
        public void close() throws IOException {
            currentStream().close();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return currentStream().read(b);
        }

        @Override
        public int hashCode() {
            return currentStream().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MultipleStdinInOne) {
                MultipleStdinInOne other = (MultipleStdinInOne) obj;
                return Objects.equals(byteArrayInputStream, other.byteArrayInputStream);
            } else {
                return false;
            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public String toString() {
            return currentStream().toString();
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }
}
