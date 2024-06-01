package org.linkevich.communication.thread;

import org.linkevich.player.Player;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@link Player} class extension for use in a single process environment (to fulfill the requirement number 5),
 * implements {@link Runnable}.
 * Utilizes given {@link BlockingQueue}s as message input and output sources.
 */
public class ThreadPlayer extends Player implements Runnable {

    private final Supplier<String> supplier;
    private final Consumer<String> consumer;

    public ThreadPlayer(String name, BlockingQueue<String> output, BlockingQueue<String> input) {
        super(name);
        this.consumer = msg -> {
            try {
                System.out.println(name + ": " + msg);
                output.put(msg);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        this.supplier = () -> {
            try {
                return input.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public void run() {
        communicate(consumer, supplier);
    }

    @Override
    protected void shutdown() {
        super.shutdown();
        System.out.println(getName() + " left the chat");
    }

    /**
     * Push message to the {@link ThreadPlayer#consumer} to initiate communication.
     * Increments the counter of messages dispatched by the Player.
     * Supposed to be called by the "initiator".
     * @param msg message string to push.
     */
    protected void send(String msg) {
        getAndIncrementCounter();
        consumer.accept(msg);
    }
}
