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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.currentThread;

class StdoutStderrPreparation {

    static DoubleFileRedirection redirectionsCapturingStandardStreamsSeparately() throws IOException {
        FileRedirection stdoutRedirection = new FileRedirection();
        FileRedirection stderrRedirection = new FileRedirection();
        return new DoubleFileRedirection(stdoutRedirection, stderrRedirection);
    }

    static DoubleFileRedirection redirectionsCapturingStandardStreamsTogether() throws IOException {
        return new DoubleFileRedirection(new FileRedirection());
    }

    static class Tee {

        static final Map<Long, DoubleFileRedirection> THREAD_TO_REDIRECTIONS;
        private static final PrintStream SYSTEM_OUT;
        private static final PrintStream SYSTEM_ERR;

        static {
            SYSTEM_OUT = System.out;
            SYSTEM_ERR = System.err;
            THREAD_TO_REDIRECTIONS = new ConcurrentHashMap<>();

            commuteStandardStreamsOnce();
        }

        private static void commuteStandardStreamsOnce() {
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) {
                    SYSTEM_OUT.write(b);
                    if (!THREAD_TO_REDIRECTIONS.isEmpty()) {
                        THREAD_TO_REDIRECTIONS.get(currentThread().getId()).stdoutRedirection.write(b);
                    }
                }
            }));
            System.setErr(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) {
                    SYSTEM_ERR.write(b);
                    if (!THREAD_TO_REDIRECTIONS.isEmpty()) {
                        THREAD_TO_REDIRECTIONS.get(currentThread().getId()).stderrRedirection.write(b);
                    }
                }
            }));
        }
    }

    static class FileRedirection {

        Path path;
        PrintStream stream;

        FileRedirection() throws IOException {
            initializeTemporaryPath();
        }

        private void initializeTemporaryPath() throws IOException {
            path = Files.createTempFile("actual_result", ".txt");
            path
                    .toFile()
                    .deleteOnExit();
            stream = new PrintStream(path.toString());
        }

        void write(int b) {
            stream.write(b);
        }
    }

    static class DoubleFileRedirection {

        FileRedirection stdoutRedirection;
        FileRedirection stderrRedirection;

        DoubleFileRedirection(FileRedirection stdoutRedirection, FileRedirection stderrRedirection) {
            this.stdoutRedirection = stdoutRedirection;
            this.stderrRedirection = stderrRedirection;
        }

        DoubleFileRedirection(FileRedirection sameRedirectionForBoth) {
            this(sameRedirectionForBoth, sameRedirectionForBoth);
        }

        void storeIn(Map<Long, DoubleFileRedirection> threadToRedirections) {
            threadToRedirections.put(currentThread().getId(), this);
        }

        FileRedirection oneRedirection() {
            return stdoutRedirection;
        }
    }
}
