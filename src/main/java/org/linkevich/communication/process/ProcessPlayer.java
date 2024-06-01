package org.linkevich.communication.process;

import org.linkevich.PidLogger;
import org.linkevich.communication.thread.ThreadPlayer;
import org.linkevich.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@link Player} class extension for use in an inter-process environment (to fulfill the requirement number 7).
 * Utilizes given {@link Socket} as message input and output sources.
 */
public class ProcessPlayer extends Player {

    private final BufferedReader lineReader;
    private final PrintWriter lineWriter;

    private final Supplier<String> supplier;
    private final Consumer<String> consumer;

    public ProcessPlayer(String name, Socket socket) throws IOException {
        super(name);
        this.lineReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.lineWriter = new PrintWriter(socket.getOutputStream(), true);

        this.supplier = () -> {
            try {
                return this.lineReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        this.consumer = msg -> {
            PidLogger.log(getName() + ": " + msg);
            this.lineWriter.println(msg);
        };
    }

    public void start() {
        communicate(consumer, supplier);
    }

    @Override
    protected void shutdown() {
        super.shutdown();
        try {
            lineReader.close();
            lineWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PidLogger.log(getName() + " left the chat");
        System.exit(0);
    }

    /**
     * Push message to the {@link ProcessPlayer#consumer} to initiate communication.
     * Increments the counter of messages dispatched by the Player.
     * Supposed to be called by the "initiator".
     * @param msg message string to push.
     */
    protected void send(String msg) {
        getAndIncrementCounter();
        consumer.accept(msg);
    }
}
