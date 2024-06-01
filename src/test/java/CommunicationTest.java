import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkevich.communication.thread.ThreadInitiator;
import org.linkevich.communication.thread.ThreadPlayer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class CommunicationTest
{
    /**
     * Test method demonstrating fulfillment of the requirement number 5.
     * Runs two instances of Player as {@link Runnable} and ensures that they reach the end of communication.
     */
    @Test
    void test_communication_threaded() throws InterruptedException {
        BlockingQueue<String> firstToSecond = new ArrayBlockingQueue<>(1);
        BlockingQueue<String> secondToFirst = new ArrayBlockingQueue<>(1);

        ThreadInitiator threadedInitiator = new ThreadInitiator("initiator", firstToSecond, secondToFirst);
        ThreadPlayer responder = new ThreadPlayer("responder", secondToFirst, firstToSecond);

        ExecutorService executorService = Executors.newWorkStealingPool();

        executorService.submit(threadedInitiator);
        executorService.submit(responder);

        executorService.shutdown();

        Assertions.assertTrue(executorService.awaitTermination(3, TimeUnit.SECONDS));
    }
}
