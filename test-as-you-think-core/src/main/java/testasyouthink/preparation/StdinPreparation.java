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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.lang.Thread.currentThread;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static testasyouthink.preparation.StdinPreparation.Redirections.currentStream;

class StdinPreparation {

    private static final String END_OF_LINE = "\n";
    private static final String PREPARATION_FAILS = "Fails to prepare the standard input stream!";
    private final Functions functions = Functions.INSTANCE;

    <$SystemUnderTest> Consumer<$SystemUnderTest> buildStdin(final CheckedConsumer<Stdin> givenStep) {
        return functions.toConsumer(() -> {
            try {
                StdinRecorder stdinRecorder = new StdinRecorder();
                givenStep.accept(stdinRecorder);
                stdinRecorder.buffer();
            } catch (Throwable throwable) {
                throw new PreparationError(PREPARATION_FAILS, throwable);
            }
        });
    }

    private static class StdinRecorder implements Stdin {

        private Collection<String> inputsToBeRead;

        StdinRecorder() {
            inputsToBeRead = new ArrayList<>();
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

        private void buffer() {
            Redirections.THREAD_TO_STREAMS.put(currentThread().getId(), new ByteArrayInputStream(inputsToBeRead
                    .stream()
                    .collect(joining(END_OF_LINE))
                    .getBytes()));
        }
    }

    static class Redirections {

        private static final Map<Long, InputStream> THREAD_TO_STREAMS;

        static {
            THREAD_TO_STREAMS = new HashMap<>();
            commuteStdinOnce();
        }

        private static void commuteStdinOnce() {
            System.setIn(new StdinFake());
        }

        static InputStream currentStream() {
            return THREAD_TO_STREAMS.get(currentThread().getId());
        }
    }

    private static class StdinFake extends InputStream {

        @Override
        public int read() throws IOException {
            return currentStream().read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return currentStream().read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return currentStream().read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {
            return currentStream().skip(n);
        }

        @Override
        public int available() throws IOException {
            return currentStream().available();
        }

        @Override
        public void close() throws IOException {
            currentStream().close();
        }

        @Override
        public synchronized void mark(int readlimit) {
            currentStream().mark(readlimit);
        }

        @Override
        public synchronized void reset() throws IOException {
            currentStream().reset();
        }

        @Override
        public boolean markSupported() {
            return currentStream().markSupported();
        }

        @Override
        public int hashCode() {
            return currentStream().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return currentStream().equals(obj);
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
