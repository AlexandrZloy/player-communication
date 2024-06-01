package org.linkevich.communication.thread;

import java.util.concurrent.BlockingQueue;

/**
 * {@link ThreadPlayer} class extension that acts as an "initiator".
 */
public final class ThreadInitiator extends ThreadPlayer {

    public ThreadInitiator(String name, BlockingQueue<String> output, BlockingQueue<String> input) {
        super(name, output, input);
    }

    @Override
    public void run() {
        send("init message");
        super.run();
    }
}
