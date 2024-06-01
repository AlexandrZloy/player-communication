package org.linkevich.player;

import org.linkevich.Configuration;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Player class - holds player name, dispatched messages counter and communication logic.
 */
public abstract class Player {

    private final String name;
    private final AtomicInteger counter = new AtomicInteger(0);

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Initiate the communication process.
     * Stops the communication when {@link Player#counter} reaches the {@link Configuration#DEFAULT_MESSAGE_LIMIT}.
     * Calls the {@link Player#shutdown()} method after the communication ends.
     * @param output consumer that accepts outgoing messages.
     * @param input supplier that produces incoming messages.
     */
    public void communicate(Consumer<String> output, Supplier<String> input) {
        while (counter.intValue() < Configuration.DEFAULT_MESSAGE_LIMIT) {
            String message = input.get();
            output.accept(getResponse(message));
        }
        shutdown();
    }

    /**
     * Callback called after the communication ends.
     */
    protected void shutdown() {
    }

    /**
     * Return the current state of the dispatched message counter and increments its value by one.
     * @return The current value of the dispatched message counter.
     */
    protected int getAndIncrementCounter() {
        return counter.getAndIncrement();
    }

    private String getResponse(String received) {
        return String.join(" ", received, String.valueOf(getAndIncrementCounter()));
    }
}
