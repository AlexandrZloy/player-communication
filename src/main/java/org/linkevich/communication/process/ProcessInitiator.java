package org.linkevich.communication.process;

import java.io.IOException;
import java.net.Socket;

/**
 * {@link ProcessPlayer} class extension that acts as an "initiator".
 */
public class ProcessInitiator extends ProcessPlayer {

    public ProcessInitiator(String name, Socket socket) throws IOException {
        super(name, socket);
    }

    @Override
    public void start() {
        send("init message");
        super.start();
    }
}
