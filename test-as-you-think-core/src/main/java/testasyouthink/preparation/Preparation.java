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
import testasyouthink.function.CheckedRunnable;
import testasyouthink.function.CheckedSupplier;
import testasyouthink.function.Functions;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.Thread.currentThread;
import static testasyouthink.preparation.Preparation.Redirections.THREAD_TO_REDIRECTIONS;
import static testasyouthink.preparation.Preparation.Redirections.redirectionsCapturingStandardStreamsSeparately;
import static testasyouthink.preparation.Preparation.Redirections.redirectionsCapturingStandardStreamsTogether;

public class Preparation<$SystemUnderTest> {

    private final Functions functions = Functions.INSTANCE;
    private final SutPreparation sutPreparation = SutPreparation.INSTANCE;
    private final ArgumentPreparation argumentPreparation = ArgumentPreparation.INSTANCE;
    private final Queue<Consumer<$SystemUnderTest>> givenSteps;
    private Supplier<$SystemUnderTest> givenSutStep;
    private Queue<Supplier> argumentSuppliers;
    private $SystemUnderTest systemUnderTest;
    private boolean standardStreamsCaptured;

    public Preparation() {
        givenSteps = new ArrayDeque<>();
        argumentSuppliers = new LinkedList<>();
        standardStreamsCaptured = false;
    }

    public Preparation(Class<$SystemUnderTest> sutClass) {
        this();
        givenSutStep = sutPreparation.buildSutSupplier(sutClass);
    }

    public Preparation($SystemUnderTest systemUnderTest) {
        this();
        givenSutStep = sutPreparation.buildSutSupplier(systemUnderTest);
    }

    public Preparation(CheckedSupplier<$SystemUnderTest> givenSutStep) {
        this();
        this.givenSutStep = sutPreparation.buildSutSupplier(givenSutStep);
    }

    public void recordGivenStep(CheckedRunnable givenStep) {
        givenSteps.add(functions.toConsumer(() -> {
            try {
                givenStep.run();
            } catch (Throwable throwable) {
                throw new PreparationError("Fails to prepare the test fixture!", throwable);
            }
        }));
    }

    public void recordGivenStep(CheckedConsumer<$SystemUnderTest> givenStep) {
        givenSteps.add(sutPreparation.buildSutSupplier(givenStep));
    }

    public void recordGivenStep(Consumer<Stdin> givenStep) {
        givenSteps.add(new StdinPreparation().buildStdin(givenStep));
    }

    public <$Argument> void recordGivenStep(CheckedSupplier<$Argument> givenStep) {
        argumentSuppliers.add(argumentPreparation.buidArgumentSupplier(givenStep));
    }

    public <$Argument> void recordGivenStep(Class<$Argument> mutableArgumentClass,
            CheckedConsumer<$Argument> givenStep) {
        argumentSuppliers.add(argumentPreparation.buildMutableArgumentSupplier(mutableArgumentClass, givenStep));
    }

    public Queue<Supplier> getArgumentSuppliers() {
        return argumentSuppliers;
    }

    public void prepareFixtures() {
        $SystemUnderTest sutToPrepareAtFirst = systemUnderTest();
        while (!givenSteps.isEmpty()) {
            givenSteps
                    .poll()
                    .accept(sutToPrepareAtFirst);
        }
    }

    public void prepareFixturesSeparately() {
        prepareFixtures();
        argumentSuppliers.forEach(Supplier::get);
    }

    private $SystemUnderTest systemUnderTest() {
        if (systemUnderTest == null && givenSutStep != null) {
            systemUnderTest = givenSutStep.get();
        }
        return systemUnderTest;
    }

    public Supplier<$SystemUnderTest> supplySut() {
        return this::systemUnderTest;
    }

    public void captureStandardStreamsSeparately() {
        if (!standardStreamsCaptured) {
            recordGivenStep(() -> redirectionsCapturingStandardStreamsSeparately().storeIn(THREAD_TO_REDIRECTIONS));
            standardStreamsCaptured = true;
        }
    }

    public void captureStandardStreamsTogether() {
        if (!standardStreamsCaptured) {
            recordGivenStep(() -> redirectionsCapturingStandardStreamsTogether().storeIn(THREAD_TO_REDIRECTIONS));
            standardStreamsCaptured = true;
        }
    }

    public Path getStdoutPath() {
        return THREAD_TO_REDIRECTIONS.get(currentThread().getId()).stdoutRedirection.path;
    }

    public Path getStderrPath() {
        return THREAD_TO_REDIRECTIONS.get(currentThread().getId()).stderrRedirection.path;
    }

    public Path getStdStreamsPath() {
        return THREAD_TO_REDIRECTIONS
                .get(currentThread().getId())
                .oneRedirection().path;
    }

    static class Redirections {

        static final Map<Long, DoubleRedirection> THREAD_TO_REDIRECTIONS;
        private static final PrintStream SYSTEM_OUT;
        private static final PrintStream SYSTEM_ERR;

        static {
            SYSTEM_OUT = System.out;
            SYSTEM_ERR = System.err;
            THREAD_TO_REDIRECTIONS = new HashMap<>();

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

        static DoubleRedirection redirectionsCapturingStandardStreamsSeparately() throws IOException {
            Redirection stdoutRedirection = new Redirection();
            Redirection stderrRedirection = new Redirection();
            return new DoubleRedirection(stdoutRedirection, stderrRedirection);
        }

        static DoubleRedirection redirectionsCapturingStandardStreamsTogether() throws IOException {
            return new DoubleRedirection(new Redirection());
        }
    }

    private static class Redirection {

        private Path path;
        private PrintStream stream;

        Redirection() throws IOException {
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

    private static class DoubleRedirection {

        private Redirection stdoutRedirection;
        private Redirection stderrRedirection;

        DoubleRedirection(Redirection stdoutRedirection, Redirection stderrRedirection) {
            this.stdoutRedirection = stdoutRedirection;
            this.stderrRedirection = stderrRedirection;
        }

        DoubleRedirection(Redirection sameRedirectionForBoth) {
            this(sameRedirectionForBoth, sameRedirectionForBoth);
        }

        void storeIn(Map<Long, DoubleRedirection> threadToRedirections) {
            threadToRedirections.put(currentThread().getId(), this);
        }

        Redirection oneRedirection() {
            return stdoutRedirection;
        }
    }
}
