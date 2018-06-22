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

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testasyouthink.fixture.GivenWhenThenDefinition;
import testasyouthink.fixture.SystemUnderTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static testasyouthink.TestAsYouThink.givenSutClass;

public class ThenStdoutAsResultTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(ThenStdoutAsResultTest.class);

    @Test
    public void should_verify_the_standard_output_as_a_result_given_a_void_target_method() {
        // GIVEN
        GivenWhenThenDefinition gwtMock = mock(GivenWhenThenDefinition.class);

        // WHEN
        givenSutClass(SystemUnderTest.class)
                .givenStdoutToBeCaptured()
                .when(sut -> {
                    gwtMock.whenAnEventHappensInRelationToAnActionOfTheConsumer();
                    System.out.println("Stdout as a result");
                })
                .thenStdoutAsResult(result -> {
                    gwtMock.thenTheActualResultIsInKeepingWithTheExpectedResult();
                    assertThat(result).hasContent("Stdout as a result");
                });

        // THEN
        InOrder inOrder = inOrder(gwtMock);
        inOrder
                .verify(gwtMock)
                .whenAnEventHappensInRelationToAnActionOfTheConsumer();
        inOrder
                .verify(gwtMock)
                .thenTheActualResultIsInKeepingWithTheExpectedResult();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void should_verify_the_standard_output_as_a_result_for_multiple_threads_given_a_void_target_method()
            throws InterruptedException, ExecutionException, TimeoutException {
        // GIVEN
        final int numberOfThreads = 10;
        List<GivenWhenThenDefinition> gwtMocks = IntStream
                .range(0, numberOfThreads)
                .mapToObj(count -> mock(GivenWhenThenDefinition.class))
                .collect(toList());
        CountDownLatch counterOfThreadsToPrepare = new CountDownLatch(numberOfThreads);
        CountDownLatch callingThreadBlocker = new CountDownLatch(1);
        CountDownLatch counterOfThreadsToComplete = new CountDownLatch(numberOfThreads);
        SoftAssertions softly = new SoftAssertions();

        // WHEN
        IntStream
                .range(0, numberOfThreads)
                .mapToObj(count -> new Thread(() -> {
                    LOGGER.debug("Entering in thread #{} :)", count);
                    givenSutClass(SystemUnderTest.class)
                            .givenStdoutToBeCaptured()
                            .when(sut -> {
                                LOGGER.debug("Thread #{} ready!", count);
                                counterOfThreadsToPrepare.countDown();
                                LOGGER.debug("Thread #{} waiting...", count);
                                callingThreadBlocker.await();
                                LOGGER.debug("Thread #{} continues.", count);
                                gwtMocks
                                        .get(count)
                                        .whenAnEventHappensInRelationToAnActionOfTheConsumer();
                                System.out.println("Stdout as a result of T#" + count);
                            })
                            .thenStdoutAsResult(result -> {
                                LOGGER.debug("Checking result of thread #{}: {}", count, result);
                                softly
                                        .assertThat(result)
                                        .hasContent("Stdout as a result of T#" + count);
                                gwtMocks
                                        .get(count)
                                        .thenTheActualResultIsInKeepingWithTheExpectedResult();
                                counterOfThreadsToComplete.countDown();
                            });
                }))
                .forEach(Thread::start);
        counterOfThreadsToPrepare.await();
        LOGGER.debug("All threads ready!");
        LOGGER.debug("Writing in console can start...");
        callingThreadBlocker.countDown();
        counterOfThreadsToComplete.await();
        LOGGER.debug("All threads complete!");

        // THEN
        gwtMocks.forEach(gwt -> {
            InOrder inOrder = inOrder(gwt);
            inOrder
                    .verify(gwt)
                    .whenAnEventHappensInRelationToAnActionOfTheConsumer();
            inOrder
                    .verify(gwt)
                    .thenTheActualResultIsInKeepingWithTheExpectedResult();
            inOrder.verifyNoMoreInteractions();
        });
        softly.assertAll();
    }
}
